/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    public static void CreateAccount (String userName, String password) throws SQLException{
        Connection conn = getSQLServerConnection();
        Statement stmt = conn.createStatement();
        String query = "INSERT INTO dbo.USER_ACCOUNT (User_Name, User_Password) VALUES ('" + userName + "', '" + password + "')";
        stmt.execute(query);
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
    
    
    
}