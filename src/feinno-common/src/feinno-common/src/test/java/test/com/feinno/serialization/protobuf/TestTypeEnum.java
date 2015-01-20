package test.com.feinno.serialization.protobuf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.feinno.serialization.protobuf.bean.FullElementsBean;
import test.com.feinno.serialization.protobuf.enumType.ByteEnumType;
import test.com.feinno.serialization.protobuf.enumType.IntegerEnumType;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.TypeEnum;
import com.feinno.util.EnumType;

public class TestTypeEnum {

	private static final Logger logger = LoggerFactory.getLogger(TestTypeEnum.class);

	@Test
	public void test() {
		logger.info("{}", TypeEnum.getCode(FullElementsBean.class));
		logger.info("{}", TypeEnum.getCode(DataCreater.newTable(false)));
		logger.info("{}", TypeEnum.getCode(ByteEnumType.byte1));
		logger.info("{}", TypeEnum.getCode(IntegerEnumType.i1));
		logger.info("{}", TypeEnum.getCode(new String[] {}));
		logger.info("{}", TypeEnum.getCode(new ArrayList<String>()));
		logger.info("{}", TypeEnum.getCode(new HashMap<Integer, String>()));
		logger.info("{}", TypeEnum.getCode(new java.util.Date()));
		logger.info("{}", TypeEnum.getCode(new java.sql.Date(System.currentTimeMillis())));
		logger.info("{}", TypeEnum.getPrimitiveClass(int.class));
		logger.info("{}", TypeEnum.getPrimitiveClass(boolean.class));
		logger.info("{}", TypeEnum.getPrimitiveClass(int[].class));
		logger.info("{}", TypeEnum.arrayToClassType(int[].class));
		logger.info("{}", TypeEnum.arrayToClassType(boolean[].class));
		logger.info("{}", TypeEnum.arrayToClassType(byte[].class));
		logger.info("{}", TypeEnum.getDate(new java.sql.Date(System.currentTimeMillis())));
		logger.info("{}", TypeEnum.getCode(DataCreater.newPerson(true).getEnum_obj()));
		logger.info("{}", TypeEnum.getCode(DataCreater.newPerson(true).getByte_Array()));
		TypeEnum.getCode(FullElementsBean.class).getName();
		TypeEnum.getCode(FullElementsBean.class).setName("aaa");
		TypeEnum.getCode(FullElementsBean.class).setCode(1);
		TypeEnum.getCode(FullElementsBean.class).setCla(FullElementsBean.class);
		TypeEnum.getCode(FullElementsBean.class.getName());
		TypeEnum.getCode(List.class);
		TypeEnum.getCode(Map.class);
		TypeEnum.getCode(Date.class);
		TypeEnum.getCode(java.sql.Date.class);
		TypeEnum.getCode(ProtoEntity.class);
		TypeEnum.getCode(Enum.class);
		TypeEnum.getCode(EnumType.class);
		TypeEnum.getCode(int[].class);
		TypeEnum.getPrimitiveClass(int.class);
		TypeEnum.getPrimitiveClass(long.class);
		TypeEnum.getPrimitiveClass(double.class);
		TypeEnum.getPrimitiveClass(float.class);
		TypeEnum.getPrimitiveClass(byte.class);
		TypeEnum.getPrimitiveClass(short.class);
		TypeEnum.getPrimitiveClass(char.class);
		TypeEnum.getPrimitiveClass(boolean.class);
		TypeEnum.getPrimitiveClass(int[].class);
		TypeEnum.getPrimitiveClass(long[].class);
		TypeEnum.getPrimitiveClass(double[].class);
		TypeEnum.getPrimitiveClass(float[].class);
		TypeEnum.getPrimitiveClass(byte[].class);
		TypeEnum.getPrimitiveClass(short[].class);
		TypeEnum.getPrimitiveClass(char[].class);
		TypeEnum.getPrimitiveClass(boolean[].class);

		TypeEnum.arrayToClassType(Integer[].class);
		TypeEnum.arrayToClassType(Long[].class);
		TypeEnum.arrayToClassType(Double[].class);
		TypeEnum.arrayToClassType(Float[].class);
		TypeEnum.arrayToClassType(Byte[].class);
		TypeEnum.arrayToClassType(Short[].class);
		TypeEnum.arrayToClassType(Character[].class);
		TypeEnum.arrayToClassType(Boolean[].class);

		TypeEnum.arrayToClassType(int[].class);
		TypeEnum.arrayToClassType(long[].class);
		TypeEnum.arrayToClassType(double[].class);
		TypeEnum.arrayToClassType(float[].class);
		TypeEnum.arrayToClassType(byte[].class);
		TypeEnum.arrayToClassType(short[].class);
		TypeEnum.arrayToClassType(char[].class);
		TypeEnum.arrayToClassType(boolean[].class);
	}

}
