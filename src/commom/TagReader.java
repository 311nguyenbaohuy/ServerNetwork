/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 * @author nguyenBaoHuy
 */
public class TagReader {
    private InputStream is;
    public TagReader(InputStream i){
        is = i;
    }
    
    public TagValue getTagValue() throws IOException{
        return new TagValue(getTag(), getValue());
    }
    
    public String getTag() throws IOException{
        String tag = "";
        int c = is.read();
        while (!Character.isWhitespace(c)){
            tag += Character.toString((char)c);
            c = is.read();
        }
        return tag;
    }
    
    public byte [] getValue() throws IOException{
        byte [] val = new byte[Tags.MAX_VALUE_LENGTH];
        int c = is.read();
        int i = 0;
        boolean complete = false;
        while ((char)c != ';') {            
            val[i] = (byte) c;
            i++;
            c = is.read();
        }
        byte [] val2 = new byte[i];
        for (int j=0; j < i; j++){
                val2[j] = val[j];
        }
        return val2;
    }
    

}
