package com.xj.iws.newserver;

import com.mysql.fabric.xmlrpc.base.ResponseParser;
import com.xj.iws.common.util.ByteUtil;
import com.xj.iws.common.variable.GlobalVariables;
import com.xj.iws.http.dao.DeviceDao;

import com.xj.iws.newserver.util.ResponParse;
import com.xj.iws.newserver.util.SocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.*;




public class InitServer {
    @Autowired
    private DeviceDao deviceDao;


    public void init(){
        List<String> runningGroupIdList=deviceDao.getNewRunningGroup();
        System.out.println("groupidlist"+runningGroupIdList);
        if(runningGroupIdList==null) return;
        List<Map> avariableDeviceList=deviceDao.getNewDeviceTermEntity(runningGroupIdList);
        System.out.println(avariableDeviceList);
        if(avariableDeviceList!=null&&avariableDeviceList.size()!=0) {
            for (int i = 0; i < avariableDeviceList.size(); i++) {
                Map map=avariableDeviceList.get(i);
                if(map==null){
                    return;
                }
                if(SocketUtil.map.get(map.get("id")+"")!=null){
                    continue;
                }
                String addrAndPortParams = (String) map.get("port");
                String[] addrAndPort = addrAndPortParams.split(":");
                System.out.println("第" + i + "个" + map);
                String ip = addrAndPort[0];
                int port = Integer.parseInt(addrAndPort[1] + "");
                Socket socket = initServer(ip, port);
                if(socket!=null){
                    String termId = map.get("id") + "";
                    SocketUtil.map.put(termId, socket);
                    int totalbytes =deviceDao.getLength((int)map.get("termid"));
                    System.out.println("总长度"+totalbytes);
                    SocketUtil.TOTAL_LENGTH_MAP.put(termId,totalbytes/2);
                    SocketUtil.START_ADDRESS_MAP.put(termId,((Long)(map.get("start"))).intValue());
                }
            }
        }
    }


    /**
     * 通讯请求并setup正在运行的控制器
     * @param ip
     * @param port
     * @return
     */
    public Socket initServer(String ip,int port){
        Socket socket=null;
        try {
            //创建Socket对象
            socket=new Socket(ip,port);
            //根据输入输出流和服务端连接
            OutputStream outputStream=socket.getOutputStream();//获取一个输出流，向服务端发送信息
            outputStream.write(GlobalVariables.CMD_REQUEST);
            InputStream inputStream=socket.getInputStream();//获取一个输入流，接收服务端的信息
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);//包装成字符流，提高效率
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);//缓冲区
            byte[] bytes= ByteUtil.streamToBytes(inputStream,22);
            String byteString =ByteUtil.byteArrayToHexString(bytes);
            System.out.println("服务端收到的数据为"+byteString);
            outputStream.write(GlobalVariables.CMD_SETUP);
            byte[] byteArr=ByteUtil.streamToBytes(inputStream,27);
            String byteArrString =ByteUtil.byteArrayToHexString(byteArr);
            System.out.println("服务端收到的数据为"+byteArrString);
            //关闭相对应的资源
        } catch (Exception e) {
            System.out.println("连接失败");
            e.printStackTrace();
        }
        return socket;
    }
}
