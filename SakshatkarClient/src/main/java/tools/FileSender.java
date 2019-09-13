package tools;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class FileSender {
    public SocketChannel createSocketChannel(String serverIP){
        try{
            SocketChannel socketChannel = SocketChannel.open();
            SocketAddress socketAddress = new InetSocketAddress(serverIP,9000);
            socketChannel.connect(socketAddress);
            return socketChannel;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void sendFile(SocketChannel socketChannel,String path){
        try {
            RandomAccessFile aFile = null;
            File file = new File(path);
            System.out.println(file.exists());
            aFile = new RandomAccessFile(file, "r");
            FileChannel inChannel = aFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();
            }
            socketChannel.close();
            aFile.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
