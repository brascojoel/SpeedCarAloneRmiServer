
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author brasc
 */
public class GameEngine extends UnicastRemoteObject implements GameEngineInterface {

    /**
     * une liste des clients par leurs id, pour les retrouver quand ils
     * communiquent avec le serveur
     */
    private final Map<Long, Player> clientsById = new HashMap<>();
    /**
     * une liste des Cores par  id des clients
     */
    private final Map<Long, Core> clientsCoreById = new HashMap<>();

    public GameEngine() throws RemoteException {
        super();

    }

    /**
     * Crée un ClientLink a partir d'un client RMI et le connecte au système
     *
     * @param client le client RMI qui se connecte
     * @return l'id unique généré pour ce client
     * @throws RemoteException
     */
    @Override
    public long connect(ClientInterface client) throws RemoteException {
        System.out.println("Nouveau client connecté : " + client);
        Player player = new Player(client.getName(), client, new Core(client));

        if (client != null) {

            clientsById.put(player.getId(), player);

            clientsCoreById.put(player.getId(), new Core(player));
            System.out.println("Nouveau client connecté : " + client.getName());
            return player.getId();
        }
        System.out.println("Erreur de connexion du client : " + client.getName());
        return 0L;
    }

    /**
     * Deconnecte un client par son ID.
     *
     * @param userId id unique du client
     * @throws RemoteException
     */
    @Override
    public void disconnect(long userId) throws RemoteException {

        Player player = clientsById.get(userId);

        if (player != null) {
            Core core = clientsCoreById.get(userId);
            if (core != null) {

                core.stopGame();
                clientsById.remove(userId);
                clientsCoreById.remove(userId);

                System.out.println(" " + player.getName() + " déconnecté");

            }
        }

    }

    /**
     * Démarre une partie
     * @param userId
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean startGame(long userId) throws RemoteException {

        Player player = clientsById.get(userId);

        boolean flag = false;
        if (player != null) {
            Core core = clientsCoreById.get(userId);
            if (core != null) {

                flag = core.startGame();

            }
        }

        return flag;
    }

    /**
     * Initialiser la partie
     * @param userId
     * @throws RemoteException 
     */
    @Override
    public void newGrid(long userId) throws RemoteException {
        Player client = clientsById.get(userId);
        if (client != null) {
            Core core = clientsCoreById.get(userId);
            if (core != null) {

               
                core.newGrid();

            }
        }
    }

    /**
     * Lancer le jeu
     * @param userId
     * @throws RemoteException 
     */
    @Override
    public void beginGame(long userId) throws RemoteException {
        Player client = clientsById.get(userId);
        if (client != null) {

            Core core = clientsCoreById.get(userId);
            if (core != null) {
                core.beginGame();

            }
        }
    }

    @Override
    public void moveCar(long userId, String choice, boolean flag) throws RemoteException {

        Player client = clientsById.get(userId);
        if (client != null) {

            Core core = clientsCoreById.get(userId);
            if (core != null) {
                core.moveCar(choice, flag);

            }
        }
    }

    @Override
    public int getScoreClient(long userId) throws RemoteException {
        int x = 0;
        Player client = clientsById.get(userId);
        if (client != null) {

            Core core = clientsCoreById.get(userId);
            if (core != null) {
                x = core.getScore();
                client.setScore(x);
                return x;

            }
        }
        return client.getScore();
    }

}
