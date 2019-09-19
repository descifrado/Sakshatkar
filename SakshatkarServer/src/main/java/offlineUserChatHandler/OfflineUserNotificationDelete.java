package offlineUserChatHandler;

import data.Notification;
import data.User;
import mainClasses.Main;

import java.sql.PreparedStatement;

public class OfflineUserNotificationDelete
{
    private User sender, reciever;
    public OfflineUserNotificationDelete(Notification notification)
    {
        this.sender=notification.getSender();
        this.reciever=notification.getReciever();
    }
    public void delete()
    {
        String query="Delete From Notification Where senderUID=? and recieverUID=?;";
        try
        {
            PreparedStatement preparedStatement= Main.connection.prepareStatement(query);
            preparedStatement.setString(1,sender.getUserUID());
            preparedStatement.setString(2,reciever.getUserUID());
            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
