package com.sbolo.syk.common.javassist;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.internal.compiler.ast.MemberValuePair;

import com.mysql.fabric.xmlrpc.base.Member;
import com.sbolo.syk.common.tools.ReflectionUtils;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.ByteMemberValue;
import javassist.bytecode.annotation.CharMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.DoubleMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.FloatMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.ShortMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import net.sf.cglib.beans.BeanMap;

/**
 *使用javassit动态生成一个java类
 * @author yy
 * @version 1.0
 * 
 */
public class JavassistBean {
	
	/**
	 * 实体Object
	 */
	private Object object;
	private String packagePath;
	private String className;
	
	public JavassistBean(List<JavassistField> fields) throws Exception {
		this("com.sbolo.syk.common.javassist", "Abc"+Math.random()*100, fields);
	}
	
	public JavassistBean(String className, List<JavassistField> fields) throws Exception {
		this("com.sbolo.syk.common.javassist", className, fields);
	}
	
	public JavassistBean(String packagePath, String className, List<JavassistField> fields) throws Exception {
		this.className = className;
		this.packagePath = packagePath;
		this.object = generateBean(fields);
		
		fields.forEach(fi -> {
			if(fi.getValue() == null){
				return;
			}
			// 为属性赋值
			ReflectionUtils.setFieldValue(this.object, fi.getFieldName(), fi.getValue());
		});
	}
	
	/**
	 * 得到该实体bean对象
	 * @return
	 */
	public Object getObject() {
		return this.object;
	}
	
	
	@SuppressWarnings("unchecked")
	private Object generateBean(List<JavassistField> fields) throws Exception{
		//ClassPool：CtClass对象的容器
		ClassPool pool = ClassPool.getDefault();
		
		//通过ClassPool生成一个public新类Emp.java
		CtClass ctClass = pool.makeClass(this.packagePath+"."+this.className);
		ClassFile ccFile = ctClass.getClassFile();
        ConstPool constpool = ccFile.getConstPool();
		
      //添加属性、setter、getter、annotation
        for(JavassistField fi : fields){
        	//创建属性
        	String fieldName = fi.getFieldName();
        	CtField field = new CtField(pool.getCtClass(fi.getClazz().getName()), fieldName, ctClass);
        	field.setModifiers(Modifier.PRIVATE);
    		ctClass.addField(field);
    		
    		//为field创建annotation
    		AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
    		List<java.lang.annotation.Annotation> annotations = fi.getAnnotations();
    		if(annotations != null && annotations.size() > 0){
    			annotations.forEach(annotation -> {
        			Annotation annot = new Annotation(annotation.annotationType().getName(), constpool);
        			InvocationHandler invo = Proxy.getInvocationHandler(annotation);
        			Object fieldValue = ReflectionUtils.getFieldValue(invo, "memberValues");
        			if(!(fieldValue instanceof Map)){
        				return;
        			}
        			((Map<String, Object>) fieldValue).forEach((k,v) -> {
        				annot.addMemberValue(k, getMemberValue(constpool, v));
        			});
        			attr.addAnnotation(annot);
        		});
    			field.getFieldInfo().addAttribute(attr);
    		}
    		
    		//创建setter、getter
    		String fieldUp = fieldName.substring(0,1).toUpperCase() + fieldName.replaceAll("(^\\w{1})", "");
    		CtMethod setter = CtNewMethod.setter("set"+fieldUp, field);
    		CtMethod getter = CtNewMethod.getter("get"+fieldUp, field);
    		ctClass.addMethod(setter);
    		ctClass.addMethod(getter);
    		
        }
        
		Class<?> clazz = ctClass.toClass();
		Object obj = clazz.newInstance();
		return obj;
	}
	
	private MemberValue getMemberValue(ConstPool constpool, Object memberValue){
		if(memberValue == null){
			return null;
		}
		
		if(memberValue instanceof Annotation){
			return new AnnotationMemberValue((Annotation) memberValue, constpool);
		}else if(memberValue instanceof Boolean){
			return new BooleanMemberValue((Boolean) memberValue, constpool);
		}else if(memberValue instanceof Byte){
			return new ByteMemberValue((Byte) memberValue, constpool);
		}else if(memberValue instanceof Character){
			return new CharMemberValue((Character) memberValue, constpool);
		}else if(memberValue instanceof Double){
			return new DoubleMemberValue((Double) memberValue, constpool);
		}else if(memberValue instanceof Float){
			return new FloatMemberValue((Float) memberValue, constpool);
		}else if(memberValue instanceof Integer){
			return new IntegerMemberValue((Integer) memberValue, constpool);
		}else if(memberValue instanceof Long){
			return new LongMemberValue((Long) memberValue, constpool);
		}else if(memberValue instanceof Short){
			return new ShortMemberValue((Short) memberValue, constpool);
		}else if(memberValue instanceof String){
			return new StringMemberValue((String) memberValue, constpool);
		}else if(memberValue instanceof Class){
			return new ClassMemberValue(memberValue.getClass().getName(), constpool);
		}else if(memberValue.getClass().isArray()){
			ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constpool);
			Object[] objs = (Object[]) memberValue;
			MemberValue[] mvs = new MemberValue[objs.length];
			for(int i=0; i<objs.length; i++){
				Object obj = objs[i];
				MemberValue e = getMemberValue(constpool, obj);
				mvs[i] = e;
			}
			arrayMemberValue.setValue(mvs);
			return arrayMemberValue;
		}else if(memberValue.getClass().isEnum()){
			EnumMemberValue enumMemberValue = new EnumMemberValue(constpool);
			enumMemberValue.setValue(((Enum<?>) memberValue).name());
			return enumMemberValue;
		}
		return null;
	}
	
}
