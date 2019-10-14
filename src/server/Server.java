/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import DAO.SQLServerConn;
import static DAO.SQLServerConn.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author nguyenBaoHuy
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Login("a", "a", "a");
        }
        catch (SQLException ex){
            System.err.println("Cannot connect database, " + ex);
            
        }
    }
    
}
