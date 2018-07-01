package com.sbolo.syk.fetch.spider;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

public class Spider {
	private static final Logger log = LoggerFactory.getLogger(Spider.class);
	
	private Pipeline pipeline;
	
	private Downloader downloader;
	
	private List<PageProcessor> listProcessor;
	
	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	
	private AtomicInteger threadAlive = new AtomicInteger();
	
	private ThreadPoolTaskExecutor threadPool2;
	
	private ReentrantLock newUrlLock = new ReentrantLock();
	
	private Condition newUrlCondition = newUrlLock.newCondition();
	
	private Set<String> urlsSet = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	
	private Map<String, Object> fields = new LinkedHashMap<String, Object>();
	
	protected Vector<String> waitManual = new Vector<String>();
	
	private boolean isRun = false;
	
	public static int runCount = 0;
	
	private int emptySleepTime = 30000;
	public Spider() {}
	public Spider(List<PageProcessor> listProcessor, Pipeline pipeline) {
		this.listProcessor = listProcessor;
		this.pipeline = pipeline;
	}
	
	public Spider(List<PageProcessor> listProcessor, Pipeline pipeline, Downloader downloader, ThreadPoolTaskExecutor threadPool) {
		this.listProcessor = listProcessor;
		this.pipeline = pipeline;
		this.downloader = downloader;
		this.threadPool2 = threadPool;
	}
	
	public Pipeline getPipeline() {
		return pipeline;
	}

	public void setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
	}

	public Downloader getDownloader() {
		return downloader;
	}

	public void setDownloader(Downloader downloader) {
		this.downloader = downloader;
	}

	public ThreadPoolTaskExecutor getThreadPool2() {
		return threadPool2;
	}

	public void setThreadPool2(ThreadPoolTaskExecutor threadPool) {
		this.threadPool2 = threadPool;
	}

	public List<PageProcessor> getListProcessor() {
		return listProcessor;
	}

	public void setListProcessor(List<PageProcessor> listProcessor) {
		this.listProcessor = listProcessor;
	}

	private void toProcess(PageProcessor curProcessor, String url) throws Exception{
		Page page = downloader.download(url);
		curProcessor.process(page, fields);
		List<String> urls = page.getAndWipeUrls(); //获取在process中新爬出的URL
		for(String newUrl : urls){
//			String url2Md5 = MD5Utils.signature(newUrl); //将URL加密为MD5
			if(urlsSet.add(newUrl)){ //URL去重
				queue.put(newUrl); //如果该URL没有出现过则添加到队列
			}
		}
		
		//队列不为空，则唤醒其他线程
		if(!queue.isEmpty()){
			signalNewUrl();
		}
	}
	
	public void run() throws Exception{
		if(isRun){
			log.info("系统正在运行....");
			return;
		}
		
		try {
			isRun = true;
			initComponent();
			for(int i=0; i<listProcessor.size(); i++){
				final PageProcessor curProcessor = listProcessor.get(i);
				try {
					int count = 0;
					log.info("===============================进入{}========================================================", curProcessor.getClass().getSimpleName());
					curProcessor.before();
					while(true){
						if(count == 0){
							queue.put(curProcessor.startUrl());
						}
						final String url = queue.poll();
						if(url == null){
							if(threadAlive.get() == 0){ //当前运行线程个数为0则跳出循环
								log.info("跳出循环");
								break;
							}
							waitNewUrl();
						}else {
							threadAlive.incrementAndGet(); //运行线程数加一
							threadPool2.execute(new Runnable() {
								@Override
								public void run() {
									try {
										toProcess(curProcessor, url);
									} catch (UnknownHostException | SocketTimeoutException | SocketException e){
										//the exception of time out that do nothing
									} catch (Exception e) {
										log.error(url,e);
									}
									finally{
										threadAlive.decrementAndGet(); //the thread of running was subtracted when it run end
									}
								}
							});
						}
						count++;
					}
					pipeline.process(fields);
				}catch(Exception e){
					log.error("", e);
				}finally{
					curProcessor.after();
					pipeline.after();
					this.destroy();
				}
			}
		} finally{
			isRun = false;
			log.info("所有processor已全部运行完毕！");
		}
	}
	
	private void waitNewUrl() {
        try {
        	newUrlLock.lock();
        	if(threadAlive.get() == 0){ //双重保障是否还有运行的线程
				return;
			}
			newUrlCondition.await(emptySleepTime, TimeUnit.MILLISECONDS); //在指定的时间范围内等待唤醒
		} catch (InterruptedException e) {
			log.error("", e);
		}finally{
			newUrlLock.unlock();
		}
    }
	
	private void signalNewUrl(){
		try {
			newUrlLock.lock();
			newUrlCondition.signalAll(); //唤醒所有线程
		} catch (Exception e) {
			log.error("", e);
		} finally{
			newUrlLock.unlock();
		}
	}
	
	public void destroy(){
		fields.clear();
		urlsSet.clear();
	}
	
	private void initComponent(){
		if(downloader == null){
			downloader = new Downloader();
		}
		if(threadPool2 == null){
			threadPool2 = new ThreadPoolTaskExecutor();
			threadPool2.setAllowCoreThreadTimeOut(true);
			threadPool2.setCorePoolSize(1);
			threadPool2.setMaxPoolSize(20);
			threadPool2.setQueueCapacity(25);
			threadPool2.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			threadPool2.initialize();
		}
	}
	
	
}
