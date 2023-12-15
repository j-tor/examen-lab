/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PSN;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.util.Date;

/**
 *
 * @author aleja
 */
public class PSNUser {
    
    RandomAccessFile Raf;
    RandomAccessFile PNS;
    HashTable users;
    long tamano = 0;
     
    public PSNUser(){
         try {

            Raf = new RandomAccessFile("users", "rw");
            PNS = new RandomAccessFile("psn", "rw");
            relodHashTable();
        } catch (FileNotFoundException e) {
            try {
                File f = new File("users");
                f.createNewFile();

                f = new File("psn");
                f.createNewFile();
                Raf = new RandomAccessFile("users", "rw");
            } catch (Exception ex) {
             
                System.out.println("error");
            }

        } catch (Exception e) {
           
             System.out.println("error");
        }
              
    }
    
    private void relodHashTable(){
         tamano = 0;
        try {
            Raf.seek(0);
            users = new HashTable();
            while (Raf.getFilePointer() < Raf.length()) {
                String username = Raf.readUTF();
                Raf.readInt();
                Raf.readInt();
                boolean registered = Raf.readBoolean();
                System.out.println("Usuario" + username + " Regitrado: " + registered);

                if (!registered) {
                    System.out.println("Usuario" + username + " not regged. SKIPPING");
                    continue;
                }

                users.add(username, tamano);
                tamano++;
            }
           

        } catch (Exception e) {

        }
            
    }
    
    
    public boolean addUser(String username){
        if (users.search(username) == 1) {
            System.out.println("se agrego usuario");
            return false;
        }
        try {
        Raf.seek(Raf.length());
        Raf.writeUTF(username);
        Raf.writeInt(0);
        Raf.writeInt(0);
        Raf.writeBoolean(true);
        } catch (Exception e) {
        }
        

        users.add(username, tamano);
        tamano++;
        return true;
    }
    

    public void deactivateUser(String username) {
    if (users.search(username) == -1) {
            System.out.println("no encontrado");
            return;
        }

        try {

            
            File newUsersFile = new File("tempUsers");
            newUsersFile.createNewFile();
            
            RandomAccessFile newUsers = new RandomAccessFile(newUsersFile, "rw");
            Raf.seek(0);
            while (Raf.getFilePointer() < Raf.length()) {
               try{
                String currentUsername = Raf.readUTF();
                
                if (username.equals(currentUsername)) {
                    Raf.skipBytes(4 * 2 + 1); 
                    continue;
                }

                int trophyPoints = Raf.readInt();
                int trophyCount = Raf.readInt();
                
                newUsers.writeUTF(currentUsername);
                newUsers.writeInt(trophyPoints);
                newUsers.writeInt(trophyCount);
                newUsers.writeBoolean(true);
            }catch(EOFException e) {
            break;
            }
            }
            
            newUsers.close();
            Raf.close();
            
            File usersFile = new File("users");
            usersFile.delete();
            newUsersFile.renameTo(usersFile);
            Raf = new RandomAccessFile("users", "rw");
            relodHashTable();
                    
            File tempTrophies = new File("temp");
            tempTrophies.createNewFile();
            RandomAccessFile tempRaf = new RandomAccessFile("temp", "rw");

            PNS.seek(0);
            while (PNS.getFilePointer() < PNS.length()) {
                String currentUsername = PNS.readUTF();
                String trophyType = PNS.readUTF();
                String trophyGame = PNS.readUTF();
                String trophyName = PNS.readUTF();
                long date = PNS.readLong();

                if (currentUsername.equals(username)) {
                    System.out.println("si me borra el trofeo");
                    continue;
                }

                tempRaf.writeUTF(currentUsername);
                tempRaf.writeUTF(trophyType);
                tempRaf.writeUTF(trophyGame);
                tempRaf.writeUTF(trophyName);
                tempRaf.writeLong(date);
            }

            tempRaf.close();
            PNS.close();

            File psnFile = new File("psn");
            psnFile.delete();
            tempTrophies.renameTo(psnFile);
            PNS = new RandomAccessFile("psn", "rw");
            deleteFileUser(username);

        } catch (IOException e) {
            e.printStackTrace();
        }      
    }

    public void deleteFileUser(String username){
        
    }
    
    public boolean addTrophieTo(String username, String trophyGame, String trophyName, Trophy type){
        if (users.search(username) == -1) {
            return false;
        }

        try {
            PNS.seek(PNS.length());
            PNS.writeUTF(username);
            PNS.writeUTF(type.name());
            PNS.writeUTF(trophyGame);
            PNS.writeUTF(trophyName);
            Date currentDate = new Date();
            PNS.writeLong(currentDate.getTime());

            Raf.seek(0);
            long currentPos = 0;
            while (Raf.getFilePointer() < Raf.length()) {
            currentPos = Raf.getFilePointer();
            String currentUsername = Raf.readUTF();

            if (!currentUsername.equals(username)) {
                Raf.skipBytes(4 * 2 + 1);
                continue;
            }
            int trophyPoints = Raf.readInt();
            int trophyCount = Raf.readInt();
            boolean registered = Raf.readBoolean();
            System.out.println("registrado");
            System.out.println(trophyPoints);
            System.out.println(trophyCount);
            System.out.println(registered);
            
            if (!registered) {
                return false;
            }

            trophyPoints += type.puntos;
            trophyCount++;

            System.out.println("Nuevo");
            System.out.println(trophyPoints);
            System.out.println(trophyCount);
            
            Raf.seek(currentPos);
            Raf.writeUTF(currentUsername);
            Raf.writeInt(trophyPoints);
            Raf.writeInt(trophyCount);
            Raf.writeBoolean(true);
            Raf.close();
            Raf = new RandomAccessFile("users", "rw");
            return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }

        }

    public String playerInfo(String username) {
        if (users.search(username) != -1) {
            System.out.println("Usuario no encontrado");
            return "";
        }else{

        try {
            String out = "";
            Raf.seek(0);
            while (Raf.getFilePointer() < Raf.length()) {
                String currentUsername = Raf.readUTF();
                int trophyPoints = Raf.readInt();
                int trophyCount = Raf.readInt();
                boolean registered = Raf.readBoolean();

                System.out.println(currentUsername + " Datos" + registered);

                if (!currentUsername.equals(username) || !registered) {
                                    

                    continue;
                }

                out = "Informacion de " + username + "\n====================\n";
                out += "\nPuntos de trofeos: " + trophyPoints;
                out += "\nCantidad de trofeos: " + trophyCount;
                out += "\nTrofeos:\n";
           

                PNS.seek(0);
                while (PNS.getFilePointer() < PNS.length()) {
                    String currentPsnUsername = PNS.readUTF();
                    String trophyType = PNS.readUTF();
                    String trophyGame = PNS.readUTF();
                    String trophyName = PNS.readUTF();
                    long date = PNS.readLong();

                    if (currentPsnUsername.equals(username)) {
                        out += "\n\tTrofeo " + trophyType + " de " + trophyGame + " llamado " + trophyName + " el "
                                + new Date(date);
                    }
                }
                out += "\n==================\n";
            }
            System.out.println("dastos"+out);
            return out;

        } catch (Exception e) {
//            e.printStackTrace();
            return "";
        }}

    }

   
    }
