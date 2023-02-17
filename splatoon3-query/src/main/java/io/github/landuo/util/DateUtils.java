package io.github.landuo.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author accidia
 */
public class DateUtils {
    private static final String BEGIN_OF_HOUR = "HH:mm";

    /**
     * 将时间戳格式化成默认格式的字符串
     * 
     * @param second
     *            秒级时间戳
     * 
     * @return
     */
    public static String formatDate(long second) {
        return formatDate(second, BEGIN_OF_HOUR);
    }

    /**
     * 将时间戳格式化成字符串
     * 
     * @param second
     *            秒级时间戳
     * @param pattern
     *            格式化的格式
     * 
     */
    public static String formatDate(long second, String pattern) {
        return DateUtil.format(new Date(second * 1000), pattern);
    }

    /**
     * 使用 yyyy-MM-dd'T'HH:mm:ss'Z' 格式获取时间
     *
     * @param date
     *            yyyy-MM-dd'T'HH:mm:ss'Z' 的时间字符串
     */
    public static LocalDateTime toLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CHINA);
        ZoneId shanghai = ZoneId.of("Asia/Shanghai");
        return LocalDateTime.parse(date, formatter).atZone(ZoneId.of("UTC")).withZoneSameInstant(shanghai)
                .toLocalDateTime();
    }

    /**
     * 将 yyyy-MM-dd'T'HH:mm:ss'Z' 格式的时间进行格式化
     *
     * @param date
     *            yyyy-MM-dd'T'HH:mm:ss'Z' 的时间字符串
     */
    public static String formatLocalDateTime(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CHINA);
        ZoneId shanghai = ZoneId.of("Asia/Shanghai");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter).atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(shanghai).toLocalDateTime();
        return LocalDateTimeUtil.format(dateTime, StrUtil.isBlank(pattern) ? BEGIN_OF_HOUR : pattern);
    }
}
