package as.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientManagerTCP extends ClientTCP {

    public ClientManagerTCP(Socket sc, BufferedReader entreeSocket, PrintStream sortieSocket) {
        super(sc, entreeSocket, sortieSocket);
    }

    public static void main(String[] args) throws Exception {
        int port = 28415 ;
        String adresseMachine = "localhost" ;

        // Création d'un socket client et connexion avec un serveur fonctionnant sur la même machine et sur le port spécifié
        Socket sc = new Socket(adresseMachine, port);

        // Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
        BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(sc.getInputStream()));

        // Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
        PrintStream sortieSocket = new PrintStream(sc.getOutputStream());

        ClientManagerTCP clientManagerTCP = new ClientManagerTCP(sc, entreeSocket, sortieSocket) ;

        // Envoi des requêtes au serveur
        clientManagerTCP.envoyerRequete();
    }

    @Override
    public void envoyerRequete() throws IOException {
        // Scanner sur System.in
        Scanner scanner = new Scanner(System.in);

        String chaine = "";

        System.out.println("Veuillez effectuer une des actions suivantes ou Tapez FIN pour arrêter :");
        System.out.println("Checker   - chk login password");
        System.out.println("Ajouter   - add login password");
        System.out.println("Modifier  - mod login Newpassword");
        System.out.println("Supprimer - del login password");

        while(!chaine.equalsIgnoreCase("FIN")) {
            // lecture clavier
            chaine = scanner.nextLine();

            // on envoie la chaine au serveur
            sortieSocket.println(chaine);

            // lecture d'une chaine envoyée à travers la connexion socket
            String chaine2 = entreeSocket.readLine();

            System.out.println("Chaine reçue : "+chaine2);
        }

        // on ferme nous aussi la connexion
        sc.close();
    }
}
