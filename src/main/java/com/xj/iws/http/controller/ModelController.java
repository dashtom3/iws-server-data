package com.xj.iws.http.controller;

import com.mysql.fabric.xmlrpc.base.Data;
import com.xj.iws.common.util.DataWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/1/9.
 */
@RestController
@RequestMapping("m")
public class ModelController {

    @RequestMapping("add")
    public DataWrapper<Void> add(){
        DataWrapper<Void> dataWrapper=new DataWrapper<Void>();
        return dataWrapper;
    }
}
