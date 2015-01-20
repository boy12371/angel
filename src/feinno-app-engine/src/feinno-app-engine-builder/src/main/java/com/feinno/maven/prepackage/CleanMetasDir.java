/**
 * 
 */
package com.feinno.maven.prepackage;

import java.io.File;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * <b>描述: 解决打包后存在多余目录的问题，但是这个问题不是每次打包都出现</b>
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Zhou.yan
 *
 */
public class CleanMetasDir extends AbstractMojo {

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
		File baseDir = project.getBasedir();
		File classesDir = new File(baseDir, "target" + File.separator + "classes");
		getLog().info(classesDir.listFiles().length + "");
	}

}
