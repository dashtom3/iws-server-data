package com.xj.iws.common.mina;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;


/**
 * Created by Administrator on 2017/12/27.
 */
public class MinaClientHanlder extends IoHandlerAdapter{
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("连接打开了");
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        System.out.println("收到服务器发来的数据为"+message);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        session.closeNow();
        System.out.println("连接被关闭了");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        System.out.println("捕获到异常"+cause.getMessage());
        session.closeNow();
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }




}
