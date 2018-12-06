package com.sbolo.syk.fetch.tool;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frostwire.jlibtorrent.FileStorage;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.TorrentInfo;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.tools.ConfigUtils;
import com.sbolo.syk.common.tools.FileUtils;
import com.sbolo.syk.common.tools.FtpUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.vo.LinkAnalyzeResultVO;
import com.sbolo.syk.fetch.spider.exception.AnalystException;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings({ "restriction" })
public class LinkAnalyst {
	private static final Logger log = LoggerFactory.getLogger(LinkAnalyst.class);
	private static String localPath = "d:/tempFile/";
	
	public static LinkAnalyzeResultVO analyseDownloadLink(String downloadLink, String decoding) throws Exception{
		if(StringUtils.isBlank(downloadLink)){
			throw new AnalystException("The analytic source is null");
		}
		if(Pattern.compile(RegexConstant.ed2k).matcher(downloadLink).find()){
			return analyseEd2k(downloadLink);
		}else if(Pattern.compile(RegexConstant.thunder).matcher(downloadLink).find()){
			return analyseThunder(downloadLink, decoding);
		}else if(Pattern.compile(RegexConstant.torrent).matcher(downloadLink).find()){
			return analyseTorrent(downloadLink);
		}else if(Pattern.compile(RegexConstant.magnet).matcher(downloadLink).find()){
			return analyseMagnet2(downloadLink);
		}
		else {
			return null;
		}
	}
	
	private static LinkAnalyzeResultVO analyseTorrent(String torrentUrl) throws Exception{
		byte[] torrentBytes = HttpUtils.getBytes(torrentUrl);
		TorrentInfo ti = new TorrentInfo(torrentBytes);
		FileStorage fs = ti.files();
		LinkAnalyzeResultVO mri = new LinkAnalyzeResultVO();
		for(int i=0; i<fs.numFiles(); i++){
			String fileName = fs.fileName(i);
			Matcher m = Pattern.compile(RegexConstant.format_end).matcher(fileName);
			if(m.find()){
				mri.setMovieFormat(m.group().substring(1));
			}
		}
		mri.setMovieSize(FileUtils.unitUp(ti.totalSize()+""));
		mri.setTorrentBytes(torrentBytes);
		mri.setDownloadLink(torrentUrl);
		torrentBytes = null;
		ti = null;
		return mri;
	}
	
	private static LinkAnalyzeResultVO analyseThunder(String thunderLink, String decoding) throws Exception {
		if(StringUtils.isBlank(decoding)){
			throw new AnalystException("\"decoding\" can't null!");
		}
		String decoderCode = thunderLink.substring(10); //前10位thunder://
		BASE64Decoder decoder = new BASE64Decoder();
        String AArealLinkZZ = new String(decoder.decodeBuffer(decoderCode),decoding);
        String realLink = AArealLinkZZ.substring(2, AArealLinkZZ.length()-2); //thunder規則，前兩位AA，后兩位ZZ
        return analyseDownloadLink(realLink, decoding);
	}
	
	private static LinkAnalyzeResultVO analyseEd2k(String ed2kLink){
		String[] ed2kLinkSplit = ed2kLink.split("\\|");
		LinkAnalyzeResultVO mri = null;
		if(ed2kLinkSplit.length > 3){
			String fileName = ed2kLinkSplit[2];
			Matcher m = Pattern.compile(RegexConstant.format_end).matcher(fileName);
			mri = new LinkAnalyzeResultVO();
			if(m.find()){
				mri.setMovieFormat(m.group().substring(1));
			}
			mri.setMovieSize(FileUtils.unitUp(ed2kLinkSplit[3]));
		}
		return mri;
	}
	
	private static LinkAnalyzeResultVO analyseMagnet2(String magnetLink){
		String movieFormat = null;
		String movieSize = null;
		Matcher m = Pattern.compile(RegexConstant.magnet_name).matcher(magnetLink);
		if(m.find()) {
			String movieName = m.group();
			m = Pattern.compile(RegexConstant.format).matcher(movieName);
			if(m.find()){
				movieFormat = m.group();
			}
		}
		
		Matcher m2 = Pattern.compile(RegexConstant.magnet_size).matcher(magnetLink);
		if(m2.find()) {
			//若此处未获取到，则在处理链接的时候再次获取
			String magenetSize = m2.group();
			movieSize = FileUtils.unitUp(magenetSize);
		}
		
		if(StringUtils.isNotBlank(movieFormat) || StringUtils.isNotBlank(movieSize)) {
			LinkAnalyzeResultVO mri = new LinkAnalyzeResultVO();
			mri.setDownloadLink(magnetLink);
			mri.setMovieFormat(movieFormat);
			mri.setMovieSize(movieSize);
			return mri;
		}
		return null;
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
    	mri.setMovieSize(FileUtils.unitUp(ti.totalSize()+""));
        
        s.stop();
        return mri;
	}
	
	private static LinkAnalyzeResultVO analyseFTP(String ftpLink){
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
			mri.setMovieSize(FileUtils.unitUp(fileSizeB));
		}
		return mri;
	}
	
//	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
////		URL resource = ClassLoader.class.getResource("/dll11");
////		String path = resource.getPath();
////		System.setProperty("java.library.path", path+";"+System.getProperty("java.library.path"));
////        Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
////        fieldSysPath.setAccessible(true);
////        fieldSysPath.set(null, null);
//        System.out.println(System.getProperty("java.library.path"));
////        System.loadLibrary("jlibtorrent-1.2.0.18-RC8");
////		System.setProperty("jlibtorrent.jni.path", "D:\\_dev\\lib\\jlibtorrent-1.2.0.18-RC8.dll");
//		TorrentInfo ti = new TorrentInfo(new File("D:/test.torrent"));
//		System.out.println();
//	}
}
