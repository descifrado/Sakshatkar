package statusHandler;

import constants.Status;
import mainClasses.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class OnlineUserHandler {
    private String userUID;
    private String userIP;

    public OnlineUserHandler(String userId,String userIP){
        this.userUID=userId;
        this.userIP = userIP;
    }

    public void makeUserOffline(){
        String query="Delete from OnlineUser where userUID=?";
        String lastSeenQuery="Insert into LastSeen values (?,?)";
        try {
            PreparedStatement preparedStatement= Main.connection.prepareStatement(query);
            preparedStatement.setString(1,this.userUID);
            preparedStatement.executeUpdate();
            preparedStatement=Main.connection.prepareStatement(lastSeenQuery);
            preparedStatement.setString(1,this.userUID);
            preparedStatement.setString(2,(new Date().getTime())+"");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void makeUserOnline(){
        String query="INSERT into OnlineUser values (?,?,?)";
        String lastSeenQuery="Delete from LastSeen where userUID = ?";
        try {
            PreparedStatement preparedStatement= Main.connection.prepareStatement(query);
            preparedStatement.setString(1,this.userUID);
            preparedStatement.setString(2, String.valueOf(Status.USING_SAKSHATKAR));
            preparedStatement.setString(3,this.userIP);
            preparedStatement.executeUpdate();
            preparedStatement= Main.connection.prepareStatement(lastSeenQuery);
            preparedStatement.setString(1,this.userUID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
