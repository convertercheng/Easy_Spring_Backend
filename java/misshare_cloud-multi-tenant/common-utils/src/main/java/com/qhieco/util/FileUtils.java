package com.qhieco.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

/**
 * @author myz
 * @date 2017年12月26日 上午11:44:05
 * 
 */
public class FileUtils {

	/**
	 * 下载文件
	 * 
	 * @Description: TODO
	 * @author myz
	 * @param file
	 *            所需下载的文件
	 * @param httpServletResponse
	 * @throws IOException
	 */
	public static void download(File file, HttpServletResponse httpServletResponse) throws IOException {

		try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
				OutputStream outputStream = httpServletResponse.getOutputStream();) {
			byte[] bt = new byte[2048];
			int len = 0;
			while ((len = bufferedInputStream.read(bt)) != -1) {
				outputStream.write(bt, 0, len);
			}
			outputStream.flush();
		}
	}
	

	/**
	 * 删除单个文件
	 * @method delete
	 * @author mengyz 
	 * @param path
	 */
	public static void delete(String path) {
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
	}

	/**
	 * 
	 * @method downloadFile
	 * @author mengyz
	 * @param webUrl
	 *            带域名的完整文件路径
	 * @param targetPath
	 *            保存的完整路径
	 */
	public static void remoteDownload(String webUrl, String targetPath) {
		// 下载网络文件
		OutputStream os = null;
		InputStream inStream = null;
		try {
			URL url = new URL(webUrl);
			URLConnection conn = url.openConnection();
			inStream = conn.getInputStream();
			os = new FileOutputStream(new File(targetPath));

			// byte[] buffer = new byte[inStream.available()];
			// inStream.read(buffer);
			// os.write(buffer);// 输出文件

			byte[] buffer = new byte[1024];
			int len = 0;
			while (-1 != (len = inStream.read(buffer))) {
				os.write(buffer, 0, len);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inStream != null)
					inStream.close();
				if (os != null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 从URL抓取一个文件写到本地 这个方法摘自 <a href=
	 * "http://commons.apache.org/proper/commons-io/apidocs/org/apache/commons/io/FileUtils.html">org.apache.commons.io.FileUtils.copyURLToFile(URL
	 * source, File destination)</a>
	 */
	public static void copyFileFromURL(String webUrl, String targetFile) {
		InputStream input = null;
		FileOutputStream output = null;
		try {
			URL source = new URL(webUrl);
			input = source.openStream();
			File destination = new File(targetFile);
			if (destination.exists()) {
				if (destination.isDirectory()) {
					throw new IOException("File '" + destination + "' exists but is a directory");
				}
				if (destination.canWrite() == false) {
					throw new IOException("File '" + destination + "' cannot be written to");
				}
			} else {
				File parent = destination.getParentFile();
				if (parent != null) {
					if (!parent.mkdirs() && !parent.isDirectory()) {
						throw new IOException("Directory '" + parent + "' could not be created");
					}
				}
			}
			output = new FileOutputStream(destination, true);
			byte[] buffer = new byte[1024];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
			output.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
