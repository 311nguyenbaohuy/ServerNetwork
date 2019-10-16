/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import static DAO.SQLServerConn.*;
import DAO.User;
import commom.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.List;
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

    private int FREE_PORT = 9001;
    
    public EchoThread(Socket clientSocket){
        conn = clientSocket;
    }
    
    private int getFreePort(){
        return FREE_PORT++;
    }
    
    public void run(){
        // Xu ly yeu cau cua client
        try{
            reader = new TagReader(conn.getInputStream());  
            writer = new TagWriter(conn.getOutputStream());
            
            TagValue tv = reader.getTagValue();
            String content = new String(tv.getContent());
//            System.out.println(content);
            content = content.replace("<", "");
            content = content.replace(">", "");
            
            String[] arrContent;
            boolean isOK;
            
            switch (tv.getTag()){
                
                // LOGIN
                case Tags.LOGIN:
                    arrContent = content.split(" ");
                    isOK = Login(arrContent[0], arrContent[1], arrContent[2]);
                    if (isOK){
                        System.out.println("OK");
                        writer.writeTag(new TagValue(Tags.SUCCESS, "<>".getBytes()));
                        writer.flush();
                    }
                    else{
                        // Send back to client
                        writer.writeTag(new TagValue(Tags.FAIL, "<>".getBytes()));
                        writer.flush();
                    }
                    break;
                    
                // REGISTER
                case Tags.REGISTER:
                    arrContent = content.split(" ");
                    isOK = CreateAccount(arrContent[0], arrContent[1]);
                    if (isOK){
                        System.out.println("OK");
                        writer.writeTag(new TagValue(Tags.SUCCESS, "<>".getBytes()));
                        writer.flush();
                    }
                    else{
                        // Send back to client
                        writer.writeTag(new TagValue(Tags.FAIL, "<>".getBytes()));
                        writer.flush();
                    }
                    break;
                    
                // LOGOUT
                case Tags.LOGOUT:
                    Logout(content);
                    writer.writeTag(new TagValue(Tags.SUCCESS, "<>".getBytes()));
                    writer.flush();
                    break;
                
                // SEARCH
                case Tags.SEARCH:
                    User usr = getUserByName(content);
                    if (usr.getID() == 0){
                        // Can't find user
                        writer.writeTag(new TagValue(Tags.FAIL, "<>".getBytes()));
                        writer.flush();
                    }
                    else{
                        String usrFound = "<" + usr.getID() + " " + usr.getUser_name() + " " + 
                                                usr.getIP_addr() + " " + usr.getStatus() + ">";
                        writer.writeTag(new TagValue(Tags.SUCCESS, usrFound.getBytes()));
                        writer.flush();
                    }
                    break;
                
                // ACCEPT FRIEND REQUEST
                case Tags.ACCEPT:
                    arrContent = content.split(" ");
                    acceptRequest(arrContent[0], arrContent[1]);
                    writer.writeTag(new TagValue(Tags.SUCCESS, "<>".getBytes()));
                    writer.flush();
                    break;
                
                // SEND ADD FRIEND REQUEST
                case Tags.REQUEST:
                    arrContent = content.split(" ");
                    isOK = createFriendRequest(arrContent[0], arrContent[1]);
                    if (isOK){
                        writer.writeTag(new TagValue(Tags.SUCCESS, "<>".getBytes()));
                        writer.flush();
                    }
                    else{
                        writer.writeTag(new TagValue(Tags.FAIL, "<>".getBytes()));
                        writer.flush(); 
                    }
                    break;
                
                // FIND LIST FRIEND
                case Tags.FIND_FRIEND:
                    List<User> users = getListFriend(content);
                    if (users.isEmpty()){
                        writer.writeTag(new TagValue(Tags.FAIL, "<>".getBytes()));
                        writer.flush();  
                    }
                    else{
                        String friends = "<";
                        for (User user : users) {
                            friends = friends + user.getID() + " " + user.getUser_name() + " " + 
                                                user.getIP_addr() + " " + user.getStatus() + "|";
                        }
                        friends = friends.substring(0, friends.length() - 1) + ">";
                        System.out.println(friends);
                        writer.writeTag(new TagValue(Tags.SUCCESS, friends.getBytes()));
                        writer.flush();    
                    }
                    break;
                    
                case Tags.FIND_REQUEST:
                    List<User> Users = getRequest(content);
                    if (Users.isEmpty()){
                        writer.writeTag(new TagValue(Tags.FAIL, "<>".getBytes()));
                        writer.flush();  
                    }
                    else{
                        String friends = "<";
                        for (User user : Users) {
                            friends = friends + user.getID() + " " + user.getUser_name() + " " + 
                                                user.getIP_addr() + " " + user.getStatus() + "|";
                        }
                        friends = friends.substring(0, friends.length() - 1) + ">";
                        writer.writeTag(new TagValue(Tags.SUCCESS, friends.getBytes()));
                        writer.flush();    
                    }
                    break;
                
                case Tags.DELETE:
                    arrContent = content.split(" ");
                    deleteRequest(arrContent[0], arrContent[1]);
                    writer.writeTag(new TagValue(Tags.SUCCESS, "<>".getBytes()));
                    writer.flush();
                    break;
                    
                default:
                    break;
            }
        } catch (IOException ex) {
            System.err.println("Network error");
        } catch (SQLException ex) {
            Logger.getLogger(EchoThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EchoThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
