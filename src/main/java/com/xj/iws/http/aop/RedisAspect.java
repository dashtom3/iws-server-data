package com.xj.iws.http.aop;

import com.xj.iws.http.dao.redis.RedisBase;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/7/10.
 *
 */
@Aspect
@Component
public class RedisAspect {
    @Autowired
    RedisBase redisBase;

    @AfterThrowing(value = "execution(* com.xj.iws.http.dao.redis.RedisBase.*(..))")
    public void emConn(){

    }
}
