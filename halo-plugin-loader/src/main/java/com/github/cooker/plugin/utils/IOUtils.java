package com.github.cooker.plugin.utils;

import java.io.Closeable;
import java.util.Optional;

/**
 * grant
 * 27/10/2021 9:17 上午
 * 描述：
 */
public class IOUtils {
    public static void close(Closeable closeable) {
        Optional.ofNullable(closeable).ifPresent(
                c->{
                    try {
                        closeable.close();
                    }catch (Exception e){};
                }
        );
    }
}
