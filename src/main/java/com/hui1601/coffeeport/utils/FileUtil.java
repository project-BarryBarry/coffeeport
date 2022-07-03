package com.hui1601.coffeeport.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static String readContent(String path) {
        try {
            return String.join("\n", Files.readAllLines(Path.of(path)));
        } catch (IOException e) {
            return null;
        }
    }
    public static InputStream readContentStream(String path) {
        try {
            return Files.newInputStream(Path.of(path));
        } catch (IOException e) {
            return null;
        }
    }
    public static void writeContent(String path, String content){
        try {
            Files.writeString(Path.of(path), content);
        } catch (IOException ignored) {
        }
    }

    public static boolean exists(String s) {
        return Files.exists(Path.of(s));
    }
}
