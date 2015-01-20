package com.feinno.ha.util.thresholds;

public class ThresholdExpression {

	public ThresholdExpression() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ThresholdExpression(String lable, float value, LogicSymbolsType symbols) {
		super();
		this.lable = lable;
		this.value = value;
		this.symbols = symbols;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public LogicSymbolsType getSymbols() {
		return symbols;
	}

	public void setSymbols(LogicSymbolsType symbols) {
		this.symbols = symbols;
	}

	@Override
	public String toString() {
		return "ThresholdExpression [lable=" + lable + ", value=" + value
				+ ", symbols=" + symbols + "]";
	}

	private String lable;
	private float value;
	private LogicSymbolsType symbols;
}
