package io.github.amsatrio.spring_crud_demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class ReadWriteFile {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.local_db}")
    private String localDb;

    private void initFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        boolean fileExists = Files.exists(path);
        if (!fileExists) {
            Files.createFile(path);
        }
    }

    public String readStringFromFile(String fileName) throws IOException {
        log.info("ReadWriteFile > readStringFromFile");

        initFile(fileName);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }

            bufferedReader.close();

            return jsonStringBuilder.toString();
        } catch (IOException exception) {
            log.error("ReadWriteFile > readStringFromFile > error ", exception);
            return null;
        }
    }

    public Integer writeStringToFile(String input, String fileName) throws IOException {
        initFile(fileName);

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(input);
            fileWriter.close();
            return 1;
        } catch (IOException exception) {
            log.error("ReadWriteFile > writeStringToFile > error ", exception);
        }

        return null;
    }

    public Boolean deleteFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        log.info("ReadWriteFile > deleteFile: " + fileName);
        return Files.deleteIfExists(path);
    }

    private Map<String, String> getMapLocalDb() throws IOException {
        String text = readStringFromFile(localDb);
        if (text == null) {
            writeStringToFile("{}", localDb);
        } else if (text.isEmpty()) {
            writeStringToFile("{}", localDb);
        }
        Map<String, String> map = new HashMap<>();
        map = objectMapper.readValue(text, new TypeReference<Map<String, String>>() {
        });

        return map;
    }

    private Integer setMapLocalDb(Map<String, String> map) throws IOException {
        String text = objectMapper.writeValueAsString(map);
        return writeStringToFile(text, localDb);
    }

    public Integer setValue(String key, String value) throws IOException {
        Map<String, String> map = getMapLocalDb();
        map.put(key, value);

        return setMapLocalDb(map);
    }

    public Integer setValueList(String key, List<String> value) throws IOException {
        Map<String, String> map = getMapLocalDb();
        map.put(key, objectMapper.writeValueAsString(value));

        return setMapLocalDb(map);
    }

    public String getValue(String key) throws IOException {
        Map<String, String> map = getMapLocalDb();

        if (map.containsKey(key)) {
            return map.get(key);
        }
        return null;
    }

    public List<String> getValueList(String key) throws IOException {
        Map<String, String> map = getMapLocalDb();

        if (map.containsKey(key)) {
            String value = map.get(key);
            return objectMapper.readValue(value, new TypeReference<List<String>>() {
            });
        }
        return null;
    }

    public Integer deleteValue(String key) throws IOException {
        Map<String, String> map = getMapLocalDb();

        map.remove(key);

        return setMapLocalDb(map);
    }

    public List<String> getAllFiles(String parentDirectory) throws IOException {
        Path directory = Paths.get(parentDirectory);

        // List all files in the directory
        List<String> filePaths = new ArrayList<>();
        try (Stream<Path> pathStream = Files.list(directory)) {
            filePaths = pathStream
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException ioException) {
            log.error("ReadWriteFile > getAllFiles > error ", ioException);
            throw ioException;
        }

        return filePaths;
    }


    public List<String> readMultipleBase64Strings(String pathFile) throws IOException {
        String base64Data = readStringFromFile(pathFile);
        if(base64Data == null) {
            return  new ArrayList<>();
        }

        List<String> base64Strings = new ArrayList<>();
        String regex = "[A-Za-z0-9+/]+={0,2}";
//        String regex = "([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(base64Data);

        while (matcher.find()) {
            String base64String = matcher.group();
            base64Strings.add(base64String);
        }

        return base64Strings;
    }
}
