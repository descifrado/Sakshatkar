package tools;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

public class UIDGenerator {
    public static String generateuid() {
        Enumeration mnet=null, ip;
        String uid = "";
        try {
            mnet = NetworkInterface.getNetworkInterfaces();

        }catch(SocketException e){
            e.printStackTrace();
        }
        try{
            while(mnet.hasMoreElements()){
                NetworkInterface networkInterface = (NetworkInterface)mnet.nextElement();
//                ip = networkInterface.getInetAddresses();
                byte mac[] = networkInterface.getHardwareAddress();
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<mac.length;i++){
                    sb.append(String.format("02X%s",mac[i],(i<mac.length-1)?"-" : ""));

                }
                uid = UUID.nameUUIDFromBytes((sb.toString()+System.currentTimeMillis()+"").getBytes())+"";
                return uid;
            }
        }catch (SocketException e){
            e.printStackTrace();
        }
        return  uid;
    }
    public static String generateuid(String string){
        String temp="";//UIDGenerator.generateuid();
        temp=temp+string;
        return ""+UUID.nameUUIDFromBytes(temp.getBytes());
    }
}