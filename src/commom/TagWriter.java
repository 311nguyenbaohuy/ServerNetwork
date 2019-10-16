/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commom;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author nguyenBaoHuy
 */
public class TagWriter {
    private OutputStream myOs;
    public TagWriter(OutputStream os){
        myOs = os;
    }
    public synchronized void writeTag(TagValue tv) throws IOException{
        myOs.write(tv.getTag().getBytes());
        myOs.write(tv.getContent());
    }
    public void close() throws IOException {
        myOs.close();
    }
    public void flush() throws IOException{
        myOs.flush();
    }
}
