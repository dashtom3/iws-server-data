package com.xj.iws.http.dao;

import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2018/1/14.
 */
public interface DataDao {


    void insetDataToDB(@Param("deviceid")String termid,@Param("data") String result,@Param("tableName")String tableName);

    int existTable(@Param("tableName")String tableName);

    void createTable(@Param("tableName")String tableName);
}
