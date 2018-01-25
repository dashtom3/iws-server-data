package com.xj.iws.common.mina;

import com.xj.iws.common.variable.GlobalVariables;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2017/12/27.
 */
public class MinaClient {
    private static final int bindPort=102;
    public static void main(String[] args) throws InterruptedException {
        // 创建一个socket连接
        NioSocketConnector connector=new NioSocketConnector();
        // 获取过滤器链
        DefaultIoFilterChainBuilder chain=connector.getFilterChain();
        ProtocolCodecFilter filter= new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8")));
        // 添加编码过滤器 处理乱码、编码问题
        chain.addLast("objectFilter",filter);
        // 消息核心处理器
        connector.setHandler(new MinaClientHanlder());
        // 设置链接超时时间
        connector.setConnectTimeoutCheckInterval(300);
        // 连接服务器，知道端口、地址
        ConnectFuture cf = connector.connect(new InetSocketAddress("192.168.88.2",bindPort));
        // 等待连接创建完成
        cf.awaitUninterruptibly();
        IoSession session=cf.getSession();
        session.write(GlobalVariables.CMD_REQUEST);
        Thread.sleep(5000);
        session.write(GlobalVariables.CMD_SETUP);

    }
}
