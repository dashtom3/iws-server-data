package com.xj.iws.server.data;

/**
 * Created by XiaoJiang01 on 2017/3/16.
 */
public class DataFormat {

    /**
     * 预处理数据
     * @param message
     * @param start
     * @param length
     * @return
     */
    public static String preData(String message, int start, int length) {
        String temp = message.substring(start,message.length());
        return temp.substring(0,length);
    }

    /**
     * 截取数据
     *
     * @param data
     * @param step
     * @return
     */
    public static String[] subData(String data, int step) {
        int count = data.length()/step;
        String[] result = new String[count];
        for (int i = 0, j = 0; j < count; i += step, j++) {
            result[j] = data.substring(i, i + step);
        }
        return result;
    }
}
