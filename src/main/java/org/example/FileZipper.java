package org.example;

import me.tongfei.progressbar.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileZipper {
    private String destDir;

    public FileZipper(String outputFilePath){
        this.destDir = outputFilePath;
    }


    public void zipper(String fileName) throws IOException {
        ProgressBar pb = new ProgressBar("Zipping files", 100); // name, initial max
        pb.start();
        Path currentRelativePath = Paths.get("");
        String originDir = currentRelativePath.toAbsolutePath()+"/ReproducedImages";
        File f = new File(originDir);
        File[] files = f.listFiles();
        FileOutputStream fos = new FileOutputStream(fileName);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (File listedFiles : files) {
            pb.step();
            FileInputStream fis = new FileInputStream(listedFiles);
            ZipEntry zipEntry = new ZipEntry(listedFiles.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
            //listedFiles.delete(); Evitar eliminacion de las imagenes borradas
        }
        zipOut.close();
        fos.close();
        pb.stop();
    }
}
