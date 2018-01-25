package com.xj.iws.server.receive;

import com.xj.iws.http.dao.redis.RedisBase;
import com.xj.iws.server.data.DataFormat;
import org.apache.log4j.net.SyslogAppender;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class Task implements Runnable {
    ServerMap serverMap = ServerMap.obtain();

    String IP;
    List<Command> commands;
    RedisBase redisBase;

    public Task() {
    }

    public Task(String IP, List<Command> commands, RedisBase redisBase) {
        this.IP = IP;
        this.commands = commands;
        this.redisBase = redisBase;
    }

    @Override
    public void run() {
        IoSession client = serverMap.getSession(IP);
        if (client == null || client.isClosing() || !client.isConnected()) return;
        try {
            for (int i = 0; i < commands.size(); i++) {
                Command command = commands.get(i);
                String code = command.getCode();
                send(client, code);
                serverMap.setRegCmd(IP, command);
                String temp = read(IP);
                if (temp != null) {
                    String data = form(command, temp);
                    redisBase.valueOps().set("temp_" + IP + "#" + command.getNumber(), data);
//                    System.out.println(IP + "#" + command.getNumber() + " : " + data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(IP + " Running Exception");
            client.closeNow();
            return;
        }
    }

    public String search(String IP, Command command) {
        IoSession client = serverMap.getSession(IP);
        if (client == null || client.isClosing() || !client.isConnected()) return null;
        String code = command.getCode();
        send(client, code);
        serverMap.setRegCmd(IP, command);
        String data = read(IP);
//        System.out.println(IP + "#" + command.getNumber() + " : " + data);
        return data;
    }

    private void send(IoSession session, String code) {
        String checkCode = CRC16.checkCode(code);
        session.write(code + checkCode);

    }

    private String read(String IP) {
        String message;
        String data = null;
        long a = System.currentTimeMillis();
        while (true) {
            message = serverMap.getMessage(IP);
            if (System.currentTimeMillis() - a > 3000) {
                IoSession ioSession = serverMap.getSession(IP);
                if (ioSession == null) break;
                ioSession.closeNow();
                serverMap.removeSession(IP);
                break;
            }
            if (message == null) {
                continue;
            } else if (CRC16.checkout(message)) {
                serverMap.removeMessage(IP);
                data = message;
                break;
            } else if (message != null) {
                serverMap.removeMessage(IP);
                break;
            }
        }
        return data;
    }

    private String form(Command command, String message) {
        if (!CRC16.matchCode(message, command.getCode())) return null;
        //
        //数据截掉前6位属性位 和后4位校验位
        //type == 1 表示单片机数据(4位1数), 其他类型与单片机相同
        //type == 2 表示电仪表数据(8位1数), 需将前后两数倒置
        //
        String temp = message.substring(6, message.length() - 4);
        String data;
        switch (command.getType()) {
            case 1:
                data = temp;
                break;
            case 2:
                String[] arrayData = DataFormat.subData(temp, 8);
                List<Integer> target = command.getTarget();
                StringBuffer strBuf = new StringBuffer();
                for (int i : target) {
                    String front = arrayData[i].substring(4, 8);
                    String back = arrayData[i].substring(0, 4);
                    strBuf.append(front + back);
                }
                data = strBuf.toString();
                break;
            default:
                data = temp;
                break;
        }
        return data;
    }
}
