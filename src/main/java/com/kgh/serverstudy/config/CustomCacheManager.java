package com.kgh.serverstudy.config;

import java.util.Collection;

public interface CustomCacheManager {
    AbstractCustomCache getCache(String name);
    Collection<String> getCacheStorageNames();
}
