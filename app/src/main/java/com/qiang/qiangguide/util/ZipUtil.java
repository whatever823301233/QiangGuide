package com.qiang.qiangguide.util;

import android.text.TextUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Qiang on 2016/7/13.
 *
 * ZIP压缩文件操作工具类 支持密码
 */
public class ZipUtil {

    private static final String UTF = "utf-8";
    private static final String ZIP = ".zip";

    /**
     * unzip(使用给定密码解压指定的ZIP压缩文件到指定目录) (如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出)
     *
     * @param zip
     *            指定的ZIP压缩文件
     * @param dest
     *            解压目录 <br />
     *            目标文件夹不可以在外置sdcard上(Android4.4以上外置sdcard不可写.因此,如果源文件在外置sdcard,
     *            则此处不可为空.)
     * @param passwd
     *            ZIP文件的密码,如果没有密码.则为null
     * @return 解压后文件数组
     * @throws ZipException
     *             压缩文件有损坏或者解压缩失败抛出:1.找不到文件;2.文件路径异常;3.压缩文件不合法,可能被损坏.
     * @since 1.1.0
     */
    public static File[] unzip(String zip, String dest, String passwd)
            throws ZipException {
        checkZipPath(zip);
        File zipFile = new File(zip);
        if (!zipFile.exists()) {
            throw new ZipException("找不到该文件");
        }
        return unzip(zipFile, dest, passwd);
    }

    /**
     * unzip(使用给定密码解压指定的ZIP压缩文件到当前目录) <br />
     * (警告:如果源文件在外置sdcard上不可调用此方法.)
     *
     * @param zip
     *            指定的ZIP压缩文件
     * @param passwd
     *            ZIP文件的密码 没有密码,则为null
     * @return 解压后文件数组
     * @throws ZipException
     *             1.找不到文件;2.文件路径异常;3.文件损坏异常;
     * @since 1.1.0
     */
    public static File[] unzip(String zip, String passwd) throws ZipException {
        checkZipPath(zip);
        File zipFile = new File(zip);
        if (!zipFile.exists()) {
            throw new ZipException("找不到该文件");
        }
        File parentDir = zipFile.getParentFile();
        return unzip(zipFile, parentDir.getAbsolutePath(), passwd);
    }

    /**
     * unzip(使用给定密码解压指定的ZIP压缩文件到指定目录)
     * <p>
     * 如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出
     *
     * @param zipFile
     *            指定的ZIP压缩文件
     * @param dest
     *            解压目录<br />
     *            目标文件夹不可以在外置sdcard上(Android4.4以上外置sdcard不可写.因此,如果源文件在外置sdcard,
     *            则此处不可为空.)
     * @param passwd
     *            ZIP文件的密码,如果没有密码.则为null
     * @return 解压后文件数组
     * @throws ZipException
     *             压缩文件有损坏或者解压缩失败抛出
     * @since 1.1.0
     */
    public static File[] unzip(File zipFile, String dest, String passwd)
            throws ZipException {
        if (!zipFile.exists()) {
            throw new ZipException("找不到该文件");
        }
        ZipFile zFile = new ZipFile(zipFile);
        zFile.setFileNameCharset(UTF);
        if (!zFile.isValidZipFile()) {
            throw new ZipException("压缩文件不合法,可能被损坏.");
        }
        File destDir = new File(dest);
        if (destDir.isDirectory() && !destDir.exists()) {
            if (!destDir.mkdir()) {
                return null;
            }
        }
        checkPasswd(passwd, zFile);
        zFile.extractAll(dest);// 解压已完成

        @SuppressWarnings("unchecked")
        List<FileHeader> headerList = zFile.getFileHeaders();
        List<File> extractedFileList = new ArrayList<File>();
        for (FileHeader fileHeader : headerList) {
            if (!fileHeader.isDirectory()) {
                extractedFileList.add(new File(destDir, fileHeader
                        .getFileName()));
            }
        }
        File[] extractedFiles = new File[extractedFileList.size()];
        extractedFileList.toArray(extractedFiles);
        return extractedFiles;
    }

    /**
     * extractSingleFile(提取单个文件)
     *
     * @param zipFilePath
     *            被解压文件路径
     * @param fileName
     *            Note that the file name is the relative file name in the zip
     *            file. For example if the zip file contains a file "mysong.mp3"
     *            in a folder "FolderToAdd", then extraction of this file can be
     *            done as below: "FolderToAdd\\myvideo.avi"
     * @param dest
     * <br />
     *            目标文件夹不可以在外置sdcard上(Android4.4以上外置sdcard不可写.因此,如果源文件在外置sdcard,
     *            则此处不可为空.)
     * @param passwd
     *            如果没有密码,则为null;
     * @throws ZipException
     * @since 1.1.0
     */
    public static void extractSingleFile(String zipFilePath, String fileName,
                                         String dest, String passwd) throws ZipException {
        checkZipPath(zipFilePath);
        File zipFile = new File(zipFilePath);

        extractSingleFile(zipFile, fileName, dest, passwd);
    }

    /**
     * extractSingleFile(提取单个文件)
     *
     * @param zipFile
     *            被解压的zip文件
     * @param fileName
     *            Note that the file name is the relative file name in the zip
     *            file. For example if the zip file contains a file "mysong.mp3"
     *            in a folder "FolderToAdd", then extraction of this file can be
     *            done as below: "FolderToAdd\\myvideo.avi"
     * @param dest
     * <br />
     *            目标文件夹不可以在外置sdcard上(Android4.4以上外置sdcard不可写.因此,如果源文件在外置sdcard,
     *            则此处不可为空.)
     * @param passwd
     *            如果没有密码,则为null;
     * @throws ZipException
     * @since 1.1.0
     */
    public static void extractSingleFile(File zipFile, String fileName,
                                         String dest, String passwd) throws ZipException {
        // Initiate ZipFile object with the path/name of the zip file.
        if (!zipFile.exists()) {
            throw new ZipException("找不到该文件");
        }
        ZipFile zFile = new ZipFile(zipFile);

        // Check to see if the zip file is password protected
        checkPasswd(passwd, zFile);
        // Note that the file name is the relative file name in the zip file.
        // For example if the zip file contains a file "mysong.mp3" in a folder
        // "FolderToAdd", then extraction of this file can be done as below:
        zFile.extractFile(fileName, dest);

    }

    /**
     * extractSelectDir(使用密码抽取压缩文件中指定的文件夹到指定的目录)
     *
     * @param zipFilePath
     *            被解压文件夹路径以(File.separator)结尾
     * @param dir
     *            要抽取出来的目录(文件夹)
     * @param dest
     *            目标文件夹.(抽取到该文件夹下) <br />
     *            目标文件夹不可以在外置sdcard上(Android4.4以上外置sdcard不可写.因此,如果源文件在外置sdcard,
     *            则此处不可为空.)
     * @param passwd
     *            密码.如果没有密码则为null
     * @throws ZipException
     *             压缩文件路径不正确.
     * @since 1.1.0
     */
    public static void extractSelectDir(String zipFilePath, String dir,
                                        String dest, String passwd) throws ZipException {
        checkZipPath(zipFilePath);

        File zipFile = new File(zipFilePath);
        extractSelectDir(zipFile, dir, dest, passwd);
    }

    /**
     * extractSelectDir(使用密码抽取压缩文件中指定的文件夹到指定的目录)
     *
     * @param zipFile
     *            需要解压的压缩文件.
     * @param dir
     *            要抽取出来的目录(文件夹)
     * @param dest
     *            目标文件夹.(抽取到该文件夹下) <br />
     *            目标文件夹不可以在外置sdcard上(Android4.4以上外置sdcard不可写.因此,如果源文件在外置sdcard,
     *            则此处不可为空.)
     * @param passwd
     *            密码.如果没有密码则为null
     * @throws ZipException
     *             压缩文件不合法.
     * @since 1.1.0
     */
    public static void extractSelectDir(File zipFile, String dir, String dest,
                                        String passwd) throws ZipException {
        if (!zipFile.exists()|| TextUtils.isEmpty(dir)) {
            return ;
        }
        ZipFile zFile = new ZipFile(zipFile);
        zFile.setFileNameCharset(UTF);
        if (!zFile.isValidZipFile()) {
            throw new ZipException("压缩文件不合法,可能被损坏.");
        }
        File destDir = new File(dest);
        if (destDir.isDirectory() && !destDir.exists()) {
            if (!destDir.mkdir()) {
                return;
            }
        }
        checkPasswd(passwd, zFile);

        @SuppressWarnings("unchecked")
        List<FileHeader> headerList = zFile.getFileHeaders();
        for (FileHeader fileHeader : headerList) {
            if (fileHeader.getFileName().contains(dir)) {
                zFile.extractFile(fileHeader, dest);
            }
        }
    }

    /**
     * zip(使用给定密码压缩指定文件或文件夹到当前目录) <br />
     * 目标文件夹不可以在外置sdcard上(Android4.4以上外置sdcard不可写.因此,如果源文件在外置sdcard,则此处不可为空.)
     *
     * @param src
     *            要压缩的文件
     * @param dest
     *            压缩文件存放路径
     * @param passwd
     *            压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径,如果为null则说明压缩失败.
     * @throws ZipException
     * @since 1.1.0
     */
    public static String zip(String src, String dest, String passwd) throws ZipException {
        return zip(src, dest, true, passwd);
    }

    /**
     * zip 使用给定密码压缩指定文件或文件夹到指定位置. <br />
     * 目标文件夹不可以在外置sdcard上(Android4.4以上外置sdcard不可写.因此,如果源文件在外置sdcard,则此处不可为空.)
     *
     * @param src
     *            要压缩的文件或文件夹路径
     * @param dest
     *            压缩文件存放路径 dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者"". <br />
     *            如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀; <br />
     *            如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀;否则视为文件名.
     * @param isCreateDir
     *            是否在压缩文件里创建目录,仅在压缩文件为目录时有效.<br />
     *            如果为false,将直接压缩目录下文件到压缩文件.
     * @param passwd
     *            压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径,如果为null则说明压缩失败.
     * @throws ZipException
     *
     * @since 1.1.0
     */
    public static String zip(String src, String dest, boolean isCreateDir,
                             String passwd) throws ZipException {
        if (TextUtils.isEmpty(src)) {
            return null;
        }
        File srcFile = new File(src);
        if (!srcFile.exists()) {
            return null;
        }
        dest = buildDestinationZipFilePath(srcFile, dest);
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); // 压缩级别
        if (!TextUtils.isEmpty(passwd)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密方式
            parameters.setPassword(passwd.toCharArray());
        }
        try {
            File zFile = new File(dest);
            if (!zFile.exists()) {
                return null;
            }
            ZipFile zipFile = new ZipFile(zFile);
            if (!srcFile.isFile()) {
                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                if (!isCreateDir) {
                    File[] subFiles = srcFile.listFiles();
                    ArrayList<File> temp = new ArrayList<File>();
                    Collections.addAll(temp, subFiles);
                    zipFile.addFiles(temp, parameters);
                    return dest;
                }
                zipFile.addFolder(srcFile, parameters);
            } else {
                zipFile.addFile(srcFile, parameters);
            }
            return dest;
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * buildDestinationZipFilePath(构建压缩文件存放路径,如果不存在将会创建)
     * (传入的可能是文件名或者目录,也可能不传,此方法用以转换最终压缩文件的存放路径)
     *
     * @param srcFile
     *            源文件
     * @param destParam
     *            压缩目标路径
     * @return 正确的压缩文件存放路径
     * @since 1.1.0
     */
    private static String buildDestinationZipFilePath(File srcFile,
                                                      String destParam) {
        if (null != destParam && !"".equals(destParam)) {
            createDestDirectoryIfNecessary(destParam); // 在指定路径不存在的情况下将其创建出来
            if (destParam.endsWith(File.separator)) {
                String fileName = "";
                if (!srcFile.isFile()) {
                    fileName = srcFile.getName();
                } else {
                    fileName = srcFile.getName().substring(0,
                            srcFile.getName().lastIndexOf("."));
                }
                destParam += fileName + ZIP;
            }
        } else {
            if (!srcFile.isFile()) {
                destParam = srcFile.getParent() + File.separator
                        + srcFile.getName() + ZIP;
            } else {
                String fileName = srcFile.getName().substring(0,
                        srcFile.getName().lastIndexOf("."));
                destParam = srcFile.getParent() + File.separator + fileName
                        + ZIP;
            }

        }
        return destParam;
    }

    /**
     * createDestDirectoryIfNecessary 在必要的情况下创建压缩文件存放目录,比如指定的存放路径并没有被创建
     *
     * @param destParam
     *            指定的存放路径,有可能该路径并没有被创建
     *
     * @since 1.1.0
     */
    private static void createDestDirectoryIfNecessary(String destParam) {
        File destDir = null;
        if (destParam.endsWith(File.separator)) {
            destDir = new File(destParam);
        } else {
            destDir = new File(destParam.substring(0,
                    destParam.lastIndexOf(File.separator)));
        }
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /**
     * checkZipPath(检查被解压文件的路径)
     *
     * @param zip
     * @throws ZipException
     * @since 1.1.0
     */
    private static void checkZipPath(String zip) throws ZipException {
        if (TextUtils.isEmpty(zip)) {
            throw new ZipException("zip文件路径不能为空");
        } else if (!zip.endsWith(ZIP)) {
            throw new ZipException("字符串格式不正确");
        }
    }

    /**
     * checkPasswd(密码检查)
     *
     * @param passwd
     * @param zFile
     * @throws ZipException
     * @since 1.1.0
     */
    private static void checkPasswd(String passwd, ZipFile zFile)
            throws ZipException {
        if (zFile.isEncrypted()) {
            // if yes, then set the password for the zip file
            if (!TextUtils.isEmpty(passwd)) {
                zFile.setPassword(passwd.toCharArray());
            } else {
                throw new ZipException("解压该文件需要密码");
            }
        }
    }

}
