package com.barrybarry.coffeeport.utils;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static @Nullable String readContent(String path) {
        try {
            return String.join("\n", Files.readAllLines(Path.of(path)));
        } catch (IOException ignored) {
        }
        return null;
    }

    public static @Nullable InputStream readContentStream(String path) {
        try {
            return Files.newInputStream(Path.of(path));
        } catch (IOException ignored) {
        }
        return null;
    }

    public static void writeContent(String path, String content) {
        try {
            Files.writeString(Path.of(path), content);
        } catch (IOException ignored) {
        }
    }

    public static boolean exists(String s) {
        return FileUtil.exists(Path.of(parseFileUrl(s)));
    }

    public static void createDir(String path) {
        try {
            Files.createDirectories(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean exists(Path path) {
        return Files.exists(path);
    }

    public static String parseFileUrl(String url) {
        final String prefix = "file:";
        if (url.startsWith(prefix)) {
            return url.substring(prefix.length());
        }
        return url;
    }

    public static String getFileName(String path) {
        String[] paths = path.split("/");
        return paths[paths.length - 1];
    }

    public static void writeContent(String path, InputStream in) {
        try {
            Path path1 = Path.of(path);
            if(exists(path)) {
                Files.delete(path1);
            }
            Files.copy(in, path1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}