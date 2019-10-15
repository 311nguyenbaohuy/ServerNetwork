/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

/**
 *
 * @author nguyenBaoHuy
 */
public class User {
    private int id = 0;
    private String user_name = "default";
    private String password = "default";
    private String ip_addr = "1.1.1.1";
    private int status = 0;

    public User(int id, String user_name, String password, String ip_addr, int stauts) {
        this.id = id;
        this.user_name = user_name;
        this.password = password;
        this.ip_addr = ip_addr;
        this.status = stauts;
    }
    public User(){}
    
    public int getID() { return id; }
    
}
