/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commom;

/**
 *
 * @author nguyenBaoHuy
 */
public class TagValue {
    
    private String tag;
    private byte [] content;
    
    public TagValue(){
        tag = "";
        content = null;
    }
    public TagValue(String t, byte[] c){
        tag = t;
        content = c;
    }
    public String getTag(){
        return tag;
    }
    public byte [] getContent(){
        return content;
    }
}
