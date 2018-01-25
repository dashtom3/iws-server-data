package com.xj.iws.http.service.impl;

import com.xj.iws.common.util.DataWrapper;
import com.xj.iws.http.dao.ServerDao;
import com.xj.iws.http.service.ServerService;
import com.xj.iws.server.receive.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by XiaoJiang01 on 2017/4/28.
 */
@Service
public class ServerServiceImpl implements ServerService {
    @Autowired
    ServerDao serverDao;
    @Autowired
    Server server;

    @Override
    public DataWrapper<Void> startServer() {
        DataWrapper<Void> dataWrapper = new DataWrapper<>();
        String port = serverDao.getPort();
        server.init(Integer.valueOf(port));
        return dataWrapper;
    }

    @Override
    public DataWrapper<Void> closeServer() {
        DataWrapper<Void> dataWrapper = new DataWrapper<>();
//        server.close();
        return dataWrapper;
    }
}
