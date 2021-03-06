package com.feinno.serialization.protobuf.generator;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import com.feinno.serialization.Serializer;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoManager;
import com.feinno.serialization.protobuf.util.ClassUtils;
import com.feinno.serialization.protobuf.util.FileUtil;
import com.feinno.serialization.protobuf.util.StringTemplateLoader;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * <b>描述: </b>用于{@link 用于ProtoEntity}类的代码生成
 * <p>
 * <b>功能: </b>这个类的功能主要是为Java原生类型例如String、List、Map等的直接序列化提供支持，他会自动将这些类型的Java类包装在
 * {@link ProtoEntity}中,将这些类型装饰成一个{@link ProtoEntity}
 * ，再交由正常的protobuf序列化处理逻辑来进行处理。
 * <p>
 * <b>用法: </b>序列化组件内部使用，通过{@link Serializer}或{@link ProtoManager}
 * 来进行Java原生类型的进行序列化时就会自动调用到
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class ProtoEntityCodeGenerator {

	/**
	 * 获得生成的JavaBean源码列表
	 * 
	 * @param clazz
	 *            这个JavaBean中都有哪些属性字段
	 * @return
	 */
	public static <T extends Object> String getProtoEntityBeanCode(T t, Class<?>... genericClass) throws IOException,
			TemplateException {
		Class<?> clazz = t.getClass();
		if ((t instanceof java.util.List) && genericClass.length > 0) {
			clazz = java.util.List.class;
		} else if ((t instanceof java.util.Map) && genericClass.length > 1) {
			clazz = java.util.Map.class;
		}
		String beanName = getProtoEntityBeanName(t, genericClass);
		ProtoJavaBeanParam templateParam = getTemplateParam(beanName, clazz, genericClass);
		// 如果是List,那么需要增加泛型信息
		if ((t instanceof java.util.List) && genericClass.length > 0) {
			templateParam.setFieldType(clazz.getName() + "<" + ClassUtils.processClassName(genericClass[0].getName())
					+ ">");
		} else if ((t instanceof java.util.Map) && genericClass.length > 1) {
			templateParam.setFieldType(clazz.getName() + "<" + ClassUtils.processClassName(genericClass[0].getName())
					+ "," + ClassUtils.processClassName(genericClass[1].getName()) + ">");
		}
		Configuration cfg = new Configuration();
		cfg.setTemplateLoader(new StringTemplateLoader(ProtoConfig.PROTO_NATIVE_ENTITY_CODE_TEMPLATE));
		cfg.setDefaultEncoding("UTF-8");
		Template template = cfg.getTemplate("");
		StringWriter writer = new StringWriter();
		template.process(templateParam, writer);

		// 这个测试代码是将生成的内容写成文件保存至某一位置
		if (ProtoManager.isDebug()) {
			File dirFile = new File(ProtoManager.SOURCE_SAVE_PATH);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			FileUtil.write(writer.toString(), ProtoManager.SOURCE_SAVE_PATH + beanName + ".java");
		}

		return writer.toString();
	}

	/**
	 * 获得用于填充模板中需要使用到的参数
	 * 
	 * @param beanName
	 * @param clazz
	 * @param genericClass
	 * @return
	 */
	private static <T extends Object> ProtoJavaBeanParam getTemplateParam(String beanName, Class<?> clazz,
			Class<?>... genericClass) {

		ProtoJavaBeanParam templateParam = new ProtoJavaBeanParam();
		templateParam.setPackageName(getPackage(clazz, genericClass));
		templateParam.setProtoPath(ProtoConfig.PACKAGE_CORE);
		templateParam.setClassName(beanName);

		StringBuilder fieldNameBuilder = new StringBuilder(clazz.getSimpleName());
		templateParam.setFieldType(ClassUtils.processClassName(clazz.getName()));
		if (clazz.isArray()) {
			// 如果是数组类型，那么在定义类名称时需要将最后的[]去掉
			fieldNameBuilder.delete(fieldNameBuilder.length() - 2, fieldNameBuilder.length()).append("Array");
			// 但是在取具体类型时，需要带着[]，但是直接只用class.getName()会出现Ljava.lang.String;的方式，因此这里特殊处理
			templateParam.setFieldType(ClassUtils.processClassName(clazz.getComponentType().getName()) + "[]");
		}
		fieldNameBuilder.append("1");
		fieldNameBuilder.setCharAt(0, Character.toLowerCase(fieldNameBuilder.charAt(0)));

		String fieldName = fieldNameBuilder.toString();

		templateParam.setFieldName(fieldName);
		templateParam.setFieldGetterName(getGetterName(fieldName));
		templateParam.setFieldSetterName(getSetterName(clazz, fieldName));
		return templateParam;
	}

	/**
	 * 获得此类型的ProtoEntityBean的名称
	 * 
	 * @param t
	 * @param genericClass
	 * @return
	 */
	public static <T extends Object> String getProtoEntityBeanName(T t, Class<?>... genericClass) {
		Class<?> clazz = t.getClass();
		StringBuilder beanName = new StringBuilder(clazz.getSimpleName());
		// 如果是List或其子类，那么统一替换为List
		if ((t instanceof java.util.List) && genericClass.length > 0) {
			clazz = java.util.List.class;
			beanName = new StringBuilder();
			beanName.append((genericClass != null && genericClass.length > 0 ? genericClass[0].getSimpleName() : ""));
			beanName.append(clazz.getSimpleName());
		} else if ((t instanceof java.util.Map) && genericClass.length > 1) {
			clazz = java.util.Map.class;
			beanName = new StringBuilder();
			beanName.append((genericClass != null && genericClass.length > 0 ? genericClass[0].getSimpleName() : ""));
			beanName.append((genericClass != null && genericClass.length > 1 ? genericClass[1].getSimpleName() : ""));
			beanName.append(clazz.getSimpleName());
		} else if (t.getClass().isArray()) {
			beanName.delete(beanName.length() - 2, beanName.length());
			beanName.append("Array");
		}
		beanName.append("ProtoEntity");
		if (beanName.charAt(0) > 96 && beanName.charAt(0) < 123) {
			// 如果开头为小写字母，那么说明是一个原始数据类型，所以将首写字母变为大写字母后，前面增加Primitive字样
			beanName.setCharAt(0, Character.toUpperCase(beanName.charAt(0)));
			beanName.insert(0, "Primitive");
		}
		return beanName.toString();
	}

	/**
	 * 获得此类型的ProtoEntityBean的名称
	 * 
	 * @param t
	 * @param genericClass
	 * @return
	 */
	public static <T extends Object> String getProtoEntityBeanFullName(T t, Class<?>... genericClass) {
		return getPackage(t.getClass(), genericClass) + "." + getProtoEntityBeanName(t, genericClass);
	}

	public static <T extends Object> String getPackage(Class<T> clazz, Class<?>... genericClass) {
		if (clazz.getPackage() != null) {
			if (genericClass != null && genericClass.length > 0) {
				// 头疼的泛型，为防止出现类名冲突，不同的Date类型粗出在不同包中
				for (Class<?> genericClassTemp : genericClass) {
					if (genericClassTemp == java.sql.Date.class) {
						return "com.feinno.sqldate." + clazz.getPackage().getName();
					}
				}

			}
			return "com.feinno." + clazz.getPackage().getName();
		} else {
			return ProtoConfig.PACKAGE_CODE;
		}
	}

	private static String getGetterName(String fieldName) {
		StringBuilder sb = new StringBuilder();
		sb.append("get").append(String.valueOf(fieldName.charAt(0)).toUpperCase()).append(fieldName.substring(1));
		return sb.toString();
	}

	private static String getSetterName(Class<?> clazz, String fieldName) {

		if (clazz == boolean.class || clazz.equals(boolean.class)) {
			StringBuilder sb = new StringBuilder();
			sb.append("is").append(String.valueOf(fieldName.charAt(0)).toUpperCase()).append(fieldName.substring(1));
			return sb.toString();
		}

		StringBuilder sb = new StringBuilder();
		sb.append("set").append(String.valueOf(fieldName.charAt(0)).toUpperCase()).append(fieldName.substring(1));
		return sb.toString();
	}

	/**
	 * <b>简述: </b>Java原生类型在序列化时，生成的{@link ProtoEntity}
	 * 时，首先会创建出一个代码模板，后用参数来填充这个模板，当前类就是用于填充这个模板的参数实体对象
	 * <p>
	 * <b>功能: </b>用于填充代码模板的参数实体对象
	 * <p>
	 * <b>用法: </b>由序列化组件在解析Java原始数据类型后对应创建并赋值的模板参数
	 * <p>
	 * 
	 * @author Lv.Mingwei
	 * 
	 */
	public static class ProtoJavaBeanParam {

		// 序列化核心组件路径
		private String protoPath;

		// 当前类所存储的包名称
		private String packageName;

		// 类名称
		private String className;

		// 字段名称
		private String fieldName;

		// 字段类型
		private String fieldType;

		// 字段的getter方法名称
		private String fieldGetterName;

		// 字段的setter方法名称
		private String fieldSetterName;

		public String getProtoPath() {
			return protoPath;
		}

		public void setProtoPath(String protoPath) {
			this.protoPath = protoPath;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public String getFieldType() {
			return fieldType;
		}

		public void setFieldType(String fieldType) {
			this.fieldType = fieldType;
		}

		public String getFieldGetterName() {
			return fieldGetterName;
		}

		public void setFieldGetterName(String fieldGetterName) {
			this.fieldGetterName = fieldGetterName;
		}

		public String getFieldSetterName() {
			return fieldSetterName;
		}

		public void setFieldSetterName(String fieldSetterName) {
			this.fieldSetterName = fieldSetterName;
		}

	}
}
