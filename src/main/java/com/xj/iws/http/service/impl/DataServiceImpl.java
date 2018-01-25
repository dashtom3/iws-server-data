package com.xj.iws.http.service.impl;

import com.xj.iws.common.util.DataWrapper;
import com.xj.iws.common.util.TimeUtil;
import com.xj.iws.http.dao.DeviceDao;
import com.xj.iws.http.dao.mysql.LoadDataInStream;
import com.xj.iws.http.service.DataService;
import com.xj.iws.http.dao.redis.RedisBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2017/6/14.
 *
 */
@Service
public class DataServiceImpl implements DataService {
    @Autowired
    RedisBase redisBase;
    @Autowired
    DeviceDao deviceDao;
    @Autowired
    LoadDataInStream loadDataInStream;

    @Override
    public DataWrapper<Void> saveAll() {
        //获取当前日期的前一天
        String date = TimeUtil.getDate(new Date(), -1);
        //找出所有正在运行的设备
        Set<String> keys = redisBase.setOps().members("keys_device_run");
        //遍历这个所有正在运行的设备
        for (String key : keys) {
            //表名为data_key
            String tableName = "data_" + key;
            //初始化一个values的set
            Set<String> values;
            try{
                //将所有正在运行的设备的数据填充进value
                values = redisBase.zSetOps().range("data_" + date + "_" + key,0,-1);
            }catch (Exception e){
                break;
            }
            //初始化一个data的list
            List<String[]> datas = new ArrayList<>();
            //字段名为time,data,error
            String fieldName = "time,data,error";
            //遍历上面获得的正在运行的设备的数据,前一天的
            for (String value : values) {
                //将每一条数据按照冒号进行拆分,得到一个字符串的数组
                String[] temp = value.split(":");
                //第一个字符串给它加上一个日期，前一天，做了一个处理的动作
                temp[0] = date + temp[0];
                //将这个字符串数组添加进一个list
                datas.add(temp);
            }

            int row = loadDataInStream.write(tableName, datas, fieldName);
            if (row != 0){
                flushData("data_" + date + "_" + key);
            }
        }
        saveRunDevice();
        return null;
    }

    private void saveRunDevice() {
        //待写入数据库的设备
        redisBase.getRedisTemplate().delete("keys_device_run");
        //正在跑的设备
        Set<String> runningDevices = redisBase.setOps().members("keys_device_running");
        //将正在跑的设备添加进药写入数据库的设备的redis记录中
        for (String device : runningDevices) {
            redisBase.setOps().add("keys_device_run", device);
        }
    }

    private void flushData(String key){
        redisBase.getRedisTemplate().delete(key);
    }
}
