package as.serveurs;

import as.utilitaires.ListeAuth;
import as.utilitaires.ThreadGestClientTCP;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurTCP implements Runnable {
    // Port reservé aux clients Manager
    private int portChecker ;

    // Port reservé aux clients Manager
    private int portManager ;

    // Le métier utilisé
    private ListeAuth listeAuth;

    public ServeurTCP(int portChecker, int portManager, ListeAuth listeAuth) {
        this.portChecker = portChecker ;
        this.portManager = portManager ;
        this.listeAuth = listeAuth;
    }

    public static void main(String[] args) throws Exception {
        int portChecker = 28414;
        int portManager = 28415;
        ListeAuth listeAuth = new ListeAuth() ;
        ServeurTCP stcp = new ServeurTCP(portChecker, portManager, listeAuth) ;
        stcp.gererLesClients();
    }

    public void run() {
        try {
            this.gererLesClients();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void gererLesClients() throws Exception {
        System.out.println("Serveur TCP : EN COURS");

        // Création d'un socket serveur générique sur le port checker
        ServerSocket ssgChecker = new ServerSocket(portChecker);

        // Création d'un socket serveur générique sur le port manager
        ServerSocket ssgManager = new ServerSocket(portManager);

        while(true) {
            // On attend une connexion puis on l'accepte pour le checker
            Socket sssChecker = ssgChecker.accept();

            // Gestion du client checker accepté via un thread
            ThreadGestClientTCP threadGestClientChk = new ThreadGestClientTCP(sssChecker, listeAuth, false) ;
            threadGestClientChk.start() ;

            // On attend une connexion puis on l'accepte pour le manager
            Socket sssManager = ssgManager.accept();

            // Gestion du client manager accepté via un thread
            ThreadGestClientTCP threadGestClientMng = new ThreadGestClientTCP(sssManager, listeAuth, true) ;
            threadGestClientMng.start() ;
        }
    }

}
