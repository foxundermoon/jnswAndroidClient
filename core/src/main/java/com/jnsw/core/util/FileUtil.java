package com.jnsw.core.util;

import java.io.File;

/**
 * Created by fox on 2015/9/21.
 */
public abstract class FileUtil {
    public static boolean deleteFile(String path) {
        File file = new File(path);
        file.deleteOnExit();
        return file.exists();
    }

    public static boolean makeDirs(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return true;
            } else {
                return false;

            }
        }
        return true;
    }

    public static void deleteAllFile(String path) {
        File file = new File(path);
        deleteAllFile(file);
    }

    public static void deleteAllFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteAllFile(f);
            }
            file.delete();
        }
    }
}
