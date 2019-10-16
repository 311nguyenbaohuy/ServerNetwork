/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commom;

import java.io.DataInputStream;
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
    
    public TagValue getTagValue() throws IOException, Exception{
        return new TagValue(this.getTag(), this.getValue());


    }
    
    public String getTag() throws IOException, Exception{
        String tag = "<";

        int c = is.read();
        if ((char)c != '<') {
            Exception e = new Exception("The content must be start with character <");
            throw e;
        }
        c = is.read();
        while ((char)c != '>'){
            tag += (char)c;
            c = is.read();          
        }
        tag += '>';
        return tag;
    }
    
    public byte [] getValue() throws IOException, Exception{
        byte [] val = new byte[Tags.MAX_VALUE_LENGTH];
        int c = is.read();
        int i = 0;
        if ((char)c != '<') {
            Exception e = new Exception("The content must be start with character <");
            throw e;
        }
        while ((char)c != '>') {
            val[i] = (byte) c;
            i++;
            c = is.read();
        }
        val[i] = (byte) c;
        i++;
        byte [] val2 = new byte[i];
        for (int j=0; j < i; j++){
                val2[j] = val[j];
        }
        return val2;
    }
}
