package com.xj.iws.newserver.util;

import sun.misc.FloatingDecimal;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Administrator on 2018/1/3.
 */
public class SecondResponseParse {
    private String responseText;

    public SecondResponseParse(String responseText) {
        this.responseText = responseText;
    }

    public void parseAll(){
        //泵前压力
        role_01(50,58,3,"泵前压力","Mpa");

        //泵后压力
        role_01(58,66,3,"泵后压力","Mpa");

        //水箱水位
        role_01(66,74,3,"泵后压力","Mpa");

        //电机电流Ia
        role_01(74,82,1,"电机电流Ia","A");

        //电机电流Ib
        role_01(82,90,1,"电机电流Ib","A");

        //电机电流Ic
        role_01(90,98,1,"电机电流Ic","A");

        //电机电压Ua
        role_01(98,106,1,"电机电压Ua","V");

        //电机电压Ub
        role_01(106,114,1,"电机电压Ub","V");

        //电机电压Uc
        role_01(114,122,1,"电机电压Uc","V");

        //1#主泵变频器频率
        role_01(122,130,2,"1#主泵变频器频率","Hz");

        //2#主泵变频器频率
        role_01(130,138,2,"2#主泵变频器频率","Hz");

        //3#主泵变频器频率
        role_01(138,146,2,"3#主泵变频器频率","Hz");

        //4#主泵变频器频率
        role_01(146,154,2,"4#主泵变频器频率","Hz");

        //5#主泵变频器频率
        role_01(154,162,2,"5#主泵变频器频率","Hz");

        //辅泵变频器频率
        role_01(162,170,2,"辅泵变频器频率","Hz");

        //出水瞬时流量1
        role_01(170,178,2,"出水瞬时流量1","M3/h");

        //出水累积流量1
        role_01(178,186,1,"出水累积流量1","M3");

        //出水瞬时流量2
        role_01(186,194,2,"出水瞬时流量2","M3/h");

        //出水累积流量2
        role_01(194,202,1,"出水累积流量2","M3");

        //累积电量
        role_01(202,210,1,"累积电量","KWH");

        //进水电动阀开度
        role_01(210,218,1,"进水电动阀开度","%");

        //1#主泵
        role_02(218,220,0,"1#主泵");

        //2#主泵
        role_02(218,220,4,"2#主泵");

        //3#主泵
        role_02(220,222,0,"3#主泵");

        //4#主泵
        role_02(220,222,4,"4#主泵");

        //5#主泵
        role_02(222,224,0,"5#主泵");

        //辅泵
        role_02(222,224,4,"辅泵");

        String code_hex_tmp0=responseText.substring(224,226);
        code_hex_tmp0=StrCastUtil.hexStrToBinaryStr(code_hex_tmp0);
        //无水故障
        String code_tmp0=code_hex_tmp0.substring(7);
        if("0".equals(code_tmp0)){
            System.out.println("无水故障"+"否");
        }else{
            System.out.println("无水故障"+"是");
        }

        //高水信号
        code_tmp0=code_hex_tmp0.substring(6,7);
        if("0".equals(code_tmp0)){
            System.out.println("高水信号"+"否");
        }else{
            System.out.println("高水信号"+"是");
        }

        //地面积水信号
        code_tmp0=code_hex_tmp0.substring(5,6);
        if("0".equals(code_tmp0)){
            System.out.println("地面积水信号"+"否");
        }else{
            System.out.println("地面积水信号"+"是");
        }

        //相序故障
        code_tmp0=code_hex_tmp0.substring(4,5);
        if("0".equals(code_tmp0)){
            System.out.println("相序故障"+"否");
        }else{
            System.out.println("相序故障"+"是");
        }

        //出口超压
        code_tmp0=code_hex_tmp0.substring(3,4);
        if("0".equals(code_tmp0)){
            System.out.println("出口超压"+"否");
        }else{
            System.out.println("出口超压"+"是");
        }

        String code_hex_tmp1=responseText.substring(226,228);
        code_hex_tmp1=StrCastUtil.hexStrToBinaryStr(code_hex_tmp1);
        //开停机远程控制切换
        String code_tmp1=code_hex_tmp1.substring(7);
        if("0".equals(code_tmp0)){
            System.out.println("开停机远程控制切换"+"本地");
        }else{
            System.out.println("开停机远程控制切换"+"远程");
        }

        //进水阀远程控制切换
        code_tmp1=code_hex_tmp1.substring(6,7);
        if("0".equals(code_tmp0)){
            System.out.println("进水阀远程控制切换"+"本地");
        }else{
            System.out.println("进水阀远程控制切换"+"远程");
        }

        //正常开门信号1
        code_tmp1=code_hex_tmp1.substring(5,6);
        if("0".equals(code_tmp0)){
            System.out.println("正常开门信号1"+"否");
        }else{
            System.out.println("正常开门信号1"+"是");
        }

        //非法入侵信号1
        code_tmp1=code_hex_tmp1.substring(4,5);
        if("0".equals(code_tmp0)){
            System.out.println("非法入侵信号1"+"否");
        }else{
            System.out.println("非法入侵信号1"+"是");
        }

        //正常开门信号2
        code_tmp1=code_hex_tmp1.substring(3,4);
        if("0".equals(code_tmp0)){
            System.out.println("正常开门信号2"+"否");
        }else{
            System.out.println("正常开门信号2"+"是");
        }

        //非法入侵信号2
        code_tmp1=code_hex_tmp1.substring(2,3);
        if("0".equals(code_tmp0)){
            System.out.println("非法入侵信号2"+"否");
        }else{
            System.out.println("非法入侵信号2"+"是");
        }

        String code_hex_tmp2=responseText.substring(228,230);
        code_hex_tmp2=StrCastUtil.hexStrToBinaryStr(code_hex_tmp2);
        //远程启停1#主泵
        String code_tmp2=code_hex_tmp2.substring(7);
        if("0".equals(code_tmp0)){
            System.out.println("远程启停1#主泵"+"停止");
        }else{
            System.out.println("远程启停1#主泵"+"启动");
        }

        //远程启停2#主泵
        code_tmp2=code_hex_tmp2.substring(6,7);
        if("0".equals(code_tmp0)){
            System.out.println("远程启停2#主泵"+"停止");
        }else{
            System.out.println("远程启停2#主泵"+"启动");
        }

        //远程启停3#主泵
        code_tmp2=code_hex_tmp2.substring(5,6);
        if("0".equals(code_tmp0)){
            System.out.println("远程启停3#主泵"+"停止");
        }else{
            System.out.println("远程启停3#主泵"+"启动");
        }

        //远程启停4#主泵
        code_tmp2=code_hex_tmp2.substring(4,5);
        if("0".equals(code_tmp0)){
            System.out.println("远程启停4#主泵"+"停止");
        }else{
            System.out.println("远程启停4#主泵"+"启动");
        }

        //远程启停5#主泵
        code_tmp2=code_hex_tmp2.substring(3,4);
        if("0".equals(code_tmp0)){
            System.out.println("远程启停5#主泵"+"停止");
        }else{
            System.out.println("远程启停5#主泵"+"启动");
        }

        //远程启停辅泵
        code_tmp2=code_hex_tmp2.substring(2,3);
        if("0".equals(code_tmp0)){
            System.out.println("远程启停辅泵"+"停止");
        }else{
            System.out.println("远程启停辅泵"+"启动");
        }

        //视频监控报警信号
        code_tmp2=code_hex_tmp2.substring(2,3);
        if("0".equals(code_tmp0)){
            System.out.println("视频监控报警信号"+"正常");
        }else{
            System.out.println("视频监控报警信号"+"报警");
        }

        //远程进水电动阀调节
        String code_hex_tmp3=responseText.substring(230,232);
        Integer number=Integer.parseInt(code_hex_tmp3,16);
        System.out.println("远程进水电动阀调节"+number);






    }


    /**
     * 一个字节，只读
     * @param start     开始坐标
     * @param end       结束坐标
     * @param floatNum  小数点几位
     * @param name      该字节表示的含义
     * @param unit      单位
     */
    private void role_01(int start,int end,int floatNum,String name,String unit){
        String code_hex=responseText.substring(start,end);
        Float value=Float.intBitsToFloat(Integer.valueOf(code_hex.trim(), 16));
        BigDecimal bd   =   new   BigDecimal(value);
        bd   =   bd.setScale(floatNum,BigDecimal.ROUND_HALF_UP);
        value   =   bd.floatValue();
        String explainText=value+unit;
        System.out.println(name+"的值为"+explainText);
    }

    /**
     * 半个字节，只读
     * @param start     开始坐标
     * @param end       结束坐标
     * @param index     从第几个位开始
     * @param name      表示的含义
     */
    private void role_02(int start,int end,int index,String name){
        String code_hex=responseText.substring(start,end);
        String code=StrCastUtil.hexStrToBinaryStr(code_hex);
        String t=code.substring(6-index,8-index);
        if("01".equals(t)){
            System.out.println(name+"变频运行："+"运行");
        }else{
            System.out.println(name+"变频运行："+"休息");
        }

        t=code.substring(5-index,6-index);
        if("0".equals(t)){
            System.out.println(name+"空开跳闸:"+"否");
        }else{
            System.out.println(name+"空开跳闸:"+"是");
        }

        t=code.substring(4-index,5-index);
        if("0".equals(t)){
            System.out.println(name+"故障信号:"+"正常");
        }else{
            System.out.println(name+"故障信号:"+"故障");
        }

    }




}
