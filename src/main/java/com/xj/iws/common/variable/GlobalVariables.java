package com.xj.iws.common.variable;

/**
 * Created by Administrator on 2017/12/21.
 */
public class GlobalVariables {
    public static byte[] CMD_REQUEST=new byte[] { (byte)0x03,(byte)0x00,(byte)0x00,(byte)0x16,(byte)0x11,
            (byte)0xe0,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0xc1, (byte)0x02,(byte)0x02,
            (byte)0x11,(byte)0xc2,(byte)0x02,(byte)0x02,(byte)0x00, (byte)0xc0,(byte)0x01,(byte)0x09};

    public static byte[] CMD_SETUP=new byte[] {(byte) 0x03,(byte) 0x00,(byte) 0x00,(byte) 0x19, (byte)0x02,(byte) 0xf0,(byte) 0x80,
            (byte)0x32,(byte) 0x01, (byte)0x00, (byte)0x00,(byte) 0xcc, (byte)0xc1, (byte)0x00,(byte) 0x08,(byte) 0x00,
            (byte)0x00,(byte) 0xf0,(byte) 0x00,(byte) 0x00,(byte) 0x01,(byte) 0x00,(byte)0x01,(byte)0x03,(byte)0xc0 };

    //基本的查询命令
    public static byte[] CMD_QUERY=new byte[]{(byte) 0x03,(byte)0x00,(byte)0x00,(byte)0x1f,(byte)0x02,(byte)0xf0,(byte)0x80,(byte)0x32,
            (byte)0x01,(byte)0x00,(byte)0x00,(byte)0xcc,(byte)0xc1,(byte)0x00,(byte)0x0e,(byte)0x00,(byte)0x00,(byte)0x04,
            (byte)0x01,(byte)0x12,(byte)0x0a,(byte)0x10,(byte)0x02,(byte)0x00,(byte)0x6c,(byte)0x00,(byte)0x01,(byte)0x84,
            (byte)0x00,(byte)0x3e,(byte)0x80};

    //5-6层的基本报文，第四个是可能会改变的值，其实后面5-6-7三层的总长度
    public static byte[] CMD_PRE=new byte[]{(byte) 0x03,(byte)0x00,(byte)0x00,(byte)0x1f,(byte)0x02,(byte)0xf0,(byte)0x80};


    //5-6层的报文的基本长度
    public static final int BASE_LENGTH=7;


    //7层的基本报文_header,最后两个字节比较重要，表示第七层的data部分的长度
    public static byte[] CMD_PRE_HEADER=new byte[]{(byte)0x32,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0xcc,(byte)0xc1,(byte)0x00,(byte)0x0e,(byte)0x00,(byte)0x00};

    //第七层中的header长度
    public static final int HEADER_LENGTH=10;

    //7层的基本报文_parameter,第一个字节（04读取,05写入）第七.八个字节表示请求数据长度 第十一个字节表示寄存器类型  第12.13.14三个数组表示的是起始的数据长度
    public static byte[] CMD_PRE_PARAMETER=new byte[]{(byte)0x04,(byte)0x01,(byte)0x12,(byte)0x0a,(byte)0x10,(byte)0x02,(byte)0x00,
            (byte)0x6c,(byte)0x00,(byte)0x01,(byte)0x84,(byte)0x00,(byte)0x3e,(byte)0x80 };

    //第七层中的parameter长度
    public static final int PARAMETER_LENGTH=14;


    //第七层中下发读取时，data是没有的
    public static final int READ_DATA_LENGTH=0;

    //第七层中下发写入,后面还需要添加要写入的数据
    public static byte[] CMD_PRE_DATA=new byte[]{(byte)0x00,(byte)0x04,(byte)0x00,(byte)0x08};


}
