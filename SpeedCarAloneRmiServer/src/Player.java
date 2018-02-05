
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author brasc
 */
public class Player extends Iplayer {

    /**
     * Utiliser le RMI pour communiquer avec le client
     */
    private ClientInterface client;
   
    /**
     * Pour ne pas faire deux threads en même temps
     */
    private Thread updateThread = null;

    /**
     *
     * @param name
     * @param client
     * @param party
     */
    public Player(String name, ClientInterface client, Core core) {
        super(name);
        //this.party = party;
        this.client = client;
        setCore(core);
        
    }

   /**
    * Pour mettre à jour l'affichage d'un client
    * @param vDisplayRoad
    * @param vDisplayObstacles
    * @param vDisplayCars
    * @param myCar
    * @param pos
    * @param nbParticipants
    * @param bGameOver
    * @param sPosition 
    */
    @Override
    public void update(Vector<Rectangle> vDisplayRoad, Vector<Rectangle> vDisplayObstacles, Vector<Rectangle> vDisplayCars, Car myCar, int pos, int nbParticipants, boolean bGameOver, String sPosition) {
        if (updateThread == null || updateThread.getState() == Thread.State.TERMINATED) {
            // On lance l'update dans un thread pour ne pas bloquer le serveur si le client est lent
            updateThread = new Thread() {
                @Override
                public void run() {
                    try {
                        System.out.println(".run()"+client);
                        System.out.println(".run() vDisplayRoad : "+vDisplayRoad);
                        System.out.println(".run() vDisplayObstacles :"+vDisplayObstacles);
                        System.out.println(".run() vDisplayCars : "+vDisplayCars);
                        System.out.println(".run() myCar : "+myCar);
                        System.out.println(".run() pos : "+pos);
                        System.out.println(".run() nbParticipants : "+nbParticipants);
                        System.out.println(".run() bGameOver : "+bGameOver);
                        System.out.println(".run() sPosition : "+sPosition);
                      //  System.out.println(".run() bGameOver : "+bGameOver);
                        client.update(vDisplayRoad, vDisplayObstacles, vDisplayCars, myCar, pos, nbParticipants, bGameOver, sPosition);
                    } catch (RemoteException ex) {
                        // Si ca plante, probablement que le client s'est deconnecté
                        // On le retire alors de la partie
                      //  party.disconnect(getId());
                        System.out.println(".run() dans update player");
                    }
                }
            };
            updateThread.start();
        }
    }

    @Override
    public void setPlayerButton(boolean flag) {
        try {
            client.setPlayButton(flag);
        } catch (RemoteException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setClient(ClientInterface client) {
        System.out.println("Modification de client");
        this.client = client;
    }

    public ClientInterface getClient() {
        return client;
    }

  

}
