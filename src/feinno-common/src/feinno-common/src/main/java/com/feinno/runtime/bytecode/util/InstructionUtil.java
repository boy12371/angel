package com.feinno.runtime.bytecode.util;

import java.io.IOException;
import java.util.Collection;

import com.feinno.runtime.bytecode.instruction.BranchInstruction;
import com.feinno.runtime.bytecode.instruction.ConstantInfoInstruction;
import com.feinno.runtime.bytecode.instruction.Instruction;
import com.feinno.runtime.bytecode.instruction.OpcodeEnum;
import com.feinno.runtime.bytecode.type.CPInfo;
import com.feinno.runtime.bytecode.type.ClassFile;

public class InstructionUtil {
	/**
	 * 指令码输出
	 * 
	 * @param classFile
	 * @param instruction
	 */
	public static String toInstructionString(ClassFile classFile, byte[] codes) {
		try {
			return toInstructionString(classFile, Instruction.readAll(codes));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 指令码输出
	 * 
	 * @param classFile
	 * @param instruction
	 */
	public static String toInstructionString(ClassFile classFile, Instruction instruction) {
		StringBuffer sb = new StringBuffer();
		OpcodeEnum opcodeEnum = OpcodeEnum.valueOf(instruction.getOpcode());
		if (instruction instanceof ConstantInfoInstruction) {
			int valueIndex = ((ConstantInfoInstruction) instruction).getValue();
			CPInfo cpInfo = ((ConstantInfoInstruction) instruction).getCpInfo(classFile);
			if (cpInfo != null) {
				sb.append(instruction.getOffset());
				sb.append(" [");
				sb.append(HexUtil.toHexString(opcodeEnum.getOpcode()));
				sb.append("] ");
				sb.append(opcodeEnum.getMnemonicCode());
				sb.append("//");
				sb.append(opcodeEnum.getDesc());
				sb.append("#");
				sb.append(valueIndex);
				sb.append(" ");
				sb.append(cpInfo);
			} else {
				sb.append(instruction.getOffset());
				sb.append(" [");
				sb.append(HexUtil.toHexString(opcodeEnum.getOpcode()));
				sb.append("] ");
				sb.append(opcodeEnum.getMnemonicCode());
				sb.append("//");
				sb.append(opcodeEnum.getDesc());
			}

		} else if (instruction instanceof BranchInstruction) {
			sb.append(instruction.getOffset());
			sb.append(" [");
			sb.append(HexUtil.toHexString(opcodeEnum.getOpcode()));
			sb.append("] ");
			sb.append(opcodeEnum.getMnemonicCode());
			sb.append(" ");
			sb.append((instruction.getOffset() + ((BranchInstruction) instruction).getValue()));
			sb.append("  //实际偏移量 ");
			sb.append(((BranchInstruction) instruction).getValue());
			sb.append(" ");
			sb.append(opcodeEnum.getDesc());
		} else {
			sb.append(instruction.getOffset());
			sb.append(" [");
			sb.append(HexUtil.toHexString(opcodeEnum.getOpcode()));
			sb.append("] ");
			sb.append(opcodeEnum.getMnemonicCode());
			sb.append("//");
			sb.append(opcodeEnum.getDesc());
		}
		return sb.toString();
	}

	/**
	 * 指令码输出
	 * 
	 * @param classFile
	 * @param instruction
	 */
	public static String toInstructionString(ClassFile classFile, Collection<Instruction> instructionList) {
		StringBuffer sb = new StringBuffer();
		for (Instruction instruction : instructionList) {
			sb.append(toInstructionString(classFile, instruction));
			sb.append("\r\n");
		}
		return sb.toString();
	}
}
