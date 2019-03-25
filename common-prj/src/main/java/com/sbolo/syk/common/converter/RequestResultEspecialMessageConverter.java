package com.sbolo.syk.common.converter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.common.ui.ResultApi;

public class RequestResultEspecialMessageConverter<T> extends AbstractHttpMessageConverter<T> implements GenericHttpMessageConverter<T> {
	private static final Logger LOG = LoggerFactory.getLogger(RequestResultEspecialMessageConverter.class);

	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		return false;
	}

	@Override
	public T read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		return null;
	}

	@Override
	public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
		if(!this.supports(clazz)){
			return false;
		}
//		if(type instanceof ParameterizedTypeImpl){
//			ParameterizedTypeImpl typeImpl = (ParameterizedTypeImpl) type;
//			Type argType = typeImpl.getActualTypeArguments()[0];
//			if(argType.getTypeName().equals(byte[].class.getTypeName())){
//				return true;
//			}
//		}else {
			String typeName = type.getTypeName();
			Pattern p = Pattern.compile("RequestResult<byte\\[\\]>");
			Matcher m = p.matcher(typeName);
			if(m.find()){
				return true;
			}
//		}
		return false;
	}

	@Override
	public void write(T t, Type type, MediaType contentType, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		HttpHeaders headers = outputMessage.getHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		byte[] bytes = Utils.objectToByte(t);
		outputMessage.getBody().write(bytes);
	}
	
	@Override
	public List<MediaType> getSupportedMediaTypes() {
		List<MediaType> mediaTypeList = new ArrayList<>();
		mediaTypeList.add(new MediaType("application", "json", Charset.forName("utf-8")));
		mediaTypeList.add(new MediaType("application", "*+json", Charset.forName("utf-8")));
		return mediaTypeList;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(ResultApi.class);
	}

	@Override
	protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		return null;
	}

	@Override
	protected void writeInternal(T t, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
	}


	
}
