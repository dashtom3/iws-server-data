package com.xj.iws.newserver.util;

import java.net.Socket;

/**
 * 对污水服务器返回的数据进行解析
 */
public class ResponParse {

    private String responseText;

    public ResponParse(String responseText) {
        this.responseText = responseText;
    }

    /**
     * 对污水服务器返回的数据进行解析
     */
    public void parseAll(){

        //解析1#风机当前状态
        role_01(50,52,"1#风机");

        //解析2#风机当前状态
        role_01(52,54,"2#风机");

        //解析1#提升泵当前状态
        role_01(54,56,"1#提升泵");

        //解析2#提升泵当前状态
        role_01(56,58,"2#提升泵");

        //解析1#污泥泵当前状态
        role_01(58,60,"1#污泥泵");

        //解析2#污泥泵当前状态
        role_01(60,62,"2#污泥泵");

        //解析1#回流泵当前状态
        role_01(62,64,"1#回流泵");

        //解析2#回流泵当前状态
        role_01(64,66,"2#回流泵");

        //解析1#MBR泵当前状态
        role_01(66,68,"1#MBR泵");

        //解析2#MBR泵当前状态
        role_01(68,70,"2#MBR泵");

        //解析1#反洗泵当前状态
        role_01(70,72,"1#反洗泵");

        //解析2#反洗泵当前状态
        role_01(72,74,"2#反洗泵");

        //解析1#化粪池泵当前状态
        role_01(74,76,"1#化粪池泵");

        //解析2#化粪池泵当前状态
        role_01(76,78,"2#化粪池泵");

        //解析格栅机当前状态
        role_01(78,80,"格栅机");

        //解析调节池液位警报
        role_02(84,86,"调节池");

        //风机运行时间设定
        role_03(110,114,"风机运行时间","min");

        //风机休息时间设定
        role_03(114,118,"风机休息时间","min");

        //风机轮换时间设定
        role_03(118,122,"风机轮换时间","min");

        //提升泵轮换时间设定
        role_03(122,126,"提升泵轮换时间","min");

        //污泥泵运行时间设定
        role_03(126,130,"污泥泵运行时间","min");

        //污泥泵休息时间设定
        role_03(130,134,"污泥泵休息时间","min");

        //污泥泵轮换时间设定
        role_03(134,138,"污泥泵轮换时间","min");

        //回流泵运行时间设定
        role_03(138,142,"回流泵运行时间","min");

        //回流泵休息时间设定
        role_03(142,146,"回流泵休息时间","min");

        //回流泵轮换时间设定
        role_03(146,150,"回流泵轮换时间","min");

        //MBR泵运行时间设定
        role_03(150,154,"MBR泵运行时间","min");

        //MBR泵休息时间设定
        role_03(154,158,"MBR泵休息时间","min");

        //MBR泵轮换时间设定
        role_03(158,162,"MBR泵轮换时间","min");

        //反洗泵运行时间设定
        role_03(162,166,"反洗泵运行时间","min");

        //反洗泵休息时间设定
        role_03(166,170,"反洗泵休息时间","min");

        //反洗泵轮换时间设定
        role_03(170,174,"反洗泵轮换时间","min");

        //化粪池泵运行时间设定
        role_03(174,178,"化粪池泵运行时间","min");

        //化粪池泵休息时间设定
        role_03(178,182,"化粪池泵休息时间","min");

        //化粪池泵轮换时间设定  格栅机运行时间设定
        role_03(186,190,"化粪池泵轮换时间","min");

        //格栅机运行时间设定
        role_03(190,194,"格栅机运行时间","min");

        //格栅机休息时间设定
        role_03(194,198,"格栅机休息时间","min");

        //氨氮量程设定
        role_03(210,214,"氨氮量程","");

        //氨氮实时检测
        role_03(214,218,"氨氮实时检测","");

        //COD量程设定
        role_03(218,222,"COD量程","");

        //COD实时检测
        role_03(222,226,"COD实时检测","");

        //PH实时检测
        role_03(226,230,"PH实时检测","");

        //液位量程设定
        role_03(230,234,"液位量程","米");

        //液位反馈
        role_03(234,238,"液位反馈","米");

        //累计流量
        role_04(250,258,"累计流量","吨");

        //瞬时流量
        role_04(258,266,"瞬时流量","吨/时");












    }

    /**
     * 手动/自动，启动/停止，运行，故障(1个字节)
     * @param start     服务器响应数据中的起始位置
     * @param end       服务器响应数据中的结束位置
     * @param name      该段数据代表的含义
     */
    private void role_01(int start,int end,String name){
        String code_hex=responseText.substring(start,end);
        String code=StrCastUtil.hexStrToBinaryStr(code_hex);
        System.out.println(name+"状态是:");
        String status=code.substring(7);
        if("0".equals(status)){
            System.out.println(name+"控制方式:  自动");
        }else{
            System.out.println(name+"控制方式:  手动");
        }

        status=code.substring(5,7);
        if("01".equals(status)){
            System.out.println(name+"启停状态:  启动");
        }else{
            System.out.println(name+"启停状态:  停止");
        }

        status=code.substring(4,5);
        if("0".equals(status)){
            System.out.println(name+"故障信号:  正常");
        }else{
            System.out.println(name+"故障信号:  故障");
        }

        status=code.substring(3,4);
        if("1".equals(status)){
            System.out.println(name+"运行信号:  运行");
        }else {
            System.out.println(name+"运行信号:  休息");
        }
    }

    /**
     * 调节池的 高/低 液位报警位(1个字节)
     * @param start
     * @param end
     * @param name
     */
    private void role_02(int start,int end,String name){
        String code_hex=responseText.substring(start,end);
        String code=StrCastUtil.hexStrToBinaryStr(code_hex);
        System.out.println(name+"状态是:");
        String status=code.substring(7);
        if("1".equals(status)){
            System.out.println(name+"低液位报警:  报警");
        }else{
            System.out.println(name+"低液位报警:  正常");
        }
        status=code.substring(6,7);
        if("1".equals(status)){
            System.out.println(name+"高液位报警:  报警");
        }else{
            System.out.println(name+"高液位报警:  正常");
        }
    }

    /**
     * 值的读取和设定(2个字节)
     * @param start
     * @param end
     * @param name
     */
    private void role_03(int start,int end,String name,String unit){
        String code_hex=responseText.substring(start,end);
        Integer number=Integer.parseInt(code_hex,16);
        System.out.println(name+"状态是:");
        System.out.println("值为"+number+unit);
    }

    /**
     * 流量的读取和设定(4个字节)
     * @param start
     * @param end
     * @param name
     */
    private void role_04(int start,int end,String name,String unit){
        String code_hex=responseText.substring(start,end);
        Float number=Float.intBitsToFloat(Integer.valueOf(code_hex.trim(), 16));
        System.out.println(name+"状态是:");
        System.out.println("值为"+number+unit);
    }


}
