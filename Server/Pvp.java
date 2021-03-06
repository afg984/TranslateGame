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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import serialize.*;

/**
 *
 * @author zlsh80826
 */
public class Pvp extends Thread {

    Socket pA;
    Socket pB;
    Socket monsterSocketA;
    Socket monsterSocketB;
    ObjectInputStream pAIn;
    ObjectInputStream pBIn;
    ObjectOutputStream pAOut;
    ObjectOutputStream pBOut;
    Listener monitor;
    boolean loadComplete = false;
    boolean pALoadComplete = false;
    boolean pBLoadComplete = false;
    boolean selectComplete = false;
    boolean pASelectComplete = false;
    boolean pBSelectComplete = false;
    boolean startTimer = false;
    boolean startGame = false;
    MonsterInfoPkg monsterInfoPkg;
    ServerMonsterControl control;
    //Career pACareer;
    //Career pBCareer;
    Info pAInfo;
    Info pBInfo;
    int time = 90;

    public Pvp(Listener monitor, Socket pA, Socket pB, Socket monsterSocketA, Socket monsterSocketB) {
        this.pA = pA;
        this.pB = pB;
        this.monitor = monitor;
        this.monsterSocketA = monsterSocketA;
        this.monsterSocketB = monsterSocketB;
        try {
            pAIn = new ObjectInputStream(pA.getInputStream());
        } catch (IOException ex) {
            System.out.println("pAInputStream init error");
        }

        try {
            pBIn = new ObjectInputStream(pB.getInputStream());
        } catch (IOException ex) {
            System.out.println("pBInputStream init error");
        }

        try {
            pAOut = new ObjectOutputStream(pA.getOutputStream());
        } catch (IOException ex) {
            System.out.println("OutputStream init error");
        }

        try {
            pBOut = new ObjectOutputStream(pB.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Pvp.class.getName()).log(Level.SEVERE, null, ex);
        }

        control = new ServerMonsterControl(this.monsterSocketA, this.monsterSocketB);
    }

    @Override
    public void run() {
        PvpListen pAHandler = new PvpListen(this, pAIn, 0);
        PvpListen pBHandler = new PvpListen(this, pBIn, 1);
        pBHandler.start();
        pAHandler.start();

        while (true) {
            if (this.getLoadComplete()) {
                sendStartSelect();
                this.setLoadComplete(false);
            }
            if (this.getSelectComplete()) {
                sendStartGame();
                this.setSelectComplete(false);
            }
            if (this.getStartGame()) {
                control.start();
                this.setStartGame(false);
                this.startTimer = true;
            }
            if (startTimer) {
                try {
                    sendMessage(time --);
                    Thread.sleep(1000);
                    if(time == 0){
                        startTimer =false;
                        sendPVPSignal();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Pvp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public synchronized void sendPVPSignal(){
        try {
            pAOut.writeObject(new Situation("startpvp"));
            pAOut.reset();
            pBOut.writeObject(new Situation("startpvp"));
            pBOut.reset();
        } catch (IOException ex) {
            System.out.println("send start select sitution error");
        }        
    }

    public void sendStartSelect() {
        try {
            pAOut.writeObject(new Situation("startselect"));
            pAOut.reset();
            pBOut.writeObject(new Situation("startselect"));
            pBOut.reset();
        } catch (IOException ex) {
            System.out.println("send start select sitution error");
        }
    }

    public void sendStartGame() {
        //System.out.println("Send start game");
        try {
            pAOut.writeObject(new Situation("startgame", pBInfo));
            pAOut.reset();
            pBOut.writeObject(new Situation("startgame", pAInfo));
            pBOut.reset();
        } catch (IOException ex) {
            System.out.println("send start select sitution error");
        }
        this.setStartGame(true);
    }

    public synchronized void sendInfoToPeer(int identify, Info info) {
        if (identify == 0) {
            try {
                pBOut.writeObject(info);
                pBOut.reset();
            } catch (IOException ex) {
                Logger.getLogger(Pvp.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (identify == 1) {
            try {
                pAOut.writeObject(info);
                pAOut.reset();
            } catch (IOException ex) {
                Logger.getLogger(Pvp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public synchronized void setLoadComplete(int id) {
        if (id == 0) {
            pALoadComplete = true;
        } else if (id == 1) {
            pBLoadComplete = true;
        }
        if (pALoadComplete && pBLoadComplete) {
            setLoadComplete(true);
        }
    }

    public synchronized void setSelectComplete(int id, Info info) {
        if (id == 0) {
            pASelectComplete = true;
            //pBCareer = career;
            pAInfo = info;
        } else if (id == 1) {
            pBSelectComplete = true;
            //pACareer = career;
            pBInfo = info;
        }
        if (pASelectComplete && pBSelectComplete) {
            setSelectComplete(true);
        }
    }

    public synchronized void setSelectComplete(boolean value) {
        this.selectComplete = value;
    }

    public synchronized void setStartGame(boolean value) {
        this.startGame = value;
    }

    public synchronized void setLoadComplete(boolean value) {
        this.loadComplete = value;
    }

    public synchronized boolean getSelectComplete() {
        return this.selectComplete;
    }

    public synchronized boolean getLoadComplete() {
        return this.loadComplete;
    }

    public synchronized boolean getStartGame() {
        return this.startGame;
    }

    public synchronized void sendExpRequestToPeer(int identify, ExpRequest er) {
        if (identify == 0) {
            try {
                pBOut.writeObject(er);
                pBOut.reset();
            } catch (IOException ex) {
                Logger.getLogger(Pvp.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                pAOut.writeObject(er);
                pAOut.reset();
            } catch (IOException ex) {
                Logger.getLogger(Pvp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public synchronized void sendDmgRequestToPeer(int identify, DmgRequest er) {
        if (identify == 0) {
            try {
                pBOut.writeObject(er);
                pBOut.reset();
            } catch (IOException ex) {
                Logger.getLogger(Pvp.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                pAOut.writeObject(er);
                pAOut.reset();
            } catch (IOException ex) {
                Logger.getLogger(Pvp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void sendMessage(int value) {
        try {
            pAOut.writeObject(new Info(0, 0, 0, false, Career.SwordMan, 0, 0, 0, 0, false, 0, 0, false, value, "time"));
            pAOut.reset();
            pBOut.writeObject(new Info(0, 0, 0, false, Career.SwordMan, 0, 0, 0, 0, false, 0, 0, false, value, "time"));
            pBOut.reset();
        } catch (IOException ex) {
            System.out.println("send message error");
        }
    }

}
