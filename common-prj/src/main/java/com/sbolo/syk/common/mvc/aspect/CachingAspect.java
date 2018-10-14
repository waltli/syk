package com.sbolo.syk.common.mvc.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.sbolo.syk.common.annotation.CacheAvl;
import com.sbolo.syk.common.annotation.CacheDel;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.Utils;

@Aspect
@Component
public class CachingAspect {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Around("@annotation(com.sbolo.syk.common.annotation.CacheAvl) || @annotation(com.sbolo.syk.common.annotation.CacheDel)")
	public Object caching(ProceedingJoinPoint point) throws Throwable {
		Signature signature = point.getSignature();
		Object[] params = point.getArgs();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method targetMethod = methodSignature.getMethod();
		Object obj = null;
		CacheAvl cacheAvl = targetMethod.getAnnotation(CacheAvl.class);
		CacheDel cacheDel = targetMethod.getAnnotation(CacheDel.class);

		if (cacheAvl != null) {
			String key = cacheAvl.key();
			String hKey = cacheAvl.hKey();
			if (null != params && params.length > 1) {
				if (StringUtils.isNotEmpty(key) && key.startsWith("#")) {
					key = String.valueOf(params[0]);
				}
				if (StringUtils.isNotEmpty(hKey) && hKey.startsWith("#")) {
					hKey = String.valueOf(params[1]);
				}
			}
			long timeout = cacheAvl.timeout();  //默认为7
			TimeUnit unit = cacheAvl.timeUnit();   //默认为天
			String dateStr = cacheAvl.expireAt();
			String format = cacheAvl.format();
			boolean freshExpire = cacheAvl.freshExpire();
			if (timeout > 0 && StringUtils.isBlank(key)) {
				throw new Exception("key, timeout必须同时设置。");
			}
			if (StringUtils.isNotBlank(dateStr) && StringUtils.isBlank(key)) {
				throw new Exception("key, expireAt必须同时设置。");
			}
			if (StringUtils.isNotBlank(dateStr) && StringUtils.isBlank(format)) {
				throw new Exception("expireAt, format必须同时设置。");
			}
			if (StringUtils.isBlank(key)) {
				key = point.getTarget().getClass().getName();
			}
			if (StringUtils.isBlank(hKey)) {
				String args = Arrays.toString(point.getArgs());
				hKey = signature.toLongString() + args;
			}
			BoundHashOperations<Object, Object, Object> boundHashOps = redisTemplate.boundHashOps(key);

			obj = boundHashOps.get(hKey);
			if (obj == null) {
				obj = point.proceed();
				boundHashOps.put(hKey, obj);

				if (timeout > 0) {
					boundHashOps.expire(timeout, unit);
				}
				if (StringUtils.isNotBlank(dateStr)) {
					Date date = DateUtil.str2Date(dateStr, format);
					boundHashOps.expireAt(date);
				}
			}

			//更新有效期。
			if (freshExpire) {
				boundHashOps.expire(timeout, unit);
			}
		} else if (cacheDel != null) {
			obj = point.proceed();
			String key = cacheDel.key();
			if (StringUtils.isBlank(key)) {
				key = point.getTarget().getClass().getName();
			}
			redisTemplate.delete(key);
		} else {
			obj = point.proceed();
		}
		return obj;
	}

}
