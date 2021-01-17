package luiz.imageRepo.utils;

import org.springframework.core.io.FileSystemResource;

import java.nio.file.Path;
import java.nio.file.Paths;


public class LocalPaths {

    public static String getBasePath() {
        return new FileSystemResource("").getFile().getAbsolutePath();
    }

    public static Path getUploadFolder() {
        return Paths.get(getBasePath() + getOSSeparator() + "uploads");
    }

    public static String getOSSeparator() {
        String os = System.getProperty("os.name").toLowerCase();

        if(os.contains("win")) return "\\";

        return "/";
    }

}