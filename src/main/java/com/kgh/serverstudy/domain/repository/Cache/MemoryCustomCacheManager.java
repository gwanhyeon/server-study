package com.kgh.serverstudy.domain.repository.Cache;
import com.kgh.serverstudy.config.CustomCacheManager;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component("memoryCustomCacheManager")
public class MemoryCustomCacheManager implements CustomCacheManager {

    private final ConcurrentMap<String, CurrentMapCustomCache> cacheMap
            = new ConcurrentHashMap<>(16);

    public CurrentMapCustomCache getCache(String name) {
        CurrentMapCustomCache cache = this.cacheMap.get(name);
        if (cache == null) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    cache = createConcurrentMapCustomCache(name);
                    this.cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }

    protected CurrentMapCustomCache createConcurrentMapCustomCache(String name) {
        return new CurrentMapCustomCache(name, new ConcurrentHashMap<>(256));
    }

    @Deprecated
    @Override
    public Collection<String> getCacheStorageNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }
}
