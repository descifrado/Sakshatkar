package offlineUserChatHandler;

import constants.NotificationType;
import constants.ResponseCode;
import mainClasses.Main;
import request.Response;
import tools.UIDGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OfflineUserChatHandler
{
    private String notifUID, senderUID, recieverUID;
    public OfflineUserChatHandler(String senderUID, String recieverUID)
    {
        this.senderUID=senderUID;
        this.recieverUID=recieverUID;
        this.notifUID= UIDGenerator.generateuid(senderUID+recieverUID+ NotificationType.MESSAGE.toString());
    }

    public void insert()
    {
        String query="SELECT * From Notification Where notificationUID=? and senderUID=? and recieverUID=?;";
        try {
            PreparedStatement preparedStatement= Main.connection.prepareStatement(query);
            preparedStatement.setString(1,notifUID);
            preparedStatement.setString(2,senderUID);
            preparedStatement.setString(3,recieverUID);
            ResultSet rs=preparedStatement.executeQuery();
            if(rs.next())
            {
                return;
            }
            else {
                String q="Insert Into Notification Values(?,?,?,?);";
                PreparedStatement preparedStatement1=Main.connection.prepareStatement(q);
                preparedStatement1.setString(1,notifUID);
                preparedStatement1.setString(2,senderUID);
                preparedStatement1.setString(3,recieverUID);
                preparedStatement1.setString(4,NotificationType.MESSAGE.toString());
                preparedStatement1.executeUpdate();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}
