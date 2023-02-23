package pro.sky.lesson30skypro.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@UtilityClass
public class CommonUtils {

    public String createDirectory(String directory) {
        Path workingDir = Paths.get("").toAbsolutePath();
        Path checkingDir = Paths.get(workingDir+"/"+directory);
        if (!Files.exists(checkingDir)) {
            try {
                Files.createDirectories(checkingDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return checkingDir.toString();
    }

    public <T> boolean writeFile(String sourceDir, T obj, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(String.format("%s\\%s.json", sourceDir, fileName));
        try {
            objectMapper.writeValue(file, obj);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T> List<T> readObjects(String sourceDir, Class<T> type) {
        List<File> files = readFiles(sourceDir);
        TreeMap<Integer, T> map = new TreeMap<>();
        List<T> obj = new ArrayList<>();
        if (!files.isEmpty()) {
            files.forEach(item -> {
                obj.addAll(readFile(item, type));
            });
        }
        return obj;
    }

    public List<File> readFiles(String sourceDir) {
        try {
           return Files.walk(Paths.get(sourceDir))
                   .filter(Files::isRegularFile)
                   .map(Path::toFile).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> readFile(File file, Class<T> type) {
        try {
            String data = FileUtils.readFileToString(file, "UTF-8");
            return objectFromString(data, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> objectFromString(String jsonString, Class<T> type) {
        List<T> obj;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.registerModule(new JavaTimeModule());
            CollectionType listType =
                    objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            obj = objectMapper.readValue(jsonString, listType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public boolean deleteFile(String sourceDir) {
        Path path = Paths.get(sourceDir);
        try {
            Files.delete(path);
            return true;
        } catch (NoSuchFileException x) {
            System.err.format("%s: такой файл не существует", path);
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", path);
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
        return false;
    }
}
