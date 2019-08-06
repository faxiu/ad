package com.imooc.ad.utils;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @Author: hekai
 * @Date: 2019-08-05 17:27
 */
public class CommonUtils {
    /** 判断map中是否有key,有则返回，无则使用factory返回一个新的对象 */
    public static <K, V> V getOrCreate(K key, Map<K, V> map, Supplier<V> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }
}
