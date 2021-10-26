package com.github.cooker.plugin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * grant
 * 26/10/2021 6:08 下午
 * 描述：
 */
@Configuration
public class AutoPluginLoaderConfiguration {

    @Bean
    public PluginLoaderRunner pluginLoaderRunner() {
        return new PluginLoaderRunner();
    }
}
