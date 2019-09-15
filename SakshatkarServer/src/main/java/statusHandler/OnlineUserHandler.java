package statusHandler;

import constants.Status;
import mainClasses.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OnlineUserHandler {
    private String userUID;

    public OnlineUserHandler(String userId){
        this.userUID=userId;
    }

    public void makeUserOffline(){
        String query="Delete from OnlineUser where userUID=?";
        try {
            PreparedStatement preparedStatement= Main.connection.prepareStatement(query);
            preparedStatement.setString(1,this.userUID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void makeUserOnline(){
        String query="INSERT into OnlineUser values (?,?)";
        try {
            PreparedStatement preparedStatement= Main.connection.prepareStatement(query);
            preparedStatement.setString(1,this.userUID);
            preparedStatement.setString(2, String.valueOf(Status.USING_SAKSHATKAR));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
