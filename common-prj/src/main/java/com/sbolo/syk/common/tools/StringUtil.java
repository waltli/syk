package com.sbolo.syk.common.tools;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RegexConstant;

public class StringUtil {
	public static String toString(Object o) {
		if (o == null)
			return "";
		else
			return o.toString();
	}

	/**
	 * 去除各式各样的空白串
	 * 
	 * @param str
	 * @return
	 */
	public static String trimAll(String str) {
		String dest = "";
		if (str != null) {
			// 倒数第二个空格与空格键打出来的空格不一样！
			Pattern p = Pattern.compile(RegexConstant.blank);
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 去除头尾的空白串加强
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return Pattern.compile("^(" + RegexConstant.blank + ")|(" + RegexConstant.blank + ")$").matcher(str)
				.replaceAll("");
	}

	/**
	 * 所有空格替换为标准空格
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceNomalBlank(String str) {
		String dest = "";
		if (str != null) {
			// 倒数第二个空格与空格键打出来的空格不一样！
			Pattern p = Pattern.compile(RegexConstant.blank);
			Matcher m = p.matcher(str);
			dest = m.replaceAll(" ");
		}
		return dest;
	}

	public static String jointDoubanUrl(String doubanId) {
		return "https://movie.douban.com/subject/" + doubanId + "/";
	}

	/**
	 * 去除字符串的“-”和空串
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank2(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("-|\\s");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static String randomString(int len) {
		String randomSource = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			int number = new Random().nextInt(62);
			sb.append(randomSource.charAt(number));
		}
		return sb.toString();
	}

	public static String StringLimit(String str, int limit) {
		int strLen = str.length();

		double count = 0.0d;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < strLen; i++) {
			int asciicode = str.codePointAt(i);
			if (asciicode > 255) {
				count = count + 1.0d;
			} else {
				count = count + 0.5d;
			}
			if (count > limit) {
				sb.append("...");
				break;
			}
			sb.append(str.charAt(i));
		}
		return sb.toString();
	}
	
	public static Boolean isLocalLink(String link){
		return link.contains(CommonConstants.local_sign);
	}
	
	public static String getId(String sign){
		return sign + UIDGenerator.getUID();
	}
	
	public static Boolean isHttp(String str) {
		if(str.startsWith("http://") || str.startsWith("https://")) {
			return true;
		}
		return false;
	}
}
