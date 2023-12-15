/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PSN;

/**
 *
 * @author aleja
 */
public class HashTable {
    Entry entry;
    long tamano=0;
    
    public void add(String username, long pos){
        if (search(username)!=-1) {
            return;
            
        }
    }
    
    public long search(String username){
        Entry entry = this.entry;
        long pos = 0;

        while (entry != null) {
            if (entry.username.equals(username)) {
                return pos;
            }
            pos++;
            entry = entry.siguiente;
        }

        return -1;
    }
    
    public void remove(String username ){
        if (entry.username.equals(username)) {
            entry = entry.siguiente;
            tamano--;
            return;
        }

        Entry before = entry;
        Entry current = entry.siguiente;
        while (current != null) {
            if (current.username.equals(username)) {
                before.siguiente = current.siguiente;
                tamano--;
                return;
            }

            before = current;
            current = current.siguiente;
        }
    }
    

    
}


