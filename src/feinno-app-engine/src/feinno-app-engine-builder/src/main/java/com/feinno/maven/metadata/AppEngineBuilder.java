/**
 * 
 */
package com.feinno.maven.metadata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.configuration.AppBeanAnnotationsLoader;
import com.feinno.maven.code.json.SimpleFormatter;
import com.google.gson.Gson;

/**
 * <b>描述: 在编译后分析appbean并且生成描述文件</b>
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: 在app-engine工程下的pom文件中添加该插件的描述</b>
 * <p>
 * 
 * @author Zhou.yan
 * @goal analyse
 */
public class AppEngineBuilder extends AbstractMojo {

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
		// 设置依赖对象获取过滤器
		project.setArtifactFilter(new ArtifactFilter() {

			public boolean include(Artifact artifact) {
				// 所有依赖项都包含
				return true;
			}

		});
		getLog().info("====================分析appbean元数据===============================");
		try {
			// 通过类路径创建class加载器
			URLClassLoader newLoader = createUrlClassLoader(project);
			// 获取编译后的classpath
			List<String> compileClz = project.getSystemClasspathElements();
			// 构建json工具类
			Gson gson = new Gson();
			// 创建一个json buffer用于在递归中存储处理过的json数据
			// StringBuffer jsonBuffer = new StringBuffer();
			List<AppBeanAnnotations> annosList = new ArrayList<AppBeanAnnotations>();
			// 遍历类路径下的类文件，寻找正确的fae appbean进行分析
			for (String clzDir : compileClz) {
				File clzFiles = new File(clzDir);
				File[] fs = clzFiles.listFiles();
				if (fs != null) {
					for (File f : fs) {
						analyseAppbean(f, newLoader, gson, clzDir, annosList);
					}
				}
			}
			// 将分析结果放入包装类型
			Appbeans appbeans = new Appbeans();
			appbeans.setAppBeans(annosList);
			// 转换为json字符串
			String json = gson.toJson(appbeans);
			// 格式化json字符串
			SimpleFormatter formatter = new SimpleFormatter();
			json = formatter.format(json);
			// 打印json到控制台
			getLog().info("\n" + json);

			// 拿到当前工程绝对路径
			String baseDir = project.getBasedir().getAbsolutePath();

			// 清理目录
			cleanMetasDirectory(new File(baseDir + metasDirectory));
			// 保存分析结果到src目录
			File srcFile = new File(baseDir + metasDirectory + savingFileName);
			writeFile(json, srcFile);
			getLog().info("src 文件大小 : " + srcFile.getUsableSpace());
			getLog().info(srcFile.getAbsolutePath() + " 生成文件成功!");
			// 保存文件到target目录
			File targetFile = new File(baseDir + targetDirectory + savingFileName);
			writeFile(json, targetFile);
			getLog().info("target 文件大小 : " + targetFile.getUsableSpace());
			getLog().info(targetFile.getAbsolutePath() + " 生成文件成功!");

			// 添加生成的资源文件
			// Resource res = new Resource();
			// res.addInclude(srcFile.getAbsolutePath());
			// project.addResource(res);
			// getLog().info("添加资源文件成功!");

		} catch (Exception e) {
			getLog().error(e);
		}

		getLog().info("============================分析结束================================");
	}

	/**
	 * 保存内容到文件
	 * 
	 * @param content 文件内容
	 * @param file 保存文件
	 * @throws IOException
	 */
	private void writeFile(String content, File file) throws IOException {
		getLog().info("Will create file : " + file.getAbsolutePath());
		File dir = file.getParentFile();
		getLog().info("Create parent directory : " + dir.getAbsolutePath());
		dir.mkdirs();
		if (!file.exists())
			file.createNewFile();
		FileOutputStream fout = new FileOutputStream(file);
		fout.write(content.getBytes());
		fout.flush();
		fout.close();
	}

	/**
	 * 清理metas目录保证每次生成的是最新的
	 */
	private void cleanMetasDirectory(File dir) {
		File parent = dir.getParentFile();
		File[] files = parent.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.getName().equals("appbeanMeta.json")) {
					f.delete();
					break;
				}
			}
		}
		files = dir.listFiles();
		if (files != null) {
			for (File f : files) {
				f.delete();
			}
		}
		getLog().info("目录清理完成");
	}

	/**
	 * 通过Maven工程的引用获取依赖关系和类的编译路径，构造出URL类加载器
	 * 
	 * @param project Maven工程引用
	 * @throws DependencyResolutionRequiredException
	 * @throws MalformedURLException
	 */
	private URLClassLoader createUrlClassLoader(MavenProject project) throws DependencyResolutionRequiredException, MalformedURLException {
		// 使用集合存储classpath
		ArrayList<URL> runtimeUrls = new ArrayList<URL>();
		// 首先添加编译后类的classpath,并且添加入集合
		List<String> runtimeClasspathElements = project.getRuntimeClasspathElements();
		for (int i = 0; i < runtimeClasspathElements.size(); i++) {
			String element = (String) runtimeClasspathElements.get(i);
			runtimeUrls.add(new File(element).toURI().toURL());
		}
		Set<Artifact> depends = project.getArtifacts();
		Iterator<Artifact> iter = depends.iterator();
		while (iter.hasNext()) {
			Artifact artf = iter.next();
			File jarf = artf.getFile();
			if (jarf != null)
				runtimeUrls.add(jarf.toURI().toURL());
		}
		getLog().info("Runtime url size : " + runtimeUrls.size());
		for (int i = 0; i < runtimeUrls.size(); i++) {
			URL url = runtimeUrls.get(i);
			if (url != null)
				getLog().info("url : " + runtimeUrls.get(i).toString());
		}
		// 构造URL类加载器
		URLClassLoader newLoader = new URLClassLoader(runtimeUrls.toArray(new URL[runtimeUrls.size()]), Thread.currentThread().getContextClassLoader());
		return newLoader;
	}

	/**
	 * 分析appbean元数据，该函数为递归函数，遍历classes目录下的所有class文件
	 * 
	 * @param file class文件
	 * @param loader 类加载器
	 * @param gson json转换类
	 * @param baseDir 类文件，根路径用于替换
	 * @param jsonBuffer 存储分析后的json数据
	 * @throws Exception
	 */
	private void analyseAppbean(File file, URLClassLoader loader, Gson gson, String baseDir, List<AppBeanAnnotations> annosList) throws Exception {
		if (file.isDirectory()) {
			File[] fs = file.listFiles();
			for (File f : fs) {
				analyseAppbean(f, loader, gson, baseDir, annosList);
			}
		} else {
			// 替换掉类文件路径，保留包路径，再处理成类名称，使用类加载器进行加载
			int baseDirLen = (baseDir + File.separator).length();
			String entryName = file.getAbsolutePath().substring(baseDirLen);
			if (entryName.endsWith(".class") && entryName.indexOf("$") == -1) {
				String className = entryName.substring(0, entryName.length() - 6).replace(File.separatorChar, '.');
				Class<?> clazz = loader.loadClass(className);
				// 忽略注释类，枚举，接口
				if (clazz.isAnnotation() || clazz.isEnum() || clazz.isInterface()) {
					return;
				}
				// 如果继承自AppBean表明为标准appbean，接着进行分析
				if (AppBean.class.isAssignableFrom(clazz) && !clazz.getName().equals(AppBean.class.getName()) && !Modifier.isAbstract(clazz.getModifiers())) {
					getLog().info("Analyse : " + clazz.getName());
					AppBeanAnnotations appBeanAnno = AppBeanAnnotationsLoader.getAppBeanAnnotaions(clazz);
					// String json = gson.toJson(appBeanAnno);
					// jsonBuffer.append(json + "\n");
					annosList.add(appBeanAnno);
				}
			}
		}
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

	/**
	 * 元数据编译目标路径
	 */
	private String targetDirectory = File.separator + "target" + File.separator + "classes" + File.separator + "META-INF" + File.separator + "metas"
			+ File.separator;
}
