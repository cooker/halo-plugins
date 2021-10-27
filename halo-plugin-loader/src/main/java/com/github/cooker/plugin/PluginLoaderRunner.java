package com.github.cooker.plugin;

import com.github.cooker.api.AbstractController;
import com.github.cooker.plugin.utils.IOUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
@Slf4j
public class PluginLoaderRunner implements ApplicationListener<ApplicationStartedEvent>
{
    @Resource
    RequestMappingHandlerMapping requestMappingHandler;

    URLClassLoader loader;
    AnnotationConfigApplicationContext context;

    @Override
    @SneakyThrows
    public void onApplicationEvent(ApplicationStartedEvent event) {
        try {
            Path path = Paths.get(event.getApplicationContext().getEnvironment().getProperty("halo.work-dir"), "plugins");
            List<Map> props = new ArrayList<>();
            URL[] urls = Files.list(path)
                    .filter(p-> {
                        if (StringUtils.containsAny(p.toString(), "yaml", "yml")) {
                            try {
                                Yaml prop = new Yaml();
                                props.add((Map)prop.loadAs(new FileInputStream(p.toString()), Properties.class));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        return p.toString().contains(".jar");
                    }).map(p-> {
                        try {
                            URL url = p.toFile().toURL();
                            log.info("载入插件：{}", url.toString());
                            return url;
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }).filter(url->url!=null).collect(Collectors.toList()).toArray(new URL[0]);

            if (ArrayUtils.isNotEmpty(urls)) {
                loader = URLClassLoader.newInstance(urls);
                context = new AnnotationConfigApplicationContext();

                for (Map prop : props) {

                    log.info("插件加载 name={} version={} description={}", prop.get("name"), prop.get("version"), prop.get("description"));
                    String[] cls = ((String)prop.get(AbstractController.class.getName())).split(",");
                    for (String clName : cls) {
                        Class cl = loader.loadClass(clName);
                        log.info("注册：{}", cl.getName());
                        context.register(cl);
                    }
                }
                context.refresh();
                System.out.println(Arrays.asList(context.getBeanDefinitionNames()));
                ConfigurableListableBeanFactory beanFactory = event.getApplicationContext().getBeanFactory();

                context.getBeanFactory().setParentBeanFactory(beanFactory);


                Method method = ReflectionUtils.findMethod(requestMappingHandler.getClass(), "detectHandlerMethods", Object.class);
                method.setAccessible(true);
                for (Map prop : props) {
                    String[] cls = ((String)prop.get(AbstractController.class.getName())).split(",");
                    for (String clName : cls) {
                        method.invoke(requestMappingHandler, context.getBean(loader.loadClass(clName)));
                    }
                }
            }
        } catch (Exception e) {
            log.error("插件载入失败：", e);
        }

    }

    @PreDestroy
    public void destroy() {
        IOUtils.close(context);
        IOUtils.close(loader);
    }
}
