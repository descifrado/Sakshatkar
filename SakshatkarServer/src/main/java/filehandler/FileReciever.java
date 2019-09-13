package filehandler;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class FileReciever {


    public SocketChannel createSocketChannel(ServerSocketChannel serverSocketChannel){
        try {
            SocketChannel socketChannel = null;
            socketChannel = serverSocketChannel.accept();
            System.out.println("Connected With Client For File IO");
            return socketChannel;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void readFile(SocketChannel socketChannel,String fileName,String filePath){
        RandomAccessFile aFile = null;
        try {
            String cwd = System.getProperty("user.dir");
            File file = new File(filePath + System.getProperty("file.separator") + fileName);
            file.createNewFile();
            aFile = new RandomAccessFile(filePath+ System.getProperty("file.separator") +fileName,"rw");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            FileChannel fileChannel = aFile.getChannel();
            while (socketChannel.read(buffer)> 0) {
                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();
            }
            Thread.sleep(1000);
            fileChannel.close();
            System.out.println("End of file reached..Closing channel");
            socketChannel.close();
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
