package as_bis.clients;

import java.net.Socket;

public abstract class ClientTCP {
    private String adresseMachine ;
    private int port ;

    public ClientTCP(String adresseMachine, int port) {
        this.adresseMachine = adresseMachine;
        this.port = port;
    }

    public void demarrer() {
        try {
            // Création d'un socket client et connexion avec un serveur fonctionnant sur l'adresse de la machine et sur le port spécifiés
            Socket sc = new Socket(adresseMachine, port);
            communiquer(sc);
        } catch (Exception e) {
            System.err.println("Erreur de connexion au serveur : " + e.getMessage());
        }
    }

    public abstract void communiquer(Socket sc) ;
}
