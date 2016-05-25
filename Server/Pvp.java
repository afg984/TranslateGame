/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import serialize.Request;

/**
 *
 * @author zlsh80826
 */
public class Pvp {
    Socket pA;
    Socket pB;
    ObjectInputStream pAIn;
    ObjectInputStream pBIn;
    ObjectOutputStream pAOut;
    ObjectOutputStream pBOut;
    Listener monitor;
    
    
    public Pvp(Listener monitor, Socket pA, Socket pB){
        this.pA = pA;
        this.pB = pB;
        this.monitor = monitor;
        /*try {
            pBIn = new ObjectInputStream(pA.getInputStream());
        } catch (IOException ex) {
            System.out.println("pAInputStream init error");
        }*/
        /*
        try {
            pBIn = new ObjectInputStream(pB.getInputStream());
        } catch (IOException ex) {
            System.out.println("pBInputStream init error");
        }*/
        
        try {
            pAOut = new ObjectOutputStream(pA.getOutputStream());
            pBOut = new ObjectOutputStream(pB.getOutputStream());
        } catch (IOException ex) {
            System.out.println("OutputStream init error");
        }
        System.out.println("Connection establish");
        try {
            pBOut.writeObject(new Request("ya"));
            pBOut.flush();
        } catch (IOException ex) {
            System.out.println("write error");
        }
    }
    
    
}
