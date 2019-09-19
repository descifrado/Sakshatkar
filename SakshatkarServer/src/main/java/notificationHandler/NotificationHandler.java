package notificationHandler;

import constants.NotificationType;
import constants.ResponseCode;
import data.Notification;
import data.User;
import mainClasses.Main;
import request.GetNotificationRequest;
import request.Response;
import tools.UIDGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NotificationHandler {
    GetNotificationRequest getNotificationRequest;

    public NotificationHandler(GetNotificationRequest getNotificationRequest) {
        this.getNotificationRequest = getNotificationRequest;
    }

    public Response getResponse(){
        String recieverID = getNotificationRequest.getUser().getUserUID();
        String query1 = "SELECT * FROM User WHERE userUID = ?";
        String query2 = "SELECT N.senderUID,U.firstName,U.lastName,U.email,U.phone,U.Company," +
                "U.profilePicture,N.type FROM Notification AS N INNER JOIN User AS U ON N.senderUID = U.userUID WHERE N.recieverUID = ?;";

        try {
            PreparedStatement preparedStatement = Main.connection.prepareStatement(query1);
            preparedStatement.setString(1,recieverID);
            ResultSet rs = preparedStatement.executeQuery();
            User reciever;
            reciever = new User();
            if (rs.next()) {
                reciever.setUserUID(rs.getString(1));
                reciever.setFirstName(rs.getString(2));
                reciever.setLastName(rs.getString(3));
                reciever.setEmail(rs.getString(4));
                reciever.setPhone(rs.getString(5));
                reciever.setCompany(rs.getString(6));
                reciever.setPpURL(rs.getString(7));
            }
            preparedStatement = Main.connection.prepareStatement(query2);
            preparedStatement.setString(1,recieverID);
            rs = preparedStatement.executeQuery();
            ArrayList<Notification> notifications = new ArrayList<Notification>();


            while(rs.next()){
                User sender;
                String notificationType;
                sender = new User();
                sender.setUserUID(rs.getString(1));
                sender.setFirstName(rs.getString(2));
                sender.setLastName(rs.getString(3));
                sender.setEmail(rs.getString(4));
                sender.setPhone(rs.getString(5));
                sender.setCompany(rs.getString(6));
                sender.setPpURL(rs.getString(7));
                notificationType = rs.getString(8);
                notifications.add(new Notification(sender,reciever, NotificationType.valueOf(notificationType)));
            }
            return new Response(UIDGenerator.generateuid(),notifications,ResponseCode.SUCCESS);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return new Response(UIDGenerator.generateuid(),null, ResponseCode.FAILED);
    }
}
