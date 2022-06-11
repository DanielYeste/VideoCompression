package org.example;


import me.tongfei.progressbar.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUnzipper {
    private String zipFilePath;
    private String destDir;
    /**
     * Get the path of the input zip and the output dir
     * @param zipFilePath
     */
    public FileUnzipper(String zipFilePath) {
        this.zipFilePath = zipFilePath;
        Path currentRelativePath = Paths.get("");
        this.destDir = currentRelativePath.toAbsolutePath()+"/UnzippedImages";
    }

    /**
     * Method that proceeds to unzip the desired file to output
     * the files in the selected dir, predefined as UnzippedImages
     */
    public void unzip() {
        ProgressBar pb = new ProgressBar("Unzipping files", 100); // name, initial max
        pb.start();
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                pb.step();
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pb.stop();
    }
}