/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import static DAO.SQLServerConn.*;
import commom.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nguyenBaoHuy
 */
public class EchoThread extends Thread{
    protected Socket conn = null;
    private TagReader reader;
    private TagWriter writer;
    private OutputStream myOS;
    
    public EchoThread(Socket clientSocket){
        conn = clientSocket;
    }
    public void run(){
        // Xu ly yeu cau cua client
        try{
            reader = new TagReader(conn.getInputStream());
            TagValue tv = reader.getTagValue();
            
            try {
                // Login
                // Logout
                // create acount
                // search
                // Chat
                // accept friend request
                // send friend request
                // get request add friend

                boolean a = Login(string, "1", "1.1.1.1");
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.writeUTF("" + a);
    
            } catch (SQLException ex) {
                Logger.getLogger(EchoThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (IOException ex) {
            System.err.println("Network error");
        }
    }
}
