package com.sbolo.syk.common.tools;

import org.apache.commons.lang3.StringUtils;

public class UIDGen {
	/** 开始时间截 (2017-11-06) */
    private final long twepoch = 1509976472321L;

    private final long workerIdBits = 3L;

    //最大为7
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    
    private final long timestampLeftShift = workerIdBits;

    private long workerId;
    
    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;
    
    private static class UIDGeneratorHolder {
        private static final UIDGen instance = new UIDGen();
    }
    
    private static UIDGen get(){
        return UIDGeneratorHolder.instance;
    }
    
	public static long getUID() {
		String workerId = ConfigUtils.getPropertyValue("workerId");
		if(StringUtils.isBlank(workerId)) {
			return getUID(0l);
		}
		return getUID(Long.valueOf(workerId));
	}

	public static long getUID(long workerId) {
		UIDGen generator = get();
		if (workerId > generator.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workId不能大于%d或小于0", generator.maxWorkerId));
        }
		generator.workerId = workerId;
        return generator.nextId();
	}
	
	public static String getUID(String sign) {
		return getUID(0l, sign);
	}
	
	public static String getUID(long workerId, String sign) {
		if(StringUtils.isBlank(sign)) {
			throw new RuntimeException("sign is null!");
		}
		return sign+getUID(workerId);
	}

    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    private synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("时间被回退，生成的无效时间戳%d", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则重新获取
        if (lastTimestamp == timestamp) {
            //阻塞到下一个毫秒,获得新的时间戳
            timestamp = tilNextMillis(lastTimestamp);
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) | workerId;
                
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }
}
