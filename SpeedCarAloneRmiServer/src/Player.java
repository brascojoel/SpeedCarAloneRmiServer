
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.ArrayList;
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
public class Player extends Iplayer {

    /**
     * Utiliser le RMI pour communiquer avec le client
     */
    private final ClientInterface client;
   
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
        setCore(core);
        this.client = client;
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

  

}
