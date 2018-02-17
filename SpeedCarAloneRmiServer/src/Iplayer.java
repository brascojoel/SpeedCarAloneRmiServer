
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author brasc
 */
public abstract class Iplayer {

    /**
     * Un random normalement plus sécurisé que le Random classique, Il est
     * réputé non devinable.
     *
     */
    private static final SecureRandom prng = new SecureRandom();

    /**
     * Nom du client choisi à la connexion
     */
    private final String name;
    
    /**
     * Score du client
     */
    private int score = 0;

    /**
     * Utiliser le Random pour generer un id unisque au client
     */
    private final long userId = prng.nextLong();

    /**
     * Pour enregistrer la partie à laquelle le client appartient
     */
    private Core core = null;
    
    
    private boolean bGameInProgress = false;
    
     /**
     * True if the player is pressing the up arrow key
     */
    public  boolean UP_P = false;
    
     /**
     * True if the player is pressing the down arrow key
     */
    public  boolean DO_P = false;

    /**
     * True if the player is pressing the right arrow key
     */
    public  boolean RI_P = false;

    /**
     * True if the player is pressing the left arrow key
     */
    public  boolean LE_P = false;

    /**
     * Constructeur
     * @param name 
     */
    protected Iplayer(String name) {
        this.name = name;
      
    }

    /**
     * 
     * @param vDisplayRoad
     * @param vDisplayObstacles
     * @param vDisplayCars
     * @param myCar
     * @param pos
     * @param nbParticipants
     * @param bGameOver
     * @param sPosition
     * @throws java.rmi.RemoteException 
     */
    public abstract void update(Vector<Rectangle> vDisplayRoad, Vector<Rectangle> vDisplayObstacles, Vector<Rectangle> vDisplayCars, Car myCar, int pos, int nbParticipants, boolean bGameOver, String sPosition) throws java.rmi.RemoteException;

  
    public abstract void setPlayerButton(boolean flag);

    /**
     * Retourne l'id unique du client
     *
     * @return
     */
    public long getId() {
        return this.userId;
    }

    public String getName() {
        return name;
    }

    /**
     * Fait entrer le client dans une arena
     * @param core
     * @return 
     */
    public boolean setCore(Core core) {
        if (core != null || this.core == null) {
            this.core = core;
            return true;
        }
        return false;
    }

    public Core getCore() {
        return core;
    }

    /**
     * 
     * @return 
     */
    public synchronized int getScore() {
        return score;
    }

    /**
     * 
     * @param score 
     */
    public synchronized void setScore(int score) {    
        this.score = score;
    }

  
    
    public synchronized boolean getDirection(boolean flag){
    
        return flag;
    }

    public boolean isUP_P() {
        return UP_P;
    }

    public void setUP_P(boolean UP_P) {
        this.UP_P = UP_P;
    }

    public boolean isDO_P() {
        return DO_P;
    }

    public void setDO_P(boolean DO_P) {
        this.DO_P = DO_P;
    }

    public boolean isRI_P() {
        return RI_P;
    }

    public void setRI_P(boolean RI_P) {
        this.RI_P = RI_P;
    }

    public boolean isLE_P() {
        return LE_P;
    }

    public void setLE_P(boolean LE_P) {
        this.LE_P = LE_P;
    }
    
    
 
   
}
