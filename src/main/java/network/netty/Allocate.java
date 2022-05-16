package network.netty;

import java.nio.ByteBuffer;

public class Allocate {

    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(11);

        // java.nio.HeapByteBuffer[pos=0 lim=11 cap=11]
        System.out.println(byteBuffer);


        // 11 Bytes Put
        byte[] source = "Hello world".getBytes();
        byteBuffer.put(source);

        // java.nio.HeapByteBuffer[pos=1 lim=11 cap=11]
        System.out.println(byteBuffer);
    }

}
