package com.sbolo.syk.common.tools;

/**
 * 0000 0000 0000 0000
 * 高级管理员 管理员 用户 黑名单
 * @author Walter
 *
 */
public class RoleManageUtils {
	
	public static void main(String[] args) {
		Long a = 32768L;
		Long b = 256L;
		
		long c = calcPower(a, b);
		
		System.out.println(c);
		String binaryString = Long.toBinaryString(c);
//		long parseInt = Long.parseLong("1000000000000000", 2);
		System.out.println(binaryString);
	}
	
	public static Long calcPower(Long... decArr) {
		Long result = 0L;
		if(decArr == null || decArr.length == 0) {
			return result;
		}
		for(Long dec : decArr) {
			if(dec == null) {
				continue;
			}
			result = result | dec;
		}
		return result;
	}
}
