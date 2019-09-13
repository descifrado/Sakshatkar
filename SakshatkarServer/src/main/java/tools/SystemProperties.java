package tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public class SystemProperties {

    /**
     *
     */
    public static Properties properties;

    /**
     *
     * @throws IOException
     */
    public static void getPropValues() throws IOException {
        FileReader fileReader;
        properties=new Properties();
        String propFileName = "config.properties";
        fileReader=new FileReader(propFileName);
        if (fileReader!=null){
            try {
                properties.load(fileReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            throw new FileNotFoundException("File Not Found");
    }

    /**
     *
     * @return
     */
    public static String getMySQLPassword(){
        try {
            getPropValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String password= properties.getProperty("MySQLPassword");
        return password;
    }

    /**
     *
     * @return
     */
    public static String getMySQLUserName(){
        try {
            getPropValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String user= properties.getProperty("MySQLUserName");
        return user;
    }

    /**
     *
     * @return
     */
    public static String getMySQLHostName(){
        try {
            getPropValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String user= properties.getProperty("MySQLHostName");
        return user;
    }
}
