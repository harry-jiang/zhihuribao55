package com.qian.zhihuribao.utils;

/**
 * Created by jqian on 2015/9/16.
 */
public class StringUtils {

    public static String appendZero(int num) {
        return num < 10 ? "0" + num : "" + num;
    }
}
