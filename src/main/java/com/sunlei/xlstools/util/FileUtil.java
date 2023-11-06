package com.sunlei.xlstools.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能描述
 *
 * @author: sunlei
 * @date: 2023年10月05日 14:11
 */
public class FileUtil {
    private static String filterSpecialChars(String fileName) {
        Pattern pattern = Pattern.compile("[\\\\/:*?\"<>|]");
        Matcher matcher = pattern.matcher(fileName);
        return matcher.replaceAll("");
    }

    public static String getSysVal() {
        String os = System.getProperty("os.name");
        String propertiesFile = null;
        String folder = null;
        if (os.startsWith("Windows")) {
            String path = System.getProperty("user.home");
            System.out.println("Windows系统路径：" + path);
            propertiesFile = path + File.separator + "sys.properties";
            File pzFile = new File(propertiesFile);
            if (pzFile.exists()) {
                folder = PropertiesUtil.getVal(propertiesFile);
            }
        }
        return folder;
    }

    public static void setSysVal(String val) {
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            String path = System.getProperty("user.home");
            System.out.println("Windows系统路径：" + path);
            String propertiesFile = path + File.separator + "sys.properties";
            File pzFile = new File(propertiesFile);
            if (!pzFile.exists()) {
                try {
                    pzFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            PropertiesUtil.setVal(propertiesFile, val);
        }
    }
}
