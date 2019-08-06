package com.imooc.ad.index;

/**
 * @Author: hekai
 * @Date: 2019-08-05 16:16
 */
public interface IndexAware<K, V> {

    V get(K key);

    void add(K key, V value);

    void update(K key, V value);

    void delete(K key, V value);
    
}
