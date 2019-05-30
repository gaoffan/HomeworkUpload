package com.goufaan.homeworkupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Misc {

    public static void ZipMultiFile(String filepath ,String zipPath) {
        try {
            File file = new File(filepath);
            File zipFile = new File(zipPath);
            InputStream input;
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for (File value : files) {
                    input = new FileInputStream(value);
                    zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + value.getName()));
                    int temp;
                    while ((temp = input.read()) != -1) {
                        zipOut.write(temp);
                    }
                    input.close();
                }
            }
            zipOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
