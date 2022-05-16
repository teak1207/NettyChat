package test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

import java.net.InetSocketAddress;

public class App {
    private static final String HOST = "0.0.0.0";
    private static final int PORT = 8080;  //서버 포트 번호
    private static final int READ_TIME_OUT = 60;  //읽기 타임아웃 설정(클라 -> 서버)
    private static final int WRITE_TIME_OUT = 30; //쓰기 타임아웃 설정(서버 -> 클라)

    public static void main(String[] args) {
        App app = new App();
        MyServerHandler handler = app.makeCallbackToDo();
        app.init(handler);
    }

    public MyServerHandler makeCallbackToDo() {
        return new MyServerHandler(new MyCallBack() {
            public void read(String read) {    //요청에 따른 처리

                System.out.println("요청들어온 메시지 입니다 : " + read);
            }

            public String write(String msg) {   //응답할 내용
                String result = "~~보낼메시지 입니다.~~\n";
                return result;
            }

            @Override
            public void afterClose(ChannelHandlerContext ctx) {  //커넥션 끊기면 할 내용
                System.out.println("커넥션이 끊기면 동작하는 메소드 입니다.");
            }
        });
    }

    public void init(final MyServerHandler handler) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //네티가 작동할 때 기본적으로 설정해야 하는 클래스
            //부트스트랩을 이용하므로서 네티 소켓의 모드나 스레드 등을 쉽게 설정할 수 있다.
            //네티의 부트스트랩은 크게 두 가지로 나뉘어진다. 하나는 ServerBootstrap이고 다른 하나는 Bootstrap이다.
            // ServerBootstrap은 서버 애플리케이션을 위한 부트스트랩이고 그냥 Bootstrap은 클라이언트를 위한 부트스트랩이다.
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 스레드 그룹 초기화  -> 이벤트 루프 설정
            serverBootstrap.group(group);
            // 채널 입출력 방식 설정

            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(new InetSocketAddress(HOST, PORT));
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("ReadTimeoutHandler", new ReadTimeoutHandler(READ_TIME_OUT));
                            socketChannel.pipeline().addLast("WriteTimeoutHandler", new WriteTimeoutHandler(WRITE_TIME_OUT));
                            socketChannel.pipeline().addLast("myHandler", handler);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)  //동시 접속 수
                    .childOption(ChannelOption.SO_KEEPALIVE, true);  //패킷여부
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
