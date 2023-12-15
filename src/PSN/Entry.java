/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PSN;

import java.io.RandomAccessFile;

/**
 *
 * @author aleja
 */
public class Entry {   
    String username;
    long pos;
    Entry siguiente;

    public Entry(String username, long pos) {
        this.username = username;
        this.pos = pos;
        siguiente = null;
    }
    
}
