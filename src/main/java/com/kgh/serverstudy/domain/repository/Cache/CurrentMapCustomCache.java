//package com.kgh.serverstudy.domain.repository.Cache;
//
//import com.kgh.serverstudy.config.CacheNotAbstractionConfig.AbstractCustomCache;
//import com.kgh.serverstudy.domain.dto.Movie;
//
//import java.util.Optional;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
//public class CurrentMapCustomCache extends AbstractCustomCache {
//
//    private final String name;
//    private final ConcurrentMap<String, Object> store;
//
//    public CurrentMapCustomCache(String name, ConcurrentHashMap<String, Object> store) {
//        this.name = name;
//        this.store = store;
//    }
//
//    public String getName() {
//        return this.name;
//    }
//
//    public Object getNativeCache() {
//        return this.store;
//    }
//
//    protected Optional<Object> lookup(Object key) {
//        return Optional.ofNullable(this.store.get(key));
//    }
//
//    @Override
//    public boolean put(String key, Object value) {
//        this.store.put(key, value);
//        return true;
//    }
//    /*
//    public <T> T get(Object key, Class<T> type) {
//        return (T) this.store.get(key);
//    }
//     */
//    public void evict(Object key) {
//
//    }
//
//    public void clear() {
//
//    }
//}
