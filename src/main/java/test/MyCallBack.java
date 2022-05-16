package test;

import io.netty.channel.ChannelHandlerContext;

public interface MyCallBack {

    public void read(String read);  // 데이터를 수신하면 동작시키는 메소드

    public String write(String msg); //사용자가 데이터를 조립할 메소드 (msg는 받은 메세지)

    public void afterClose(ChannelHandlerContext ctx); // 종료 시 동작 시킬 메소드

}
