package com.xj.iws.server.receive;

import com.xj.iws.common.enums.LogEnum;
import com.xj.iws.http.dao.LogDao;
import com.xj.iws.http.dao.redis.RedisBase;
import com.xj.iws.server.data.DataFormat;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2017/6/26.
 */
@Component
public class ServerHandler implements IoHandler {
    @Autowired
    RedisBase redisBase;
    @Autowired
    LogDao logDao;

    ServerMap serverMap = ServerMap.obtain();
    private Map<String, StringBuffer> tempMsgMap = new ConcurrentHashMap<>();

    private String getIP(IoSession ioSession) {
        String address = ioSession.getRemoteAddress().toString();
        String IP = address.substring(1, address.indexOf(":"));
        return IP;
    }

    /**
     * 对客户端发送过来的数据进行处理
     * @param IP
     * @param message
     */
    private void appendMsg(String IP, String message) {
        //得到此客户端的当前指令符
        Command command = serverMap.getRegCmd(IP);
        //获取校验长度
        String code = command.getCode();
        int checkLength = (Integer.parseInt(code.substring(8, 12), 16) * 2 + 5) * 2;
        StringBuffer tempMsg = tempMsgMap.get(IP);
        if (tempMsg == null) {
            try {
                if (!code.substring(0,4).equals(message.substring(0,4))) return;
            }catch (NullPointerException e){
                return;
            }
            tempMsg = new StringBuffer();
            tempMsgMap.put(IP, tempMsg);
        }
        tempMsg.append(message);
        if (tempMsg.length() >= checkLength) {
            serverMap.setMessage(IP, tempMsg.toString());
            tempMsgMap.remove(IP);
        }
    }

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        System.out.println(ioSession.getService().getManagedSessionCount());
        String IP = getIP(ioSession);
        serverMap.setSession(IP, ioSession);
        tempMsgMap.remove(IP);
        serverMap.removeMessage(IP);
        System.out.println(IP + "create");
        logDao.log(IP + ":" + LogEnum.CLIENT_CONNECTION.getLabel());
    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {
        String IP = getIP(ioSession);
        System.out.println(IP + "sessionOpen");

    }

    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {
        System.out.println(ioSession.getService().getManagedSessionCount());
        String IP = getIP(ioSession);
        System.out.println(IP + "sessionClose");
        serverMap.removeSession(IP);
    }

    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
        String IP = getIP(ioSession);
        System.out.println(IP + "空闲状态");
        ioSession.closeNow();

    }

    @Override
    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
//        String IP = getIP(ioSession);
//        System.out.println(IP + "exception");
        ioSession.closeNow();
//        serverMap.removeSession(IP);
    }

    /**
     * 收到客户端发送过来的消息
     * @param ioSession
     * @param o
     * @throws Exception
     */
    @Override
    public void messageReceived(IoSession ioSession, Object o) throws Exception {
        //获取客户端的Ip地址
        String IP = getIP(ioSession);
        appendMsg(IP,o.toString());
//        serverMap.setMessage(IP, o.toString());
    }

    @Override
    public void messageSent(IoSession ioSession, Object o) throws Exception {
//        String IP = getIP(ioSession);
//        System.out.println(IP+"send: "+o.toString());
    }

    @Override
    public void inputClosed(IoSession ioSession) throws Exception {
        String IP = getIP(ioSession);
        ioSession.closeNow();
//        serverMap.removeSession(IP);
        logDao.log(IP + ":" + LogEnum.CLIENT_BREAK.getLabel());
    }
}
