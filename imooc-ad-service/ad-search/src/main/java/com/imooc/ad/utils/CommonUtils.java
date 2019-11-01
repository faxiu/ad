package com.imooc.ad.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @Author: hekai
 * @Date: 2019-08-05 17:27
 */
@Slf4j
public class CommonUtils {
    /**
     * 判断map中是否有key,有则返回，无则使用factory返回一个新的对象
     */
    public static <K, V> V getOrCreate(K key, Map<K, V> map, Supplier<V> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }

    public static String stringConcat(String... args) {
        StringBuilder result = new StringBuilder();
        for (String arg : args) {
            result.append(arg);
            result.append("-");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    //Tue Jan 01 08:00:00 CST 2019
    public static Date parseStringDate(String dateString) {
        try {
            DateFormat dataFormat = new SimpleDateFormat("EEE MM dd HH:mm:ss zzz yyyy", Locale.US);
            return DateUtils.addHours(dataFormat.parse(dateString), -8);
        } catch (ParseException e) {
            log.error("parseStringDate error: {}", dateString);
            return null;
        }
    }
}
