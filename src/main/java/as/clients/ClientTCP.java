package as.clients;

import java.net.Socket;

public abstract class ClientTCP {
    private String adresseMachine ;
    private int port ;

    public ClientTCP(String adresseMachine, int port) {
        this.adresseMachine = adresseMachine;
        this.port = port;
    }

    /**
     * demarre le client et configure la communication avec un serveur TCP
     */
    public void demarrer() {
        try {
            // Création d'un socket client et connexion avec un serveur fonctionnant sur l'adresse de la machine et sur le port spécifiés
            Socket sc = new Socket(adresseMachine, port);

            // configuration de la communication
            communiquer(sc);
        } catch (Exception e) {
            System.err.println("Erreur de connexion au serveur : " + e.getMessage());
        }
    }

    /**
     * Configure la communication avec un serveur TCP
     * @param sc le socket client
     */
    public abstract void communiquer(Socket sc) ;
}
