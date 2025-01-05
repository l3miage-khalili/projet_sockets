package as_bis.serveurs;

import as_bis.utilitaires.GestClientTCP;
import as_bis.utilitaires.ListeAuth;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurTCP {
    // Port des clients "checker"
    private int portChk;

    // Port des clients "manager"
    private int portMng;

    // Le métier utilisé par le serveur
    private ListeAuth listeAuth;

    public ServeurTCP(int portChk, int portMng, ListeAuth listeAuth) {
        this.portChk = portChk;
        this.portMng = portMng;
        this.listeAuth = listeAuth;
    }

    public void demarrer() {
        // Démarrage de deux threads pour gérer les clients "checker" et "manager"
        new Thread(() -> gererClients(portChk)).start();
        new Thread(() -> gererClients(portMng)).start();
    }

    private void gererClients(int port) {
        try {
            // Création d'un socket serveur générique sur le port
            ServerSocket ssg = new ServerSocket(port);
            System.out.println("Serveur TCP démarré sur le port " + port);

            while(true) {
                // On attend une connexion puis on l'accepte
                System.out.println("En attente de connexions...");
                Socket socketClient = ssg.accept();
                System.out.println("Connexion acceptée depuis " + socketClient.getInetAddress());

                // Déléguer le traitement à un gestionnaire de client TCP
                boolean estManager ;
                if (port == portChk){
                    estManager = false ;
                }
                else {
                    estManager = true ;
                }
                new Thread(new GestClientTCP(socketClient, this.listeAuth, estManager)).start();
            }

        } catch (IOException e) {
            System.err.println("Erreur de demarrage du serveur ou lors de la gestion d'un client : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        /* configuration du serveur TCP */
        int portChk = 28414 ;
        int portMng = 28415 ;
        ListeAuth listeAuth = new ListeAuth() ;

        // Instanciation du serveur
        ServeurTCP serveurTCP = new ServeurTCP(portChk, portMng, listeAuth) ;

        // demarrage du serveur et gestion des clients
        serveurTCP.demarrer();
    }
}
