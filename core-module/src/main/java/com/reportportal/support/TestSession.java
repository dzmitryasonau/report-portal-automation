package com.reportportal.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.reportportal.exceptions.TestExecutionException;

public final class TestSession {

    private static TestSession instance;
    private final Map<String, Object> map;

    private TestSession() {
        map = new ConcurrentHashMap<>();
    }

    public static TestSession getInstance() {
        if (instance == null) {
            synchronized (TestSession.class) {
                if (instance == null) {
                    instance = new TestSession();
                }
            }
        }
        return instance;
    }

    public Object get(String key) {
        throwIfNotExists(key);
        return map.get(key);
    }

    public <T> T get(String key, Class<T> tClass) {
        throwIfNotExists(key);
        return tClass.cast(map.get(key));
    }

    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public Object remove(String key) {
        return map.remove(key);
    }

    private void throwIfNotExists(String key) {
        if (!map.containsKey(key)) {
            throw new TestExecutionException("%s is not present in the %s", key, TestSession.class.getSimpleName());
        }
    }
}
