package org.superboot.utils;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * <b> 文件操作类 </b>
 * <p>
 * 功能描述:提供对文件的读写、文件夹访问操作
 * </p>
 *
 * @author jesion
 * @date 2017/9/5
 * @time 17:01
 * @Path org.superboot.utils.FileUtil
 */
public class FileUtil {

    /**
     * 读取文件
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    public static byte[] read(String path)
            throws IOException {
        int base_size = 1024;
        File file = new File(path);

        if (!(file.exists())) {
            return null;
        }

        FileInputStream fis = new FileInputStream(file);
        int len = 0;
        byte[] dataByte = new byte[base_size];

        ByteArrayOutputStream out = new ByteArrayOutputStream(base_size);
        while ((len = fis.read(dataByte)) != -1) {
            out.write(dataByte, 0, len);
        }
        byte[] content = out.toByteArray();

        fis.close();
        out.close();

        if (content.length == 0) {
            return null;
        }

        return content;
    }

    /**
     * 写文件
     * @param path 路径
     * @param data 文件数据
     * @throws IOException
     */
    public static void write(String path, byte[] data)
            throws IOException {
        File file = new File(path);

        if (!(file.exists())) {
            file.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.flush();
        fos.close();
    }

    /**
     * 获取文件列表
     * @param baseDirName 文件夹名称
     * @return
     */
    public static List<String> findFiles(String baseDirName) {
        List files = new ArrayList();

        File baseDir = new File(baseDirName);
        if ((!(baseDir.exists())) || (!(baseDir.isDirectory()))) {
            System.err.println("search error：" + baseDirName + "is not a dir！");
        } else {
            String[] filelist = baseDir.list();
            for (String fileName : filelist) {
                files.add(fileName);
            }
        }
        return files;
    }

    /**
     * 读取文件夹文件列表
     * @param baseDirName 文件夹名称
     * @param fileFilter 过滤器
     * @return
     */
    public static List<String> findFileNames(String baseDirName, FileFilter fileFilter) {
        List files = new ArrayList();

        File baseDir = new File(baseDirName);
        if ((!(baseDir.exists())) || (!(baseDir.isDirectory()))) {
            System.err.println("search error：" + baseDirName + "is not a dir！");
        } else {
            File[] filelist = baseDir.listFiles(fileFilter);
            for (File file : filelist) {
                if (file.isFile()){
                    files.add(file.getName());

                }
            }
        }
        return files;
    }

    /**
     * 获取文件的MD5
     *
     * @param fileName 文件完整路径+名称
     * @return
     */
    public static String getFileMD5(String fileName) {
        File file = new File(fileName);
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件的SHA-1码 一般效率低于MD5
     *
     * @param fileName
     * @return
     */
    public static String getFileSha1(String fileName) {
        File file = new File(fileName);
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
