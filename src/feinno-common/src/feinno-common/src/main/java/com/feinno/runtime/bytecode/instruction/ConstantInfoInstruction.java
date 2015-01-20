package com.feinno.runtime.bytecode.instruction;

import com.feinno.runtime.bytecode.type.CPInfo;
import com.feinno.runtime.bytecode.type.ClassFile;

/**
 * 标识其中指向常量池中的数据
 * 
 * @author Lv.Mingwei
 * 
 */
public abstract class ConstantInfoInstruction extends Instruction {

	public ConstantInfoInstruction(byte opcode) {
		super(opcode);
	}

	public abstract int getValue();

	public CPInfo getCpInfo(ClassFile classFile) {
		return getValue() < classFile.getConstant_pool_count() ? classFile.getConstant_pool()[getValue()] : null;
	}

}
