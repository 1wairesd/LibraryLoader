package com.wairesd.util.velocitylibraryloader.library;

public interface LibraryLoader {
    void loadLibrary(String url, String sourceName) throws Exception;
    boolean isLibraryLoaded(String name);
    void loadLibraryMaven(String repoUrl, String groupId, String artifactId, String version, String sourceName) throws Exception;
} 