package com.qian.zhihuribao.utils;

import android.content.Context;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

/**
 * Created by jqian on 2015/10/12.
 */
public class CacheUtil {
    static DB snappydb;

    public static void open(Context ctx) {
        try {
            snappydb = DBFactory.open(ctx);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public static void save(String key, String value) {
        try {
            snappydb.put(key, value);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        try {
            return snappydb.get(key);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close() {
        try {
            snappydb.close();
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }
}
