package com.excellence.basetoolslibrary.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : https://veizhang.github.io/
 *     time   : 2017/1/23
 *     desc   : 文件相关工具类
 *     			权限 {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE}
 * </pre>
 */

public class FileUtils
{

	public static final long KB = 1024;
	public static final long MB = 1024 * 1024;
	public static final long GB = 1024 * 1024 * 1024;
	public static final long TB = 1024 * 1024 * 1024 * 1024L;
	public static final String DEFAULT_FORMAT_PATTERN = "#.##";

	/**
	 * 创建文件
	 *
	 * @param file File类型
	 * @return 文件是否创建成功
	 */
	public static boolean createNewFile(File file)
	{
		try
		{
			return !isFileExists(file) && file.createNewFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 创建文件
	 *
	 * @param filePath 文件路径字符串
	 * @return 文件是否创建成功
	 */
	public static boolean createNewFile(String filePath)
	{
		return !StringUtils.isEmpty(filePath) && createNewFile(new File(filePath));
	}

	/**
	 * 删除文件
	 *
	 * @param file File类型
	 * @return 是否成功删除文件
	 */
	public static boolean deleteFile(File file)
	{
		return isFileExists(file) && file.delete();
	}

	/**
	 * 删除文件
	 *
	 * @param filePath 文件路径字符串
	 * @return 是否成功删除文件
	 */
	public static boolean deleteFile(String filePath)
	{
		return !StringUtils.isEmpty(filePath) && deleteFile(new File(filePath));
	}

	/**
	 * 创建目录
	 *
	 * @param dir File类型
	 * @return 目录是否创建成功
	 */
	public static boolean mkDir(File dir)
	{
		return !isFileExists(dir) && dir.mkdirs();
	}

	/**
	 * 创建目录
	 *
	 * @param dirPath 目录路径字符串
	 * @return 目录是否创建成功
	 */
	public static boolean mkDir(String dirPath)
	{
		return !StringUtils.isEmpty(dirPath) && mkDir(new File(dirPath));
	}

	/**
	 * 删除目录
	 *
	 * @param dir File类型
	 * @return 目录是否成功删除
	 */
	public static boolean deleteDir(File dir)
	{
		if (!isFileExists(dir))
			return false;

		if (dir.isDirectory())
		{
			File[] fileList = dir.listFiles();
			if (fileList == null)
				return false;
			for (File file : fileList)
			{
				boolean success = deleteDir(file);
				if (!success)
					return false;
			}
		}
		return dir.delete();
	}

	/**
	 * 删除目录
	 *
	 * @param dirPath 目录路径字符串
	 * @return 目录是否成功删除
	 */
	public static boolean deleteDir(String dirPath)
	{
		return !StringUtils.isEmpty(dirPath) && deleteDir(new File(dirPath));
	}

	/**
	 * 删除目录下的后缀文件
	 *
	 * @param dir File类型
	 * @param postfix 后缀
	 */
	public static boolean deletePostfixFiles(File dir, String postfix)
	{
		if (!isFileExists(dir) || StringUtils.isEmpty(postfix))
			return false;

		if (dir.isFile() && dir.getName().endsWith(postfix))
			return dir.delete();
		else if (dir.isDirectory())
		{
			File[] fileList = dir.listFiles();
			if (fileList == null)
				return false;
			for (File file : fileList)
			{
				boolean success = deletePostfixFiles(file, postfix);
				if (!success)
					return false;
			}
		}
		return true;
	}

	/**
	 * 删除目录下的后缀文件
	 *
	 * @param dirPath 目录路径字符串
	 * @param postfix 后缀
	 */
	public static boolean deletePostfixFiles(String dirPath, String postfix)
	{
		return !StringUtils.isEmpty(dirPath) && deletePostfixFiles(new File(dirPath), postfix);
	}

	/**
	 * 格式化文件大小
	 * <p>自定格式，保留位数</p>
	 *
	 * @param fileSize 文件大小
	 * @param pattern 保留格式
	 * @return 转换后文件大小
	 */
	public static String formatFileSize(long fileSize, String pattern)
	{
		DecimalFormat sizeFormat = new DecimalFormat(pattern);
		String unitStr = "Bytes";
		long unit = 1;
		if (fileSize >= TB)
		{
			unitStr = "TB";
			unit = TB;
		}
		else if (fileSize >= GB)
		{
			unitStr = "GB";
			unit = GB;
		}
		else if (fileSize >= MB)
		{
			unitStr = "MB";
			unit = MB;
		}
		else if (fileSize >= KB)
		{
			unitStr = "KB";
			unit = KB;
		}
		return sizeFormat.format((double) fileSize / unit) + unitStr;
	}

	/**
	 * 格式化文件大小
	 * <p>默认格式，保留两位小数</p>
	 *
	 * @param fileSize 文件大小
	 * @return 转换后文件大小
	 */
	public static String formatFileSize(long fileSize)
	{
		return formatFileSize(fileSize, DEFAULT_FORMAT_PATTERN);
	}

	/**
	 * 获取文件或者目录大小
	 *
	 * @param file File类型
	 * @return
	 */
	public static long getFilesSize(File file)
	{
		long fileSize = 0;
		if (isFileExists(file))
		{
			if (file.isDirectory())
				fileSize = getDirSize(file);
			else
				fileSize = getFileSize(file);
		}
		return fileSize;
	}

	/**
	 * 获取文件或者目录大小
	 *
	 * @param filePath 文件路径字符串
	 * @return
	 */
	public static long getFilesSize(String filePath)
	{
		if (isFileExists(filePath))
			return getFilesSize(new File(filePath));
		return 0;
	}

	/**
	 * 获取文件大小
	 *
	 * @param file File类型
	 * @return
	 */
	public static long getFileSize(File file)
	{
		try
		{
			if (isFileExists(file))
			{
				if (file.isFile())
				{
					FileInputStream inStream = new FileInputStream(file);
					return inStream.available();
				}
				else if (file.isDirectory())
					return getDirSize(file);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取文件大小
	 *
	 * @param filePath 文件路径字符串
	 * @return
	 */
	public static long getFileSize(String filePath)
	{
		if (isFileExists(filePath))
			return getFileSize(new File(filePath));
		return 0;
	}

	/**
	 * 获取目录大小
	 *
	 * @param dir File类型
	 * @return
	 */
	public static long getDirSize(File dir)
	{
		long size = 0;
		if (isFileExists(dir))
		{
			File[] fileList = dir.listFiles();
			if (fileList == null)
				return size;
			for (File file : fileList)
			{
				if (file.isDirectory())
					size += getDirSize(file);
				else
					size += getFileSize(file);
			}
		}
		return size;
	}

	/**
	 * 获取目录大小
	 *
	 * @param filePath 文件路径字符串
	 * @return
	 */
	public static long getDirSize(String filePath)
	{
		if (isFileExists(filePath))
			return getDirSize(new File(filePath));
		return 0;
	}

	/**
	 * 获取目录剩余空间
	 * 剩余空间 = 总空间 - 已使用空间
	 * 剩余空间 ！= 可用空间
	 *
	 * @param dir File类型
	 * @return
	 */
	public static long getDirFreeSpace(File dir)
	{
		long freeSpace = 0;
		if (isFileExists(dir))
		{
			if (dir.isDirectory())
				freeSpace = dir.getFreeSpace();
			else if (dir.isFile())
				freeSpace = dir.getParentFile().getFreeSpace();
		}
		return freeSpace;
	}

	/**
	 * 获取目录剩余空间
	 * 剩余空间 = 总空间 - 已使用空间
	 * 剩余空间 ！= 可用空间
	 * 
	 * @param filePath 文件路径字符串
	 * @return
	 */
	public static long getDirFreeSpace(String filePath)
	{
		if (isFileExists(filePath))
			return getDirFreeSpace(new File(filePath));
		return 0;
	}

	/**
	 * 获取目录总空间
	 *
	 * @param dir File类型
	 * @return
	 */
	public static long getDirTotalSpace(File dir)
	{
		long totalSpace = 0;
		if (isFileExists(dir))
		{
			if (dir.isDirectory())
				totalSpace = dir.getTotalSpace();
			else if (dir.isFile())
				totalSpace = dir.getParentFile().getTotalSpace();
		}
		return totalSpace;
	}

	/**
	 * 获取目录总空间
	 * 
	 * @param filePath 文件路径字符串
	 * @return
	 */
	public static long getDirTotalSpace(String filePath)
	{
		if (isFileExists(filePath))
			return getDirTotalSpace(new File(filePath));
		return 0;
	}

	/**
	 * 获取目录可用空间
	 *
	 * @param dir File类型
	 * @return
	 */
	public static long getDirUsableSpace(File dir)
	{
		long usableSpace = 0;
		if (isFileExists(dir))
		{
			if (dir.isDirectory())
				usableSpace = dir.getUsableSpace();
			else if (dir.isFile())
				usableSpace = dir.getParentFile().getUsableSpace();
		}
		return usableSpace;
	}

	/**
	 * 获取目录可用空间
	 *
	 * @param filePath 文件路径字符串
	 * @return
	 */
	public static long getDirUsableSpace(String filePath)
	{
		if (isFileExists(filePath))
			return getDirUsableSpace(new File(filePath));
		return 0;
	}

	/**
	 * 修改目录、文件权限
	 *
	 * @param path 目录、文件路径字符串
	 * @param permission 权限字符串，如：777
	 */
	public static void chmod(String path, String permission)
	{
		ShellUtils.execProcessBuilderCommand("chmod", permission, path);
	}

	/**
	 * 修改目录、文件权限
	 *
	 * @param file File类型
	 * @param permission 权限字符串，如：777
	 */
	public static void chmod(File file, String permission)
	{
		chmod(file.getPath(), permission);
	}

	/**
	 * 修改目录、文件777权限
	 *
	 * @param path 目录、文件路径字符串
	 */
	public static void chmod777(String path)
	{
		chmod(path, "777");
	}

	/**
	 * 修改目录、文件777权限
	 *
	 * @param file File类型
	 */
	public static void chmod777(File file)
	{
		chmod(file.getPath(), "777");
	}

	/**
	 * 判断文件或目录是否存在
	 *
	 * @param file File类型
	 * @return
	 */
	public static boolean isFileExists(File file)
	{
		return file != null && file.exists();
	}

	/**
	 * 判断文件或目录是否存在
	 *
	 * @param filePath 路径字符串
	 * @return
	 */
	public static boolean isFileExists(String filePath)
	{
		return !StringUtils.isEmpty(filePath) && isFileExists(new File(filePath));
	}
}
