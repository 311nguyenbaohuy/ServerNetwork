/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


/**
 *
 * @author nguyenBaoHuy
 */
public class SQLServerConn {
   
    public static Connection getSQLServerConnection() throws SQLException {
        String hostName = "localhost";
        String databaseName = "CHATDB";
        String userName = "sa";
        String password = "sa";
        
        return getSQLSConnection(hostName, databaseName, userName, password);
    }
    
    public static Connection getSQLSConnection(String hostName, String databaseName,
                                             String userName, String password) throws SQLException
  {
      String dbURL = "jdbc:sqlserver://" + hostName + ";databaseName=" + databaseName;
      Connection conn = DriverManager.getConnection(dbURL, userName, password);
      return conn;
  }
  
    // CHECK LOGIN, ENABLE ONLINE AND UPDATE IP_ADDR
    public static boolean Login(String userName, String password, String IP_addr) throws SQLException{
        Connection conn = getSQLServerConnection();
        
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM dbo.USER_ACCOUNT WHERE User_Name = '" + userName + "' AND User_Password = " + password;
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()){
            query = "UPDATE dbo.USER_ACCOUNT SET "
                    + "IP_addr = '" + IP_addr + "', Status = 1 " + "WHERE User_Name = '" + userName + "'";
            stmt.execute(query);
        return true;
        }
        return false;
  }
  
    // Logout
    public static void Logout(String userName) throws SQLException{
      Connection conn = getSQLServerConnection();
      Statement stmt = conn.createStatement();
      String query = "UPDATE dbo.USER_ACCOUNT SET Status = 0 " + "WHERE User_Name = '" + userName + "'";
      stmt.execute(query);
    }

    // CREATE ACCOUNT, INSERT TO DB
    public static boolean CreateAccount (String userName, String password) throws SQLException{
        Connection conn = getSQLServerConnection();
        Statement stmt = conn.createStatement();
        String query = "INSERT INTO dbo.USER_ACCOUNT (User_Name, User_Password) VALUES ('" + userName + "', '" + password + "')";
        try {
            stmt.execute(query);
            return true;
        } catch (Exception e) {
            return false;
        }
  }
  
    
    public static User getUserByName(String userName) throws SQLException {
        Connection conn = getSQLServerConnection();
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM dbo.USER_ACCOUNT WHERE User_Name = '" + userName +"'";
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()){
            return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
        }
        else{
            return new User();
        }
    }
    
    // If insert successful return true else return fasle
    public static boolean createFriendRequest(String userName, String friendName) throws SQLException{
        Connection conn = getSQLServerConnection();
        Statement stmt = conn.createStatement();
        User user = getUserByName(userName);
        User friend = getUserByName(friendName);
        
        int userID = user.getID();
        int friendID = friend.getID();

        String query = "INSERT INTO dbo.FRIEND_REQUEST (User_Id, Friend_Id) VALUES (" + userID + "," + friendID + ")";
        
        return stmt.execute(query);
    }
    
    // Insert to FRIEND and Delete FriendRequest
    public static int acceptRequest(String userName, String friendName) throws SQLException{
        Connection conn = getSQLServerConnection();
        int userID = getUserByName(userName).getID();
        int friendID = getUserByName(friendName).getID();
        
        String runSP = "{? = call dbo.p_add_list_friend (?, ?)}";
        CallableStatement cstmt = conn.prepareCall(runSP);
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.setInt(2, userID);
        cstmt.setInt(3, friendID);
        cstmt.execute();
        return cstmt.getInt(1);
        // return 0 for success else fail.
    }
    
    // Find list Friend
    public static List<User> getListFriend(String userName) throws SQLException{
        Connection conn = getSQLServerConnection();
        Statement stmt = conn.createStatement();
        User user = getUserByName(userName);
        int userID = user.getID();
        String query = "SELECT USER_ACCOUNT.User_Id, User_Name, User_Password, IP_addr, Status FROM dbo.USER_ACCOUNT, "
                + "(SELECT * FROM dbo.FRIEND WHERE User_Id = " + userID
                + ")temp WHERE USER_ACCOUNT.User_Id = temp.Friend_Id";
        ResultSet rs = stmt.executeQuery(query);
        List<User> lstFriend = new ArrayList<>();
        while(rs.next()){
            User usr = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            lstFriend.add(usr);
        }
        return lstFriend;
    }
    
    public static List<User> getRequest(String userName) throws SQLException{
        Connection conn = getSQLServerConnection();
        Statement stmt = conn.createStatement();
        User user = getUserByName(userName);
        int userID = user.getID();
        String query = "SELECT USER_ACCOUNT.User_Id, User_Name, User_Password, IP_addr, Status \n" +
                       "FROM dbo.USER_ACCOUNT, dbo.FRIEND_REQUEST \n" +
                        "WHERE FRIEND_REQUEST.User_Id = USER_ACCOUNT.User_Id  " +
                        "AND Friend_Id = " + userID;
        ResultSet rs = stmt.executeQuery(query);
        List<User> lstFriend = new ArrayList<>();
        while(rs.next()){
            User usr = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            lstFriend.add(usr);
        }
        return lstFriend;
    }
    
    public static void deleteRequest(String userName, String friendName) throws SQLException{
        Connection conn = getSQLServerConnection();
        Statement stmt = conn.createStatement();
        int userID = getUserByName(userName).getID();
        int friendID = getUserByName(friendName).getID();
        
        String query = "DELETE FROM dbo.FRIEND_REQUEST WHERE USER_ID = " + userID + " AND Friend_Id = " + friendID;
        stmt.executeQuery(query); 
    }
    
    // REMOVE request add friend.
}