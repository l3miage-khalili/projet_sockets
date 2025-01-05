package as.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientChkTCP extends ClientTCP {

    public ClientChkTCP(Socket sc, BufferedReader entreeSocket, PrintStream sortieSocket) {
        super(sc,entreeSocket,sortieSocket);
    }

    public static void main(String[] args) throws Exception {
        int port = 28414 ;
        String adresseMachine = "localhost" ;

        // Création d'un socket client et connexion avec un serveur fonctionnant sur la même machine et sur le port spécifié
        Socket sc = new Socket(adresseMachine, port);

        // Construction d'un BufferedReader pour lire du texte envoyé à travers la connexion socket
        BufferedReader entreeSocket = new BufferedReader(new InputStreamReader(sc.getInputStream()));

        // Construction d'un PrintStream pour envoyer du texte à travers la connexion socket
        PrintStream sortieSocket = new PrintStream(sc.getOutputStream());

        ClientChkTCP clientChkTCP = new ClientChkTCP(sc, entreeSocket, sortieSocket) ;

        // Envoi des requêtes au serveur
        clientChkTCP.envoyerRequete();
    }

    @Override
    public void envoyerRequete() throws IOException {
        // Scanner sur System.in
        Scanner scanner = new Scanner(System.in);

        String chaine = "";

        System.out.println("Veuillez faire votre check ou Tapez FIN pour arrêter :");

        while(!chaine.equalsIgnoreCase("FIN")) {
            // lecture clavier
            chaine = scanner.nextLine();
            sortieSocket.println(chaine); // on envoie la chaine au serveur

            // lecture d'une chaine envoyée à travers la connexion socket
            String chaine2 = entreeSocket.readLine();
            System.out.println("Chaine reçue : "+chaine2);
        }

        // on ferme nous aussi la connexion
        sc.close();
    }
}
