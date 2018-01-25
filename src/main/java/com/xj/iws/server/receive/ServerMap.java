package com.xj.iws.server.receive;

import org.apache.mina.core.session.IoSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * Created by XiaoJiang01 on 2017/6/26.
 */
public class ServerMap {

    private static ServerMap serverMap;

    private Map<String, IoSession> ioSessionMap;
    private Map<String, List<Command>> commandMap;
    private Map<String, Command> regCmdMap;
    private Map<String, Future> taskMap;
    private Map<String, String> messageMap;

    private ServerMap() {
        ioSessionMap = new ConcurrentHashMap<>();
        commandMap = new ConcurrentHashMap<>();
        regCmdMap = new ConcurrentHashMap<>();
        taskMap = new ConcurrentHashMap<>();
        messageMap = new ConcurrentHashMap<>();
    }

    public static ServerMap obtain() {
        if (serverMap == null) {
            serverMap = new ServerMap();
        }
        return serverMap;
    }

    public IoSession getSession(String IP) {
        return ioSessionMap.get(IP);
    }

    public void setSession(String IP, IoSession session) {
        ioSessionMap.put(IP, session);
    }

    public void removeSession(String IP) {
        ioSessionMap.remove(IP);
    }

    public List<Command> getCommand(String IP) {
        return commandMap.get(IP);
    }

    public void setCommand(String IP, List<Command> command) {
        commandMap.put(IP, command);
    }

    public void removeCommand(String IP) {
        commandMap.remove(IP);
    }

    public Future getTask(String IP) {
        return taskMap.get(IP);
    }

    public void setTask(String IP, Future future) {
        taskMap.put(IP, future);
    }

    public void removeTask(String IP) {
        taskMap.remove(IP);
    }

    public String getMessage(String IP) {
        return messageMap.get(IP);
    }

    public void setMessage(String IP, String message) {
        messageMap.put(IP, message);
    }

    public void removeMessage(String IP) {
        messageMap.remove(IP);
    }

    public Command getRegCmd(String IP) {
        return regCmdMap.get(IP);
    }

    public void setRegCmd(String IP, Command command) {
        regCmdMap.put(IP, command);
    }

    public void removeRegCmd(String IP) {
        regCmdMap.remove(IP);
    }
}
