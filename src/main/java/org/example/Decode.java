package org.example;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Decode {

    public void decode(){
        Path currentRelativePath = Paths.get("");

        File file = new File(currentRelativePath.toAbsolutePath()+"/DecodedImages/MyImage"+x+".jpg");
    }

}
