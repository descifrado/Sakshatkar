package authenticationHandler;

import constants.ResponseCode;
import mainClasses.Main;
import request.Response;
import request.SignUpRequest;
import tools.UIDGenerator;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUp implements Serializable
{
    private String userUID, password;
    private String email, phone;
    private String fname, lname;

    public SignUp(SignUpRequest request) throws CloneNotSupportedException {
        this.fname=request.getUser().getFirstName();
        this.lname=request.getUser().getLastName();
        this.phone=request.getUser().getPhone();
        this.email=request.getUser().getEmail();
        this.password=request.getPassword();
        this.userUID=request.getUser().getUserUID();
    }

    public Response insert()
    {
        String q="INSERT INTO User VALUES (?,?,?,?,?);";
        String passwordQuery = "INSERT INTO Password VALUES (?,?)";
        try
        {
            PreparedStatement stmt= Main.connection.prepareStatement(q);
            stmt.setString(1,this.userUID);
            stmt.setString(2,this.fname);
            stmt.setString(3,this.lname);
            stmt.setString(4,this.email);
            stmt.setString(5,this.phone);
            stmt.executeUpdate();

            stmt = Main.connection.prepareStatement(passwordQuery);
            stmt.setString(1,this.userUID);
            stmt.setString(2,this.password);
            stmt.executeUpdate();

            return new Response((UIDGenerator.generateuid()),null, ResponseCode.SUCCESS);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return new Response(UIDGenerator.generateuid(),null, ResponseCode.FAILED);
    }
}
