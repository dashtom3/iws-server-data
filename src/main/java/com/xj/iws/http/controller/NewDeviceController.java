package com.xj.iws.http.controller;


import com.xj.iws.newserver.util.SocketUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.Socket;
import java.util.Map;
import java.util.Set;

/**
 * 新协议的设备管理
 */
@Controller
@RequestMapping("api/newDevice")
public class NewDeviceController {


    @RequestMapping("query")
    @ResponseBody
    public String query(){
        Map<String,Socket> map= SocketUtil.map;
        Set<String> keySet=map.keySet();
        String result="";
        for(String key:keySet){
            Socket socket=map.get(key);
            byte[] cmd2 = new byte[] {(byte) 0x03,(byte)0x00,(byte)0x00,(byte)0x24,(byte)0x02,(byte)0xf0,(byte)0x80,(byte)0x32,
                    (byte)0x01,(byte)0x00,(byte)0x00,(byte)0xcc,(byte)0xc1,(byte)0x00,(byte)0x0e,(byte)0x00,(byte)0x05,(byte)0x05,
                    (byte)0x01,(byte)0x12,(byte)0x0a,(byte)0x10,(byte)0x02,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x84,
                    (byte)0x00,(byte)0x3e,(byte)0x80,(byte)0x00,(byte)0x04,(byte)0x00,(byte)0x08,(byte)0x02};
            result=SocketUtil.sendCommand(socket,cmd2,22);
        }
        return result;
    }

}
