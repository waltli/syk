package com.sbolo.syk.common.tools;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.im4java.process.StandardStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sbolo.syk.common.constants.ContentTypeEnum;
import com.sbolo.syk.common.http.HttpUtils;

/**
 * 使用此工具类必须在服务器中安装GraphicsMagick工具
 * @author Walter
 *
 */
public class GrapicmagickUtils {
    private static String GRAPHICS_MAGICK_PATH = ConfigUtil.getPropertyValue("gm.home");
 
    private static boolean IS_WINDOWS = SystemUtils.IS_OS_WINDOWS;
    
    public static void main(String[] args) throws Exception {
    	File file = new File("E:\\11111.jpg");
    	InputStream is = new FileInputStream(file);
    	byte[] bytes = IOUtils.toByteArray(is);
    	File file2 = new File("E:\\mark.png");
    	InputStream is2 = new FileInputStream(file2);
    	byte[] markBytes = IOUtils.toByteArray(is2);
		byte[] finalBytes = watermark(bytes, markBytes);
//    	byte[] finalBytes = descale(bytes, 500, 500);
		FileUtils.saveFile(finalBytes, "D:/", "test", "png");
	}
    
    /**
     * 给图片加水印
     * @param srcPath  源图片路径
     * @param destPath 目标图片路径
     */
    public static byte[] watermark(byte[] srcBytes, byte[] markBytes) throws Exception {
    	IMOperation op = new IMOperation();
    	op.addImage("-"); // 输入或输出图片占位，表示此处的图片以流的方式获取（通过下面的setInputProvider(pipeIn)方法传入）
    	op.addImage(); // 图片占位，表示此处的图片在下面run方法中以参数的形式传入（参数要么是String类型的path，要么是BufferedImage）
    	op.addRawArgs("-gravity", "southeast"); //设置插入事物的位置，west, east, north, south, .......
    	op.addRawArgs("-dissolve", "0");
    	op.addRawArgs("+profile", "*");// 去除Exif信息，减小图片大小
    	// 此处可设置图片输出的压缩方式：
    	// 假设文件的contentType为：image/jpeg
    	// 则传入"jpeg:-"。
	    op.addImage("-"); // 输入或输出图片占位，表示此处的图片以流的方式获取（通过下面的setOutputConsumer(pipeOut)方法传入）
	    //输入
	    InputStream is = null;
	    Pipe pipeIn = null;
	    
	    //水印
	    InputStream markIs = null;
	    BufferedImage mark = null;
	    
	    //输出
	    ByteArrayOutputStream os = null;
	    Pipe pipeOut = null;
	    try {
	    	is = new ByteArrayInputStream(srcBytes); 
	    	pipeIn = new Pipe(is, null);
	    	
	    	markIs = new ByteArrayInputStream(markBytes);
	    	mark = ImageIO.read(markIs);
	    	
	    	os = new ByteArrayOutputStream();
	    	pipeOut = new Pipe(null, os);
	    	
	    	// set up command
	    	CompositeCmd convert = new CompositeCmd(true);
	    	if (IS_WINDOWS) {
        		// linux下不要设置此值，不然会报错
        		convert.setSearchPath(GRAPHICS_MAGICK_PATH);
        	}
	    	convert.setInputProvider(pipeIn);
	    	convert.setOutputConsumer(pipeOut);
	    	convert.run(op, mark);
	    	return os.toByteArray();
		} finally {
			if(is != null) {
				pipeIn = null;
				is.close();
				is = null;
			}
			
			if(markIs != null) {
				mark = null;
				markIs.close();
				markIs = null;
			}
			
			if(pipeOut != null) {
				pipeOut = null;
				os.close();
				os = null;
			}
		}
    }
    
    /**
     * 缩放图片大小
     *
     * @throws IM4JavaException
     * @throws InterruptedException
     * @throws IOException
     * @return
     */
    public static byte[] descale(byte[] bytes, Integer width, Integer height)
            throws IOException, InterruptedException, IM4JavaException {
    	StringBuffer resize = new StringBuffer("x");
    	if(width != null) {
    		resize.insert(0, width);
    	}
    	if(height != null) {
    		resize.append(height);
    	}
    	resize.append(">"); //加了>,表示只有当图片的宽与高，大于给定的宽与高时，才进行“缩小”操作。 
    	IMOperation op = new IMOperation();
        op.addImage("-"); // 命令：从输入流中读取图片
        op.addRawArgs("-resize", resize.toString());// 按照给定比例缩放图片
        op.addRawArgs("-gravity", "center"); // 缩放参考位置 对图像进行定位
//        op.addRawArgs("-extent", width + "x" + height); // 限制JPEG文件的最大尺寸，会留白边
        op.addRawArgs("+profile", "*");// 去除Exif信息，减小图片大小
        op.addImage("-");
        InputStream is = null;
        ByteArrayOutputStream os = null;
        Pipe pipeIn = null;
        Pipe pipeOut = null;
        try {
        	is = new ByteArrayInputStream(bytes); 
        	os = new ByteArrayOutputStream();
        	pipeIn = new Pipe(is, null);
        	pipeOut = new Pipe(null, os);
        	ConvertCmd cmd = new ConvertCmd(true);
        	if (IS_WINDOWS) {
        		// linux下不要设置此值，不然会报错
        		cmd.setSearchPath(GRAPHICS_MAGICK_PATH);
        	}
        	cmd.setInputProvider(pipeIn);
        	cmd.setOutputConsumer(pipeOut);
        	cmd.run(op);
        	return os.toByteArray();
		} finally {
			if(is != null) {
				pipeIn = null;
				is.close();
				is = null;
			}
			if(pipeOut != null) {
				pipeOut = null;
				os.close();
				os = null;
			}
		}
    }
}
