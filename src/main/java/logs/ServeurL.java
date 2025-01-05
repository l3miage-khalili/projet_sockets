package logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServeurL {
    private int port ;

    // contient les requêtes loggées par le client
    private ArrayList<String> listeRequetes ;

    public ServeurL(int port) {
        this.port = port;
        this.listeRequetes = new ArrayList<>() ;
    }

    public void demarrer() {
        try {
            // Création d'un socket serveur générique sur le port
            ServerSocket ssg = new ServerSocket(port);
            System.out.println("Serveur L démarré sur le port " + port);

            while(true) {
                // On attend une connexion puis on l'accepte
                System.out.println("En attente de connexions...");

                Socket socketClient = ssg.accept();
                System.out.println("Connexion acceptée depuis " + socketClient.getInetAddress());

                // Gestion du client accepté
                gererClient(socketClient) ;
            }
        } catch (IOException e) {
            System.err.println("Erreur de demarrage du serveur ou lors de la gestion d'un client : " + e.getMessage());
        }
    }

    private void gererClient(Socket socketClient) {
        try {
            // Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
            BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

            // Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
            PrintStream sortieSocket = new PrintStream(socketClient.getOutputStream());

            String chaine = "" ;
            String reponse = "Requête loggée avec succès" ;

            while(chaine != null) {
                // lecture d'une chaine envoyée à travers la connexion socket
                chaine = entreeSocket.readLine();

                // si elle est nulle c'est que le client a fermé la connexion
                if (chaine != null) {
                    System.out.println("chaine reçue du client AS : " + chaine);

                    // Enregistrement de la chaine reçue
                    enregistrerRequete(chaine) ;

                    // on envoie la chaine au client
                    sortieSocket.println(reponse) ;
                }
            }

            // on ferme nous aussi la connexion
            socketClient.close();

        } catch (IOException e) {
            System.err.println("Erreur lors de gestion du client : " + e.getMessage());
        }

    }

    private void enregistrerRequete(String requete) {
        this.listeRequetes.add(requete) ;
    }

    public static void main(String[] args) {
        /* Configuration du serveur L */
        int port = 3244 ;

        // Instanciation du serveur
        ServeurL serveurL = new ServeurL(port) ;

        // demarrage du serveur et gestion du client AS
        serveurL.demarrer();
    }

}
