package test.com.feinno.ha.logging.newtest.configbean;

import java.util.Stack;

import com.feinno.util.ConfigBean;

public class ConfigBeanTree {
	// 用于跟踪记录有多少个feild
	public int fieldCounter = 0;

	/**
	 * 向控制台输出configBean的内部结构，用于测试使用
	 * 
	 * @param configBean
	 */
	public void printTree(ConfigBean configBean) {
		Stack<Boolean> lastStack = new Stack<Boolean>();

		for (String key : configBean.fieldKeySet()) {
			fieldCounter++;
			tree(key + " : [" + configBean.getFieldValue(key) + "]", null, 0, lastStack);
		}
		for (String key : configBean.childKeySet()) {
			tree(key, configBean.getChild(key), 0, lastStack);
		}
		System.out.println("共发现" + fieldCounter + "个Field节点");
		fieldCounter = 0;
	}

	private void tree(String strTemp, ConfigBean configBean, int level, Stack<Boolean> lastStack) {

		if (strTemp != null) {
			print(strTemp, level, lastStack);
		}
		if (configBean == null) {
			return;
		}

		// 仅是为了计数加空格
		int count = 0;
		for (@SuppressWarnings("unused")
		String key : configBean.fieldKeySet()) {
			count++;
		}
		for (@SuppressWarnings("unused")
		String key : configBean.childKeySet()) {
			count++;
		}

		int i = 0;
		for (String key : configBean.fieldKeySet()) {
			fieldCounter++;
			lastStack.push(i == count - 1);
			tree(key + " : [" + configBean.getFieldValue(key) + "]", null, level + 1, lastStack);
			lastStack.pop();
			i++;
		}
		for (String key : configBean.childKeySet()) {
			lastStack.push(i == count - 1);
			tree(key, configBean.getChild(key), level + 1, lastStack);
			lastStack.pop();
			i++;
		}
	}

	private void print(String strTemp, int level, Stack<Boolean> lastStack) {
		for (int i = 0, k = lastStack.size() - 1; i < k; i++) {
			System.out.print(lastStack.get(i) ? "   " : "|   ");
		}
		if (level > 0) {
			System.out.print(lastStack.get(lastStack.size() - 1) ? "└--" : "|--");
		}
		System.out.println(strTemp);
	}
}
