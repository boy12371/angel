/**
 * 
 */
package com.feinno.maven.resource;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * <b>描述: 在分析appbean之前首先生成存放资源文件的目录与文件，保证资源文件可以被打入包内</b>
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Zhou.yan
 * @goal resources
 */
public class GenerateResources extends AbstractMojo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		@SuppressWarnings("rawtypes")
		Map map = getPluginContext();
		// 通过上下文拿到当前工程的引用
		MavenProject project = (MavenProject) map.get("project");

		// 先生成json文件及目录，不然在打包期间该文件不会被打入包内
		String baseDir = project.getBasedir().getAbsolutePath();
		File jsonDir = new File(baseDir + metasDirectory);
		jsonDir.mkdirs();

		// 创建json文件告诉maven这些文件需要打入包内
		File jsonFile = new File(jsonDir.getAbsolutePath() + File.separator + savingFileName);
		try {
			jsonFile.createNewFile();
			getLog().info(jsonFile.getAbsolutePath() + " 文件创建成功.");
		} catch (IOException e) {
			getLog().info(jsonFile.getAbsolutePath() + " 文件创建失败.");
			e.printStackTrace();
		}

		getLog().info("资源文件生成完成");
	}

	/**
	 * 保存文件名，默认appbeans.json，参数通过配置项传入
	 * 
	 * @parameter default-value="appbeans.json"
	 */
	private String savingFileName;
	/**
	 * 元数据目录相对路径
	 */
	private String metasDirectory = File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "META-INF" + File.separator + "metas"
			+ File.separator;
}
