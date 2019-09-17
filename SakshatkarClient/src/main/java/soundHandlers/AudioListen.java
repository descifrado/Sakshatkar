package soundHandlers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.sound.sampled.*;

public class AudioListen implements Runnable {

    AudioInputStream audioInputStream;
    static AudioInputStream ais;
    static AudioFormat format;
    static boolean status = true;
    static int port = 50005;
    static int sampleRate = 44100;

    static DataLine.Info dataLineInfo;
    static SourceDataLine sourceDataLine;




    @Override
    public void run() {
        System.out.println("Audio Server started at port:"+port);

        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }


        byte[] receiveData = new byte[4096];

        format = new AudioFormat(sampleRate, 16, 2, true, false);
        dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        try {
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            sourceDataLine.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        sourceDataLine.start();



        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        ByteArrayInputStream baiss = new ByteArrayInputStream(receivePacket.getData());

        while (status == true)
        {
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ais = new AudioInputStream(baiss, format, receivePacket.getLength());
            toSpeaker(receivePacket.getData());
        }

        sourceDataLine.drain();
        sourceDataLine.close();
    }
    public static void toSpeaker(byte soundbytes[]) {
        try
        {

            sourceDataLine.write(soundbytes, 0, soundbytes.length);
        } catch (Exception e) {
            System.out.println("Not working in speakers...");
            e.printStackTrace();
        }
    }

}