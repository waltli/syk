package com.sbolo.syk.fetch.spider;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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

import com.sbolo.syk.fetch.spider.exception.SpiderException;

public class Spider {
	private static final Logger log = LoggerFactory.getLogger(Spider.class);
	
	private List<Pipeline> listPipeline;
	
	private Distinct distinct;
	
	private Downloader downloader;
	
	private List<PageProcessor> listProcessor;
	
	private BlockingQueue<String> queue;
	
	private AtomicInteger threadAlive;
	
	private ThreadPoolExecutor threadPool;
	
	private ReentrantLock newUrlLock;
	
	private Condition newUrlCondition;
	
	private Set<String> urlsSet;
	
	private Map<String, Object> fields;
	
	private boolean isRun = false;
	
	public static int runCount = 0;
	
	private int emptySleepTime = 30000;
	public Spider() {}
	public Spider(List<PageProcessor> listProcessor, Distinct distinct, List<Pipeline> listPipeline) {
		this.listProcessor = listProcessor;
		this.distinct = distinct;
		this.listPipeline = listPipeline;
	}
	
	public Spider(List<PageProcessor> listProcessor, Distinct distinct, List<Pipeline> listPipeline, Downloader downloader) {
		this.listProcessor = listProcessor;
		this.distinct = distinct;
		this.listPipeline = listPipeline;
		this.downloader = downloader;
	}
	
	public List<Pipeline> getListPipeline() {
		return listPipeline;
	}
	public void setListPipeline(List<Pipeline> listPipeline) {
		this.listPipeline = listPipeline;
	}
	public Distinct getDistinct() {
		return distinct;
	}
	public void setDistinct(Distinct distinct) {
		this.distinct = distinct;
	}
	public Downloader getDownloader() {
		return downloader;
	}

	public void setDownloader(Downloader downloader) {
		this.downloader = downloader;
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
			init();
			Random random = new Random();
			for(int i=0; i<listProcessor.size(); i++){
				final PageProcessor curProcessor = listProcessor.get(i);
				try {
					log.info("===============================进入{}================================", curProcessor.getClass().getSimpleName());
					curProcessor.before();
					//第一次填充开始url
					queue.put(curProcessor.startUrl());
					while(true){
						final String url = queue.poll();
						if(url == null){
							if(threadAlive.get() == 0){ //当前运行线程个数为0则跳出循环
								log.info(curProcessor.getClass().getSimpleName()+"网页遍历结束");
								break;
							}
							waitNewUrl();
						}else {
//							threadAlive.incrementAndGet(); //运行线程数加一
//							threadPool.execute(new Runnable() {
//								@Override
//								public void run() {
									try {
										Thread.sleep(10000);
										toProcess(curProcessor, url);
									} catch (UnknownHostException | SocketTimeoutException | SocketException e){
										//the exception of time out that do nothing
									} catch (SpiderException e) {
										log.warn(e.getMessage());
									} catch (Exception e) {
										log.error(url,e);
									} finally{
//										threadAlive.decrementAndGet(); //the thread of running was subtracted when it run end
									}
//								}
//							});
						}
					}
				} catch(Exception e){
					log.error("", e);
				} finally {
					curProcessor.after();
				}
			}
			if(fields == null || fields.size() == 0) {
				return;
			}
			try {
				distinct.before();
				distinct.process(fields);
			} finally {
				distinct.after();
			}
			if(fields == null || fields.size() == 0) {
				return;
			}
			for(int i=0; i<listPipeline.size(); i++) {
				Pipeline pipeline = listPipeline.get(i);
				try {
					pipeline.before();
					pipeline.process(fields);
				} finally {
					pipeline.after();
				}
			}
		} finally{
			isRun = false;
			this.destroy();
			log.info("spider执行结束！");
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
		if(queue != null) {
			queue.clear();
			queue = null;
		}
		
		if(urlsSet != null) {
			urlsSet.clear();
			urlsSet = null;
		}
		
		if(fields != null) {
			fields.clear();
			fields = null;
		}
		
		threadAlive = null;
		newUrlCondition = null;
		newUrlLock = null;
	}
	
	private void init(){
		queue = new LinkedBlockingQueue<String>();
		
		threadAlive = new AtomicInteger();
		
		newUrlLock = new ReentrantLock();
		
		newUrlCondition = newUrlLock.newCondition();
		
		urlsSet = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
		
		fields = new LinkedHashMap<String, Object>();
		
		if(downloader == null){
			downloader = new Downloader();
		}
		if(threadPool == null){
//			threadPool = new ThreadPoolExecutor(1, 20, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(25), new ThreadPoolExecutor.CallerRunsPolicy());
			//线程数不能开的太多，否则短时间内请求过多，网站会认为是攻击。
			threadPool = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(5), new ThreadPoolExecutor.CallerRunsPolicy());
			threadPool.allowCoreThreadTimeOut(true);
		}
	}
	
	
}
