package com.github.cooker.maven;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * grant
 * 28/10/2021 3:35 下午
 * 描述：
 */
public class ClassScannerTest {

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException {
        URLClassLoader classLoader = new URLClassLoader(new URL[]{
               Paths.get("/Users/grant/Downloads/tpm").toUri().toURL()
        });
        Class cl = classLoader.loadClass("com.github.cooker.template.HelloController");
        System.out.println(cl);
        System.out.println(new File("/Users/grant/Downloads/tpm").toURL());
        System.out.println(Paths.get("/Users/grant/Downloads/tpm").toUri().toURL());
    }

    @SneakyThrows
    @Test
    public void scanClasses() {
        List<File> files = new ArrayList<>();
        func(Paths.get("/Users/grant/Downloads/tpm").toFile(), files);
        files.stream()
                .filter(file -> file.toString().contains(".class"))

                .forEach(System.out::println);
    }

    public static void func(File file, List<File> list) {
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (f.isDirectory()) {
                func(f, list);
            } else if (f.isFile()) {
                list.add(f);
            }
        }
    }
}
