package com.xj.iws.common.enums;

import java.io.Serializable;

/**
 * Created by TIANCHENGYUAN103 on 2015-12-04.
 *
 */
public enum LogEnum implements Serializable {
    SERVER_START(1,"服务器启动"),
    SERVER_EXIT(2,"服务器关闭"),
    CLIENT_CONNECTION(3,"客户端连接"),
    CLIENT_BREAK(4,"客户端网络连接中断"),
    READER_START(5,"启动数据读取"),
    READER_CLOSE(6,"关闭数据读取"),
    DATA_SAVE(7,"执行数据存储")
    ;
    private int code;
    private String label;

    LogEnum() {
    }

    LogEnum(int code, String label) {

        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
