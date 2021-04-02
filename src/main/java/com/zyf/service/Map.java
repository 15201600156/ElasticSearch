package com.zyf.service;

public interface Map<K,V> {
    V put(K k,V v);
    V get(K k);
    int size();

    interface Entry<K,V>{
        K getKey();
        V getValue();
    }
}
