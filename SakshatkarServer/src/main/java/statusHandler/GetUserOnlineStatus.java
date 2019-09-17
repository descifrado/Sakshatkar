package statusHandler;

import constants.ResponseCode;
import data.User;
import mainClasses.Main;
import request.Response;
import tools.UIDGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GetUserOnlineStatus {

    public static ResponseCode getStatus(String userUID) {
        String query = "SELECT OnlineUser.userUID FROM OnlineUser WHERE OnlineUser.userUID = \"" + userUID + "\"";
        System.out.println(query);
        try {
            PreparedStatement preparedStatement = Main.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResponseCode.SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseCode.FAILED;
    }
}
