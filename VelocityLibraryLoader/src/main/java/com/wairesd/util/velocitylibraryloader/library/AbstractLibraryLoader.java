package com.wairesd.util.velocitylibraryloader.library;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractLibraryLoader implements LibraryLoader {
    protected final Set<String> loadedLibraries = ConcurrentHashMap.newKeySet();
    protected final Map<String, File> libraryCache = new ConcurrentHashMap<>();

    @Override
    public boolean isLibraryLoaded(String name) {
        return loadedLibraries.contains(name);
    }

    protected void markLoaded(String name, File file) {
        loadedLibraries.add(name);
        libraryCache.put(name, file);
    }
} 