package edu.application.notepad.utils;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileUtils {

    public static String loadText(InputStream inputStream) {
        try (inputStream; BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder builder = new StringBuilder();
            String currentLine;

            while ((currentLine = reader.readLine()) != null)
                builder.append(currentLine + "\n");

            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String loadText(File file) {
        try (FileReader fr = new FileReader(file); BufferedReader reader = new BufferedReader(fr)) {

            StringBuilder builder = new StringBuilder();
            String currentLine;

            while ((currentLine = reader.readLine()) != null)
                builder.append(currentLine + "\n");

            return builder.toString();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<File, String> loadOpenedTabs() {
        File file = new File("tabs.txt");

        Map<File, String> map = new LinkedHashMap<>();

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            String contentFile = loadText(file);

            for(String tab : contentFile.split("[\\r\\n]+")) {
                File tabFile = new File(tab);

                if(tabFile.exists()) map.put(tabFile, loadText(tabFile));
            }
        }

        return map;
    }

    public static void writeOpenedTabs(Map<File, String> map) {
        try (FileWriter fw = new FileWriter("tabs.txt"); BufferedWriter writer = new BufferedWriter(fw)) {

            for(Map.Entry<File, String> entry : map.entrySet()) {
                if(entry.getKey().exists())
                    writer.write(entry.getKey().getPath() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(File file, String content) {
        try (FileWriter fw = new FileWriter(file); BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(content);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
