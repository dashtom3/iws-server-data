package com.xj.iws.server.data;

import com.xj.iws.http.dao.DeviceDao;
import com.xj.iws.server.receive.Command;
import com.xj.iws.http.dao.redis.RedisBase;
import com.xj.iws.server.receive.ServerMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by XiaoJiang01 on 2017/4/13.
 */
@Component
public class DataReader {
    @Autowired
    RedisBase redisBase;
    @Autowired
    DeviceDao deviceDao;
    @Autowired
    DataCheck checker;
    @Autowired
    DataAlarm alarm;

    private Map<String, Future> readMap;

    private ServerMap serverMap = ServerMap.obtain();
    private ScheduledExecutorService executorService;

    public DataReader() {
        this.executorService = new ScheduledThreadPoolExecutor(1000);
        this.readMap = new HashMap<>();
    }

    public void read(String port) {
        Future future = readMap.get(port);
        if (future != null && !future.isCancelled()) return;
        List<Command> commands = serverMap.getCommand(port);
        List<DataCheck> checkers = check(port,commands);
        List<DataAlarm> alarms = alarm(port, commands);
        DataTask task = new DataTask(redisBase, deviceDao, port, commands, checkers, alarms);
        future = executorService.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        readMap.put(port, future);
    }

    public void close(String port) {
        readMap.get(port).cancel(true);
    }

    /**
     * 创建检查器
     *
     * @return
     */
    private List<DataCheck> check(String port, List<Command> commands) {
        List<DataCheck> checkers = new ArrayList<>();
        for (Command command : commands) {
            DataCheck checker = this.checker.create(port, command.getNumber());
            checkers.add(checker);
        }
        return checkers;
    }

    /**
     * 创建报警器
     *
     * @return
     */
    private List<DataAlarm> alarm(String port, List<Command> commands) {
        List<DataAlarm> alarms = new ArrayList<>();
        for (Command command : commands) {
            DataAlarm alarm = this.alarm.create(port, command.getNumber());
            alarms.add(alarm);
        }
        return alarms;
    }
}
