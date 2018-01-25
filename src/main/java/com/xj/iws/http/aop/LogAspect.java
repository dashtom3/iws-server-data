package com.xj.iws.http.aop;

import com.xj.iws.common.enums.LogEnum;
import com.xj.iws.http.dao.LogDao;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/6/23.
 */
@Aspect
@Component
public class LogAspect {
    @Autowired
    LogDao logDao;

    @After(value = "execution(public * com.xj.iws.server.receive.Server.init(..))")
    public void startServer() {
        logDao.log(LogEnum.SERVER_START.getLabel());
        System.out.println("start");
    }

    @After(value = "execution(public * com.xj.iws.server.receive.Server.runClient(..))")
    public void startClient(JoinPoint joinPoint) {
        Object[] params = joinPoint.getArgs();
        String IP = (String) params[0];
        logDao.log(IP + ":" + LogEnum.READER_START.getLabel());
    }

    @After(value = "execution(public * com.xj.iws.server.receive.Server.closePort(..))")
    public void closeClient(JoinPoint joinPoint) {
        Object[] params = joinPoint.getArgs();
        String IP = (String) params[0];
        logDao.log(IP + ":" + LogEnum.READER_CLOSE.getLabel());
    }

    @After(value = "execution(public * com.xj.iws.http.service.DataService.saveAll())")
    public void saveData(){
        logDao.log(LogEnum.DATA_SAVE.getLabel());
    }
}
