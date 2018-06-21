package com.sbolo.syk.common.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frostwire.jlibtorrent.FileStorage;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.TorrentInfo;
import com.frostwire.jlibtorrent.swig.file_storage;
import com.frostwire.jlibtorrent.swig.string_view;
import com.frostwire.jlibtorrent.swig.torrent_info;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.exception.AnalystException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.vo.LinkAnalyzeResultVO;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class LinkAnalyst {
	private static final Logger log = LoggerFactory.getLogger(LinkAnalyst.class);
	private static String localPath = "d:/tempFile/";
	
	public static LinkAnalyzeResultVO analysis(String downloadLink, String thunderDecoding) throws Exception{
		if(StringUtils.isBlank(downloadLink)){
			throw new AnalystException("The download link is null");
		}
		if(Pattern.compile(RegexConstant.ed2k).matcher(downloadLink).find()){
			return analyseEd2k(downloadLink);
		}else if(Pattern.compile(RegexConstant.magnet).matcher(downloadLink).find()){
			return analyseMagnet(downloadLink);
		}else if(Pattern.compile(RegexConstant.thunder).matcher(downloadLink).find()){
			return analystThunder(downloadLink, thunderDecoding);
		}else if(Pattern.compile(RegexConstant.torrent).matcher(downloadLink).find()){
			return analyseTorrent(downloadLink);
		}else {
			return null;
		}
	}
	
	private static LinkAnalyzeResultVO analystThunder(String thunderLink, String thunderDecoding) throws Exception {
		if(StringUtils.isBlank(thunderDecoding)){
			throw new AnalystException("\"thunderDecoding\" can't null!");
		}
		String decoderCode = thunderLink.substring(10); //前10位thunder://
		BASE64Decoder decoder = new BASE64Decoder();
        String AArealLinkZZ = new String(decoder.decodeBuffer(decoderCode),thunderDecoding);
        String realLink = AArealLinkZZ.substring(2, AArealLinkZZ.length()-2); //thunder規則，前兩位AA，后兩位ZZ
        return analysis(realLink, thunderDecoding);
	}
	
	private static LinkAnalyzeResultVO analyseFTP(String ftpLink){
		//ftp://ygdy8:ygdy8@y219.dydytt.net:9250/[阳光电影www.ygdy8.com].流浪猫鲍勃.BD.720p.中英双字幕.mkv
		int atIndex = ftpLink.indexOf("@");
		String account = ftpLink.substring(6, atIndex);  //6之前是协议名字 "ftp://"
		String[] accountSplit = account.split(":");
		String username = accountSplit[0];
		String password = accountSplit[1];
		int firstSeparatorIndex = ftpLink.indexOf("/", atIndex);
		String ipAndPort = ftpLink.substring(atIndex+1, firstSeparatorIndex);
		String[] ipAndPortSplit = ipAndPort.split(":");
		String ip = ipAndPortSplit[0];
		int port = Integer.valueOf(ipAndPortSplit[1]);
		String remotePath = "/";
		int lastSeparatorIndex = ftpLink.lastIndexOf("/");
		if(lastSeparatorIndex != firstSeparatorIndex){
			remotePath = ftpLink.substring(firstSeparatorIndex, lastSeparatorIndex);
		}
		String fileName = ftpLink.substring(lastSeparatorIndex+1);
		FTPFile[] files = FtpUtils.getFiles(ip, port, username, password, remotePath, fileName, localPath);
		LinkAnalyzeResultVO mri = null;
		if(files != null){
			mri = new LinkAnalyzeResultVO();
			Matcher m = Pattern.compile(RegexConstant.format_end).matcher(fileName);
			if(m.find()){
				mri.setMovieFormat(m.group().substring(1));
			}
			String fileSizeB = null;
			for(int i=0; i<files.length; i++){
				fileSizeB += files[i].getSize();
			}
			mri.setMovieSize(getCalc(fileSizeB));
		}
		return mri;
	}
	
	public static LinkAnalyzeResultVO analyseEd2k(String ed2kLink){
		//ed2k://|file|The.Flash.S03E01.720p.FIX%E5%AD%97%E5%B9%95%E4%BE%A0.mkv|698789919|577175B79ABA68B0917BFF1FD8BCBADE|h=DWEJJPUDQKJ2HEZZ5J7BGRVOM34SBFAE|/
		String[] ed2kLinkSplit = ed2kLink.split("\\|");
		LinkAnalyzeResultVO mri = null;
		if(ed2kLinkSplit.length > 3){
			String fileName = ed2kLinkSplit[2];
			Matcher m = Pattern.compile(RegexConstant.format_end).matcher(fileName);
			mri = new LinkAnalyzeResultVO();
			if(m.find()){
				mri.setMovieFormat(m.group().substring(1));
			}
			mri.setMovieSize(getCalc(ed2kLinkSplit[3]));
		}
		return mri;
	}
	
	private static String getCalc(String source){
        if(source != null){
        	BigDecimal val = new BigDecimal(source);
        	BigDecimal s = new BigDecimal(1024);
        	String unit = "B";
        	if(val.compareTo(s)>0){
        		val = val.divide(s);
        		unit = "KB";
        	}
        	if(val.compareTo(s)>0){
        		val = val.divide(s);
        		unit = "MB";
        	}
        	if(val.compareTo(s)>0){
        		val = val.divide(s);
        		unit = "GB";
        	}
        	return val.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+unit;
        }else {
        	return null;
        }
		
	}
	
	private static TorrentInfo getTorrentInfo(final String torrentUrl) throws Exception{
		TorrentInfo ti = null;
		if(StringUtil.isLocalLink(torrentUrl)){
			String dir = ConfigUtil.getPropertyValue("torrentDir");
			File torrent = new File(dir+File.separator+torrentUrl);
			ti = new TorrentInfo(torrent);
		}else {
			HttpResult<TorrentInfo> result = HttpUtils.httpGet(torrentUrl, new HttpSendCallback<TorrentInfo>() {
				@Override
				public TorrentInfo onResponse(Response response) throws Exception {
					if(!response.isSuccessful()){
						throw new AnalystException("Return Code："+response.code()+" when analyse torrent. link: "+torrentUrl);
					}
					String contentType = response.header("Content-type");
					if(!contentType.equals("application/octet-stream") && !contentType.equals("application/x-bittorrent")){
						throw new AnalystException("解析种子返回的流文件不是标准bt流!");
					}
					byte[] bytes = response.body().bytes();
					
					return new TorrentInfo(bytes);
				}
			});
			ti = result.getValue();
		}
		return ti;
	}
	
	private static LinkAnalyzeResultVO analyseTorrent(String torrentUrl) throws Exception{
		LinkAnalyzeResultVO mri = null;
		try {
			TorrentInfo ti = getTorrentInfo(torrentUrl);
			FileStorage fs = ti.files();
			mri = new LinkAnalyzeResultVO();
			for(int i=0; i<fs.numFiles(); i++){
				String fileName = fs.fileName(i);
				Matcher m = Pattern.compile(RegexConstant.format_end).matcher(fileName);
				if(m.find()){
					mri.setMovieFormat(m.group().substring(1));
				}
			}
			mri.setMovieSize(getCalc(ti.totalSize()+""));
			return mri;
		} catch (Exception e) {
			if(e instanceof IllegalArgumentException){
				throw new AnalystException("解析种子文件失败！请检查种子文件是否可用！");
			}
			throw e;
		}
	}
	
	public static LinkAnalyzeResultVO analyseTorrent(byte[] bytes) throws Exception{
		LinkAnalyzeResultVO mri = null;
		try {
			TorrentInfo ti = new TorrentInfo(bytes);
			FileStorage fs = ti.files();
			mri = new LinkAnalyzeResultVO();
			for(int i=0; i<fs.numFiles(); i++){
				String fileName = fs.fileName(i);
				Matcher m = Pattern.compile(RegexConstant.format_end).matcher(fileName);
				if(m.find()){
					mri.setMovieFormat(m.group().substring(1));
				}
			}
			mri.setMovieSize(getCalc(ti.totalSize()+""));
			return mri;
		} catch (Exception e) {
			if(e instanceof IllegalArgumentException){
				throw new AnalystException("解析种子文件失败！请检查种子文件是否可用！");
			}
			throw e;
		}
	}
	
	private static LinkAnalyzeResultVO analyseMagnet(String magnetLink) throws InterruptedException, IOException{
		final SessionManager s = new SessionManager();
        s.start();
        
        final CountDownLatch signal = new CountDownLatch(1);

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long nodes = s.stats().dhtNodes();
                // wait for at least 10 nodes in the DHT.
                if (nodes >= 2) {
                    log.info("DHT contains " + nodes + " nodes");
                    signal.countDown();
                    timer.cancel();
                }
            }
        }, 0, 1000);

//        log.info("Waiting for nodes in DHT (30 seconds)...");
        boolean r = signal.await(30, TimeUnit.SECONDS);
        if (!r) {
        	log.info("DHT bootstrap timeout");
        	return null;
//            System.exit(0);
        }

//        log.info("Fetching the magnet uri, please wait...");
        LinkAnalyzeResultVO mri = null;
        byte[] bytes = s.fetchMagnet(magnetLink, 110);
		TorrentInfo ti = new TorrentInfo(bytes);
    	FileStorage fs = ti.files();
    	mri = new LinkAnalyzeResultVO();
    	for(int i=0; i<fs.numFiles(); i++){
    		String fileName = fs.fileName(i);
    		Matcher m = Pattern.compile(RegexConstant.format_end).matcher(fileName);
    		if(m.find()){
    			mri.setMovieFormat(m.group().substring(1));
    		}
	    }
    	mri.setMovieSize(getCalc(ti.totalSize()+""));
        
        s.stop();
        return mri;
	}
	
	public static void main(String[] args) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
        String AArealLinkZZ = new String(decoder.decodeBuffer("QUFodHRwOi8vZGwxMzIuODBzLmltOjkyMC8xNzA3L1vmpZrkuZTkvKBd56ysNTbpm4YvW+almuS5lOS8oF3nrKw1Numbhl9iZC5tcDRaWg=="), "utf-8");
        System.out.println(AArealLinkZZ);
	}
}
