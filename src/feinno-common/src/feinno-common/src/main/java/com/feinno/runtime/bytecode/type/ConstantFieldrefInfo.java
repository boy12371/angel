package com.feinno.runtime.bytecode.type;

/**
 * 常量池中的字段类型信息表示类
 * 
 * @author Lv.Mingwei
 * 
 */
public class ConstantFieldrefInfo extends ConstantReference {

	public ConstantFieldrefInfo(ClassFile classFile) {
		super(classFile, ConstantType.CONSTANT_FIELDREF);
	}

	public ConstantFieldrefInfo(ClassFile classFile, int classIndex, int nameAndTypeIndex) {
		super(classFile, ConstantType.CONSTANT_FIELDREF, classIndex, nameAndTypeIndex);
	}

	protected ConstantType getConstantType() {
		return ConstantType.CONSTANT_FIELDREF;
	}
}
