package com.github.cooker.maven;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * grant
 * 28/10/2021 11:04 上午
 * 描述：
 */
@Mojo( name = "build", defaultPhase = LifecyclePhase.COMPILE, threadSafe = true, requiresDependencyResolution = ResolutionScope.RUNTIME )
public class BuildMojo extends AbstractMojo implements Contextualizable {

    /**
     * The current Maven project.
     */
    @Parameter( defaultValue = "${project}", readonly = true, required = true )
    private MavenProject project;

    Map root = new HashMap<>();

    @Override
    @SneakyThrows
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("=== 插件生成 ===");
        getLog().info("名称：" + project.getName());
        getLog().info("版本：" + project.getVersion());

        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(this.getClass(), "/");
        cfg.setDefaultEncoding("UTF-8");
        Template template = cfg.getTemplate("plugin-template.flt");

        Writer out = new OutputStreamWriter(
                new FileOutputStream(Paths.get(getTargetDir(), project.getName() + ".yaml").toFile())
        );
        setVariable("name", project.getName());
        setVariable("version", project.getVersion());
        setVariable("description", project.getDescription());
        addController();
        template.process(root, out);
        out.flush();
        out.close();
    }

    private void addController() throws IOException {
        List<URL> urls = new ArrayList<>();
        urls.add(
                Paths.get(getTargetDir(), "/classes").toFile().toURL()
        );
        URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[0]));
        List<String> controllers = scanClass(
                Paths.get(getTargetDir(), "/classes").toFile()
        );
        setVariable("controller", StringUtils.join(controllers.toArray(), ","));
        loader.close();
    }

    private List<String> scanClass(File f) {
        List<File> ret = new ArrayList<>();
        scanClass(f, ret);
        return ret.stream().filter(it->it.getName().contains(".class"))
                .map(it->it.toString().replaceAll(getTargetDir(), ""))
                .map(it->it.replaceAll("/classes/", ""))
                .map(it->it.replaceAll(".class", ""))
                .map(it->it.replaceAll("/", "."))
                .collect(Collectors.toList());
    }

    private void scanClass(File f, List<File> list) {
        File[] fs = f.listFiles();
        for (File it : fs) {
            if (it.isDirectory()) {
                scanClass(it, list);
            } else if (it.isFile()) {
                list.add(it);
            }
        }
        return;
    }

    public void setVariable(String key, Object val) {
        root.put(key, val);
    }

    private String getTargetDir() {
        return Paths.get(project.getBasedir().getAbsolutePath(), "target").toString();
    }



    @Override
    public void contextualize(Context context) throws ContextException {
//        getLog().info("" + context.getContextData());
    }
}
