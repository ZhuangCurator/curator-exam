package com.curator.common.util;

import com.curator.common.support.ResultResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Array;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Jun
 * @date 2021/4/15
 */
public class Help {

    public static final String FULLDATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 当前时间转为字符串
     *
     * @return 时间字符串
     */
    public static String currentTimeToTxt() {
        return localDateTimeToTxt(System.currentTimeMillis());
    }

    /**
     * 当前时间以自定义格式转为字符串
     *
     * @param format 自定义格式
     * @return 时间字符串
     */
    public static String currentTimeToTxt(String format) {
        return localDateTimeToTxt(System.currentTimeMillis(), format);
    }

    /**
     * 将时间以自定义格式转为字符串
     *
     * @param date   时间对象
     * @param format 自定义格式
     * @return 时间字符串
     */
    public static String localDateTimeToTxt(LocalDateTime date, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(dateTimeFormatter);
    }

    /**
     * 将时间转为字符串
     *
     * @param date 时间对象
     * @return 时间字符串
     */
    public static String localDateTimeToTxt(LocalDateTime date) {
        return localDateTimeToTxt(date, DATETIME_FORMAT);
    }

    /**
     * 将时间戳以自定义格式转为字符串
     *
     * @param timestamp 时间戳
     * @param format    自定义格式
     * @return 时间字符串
     */
    public static String localDateTimeToTxt(long timestamp, String format) {
        return localDateTimeToTxt(longToLocalDateTime(timestamp), format);
    }

    /**
     * 将时间戳转为字符串
     *
     * @param timestamp 时间戳
     * @return 时间字符串
     */
    public static String localDateTimeToTxt(long timestamp) {
        return localDateTimeToTxt(timestamp, DATETIME_FORMAT);
    }

    /**
     * 将字符串以自定义格式转为时间
     *
     * @param date   时间字符串
     * @param format 自定义格式
     * @return 时间对象
     */
    public static LocalDateTime txtToLocalDateTime(String date, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(date, dateTimeFormatter);
    }

    /**
     * 将字符串转为时间
     *
     * @param date 时间字符串
     * @return 时间对象
     */
    public static LocalDateTime txtToLocalDateTime(String date) {
        return txtToLocalDateTime(date, DATETIME_FORMAT);
    }

    /**
     * 将时间戳转为时间
     *
     * @param timestamp 时间戳
     * @return 时间对象
     */
    public static LocalDateTime longToLocalDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 字符串是否为null值或空字符串
     *
     * @param src 字符串对象
     * @return true/false
     */
    public static boolean isEmpty(String src) {
        return src == null || "null".equals(src) || src.length() == 0;
    }

    /**
     * 字符串是否为null值或空字符串或空格
     *
     * @param src 字符串对象
     * @return true/false
     */
    public static boolean isEmptyOrBlank(String src) {
        return isEmpty(src) || "".equals(src.trim());
    }

    /**
     * 集合是否为null值或不包含任何元素
     *
     * @param collection 集合对象
     * @return true/false
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * map是否为null值或不包含任何元素
     *
     * @param map Map对象
     * @return true/false
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 数组是否为null值或不包含任何元素
     *
     * @param array 数组对象
     * @return true/false
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断对象是否为Empty
     *
     * @param src 判断对象
     * @return rue/false
     */
    public static boolean isEmpty(Object src) {
        if (src == null) {
            return true;
        } else if (src instanceof String) {
            return isEmpty((String) src);
        } else if (src instanceof Map) {
            return isEmpty((Map<?, ?>) src);
        } else if (src instanceof Collection) {
            return isEmpty((Collection<?>) src);
        } else if (src.getClass().isArray()) {
            return Array.getLength(src) == 0;
        } else {
            return false;
        }
    }

    /**
     * 字符串是否为null值或空字符串 =》 相反判定
     *
     * @param src 字符串对象
     * @return true/false
     */
    public static boolean isNotEmpty(String src) {
        return !isEmpty(src);
    }

    /**
     * 字符串是否为null值或空字符串或空格 =》 相反判定
     *
     * @param src 字符串对象
     * @return true/false
     */
    public static boolean isNotEmptyAndBlank(String src) {
        return !isEmptyOrBlank(src);
    }

    /**
     * 集合是否为null值或不包含任何元素 =》 相反判定
     *
     * @param collection 集合对象
     * @return true/false
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * map是否为null值或不包含任何元素 =》 相反判定
     *
     * @param map Map对象
     * @return true/false
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 数组是否为null值或不包含任何元素 =》 相反判定
     *
     * @param array 数组对象
     * @return true/false
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 判断对象是否为Empty =》 相反判定
     *
     * @param src 判断对象
     * @return true/false
     */
    public static boolean isNotEmpty(Object src) {
        return !isEmpty(src);
    }

    /**
     * 字符串数组是否包含某个字符串
     *
     * @param array 字符串数组
     * @param value 某个固定字符串
     * @return true/false
     */
    public static boolean contains(String[] array, String value) {
        int i = 0;
        for (int l = array.length; i < l; ++i) {
            String o = array[i];
            if (o == null && value == null || o != null && o.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将Stream对象转为字符串
     *
     * @param stream            对象
     * @param separator         分隔符
     * @param endsWithSeparator 是否以分隔符为结束
     * @return 字符串
     */
    public static String concat(Stream<?> stream, String separator, boolean endsWithSeparator) {
        String str = stream.map(Object::toString).collect(Collectors.joining(separator));

        if (endsWithSeparator) {
            str = str + separator;
        }
        return str;
    }

    /**
     * 将Stream对象转为字符串
     *
     * @param stream    对象
     * @param separator 分隔符
     * @return 字符串
     */
    public static String concat(Stream<?> stream, String separator) {
        return concat(stream, separator, false);
    }

    /**
     * 将Collection集合转为字符串
     *
     * @param collection        集合
     * @param separator         分隔符
     * @param endsWithSeparator 是否以分隔符为结束
     * @return 字符串
     */
    public static String concat(Collection<?> collection, String separator, boolean endsWithSeparator) {
        return isNotEmpty(collection) ? concat(collection.stream(), separator, endsWithSeparator) : "";
    }

    /**
     * 将Collection集合转为字符串
     *
     * @param collection 集合
     * @param separator  分隔符
     * @return 字符串
     */
    public static String concat(Collection<?> collection, String separator) {
        return concat(collection, separator, false);
    }

    /**
     * 将Iterator对象转为字符串
     *
     * @param iterator          对象
     * @param separator         分隔符
     * @param endsWithSeparator 是否以分隔符为结束
     * @return 字符串
     */
    public static String concat(Iterator<?> iterator, String separator, boolean endsWithSeparator) {
        Stream<?> stream = StreamSupport.stream(((Iterable) () -> iterator).spliterator(), false);
        return concat(stream, separator, endsWithSeparator);
    }

    /**
     * 将Iterator对象转为字符串
     *
     * @param iterator  对象
     * @param separator 分隔符
     * @return 字符串
     */
    public static String concat(Iterator<?> iterator, String separator) {
        return concat(iterator, separator, false);
    }

    /**
     * 将数组转为字符串
     *
     * @param array             对象
     * @param separator         分隔符
     * @param endsWithSeparator 是否以分隔符为结束
     * @return 字符串
     */
    public static String concat(Object[] array, String separator, boolean endsWithSeparator) {
        return concat(Arrays.stream(array), separator, endsWithSeparator);
    }

    /**
     * 将数组转为字符串
     *
     * @param array     对象
     * @param separator 分隔符
     * @return 字符串
     */
    public static String concat(Object[] array, String separator) {
        return concat(array, separator, false);
    }

    /**
     * 将字符串分解为集合
     *
     * @param src     字符串
     * @param splitor 分离标志
     * @return 集合
     */
    public static List<String> split2List(String src, String split) {
        StringTokenizer tokenizer = new StringTokenizer(src, split);
        List<String> list = new ArrayList<>();

        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }

    public static void main(String[] args) throws JsonProcessingException {

        List<String> list = Collections.singletonList("list");
        ResultResponse<List<String>> res = ResultResponse.<List<String>>builder().success().message("xxxx").data(list).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(res);
        System.out.println(str);
        ResultResponse<List<String>> obj = objectMapper.readValue(str, new TypeReference<ResultResponse<List<String>>>() {});
        System.out.println(obj.toString());
    }

}
