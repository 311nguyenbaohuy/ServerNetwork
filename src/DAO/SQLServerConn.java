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
        String query = "UPDATE dbo.USER_ACCOUNT SET IP_addr = ?, Status = 1 "
                + "WHERE User_Name = ? AND User_Password = ?";
        CallableStatement cstmt = conn.prepareCall(query);
        cstmt.setString(1, IP_addr);
        cstmt.setString(2, userName);
        cstmt.setString(3, password);

        return cstmt.executeUpdate() != 0;
  }
  
    // Logout
    public static void Logout(String userName) throws SQLException{
        Connection conn = getSQLServerConnection();
        String query = "UPDATE dbo.USER_ACCOUNT SET Status = 0 WHERE User_Name = ? ";
        CallableStatement cstmt = conn.prepareCall(query);
        cstmt.setString(1, userName);
        cstmt.execute();
    }

    // CREATE ACCOUNT, INSERT TO DB
    public static boolean CreateAccount (String userName, String password) throws SQLException{
        Connection conn = getSQLServerConnection();
        String query = "INSERT INTO dbo.USER_ACCOUNT (User_Name, User_Password) VALUES (?, ?)";
        CallableStatement cstmt = conn.prepareCall(query);
        cstmt.setString(1, userName);
        cstmt.setString(2, password);
        try {
            cstmt.execute();
            return true;
        } catch (Exception e) {
            return false;
        }
  }
  
    
    public static User getUserByName(String userName) throws SQLException {
        Connection conn = getSQLServerConnection();
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM dbo.USER_ACCOUNT WHERE User_Name = ?";
        CallableStatement cstmt = conn.prepareCall(query);
        cstmt.setString(1, userName);
        ResultSet rs = cstmt.executeQuery();
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
        User user = getUserByName(userName);
        User friend = getUserByName(friendName);
        
        int userID = user.getID();
        int friendID = friend.getID();
        
        String query = "INSERT INTO dbo.FRIEND_REQUEST (User_Id, Friend_Id) VALUES (?, ?)";
        CallableStatement cstmt = conn.prepareCall(query);
        cstmt.setInt(1, userID);
        cstmt.setInt(2, friendID);
        try {
            cstmt.execute();
            return true;
        } catch (Exception e) {
            return false;
        }
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
  
        int userID = getUserByName(userName).getID();
        int friendID = getUserByName(friendName).getID();
        
        String query = "DELETE FROM dbo.FRIEND_REQUEST WHERE USER_ID = ? AND Friend_Id = ?";
        CallableStatement cstmt = conn.prepareCall(query);
        cstmt.setString(1, Integer.toString(userID));
        cstmt.setString(2, Integer.toString(friendID));
        cstmt.execute();
    }
    // REMOVE request add friend.
    
    public static boolean createGroup(String groupName, String userName) throws SQLException {
        Connection conn = getSQLServerConnection();      
       
        String query = "INSERT INTO dbo.GROUP_CHAT VALUES (?, ?)";
        CallableStatement cstmt = conn.prepareCall(query);
        cstmt.setString(1, groupName);
        cstmt.setString(2, userName);
        try {
            cstmt.execute();
            return true;
        } catch (Exception e) {
            return false;   
        }
    }
    
    public static boolean leaveGroup(String groupName, String userName) throws SQLException {
        Connection conn = getSQLServerConnection();      
       
        String query = "DELETE FROM dbo.GROUP_CHAT WHERE group_name = ? AND USER_NAME = ?";
        CallableStatement cstmt = conn.prepareCall(query);
        cstmt.setString(1, groupName);
        cstmt.setString(2, userName);
        try {
            cstmt.execute();
            return true;
        } catch (Exception e) {
            return false;   
        }
    }
    
    public static List<User> getMember(String groupName) throws SQLException{
        Connection conn = getSQLServerConnection();      
       
        String query = "SELECT User_Id, GROUP_CHAT.user_name, User_Password, IP_addr, Status FROM dbo.USER_ACCOUNT, dbo.GROUP_CHAT WHERE GROUP_CHAT.user_name = USER_ACCOUNT.User_Name AND group_name = ?";
        CallableStatement cstmt = conn.prepareCall(query);
        cstmt.setString(1, groupName);
        ResultSet rs = cstmt.executeQuery();
        List<User> listUsers = new ArrayList<>();
        while (rs.next()){
            User usr = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            listUsers.add(usr);
        }
        return listUsers;
    }
    
    public static List<String> getMyGroup(String userName) throws SQLException{
        Connection conn = getSQLServerConnection();      
       
        String query = "SELECT group_name FROM dbo.GROUP_CHAT WHERE USER_NAME = ?";
        CallableStatement cstmt = conn.prepareCall(query);
        cstmt.setString(1, userName);
        ResultSet rs = cstmt.executeQuery();
        List<String> list = new ArrayList<String>();
        while (rs.next()){
            list.add(rs.getString(1));
        }
        return list;
    }
}
