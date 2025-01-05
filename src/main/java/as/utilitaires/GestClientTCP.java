package as.utilitaires;

import as.clients.ClientTCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class GestClientTCP extends ClientTCP implements Runnable {
    /* Attributs utilisés en tant que gestionnaire d'un client TCP */
    private Socket socketClient ;

    // métier utilisé pour traiter les requêtes clients
    private ListeAuth listeAuth ;

    // objet utilisant le métier pour analyser les requêtes clients
    private AnalyseurRequete analyseurRequete ;

    private boolean estManager ;

    /* Attributs utilisés en tant que client du serveur Log */

    // BufferedReader pour lire du texte envoyé à travers la connexion socket
    private BufferedReader entreeSocket ;

    // PrintStream pour envoyer du texte à travers la connexion socket
    private PrintStream sortieSocket ;

    public GestClientTCP(Socket socketClient, ListeAuth listeAuth, boolean estManager, String adresseMachineLog, int portLog) {
        super(adresseMachineLog, portLog); // instanciation du client AS
        this.demarrer() ;        // demarrage du client AS
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

                    // analyse et traitement de la chaine reçue
                    retourMetier = analyseurRequete.analyser(chaine, estManager);

                    // on envoie le retour au client
                    sortieSocket.println(retourMetier) ;

                    // log de la requête reçue vers le serveur L
                    loggerRequete(chaine) ;
                }
            }

            // on ferme nous aussi la connexion
            socketClient.close();

        } catch (IOException e) {
            System.err.println("Erreur avec le client : " + e.getMessage());
        }

    }

    @Override
    public void communiquer(Socket sc) {
        try {
            // Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
            this.entreeSocket = new BufferedReader(new InputStreamReader(sc.getInputStream()));

            // Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
            this.sortieSocket = new PrintStream(sc.getOutputStream());

        } catch (IOException e) {
            System.err.println("Erreur de communication avec le serveur L : " + e.getMessage());
        }
    }

    private void loggerRequete(String chaine) {
        try {
            // extraction des différentes parties de la requête
            String[] partiesRequete = chaine.split(" ") ;

            // contient le texte Json à envoyer au serveur L
            String chaineEnJson = "" ;

            int tailleRequete = partiesRequete.length ;

            if (tailleRequete == 3){
                // construction de la requête sous format Json en dur
                chaineEnJson += "{\n" ;
                chaineEnJson += "   type : " + partiesRequete[0] ;
                chaineEnJson += ",\n" ;
                chaineEnJson += "   login : " + partiesRequete[1] ;
                chaineEnJson += ",\n" ;
                chaineEnJson += "   password : " + partiesRequete[2] ;
                chaineEnJson += "\n}" ;
            }
            else {
                chaineEnJson += "{ " + chaine + " }\n" ;
            }

            // on envoie la chaine au serveur
            sortieSocket.println(chaineEnJson);
            System.out.println("-----------------------------------------------");
            System.out.println("Chaine envoyée au serveur L : " + chaineEnJson) ;
            System.out.println("------------------------------------------------");

            // lecture d'une chaine envoyée à travers la connexion socket
            String retourServ = entreeSocket.readLine();
            System.out.println("Chaine reçue du serveur L : " + retourServ);

        } catch (Exception e) {
            System.err.println("Erreur lors du log de la requête vers le serveur L : " + e.getMessage());
        }

    }
}
