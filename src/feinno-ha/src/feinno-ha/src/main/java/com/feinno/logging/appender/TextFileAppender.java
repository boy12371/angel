/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-11-29
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.logging.appender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import com.feinno.logging.LogEvent;
import com.feinno.logging.common.LogCommon;

/**
 * 记录日志的文件平台,提供将日志写入指定文件的方法
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class TextFileAppender implements Appender {
	private static final String NEWLINE = "\r\n";
	/**
	 * 此Appender是否有效标志,默认为有效
	 */
	private boolean valid = true;
	/**
	 * 写入日志的文件名
	 */
	private String fileName;
	/**
	 * 写入日志文件字符流
	 */
	private BufferedWriter logWriter;

	/**
	 * 每隔一小时重新设置文件名,及重新创建BufferedWriter字符流
	 */
	private Thread fileCreaterThread;
	private boolean fileCreaterIsRuning;

	/**
	 * 带参构造器,在new 此对象时必须指定日志存放的绝对路径
	 * 
	 * @param filePath
	 *            日志存放路径
	 */
	public TextFileAppender(String filePath) {
		File tempFolder = new File(filePath.toString());
		if (!tempFolder.exists()) {
			tempFolder.mkdirs();
		}
		creatNewFile(filePath);
	}

	/*
	 * public boolean isValid() { return valid; }
	 */
	/**
	 * 设置Appender是否可用 true-可用 false-不可用
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
		// 当日志文件平台设置为false时,将io流关闭
		if (!valid) {
			close();
		} else {
			// 文件平台设置为true时,重新创建io流
			try {
				logWriter = new BufferedWriter(new FileWriter(fileName, true));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public String getFileName() {
		return fileName;
	}

	/**
	 * 向指定的文件写入批量日志信息
	 * 
	 * @param events
	 *            将要输出的日志信息集合
	 */
	public void doAppend(Queue<LogEvent> queue) {
		if (queue == null)
			return;
		List<LogEvent> events = new ArrayList<LogEvent>(queue);
		try {
			// 开始记录日志
			for (Iterator<LogEvent> it = events.iterator(); it.hasNext();) {
				// 此处将String取出再赋予到logWriter的write方法的原因是为了不使用同步快也不会影响到logWriter重新创建以及关闭留时的原子性
				String string = it.next().toString();
				logWriter.write(string);
				logWriter.write(NEWLINE);
				// 记录后删除
				it.remove();
			}
			// 刷新缓存
			logWriter.flush();
		} catch (IOException e) {
			// 发生异常时,将没有记录的日志,继续记录
			firstExceptionHandle(events);
			e.printStackTrace();
		}
	}

	/**
	 * 向指定的文件写入日志信息
	 * 
	 * @param events
	 *            将要输出的日志信息
	 */
	public void doAppend(LogEvent event) {
		if (event == null)
			return;
		try {
			// 开始记录日志
			logWriter.write(event.toString());
			logWriter.write(NEWLINE);
			// 刷新缓存
			logWriter.flush();
		} catch (IOException e) {
			// 继续记录日志
			firstExceptionHandle(event);
			e.printStackTrace();
		}
	}

	/**
	 * 第一次记录日志发生异常处理
	 * 
	 * @param obj
	 *            单条日志或日志集合
	 */
	@SuppressWarnings("unchecked")
	public void firstExceptionHandle(Object obj) {
		try {
			// 关闭当前的字符流
			close();
			// 重新创建BufferedWriter ,文件名加"(1)",重新写入新的文件中
			logWriter = new BufferedWriter(new FileWriter(fileName.substring(0, fileName.length() - 4) + "(1)", true));
			// 单条日志时
			if (obj instanceof LogEvent) {
				logWriter.write(obj.toString());
				logWriter.write(NEWLINE);
			}
			// 日志集合
			if (obj instanceof List) {
				for (Iterator<LogEvent> it = ((List<LogEvent>) obj).iterator(); it.hasNext();) {
					String string = it.next().toString();
					logWriter.write(string);
					logWriter.newLine();
					// 记录后删除
					it.remove();
				}

			}
			logWriter.flush();

		} catch (IOException e) {
			// 再次将未记录的日志 继续记录
			secondExceptionHandle(obj);
			e.printStackTrace();
		}

	}

	/**
	 * 再次记录日志时发生异常处理
	 * 
	 * @param obj
	 *            单条日志或日志集合
	 */
	@SuppressWarnings("unchecked")
	public void secondExceptionHandle(Object obj) {
		try {
			// 关闭当前的字符流
			close();
			// 重新创建BufferedWriter ,文件名加"(2)",重新写入新的文件中
			logWriter = new BufferedWriter(new FileWriter(fileName.substring(0, fileName.length() - 4) + "(2)", true));

			// 单条日志时
			if (obj instanceof LogEvent) {
				logWriter.write(obj.toString());
				logWriter.write(NEWLINE);
			}
			// 日志集合
			if (obj instanceof List) {
				for (LogEvent event : (List<LogEvent>) obj) {
					logWriter.write(event.toString());
					logWriter.write(NEWLINE);
				}
			}
			logWriter.flush();

		} catch (IOException e) {
			// 当第三次发生异常时,将没有记录的日志抛掉即可,但是要关闭当前发生异常的io流,重新创建BufferWriter流
			// 关闭当前的字符流
			close();
			// 重新创建BufferedWriter ,文件名还是加"(2)"
			try {
				logWriter = new BufferedWriter(new FileWriter(fileName.substring(0, fileName.length() - 4) + "(2)",
						true));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	}

	/**
	 * 每隔一小时重新设置文件名,及重新创建BufferedWriter字符流
	 * 
	 * @param path
	 *            文件路径
	 */
	public void creatNewFile(String path) {
		// 执行定时任务,每小时重新设置文件名,并重新创建BufferedWriter字符流
		// fileCreater.schedule(new CreatFileTask(path), 0, 1000 * 60 * 60);
		fileCreaterThread = new Thread(new CreatFileTask(path));
		fileCreaterIsRuning = true;
		fileCreaterThread.start();
	}

	/**
	 * 关闭字符流
	 * 
	 * @param writer
	 *            字符流
	 */
	public void close() {
		if (logWriter != null)
			try {
				// 关闭之前刷新
				logWriter.flush();
				logWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public void destroy() {
		fileCreaterIsRuning = false;
		close();
	}

	/**
	 * 设置Appender是否可用 true-可用 false-不可用
	 */
	/*
	 * public void setEnabled(boolean enabled) { this.setValid(enabled); }
	 */

	/**
	 * 获取Appender是否可用
	 */
	public boolean isEnabled() {
		return valid;
	}

	/**
	 * 定时任务类,每隔一小时换一个文件
	 */
	private class CreatFileTask implements Runnable {
		/**
		 * 日志存放路径
		 */
		private String path;

		/**
		 * 参数构造器
		 * 
		 * @param path
		 *            日志存放路径
		 */
		public CreatFileTask(String path) {
			this.path = path;
			fileName = LogCommon.formatDate(path);
			try {
				logWriter = new BufferedWriter(new FileWriter(fileName, true));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		/**
		 * 定时执行的任务
		 */
		public void run() {
			while (fileCreaterIsRuning) {
				// 重新设置文件名
				fileName = LogCommon.formatDate(path);
				// 重新创建 字符流
				try {
					BufferedWriter logWriterTemp = new BufferedWriter(new FileWriter(fileName, true));
					BufferedWriter logWriterBak = logWriter;
					logWriter = logWriterTemp;
					// 如果当前字符流 不为空则关闭
					if (logWriterBak != null) {
						try {
							// 关闭之前刷新
							logWriterBak.flush();
							logWriterBak.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				Calendar nowCalendar = Calendar.getInstance();
				Calendar afterClaendar = Calendar.getInstance();
				afterClaendar.add(Calendar.HOUR, 1);
				afterClaendar.set(Calendar.MINUTE, 0);
				afterClaendar.set(Calendar.MILLISECOND, 0);
				long waitTime = afterClaendar.getTimeInMillis() - nowCalendar.getTimeInMillis();
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

}