package as.utilitaires;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ThreadGestClientTCP extends Thread {

    private Socket sss ;
    private ListeAuth listeAuth ;
    private ParserMessage parserMessage ;
    private boolean estManager ;

    public ThreadGestClientTCP(
            Socket sss,
            ListeAuth listeAuth,
            boolean estManager
    ) {
        this.sss = sss ;
        this.listeAuth = listeAuth ;
        parserMessage = new ParserMessage(listeAuth) ;
        this.estManager = estManager ;
    }

    @Override
    public void run() {
        try {
            this.gererClient(sss, estManager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void gererClient(Socket sss, boolean estManager) throws IOException {

        // Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
        BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(sss.getInputStream()));

        // Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
        PrintStream sortieSocket = new PrintStream(sss.getOutputStream());

        String chaine = "";

        while(chaine != null) {
            // lecture d'une chaine envoyée à travers la connexion socket
            chaine = entreeSocket.readLine();

            // si elle est nulle c'est que le client a fermé la connexion
            if (chaine != null){
                System.out.println("Chaine reçue du client TCP : " + chaine);
                sortieSocket.println(parserMessage.parser(chaine, estManager));
            }
        }

        // on ferme nous aussi la connexion
        sss.close();
    }
}
