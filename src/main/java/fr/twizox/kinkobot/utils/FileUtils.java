package fr.twizox.kinkobot.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.twizox.kinkobot.KinkoBot;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static Path getRoot() {
        return Path.of(KinkoBot.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
    }

    public static void saveResource(String resourcePath, boolean replace) {
        InputStream inputStream = KinkoBot.class.getResourceAsStream(File.separator + resourcePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + KinkoBot.class);
        }
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        Path target = getRoot().resolve(resourcePath);

        File outFile = target.toFile();

        if (!outFile.exists() || replace) {
            try {
                OutputStream outputStream = new FileOutputStream(outFile);
                OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                }
                writer.write(sb.toString());

                reader.close();
                writer.close();
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
        }
    }

    public static JsonObject parseJSONFile(String filename) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        Gson gson = new Gson();
        return gson.fromJson(content, JsonObject.class);
    }

    public static void saveJSONFile(String fileName, JsonObject data) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter writer = new FileWriter(getRoot() + File.separator + fileName);
            gson.toJson(data, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
