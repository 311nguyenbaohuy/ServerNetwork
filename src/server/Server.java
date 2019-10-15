/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import DAO.SQLServerConn;
import static DAO.SQLServerConn.*;
import DAO.User;
import commom.TagReader;
import commom.TagValue;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.net.ServerSocketFactory;

/**
 *
 * @author nguyenBaoHuy
 */
public class Server {
    /**
     * @param args the command line arguments
     */
    static ServerSocket server = null;
    static Socket conn = null;
    private int PORT = 9000;
    
    public Server() throws UnknownHostException, IOException{
        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                conn =  server.accept();
            } catch (IOException e) {
                System.err.println("I/O error: " + e);
            }
            
            new EchoThread(conn).start();
        }
    }
    
    
    public static void main(String[] args) throws IOException {
        TagReader tr = new TagReader(System.in);
        TagValue tv = tr.getTagValue();
        byte[] a = tv.getContent();
        String s = new String(a);
        System.out.println(s);
 	
    }
    
}
