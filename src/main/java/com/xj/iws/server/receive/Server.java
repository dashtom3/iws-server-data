package com.xj.iws.server.receive;

import com.xj.iws.http.dao.redis.RedisBase;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/6/26.
 */
@Component
public class Server {
    @Autowired
    ServerHandler serverHandler;
    @Autowired
    RedisBase redisBase;

    ServerMap serverMap = ServerMap.obtain();

    private ScheduledExecutorService sendExecutor = Executors.newScheduledThreadPool(100);

    public void init(int port) {
        IoAcceptor acceptor;
        try {
            // 创建一个非阻塞的server端的Socket
            acceptor = new NioSocketAcceptor();
            // 设置过滤器（使用mina提供的文本换行符编解码器）
            acceptor.getFilterChain().addLast("codec",
                    new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"),
                            LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue())));
            acceptor.getFilterChain().addLast("executor",new ExecutorFilter());
            // 自定义的编解码器
            // acceptor.getFilterChain().addLast("codec", new
            // ProtocolCodecFilter(new CharsetCodecFactory()));
            // 设置读取数据的换冲区大小
            acceptor.getSessionConfig().setReadBufferSize(2048);
            // 读写通道10秒内无操作进入空闲状态
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
            // 为接收器设置管理服务
            acceptor.setHandler(serverHandler);
            // 绑定端口
            acceptor.bind(new InetSocketAddress(port));
            System.out.println("服务器启动成功... " + "\n\r" + "端口号：" + port);
        } catch (Exception e) {
            System.out.println("服务器启动异常!!");
            e.printStackTrace();
        }
    }

    public void runClient(String IP, List<Command> commands) {
        Future future = serverMap.getTask(IP);
        if (future != null && !future.isCancelled()) return;
        ScheduledFuture scheduledFuture = sendExecutor.scheduleWithFixedDelay(new Task(IP, commands, redisBase), 0, 200, TimeUnit.MILLISECONDS);
        serverMap.setTask(IP, scheduledFuture);
    }

    public String manual(String IP, Command command) {
        return new Task().search(IP, command);
    }

    public void closePort(String port) {
        Future future = serverMap.getTask(port);
        future.cancel(true);
        serverMap.removeTask(port);
        serverMap.removeCommand(port);
    }

    public void closeSession(String IP){
        IoSession ioSession = serverMap.getSession(IP);
        ioSession.closeNow();
    }
}
