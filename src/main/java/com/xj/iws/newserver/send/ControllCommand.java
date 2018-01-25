package com.xj.iws.newserver.send;


import com.xj.iws.common.variable.GlobalVariables;
import com.xj.iws.newserver.util.SocketUtil;
import com.xj.iws.newserver.util.StrCastUtil;

import java.net.Socket;
import java.util.Arrays;

/**
 * 生成指令的工具类
 */
public class ControllCommand {

    /**
     * 将指定的PLC的一号风机设为手动
     * @param termid  该PLC的id
     */
    public void handBtn1_Click(String termid){
        //根据termid,得到相应的socket连接
        Socket socket= SocketUtil.map.get(termid);
        //控制之前要先查询一遍
        String result=SocketUtil.sendCommand(socket, GlobalVariables.CMD_QUERY,133);
        //得到两千的寄存器的值
        String v2000=result.substring(50,52);
        //将第一位写1
        Integer v2000Num=Integer.parseInt(v2000,16);//输出16进制数v2000在十进制下的数。
        v2000Num= StrCastUtil.write1ToNum(v2000Num,1);
        //
        String v2000_send=Integer.toHexString(v2000Num);
        int v2000_cmd=Integer.parseInt(v2000_send,16);
        byte[] CMD_QUERY=new byte[]{(byte) 0x03,(byte)0x00,(byte)0x00,(byte)0x24,(byte)0x02,(byte)0xf0,(byte)0x80,(byte)0x32,
                (byte)0x01,(byte)0x00,(byte)0x00,(byte)0xcc,(byte)0xc1,(byte)0x00,(byte)0x0e,(byte)0x00,(byte)0x05,(byte)0x05,
                (byte)0x01,(byte)0x12,(byte)0x0a,(byte)0x10,(byte)0x02,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x84,
                (byte)0x00,(byte)0x3e,(byte)0x80,(byte)0x00,(byte)0x04,(byte)0x00,(byte)0x08};
        CMD_QUERY= Arrays.copyOf(CMD_QUERY, CMD_QUERY.length+1);//数组扩容
        CMD_QUERY[CMD_QUERY.length-1]=(byte)v2000_cmd;         //插入数据
        String responseText=SocketUtil.sendCommand(socket,CMD_QUERY,22);

    }



}
