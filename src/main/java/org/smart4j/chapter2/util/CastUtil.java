package org.smart4j.chapter2.util;

import org.apache.commons.lang3.StringUtils;

/*
* 转型操作工具类
* */
public final class CastUtil {

    /*
    * 转为String型
    * */
    public static String castString(Object obj) {
        return castString(obj, "");
    }

    /*
    * 转为String型（提供默认值）
    * */
    public static String castString(Object obj, String defaultValue) {
        return obj != null ? String.valueOf(obj) : defaultValue;
    }

    /*
    * 转为double型
    * */
    public static double castDouble(Object obj) {
        return castDouble(obj, 0);
    }

    /*
    * 转为double型（提供默认值）
    * */
    public static double castDouble(Object obj, double defaultValue) {
        double doubleValue = defaultValue;
        if (obj != null) {
            String stringValue = castString(obj);
            if (StringUtils.isNotEmpty(stringValue)) {
                try {
                    doubleValue = Double.parseDouble(stringValue);
                } catch (NumberFormatException e) {
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }

    /*
   * 转为long型
   * */
    public static long castLong(Object obj) {
        return castLong(obj, 0);
    }

    /*
    * 转为long型（提供默认值）
    * */
    public static long castLong(Object obj, long defaultValue) {
        long longValue = defaultValue;
        if (obj != null) {
            String stringValue = castString(obj);
            if (StringUtils.isNotEmpty(stringValue)) {
                try {
                    longValue = Long.parseLong(stringValue);
                } catch (NumberFormatException e) {
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    /*
   * 转为int型
   * */
    public static int castInt(Object obj) {
        return castInt(obj, 0);
    }

    /*
    * 转为int型（提供默认值）
    * */
    public static int castInt(Object obj, int defaultValue) {
        int intValue = defaultValue;
        if (obj != null) {
            String stringValue = castString(obj);
            if (StringUtils.isNotEmpty(stringValue)) {
                try {
                    intValue = Integer.parseInt(stringValue);
                } catch (NumberFormatException e) {
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    /*
   * 转为boolean型
   * */
    public static boolean castBoolean(Object obj) {
        return castBoolean(obj, false);
    }

    /*
    * 转为boolean型（提供默认值）
    * */
    public static boolean castBoolean(Object obj, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        if (obj != null) {
            booleanValue = Boolean.parseBoolean(castString(obj));
        }
        return booleanValue;
    }

}
