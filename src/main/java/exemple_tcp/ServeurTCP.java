package exemple_tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurTCP {
    public static void main(String[] args) throws Exception {

        // Création d'un socket serveur générique sur le port 40000
        ServerSocket ssg = new ServerSocket(40000);

        while(true) {
            // On attend une connexion puis on l'accepte
            Socket sss = ssg.accept();

            // Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
            BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(sss.getInputStream()));
            // Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
            PrintStream sortieSocket = new PrintStream(sss.getOutputStream());

            String chaine = "";

            while(chaine != null) {
                // lecture d'une chaine envoyée à travers la connexion socket
                chaine = entreeSocket.readLine();

                // si elle est nulle c'est que le client a fermé la connexion
                if (chaine != null)
                    sortieSocket.println(chaine); // on envoie la chaine au client
            }

            // on ferme nous aussi la connexion
            sss.close();
        }
    }
}

