package com.xj.iws.newserver.util;

import com.xj.iws.common.util.ByteUtil;
import com.xj.iws.http.dao.DeviceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 向PLC客户端发送指令，并获取返回值
 */

public class SocketUtil {


    public static Map<String,Socket> map=new HashMap<String,Socket>();

    public static Map<String,Integer> TOTAL_LENGTH_MAP=new HashMap<String,Integer>();

    public static Map<String,Integer> START_ADDRESS_MAP=new HashMap<>();



    /**
     * 初始化一个PLC的socket
     */


    /**
     * 下发指令
     * @param socket
     * @param cmdBytes
     * @return
     */
    public static String sendCommand(Socket socket,byte[] cmdBytes,int length) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        String byteString = "";
        try {
            outputStream = socket.getOutputStream();
            outputStream.write(cmdBytes);
            inputStream = socket.getInputStream();//获取一个输入流，接收服务端的信息
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);//包装成字符流，提高效率
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);//缓冲区
        byte[] bytes = ByteUtil.streamToBytes(inputStream, length);
        byteString = ByteUtil.byteArrayToHexString(bytes);
        System.out.println("收到服务器的数据为" + byteString);
        return byteString;
    }

    public static Socket getSocket(String deviceid) {
        Socket socket=map.get(deviceid);
        if(socket==null){
            initSocket(deviceid);
        }
        return map.get(deviceid);
    }

    private static void initSocket(String deviceid) {

    }
}
