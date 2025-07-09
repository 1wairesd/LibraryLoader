package com.wairesd.util.velocitylibraryloader;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import com.wairesd.util.velocitylibraryloader.library.AbstractLibraryLoader;

@Plugin(
        id = "velocitylibraryloader",
        name = "VelocityLibraryLoader",
        version = "1.0",
        authors = {"1wairesd"}
)

public class VelocityLibraryLoader extends AbstractLibraryLoader {
    @Inject private Logger logger;
    @Inject private ProxyServer proxy;

    private final File libsDir;
    private final File mavenLibsDir;

    public VelocityLibraryLoader() {
        this.libsDir = new File("plugins/libs");
        if (!libsDir.exists()) libsDir.mkdirs();
        this.mavenLibsDir = new File("libraries");
        if (!mavenLibsDir.exists()) mavenLibsDir.mkdirs();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }

    @Override
    public void loadLibrary(String url, String sourceName) throws Exception {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        File outFile = new File(libsDir, fileName);
        if (!outFile.exists()) {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            try (InputStream in = conn.getInputStream(); FileOutputStream out = new FileOutputStream(outFile)) {
                byte[] buf = new byte[8192];
                int len;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
            }
        }
        markLoaded(fileName, outFile);
        addToClasspath(outFile);
        logger.info("[VelocityLibraryLoader] [" + sourceName + "] Loaded library " + outFile.getAbsolutePath());
    }

    @Override
    public void loadLibraryMaven(String repoUrl, String groupId, String artifactId, String version, String sourceName) throws Exception {
        String groupPath = groupId.replace('.', '/');
        String jarName = artifactId + "-" + version + ".jar";
        String mavenPath = groupPath + "/" + artifactId + "/" + version + "/" + jarName;
        String fullUrl = repoUrl.endsWith("/") ? repoUrl + mavenPath : repoUrl + "/" + mavenPath;
        File outFile = new File(mavenLibsDir, mavenPath);
        if (!outFile.getParentFile().exists()) outFile.getParentFile().mkdirs();
        if (!outFile.exists()) {
            HttpURLConnection conn = (HttpURLConnection) new URL(fullUrl).openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            try (InputStream in = conn.getInputStream(); FileOutputStream out = new FileOutputStream(outFile)) {
                byte[] buf = new byte[8192];
                int len;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
            }
        }
        markLoaded(jarName, outFile);
        addToClasspath(outFile);
        logger.info("[VelocityLibraryLoader] [" + sourceName + "] Loaded library " + outFile.getAbsolutePath());
    }

    private void addToClasspath(File file) throws Exception {
        URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        URL url = file.toURI().toURL();
        for (URL it : sysLoader.getURLs()) {
            if (it.equals(url)) return;
        }
        java.lang.reflect.Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(sysLoader, url);
    }
}
