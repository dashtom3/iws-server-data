package com.xj.iws.http.listener;

import com.xj.iws.common.variable.GlobalVariables;
import com.xj.iws.http.dao.AlarmDao;
import com.xj.iws.http.dao.DataDao;
import com.xj.iws.http.dao.DeviceDao;
import com.xj.iws.http.dao.NewsDao;
import com.xj.iws.http.entity.AlarmEntity;
import com.xj.iws.http.entity.DeviceTermEntity;
import com.xj.iws.http.entity.NewsEntity;
import com.xj.iws.http.entity.PointFieldEntity;
import com.xj.iws.newserver.InitServer;
import com.xj.iws.newserver.util.ResponParse;
import com.xj.iws.newserver.util.SocketUtil;
import com.xj.iws.newserver.util.StrCastUtil;
import com.xj.iws.server.data.DataAlarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.Socket;
import java.util.List;
import java.util.Set;

/**
 * 定时查询数据并保存进数据库
 */
@Component("recieveDataForS7")
public class RecieveDataForS7 {

    private static int odd=0;//用于计数四个BIT

        @Autowired
        private InitServer initServer;

        @Autowired
        private DataDao dataDao;

        @Autowired
        private DeviceDao deviceDao;

        @Autowired
        private DataAlarm dataAlarm;

        @Autowired
        private AlarmDao alarmDao;

        @Autowired
        private NewsDao newsDao;

        @Scheduled(cron="*/5 * * * * ?")
        public void saveDataForDirtyWater(){
            System.out.println("开始执行");
            initServer.init();
            Set<String> keySet= SocketUtil.map.keySet();
            String result="";
            for(String key:keySet){
                Socket socket=SocketUtil.map.get(key);
                //请求数据的长度
                int length=SocketUtil.TOTAL_LENGTH_MAP.get(key);
                String hexString=String.format("%04x", length);
                System.out.println(hexString);
                byte[] bytes= StrCastUtil.hexStringToBytes(hexString);
                byte[] cmd2 = GlobalVariables.CMD_QUERY;
                int start=SocketUtil.START_ADDRESS_MAP.get(key)*8;
                System.out.println(start);
                String hexStringstart=String.format("%06x", start);
                System.out.println(hexStringstart);
                byte[] bytesTwo=StrCastUtil.hexStringToBytes(hexStringstart);
                //请求数据长度
                cmd2[23]=bytes[0];
                cmd2[24]=bytes[1];
                System.out.println("bytes[0]"+bytes[0]);
                System.out.println("bytes[1]"+bytes[1]);
                //起始地址
                cmd2[28]=bytesTwo[0];
                cmd2[29]=bytesTwo[1];
                cmd2[30]=bytesTwo[2];
                System.out.println("bytesTwo[0]"+bytesTwo[0]);
                System.out.println("bytesTwo[1]"+bytesTwo[1]);
                System.out.println("bytesTwo[2]"+bytesTwo[2]);
                result=SocketUtil.sendCommand(socket,cmd2,SocketUtil.TOTAL_LENGTH_MAP.get(key)+25);
                loadDataToDB(key,result);
            }

        }

        public void loadDataToDB(String key,String result){
            String tableName="data_tcp_"+key;
            int existTable=dataDao.existTable(tableName);
            if(existTable==0){
                dataDao.createTable(tableName);
            }
            dataDao.insetDataToDB(key,result,tableName);
            //检验数据是否有异常
            checkCodeForTCP(key,result);
        }

    private void checkCodeForTCP(String key, String data) {
        //1.获取点表规则
        //根据deviceId来获取termid
        Integer termid=deviceDao.getTermId(Integer.parseInt(key));
        List<PointFieldEntity> pointFields = deviceDao.getPointField(termid);
        //2.进行比对
        int index=0;
        data=data.substring(50);//去掉5-6层，留下7层数据
        AlarmEntity alarm= alarmDao.getAddress(Integer.parseInt(key));
        alarm.setName(alarm.getSystemName()+alarm.getLocationName()+alarm.getRoomName()+alarm.getGroupName()+"#监测异常");
        System.out.println("key"+key);
        String  deviceTermName=deviceDao.getDeviceTermNameByDeviceId(Integer.parseInt(key));
        System.out.println("deviceTermName"+deviceTermName);
        for(int i=0;i<pointFields.size();i++){
            PointFieldEntity field=pointFields.get(i);
            //3.判断是否有异常，如果有异常保存进数据库异常表
            //将电表规则进行数据解析
            switch (field.getRoleId()){
                case 6:
                    numberToZero();
                    role01(data,field,index,alarm,deviceTermName);
                    index+=2;
                    break;
                case 7:
                    numberToZero();
                    role02(data,field,index,alarm,deviceTermName);
                    index+=2;
                    break;
                case 8:
                    numberToZero();
                    index+=4;
                    break;
                case 9:
                    numberToZero();
                    role04(data,field,index,3,alarm,deviceTermName);
                    index+=8;
                    break;
                case 10:
                    numberToZero();
                    role04(data,field,index,3,alarm,deviceTermName);
                    index+=8;
                    break;
                case 11:
                    if(odd==0){
                        role05(data,field,index,alarm,deviceTermName);
                        index+=2;
                        odd++;
                    }else{
                        role06(data,field,index,alarm,deviceTermName);
                        numberToZero();
                    }
                    break;
                case 12:
                    numberToZero();
                    role07(data,field,index,alarm,deviceTermName);
                    index+=2;
                    break;
                case 13:
                    numberToZero();
                    role08(data,field,index,alarm,deviceTermName);
                    index+=2;
                    break;
                case 18:
                    numberToZero();
                    index+=20;
                    break;
                case 16:
                    numberToZero();
                    index+=8;
                    break;
                case 14:
                    numberToZero();
                    index+=2;
                    break;
                case 17:
                    numberToZero();
                    role09(data,field,index,alarm,deviceTermName);
                    index+=2;
                    break;
                case 15:
                    numberToZero();
                    index+=4;
                    break;
                case 19:
                    numberToZero();
                    index+=100;
                    break;
                default:
                    numberToZero();
                    break;
            }
        }
        numberToZero();
    }

    private void role09(String data, PointFieldEntity field, int index, AlarmEntity alarm, String deviceTermName) {
        String temp_data=data.substring(index-2,index);
        String code= StrCastUtil.hexStrToBinaryStr(temp_data);
        StringBuffer sb=new StringBuffer(deviceTermName);
        NewsEntity news =null;
        if("1".equals(code.substring(7))){
            sb.append(field.getName()).append("无水故障");
            alarm.setDescribes(sb.toString());
            news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
            newsDao.add(news);
        }else if("1".equals(code.substring(6,7))){
            sb.append(field.getName()).append("高水信号");
            alarm.setDescribes(sb.toString());
            news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
            newsDao.add(news);
        }else if("1".equals(code.substring(5,6))){
            sb.append(field.getName()).append("地面积水信号");
            alarm.setDescribes(sb.toString());
            news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
            newsDao.add(news);
        }else if("1".equals(code.substring(4,5))){
            sb.append(field.getName()).append("相序故障");
            alarm.setDescribes(sb.toString());
            news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
            newsDao.add(news);
        }else if("1".equals(code.substring(3,4))) {
            sb.append(field.getName()).append("地面积水信号");
            alarm.setDescribes(sb.toString());
            news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
            newsDao.add(news);
        }
    }

    private void role08(String data, PointFieldEntity field, int index, AlarmEntity alarm, String deviceTermName) {
        String temp_data=data.substring(index,2+index);
        String code= StrCastUtil.hexStrToBinaryStr(temp_data);
        StringBuffer sb=new StringBuffer(deviceTermName);
        NewsEntity news =null;
        if("1".equals(code.substring(6,7))){
            sb.append(field.getName()).append("视频监控报警");
            alarm.setDescribes(sb.toString());
            news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
            newsDao.add(news);
        }
    }

    private void role07(String data, PointFieldEntity field, int index, AlarmEntity alarm, String deviceTermName) {
        String temp_data=data.substring(index,2+index);
        String code= StrCastUtil.hexStrToBinaryStr(temp_data);
        StringBuffer sb=new StringBuffer(deviceTermName);
        NewsEntity news =null;
        if("1".equals(code.substring(5,6))){
            sb.append(field.getName()).append("非法入侵信号2");
            alarm.setDescribes(sb.toString());
            news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
            newsDao.add(news);
        }else if("1".equals(code.substring(3,4))){
            sb.append(field.getName()).append("非法入侵信号1");
            alarm.setDescribes(sb.toString());
            news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
            newsDao.add(news);
        }
    }

    private void role06(String data, PointFieldEntity field, int index, AlarmEntity alarm, String deviceTermName) {
        String temp_data=data.substring(index-2,index);
        String code= StrCastUtil.hexStrToBinaryStr(temp_data);
        StringBuffer sb=new StringBuffer(deviceTermName);
        NewsEntity news =null;
        if("1".equals(code.substring(0,1))){
            sb.append(field.getName()).append("故障");
            alarm.setDescribes(sb.toString());
            news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
            newsDao.add(news);
        }

    }

    private void role05(String data, PointFieldEntity field, int index, AlarmEntity alarm, String deviceTermName) {
        String temp_data=data.substring(index,2+index);
        String code= StrCastUtil.hexStrToBinaryStr(temp_data);
        StringBuffer sb=new StringBuffer(deviceTermName);
        NewsEntity news =null;
        if("1".equals(code.substring(4,5))){
            sb.append(field.getName()).append("故障");
            alarm.setDescribes(sb.toString());
            news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
            newsDao.add(news);
        }
    }

    private void role04(String data, PointFieldEntity field, int index, int length, AlarmEntity alarm, String deviceTermName) {
        /*String temp_data=data.substring(index,8+index);
        Float value=Float.intBitsToFloat((int)Long.parseLong(temp_data.trim(), 16));
        System.out.println("value"+value);
        System.out.println("multiple"+field.getMultiple());
        if(field.getMultiple()!=0){
            value=value/(field.getMultiple());//除以倍率
            System.out.println("value"+value);
        }
        BigDecimal b  =   new BigDecimal(value);
        BigDecimal bigDecimal= b.setScale(length, BigDecimal.ROUND_HALF_UP);
        StringBuffer sb=new StringBuffer(deviceTermName);
        NewsEntity news =null;
        if(field.getMax()!=0){
            BigDecimal max=new BigDecimal(field.getMax());
            if(bigDecimal.compareTo(max)==1){
                sb.append(field.getName()).append("超过上限");
                alarm.setDescribes(sb.toString());
                news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
                newsDao.add(news); 
            }
        }
        if(field.getMin()!=0){
            BigDecimal min=new BigDecimal(field.getMin());
            if(min.compareTo(bigDecimal)==1){
                sb.append(field.getName()).append("小于下限");
                alarm.setDescribes(sb.toString());
                news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
                newsDao.add(news);
            }
        }*/
        return;
    }


    private void role02(String data, PointFieldEntity field, int index, AlarmEntity alarm, String deviceTermName) {
        String temp_data=data.substring(index,2+index);
        String code= StrCastUtil.hexStrToBinaryStr(temp_data);
        StringBuffer sb=new StringBuffer(deviceTermName);
        NewsEntity news =null;
        if("01".equals(code.substring(6,8))){
            sb.append(field.getName()).append("低液位报警");
            alarm.setDescribes(sb.toString());
            news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
            newsDao.add(news);
        }else if("10".equals(code.substring(6,8))){
            sb.append(field.getName()).append("高液位报警");
            alarm.setDescribes(sb.toString());
            news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
            newsDao.add(news);
        }
    }

    private void role01(String data, PointFieldEntity field, int index,AlarmEntity alarm,String deviceTermName) {
        String temp_data=data.substring(index,2+index);
        String code= StrCastUtil.hexStrToBinaryStr(temp_data);
        StringBuffer sb=new StringBuffer(deviceTermName);
        if("1".equals(code.substring(4,5))){
            sb.append(field.getName()).append("故障");
            alarm.setDescribes(sb.toString());
            NewsEntity news = new NewsEntity(alarm.getId(),alarm.getName(), alarm.getDescribes());
            newsDao.add(news);
        }
    }

    //清零计数器
    private void numberToZero(){
        if(odd!=0){
            odd=0;
        }
    }


}

