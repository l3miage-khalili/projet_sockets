package as_bis.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCPChk extends ClientTCP {

    public ClientTCPChk(String adresseMachine, int port) {
        super(adresseMachine, port);
    }

    @Override
    public void communiquer(Socket sc) {
        try {
            // Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
            BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(sc.getInputStream()));

            // Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
            PrintStream sortieSocket = new PrintStream(sc.getOutputStream());

            String chaine = "";

            // Scanner sur System.in
            Scanner scanner = new Scanner(System.in);

            System.out.println("Veuillez faire votre check ou Tapez FIN pour arrêter :");

            while(!chaine.equalsIgnoreCase("FIN")) {
                // lecture clavier
                chaine = scanner.nextLine();
                sortieSocket.println(chaine); // on envoie la chaine au serveur

                // lecture d'une chaine envoyée à travers la connexion socket
                String retourServ = entreeSocket.readLine();
                System.out.println("Chaine reçue du serveur TCP : " + retourServ);
            }

            // on ferme nous aussi la connexion
            sc.close();
        } catch (IOException e) {
            System.err.println("Erreur de communication avec le serveur TCP : " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        /* Configuration du client TCP checker */
        String adresseMachine = "localhost" ;
        int port = 28414 ;

        // Instanciation d'un client
        ClientTCPChk clientTCPChk = new ClientTCPChk(adresseMachine, port) ;

        // demarrage du client et communication avec le serveur
        clientTCPChk.demarrer();
    }
}
