package com.feinno.appengine.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class FileHelper {
	

	/**FileHelper
     * 将HTTP资源另存为文件
     *
     * @param destUrl String
     * @throws Exception
     */
    public static void saveToFile(URL url,String downloadPath) throws IOException {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;       
        byte[] buf = new byte[1024];
        int size = 0;
       
//建立链接
        httpUrl = (HttpURLConnection) url.openConnection();
//连接指定的资源
        httpUrl.connect();
//获取网络输入流
        bis = new BufferedInputStream(httpUrl.getInputStream());
//建立文件
        String fileName= downloadPath + url.getFile().substring(url.getFile().lastIndexOf("name=")+5);
        fos = new FileOutputStream(fileName);
//保存文件
        while ((size = bis.read(buf)) != -1)
            fos.write(buf, 0, size);

        fos.close();
        bis.close();
        httpUrl.disconnect();
    }
    
    /**
	  * 解压tar.gz文件
	  * tar文件只是把多个文件或文件夹打包合成一个文件，本身并没有进行压缩。gz是进行过压缩的文件。
	  * @param dir
	  * @param filepath
	  * @throws Exception
	  */
	 public static void deGzipArchive(String dir, String filepath)
	   throws Exception {
	  final File input = new File(filepath);
	  final InputStream is = new FileInputStream(input);
	  final CompressorInputStream in = new GzipCompressorInputStream(is);
	  TarArchiveInputStream tin = new TarArchiveInputStream(in);
	  TarArchiveEntry entry = tin.getNextTarEntry();
	  while (entry != null) {
	   File archiveEntry = new File(dir, entry.getName());
	   archiveEntry.getParentFile().mkdirs();
	   if (entry.isDirectory()) {
	    archiveEntry.mkdir();
	    entry = tin.getNextTarEntry();
	    continue;
	   }
	   OutputStream out = new FileOutputStream(archiveEntry);
	   IOUtils.copy(tin, out);
	   out.close();
	   entry = tin.getNextTarEntry();
	  }
	  in.close();
	  tin.close();
	 }
	 
	 public static void main(String[] args)
	 {
//		 try {
//			//./downloads
//				File dirDown = new File("./downloads");
//				if (!dirDown.isDirectory())
//				{ //目录不存在
//					dirDown.mkdir(); //创建目录
//				} 
//				//./packages
//				File dirPackage = new File("./packages");
//				if (!dirPackage.isDirectory())
//				{ //目录不存在
//					dirPackage.mkdir(); //创建目录
//				} 
//			URL url = new URL("http://127.0.0.1:8000/share/0319/Branch_4.3.3.1125_Wats_2012-03-13_10-44-23.tar.gz");
//			FileHelper.saveToFile(url,"./downloads");
//			FileHelper.deGzipArchive("./packages/Branch_4.3.3.1125_Wats_2012-03-13_10-44-23.tar.gz","./downloads/Branch_4.3.3.1125_Wats_2012-03-13_10-44-23.tar.gz");
//		 } catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		 
		 List<URL> url;
		try {
			url = getURL(new File("/home/lichunlei/work/testfae/lib/"),false);
			for(int i =0 ; i<url.size(); i++ )
				System.out.println(url.get(i).toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
	 
	 /**
		 * 获取单个文件的MD5值！
		 * 
		 * @param file
		 * @return
		 */
		public static String getFileMD5(File file) {
			if (!file.isFile()) {
				return null;
			}
			MessageDigest digest = null;
			FileInputStream in = null;
			byte buffer[] = new byte[1024];
			int len;
			try {
				digest = MessageDigest.getInstance("MD5");
				in = new FileInputStream(file);
				while ((len = in.read(buffer, 0, 1024)) != -1) {
					digest.update(buffer, 0, len);
				}
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			BigInteger bigInt = new BigInteger(1, digest.digest());
			return bigInt.toString(16);
		}

		/**
		 * 获取文件夹中文件的MD5值
		 * 
		 * @param file
		 * @param listChild
		 *            ;true递归子目录中的文件
		 * @return
		 */
		public static Map<String, String> getDirMD5(File file, boolean listChild) {
			if (!file.isDirectory()) {
				return null;
			}
			// <filepath,md5>
			Map<String, String> map = new HashMap<String, String>();
			String md5;
			File files[] = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				if (f.isDirectory() && listChild) {
					map.putAll(getDirMD5(f, listChild));
				} else {
					md5 = getFileMD5(f);
					if (md5 != null) {
						map.put(f.getName(), md5);
					}
				}
			}
			return map;
		}
		
		public  static List<URL> getURL(File directory, boolean listChild) throws MalformedURLException
		{
			List<URL>  list = new ArrayList<URL>();

			if(!directory.isDirectory())
			{
				if(directory.getName().endsWith(".class") || directory.getName().endsWith(".jar"))
					list.add(directory.toURI().toURL());
			}
			else
			{
				File files[] = directory.listFiles();
				for (int i = 0; i < files.length; i++) {
					File f = files[i];
					if (f.isDirectory() && listChild) {
						list.addAll(getURL(f, listChild));
					} else {
						if(f.getName().endsWith(".class") || f.getName().endsWith(".jar"))
							list.add(f.toURI().toURL());
					}
				}
			}
			return list;
				
		}
}
