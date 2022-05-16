package network.netty;

import java.nio.ByteBuffer;

public class AllocateDirect {

    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(11);

        System.out.println(byteBuffer);

        System.out.println(byteBuffer.isDirect());
    }
}
