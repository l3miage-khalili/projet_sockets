package as_bis.utilitaires;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class GestClientTCP implements Runnable {
    private Socket socketClient ;

    // métier utilisé pour traiter les requêtes clients
    private ListeAuth listeAuth ;

    // objet utilisant le métier pour analyser les requêtes clients
    private AnalyseurRequete analyseurRequete ;

    private boolean estManager ;

    public GestClientTCP(Socket socketClient, ListeAuth listeAuth, boolean estManager) {
        this.socketClient = socketClient;
        this.listeAuth = listeAuth ;
        this.analyseurRequete = new AnalyseurRequete(this.listeAuth) ;
        this.estManager = estManager ;
    }

    @Override
    public void run() {
        try {
            gererClient(estManager) ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void gererClient(boolean estManager) {
        try {
            // Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
            BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

            // Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
            PrintStream sortieSocket = new PrintStream(socketClient.getOutputStream());

            String chaine = "";
            String retourMetier ;

            while(chaine != null) {
                // lecture d'une chaine envoyée à travers la connexion socket
                chaine = entreeSocket.readLine();

                // si elle est nulle c'est que le client a fermé la connexion
                if (chaine != null) {
                    System.out.println("Chaine reçue du client TCP : " + chaine);
                    retourMetier = analyseurRequete.analyser(chaine, estManager);
                    sortieSocket.println(retourMetier); // on envoie le retour au client
                }
            }

            // on ferme nous aussi la connexion
            socketClient.close();

        } catch (IOException e) {
            System.err.println("Erreur avec le client : " + e.getMessage());
        }

    }


}
