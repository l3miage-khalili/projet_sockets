package as.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public abstract class ClientTCP {
    // Socket client
    Socket sc ;

    // Objet BufferedReader pour lire du texte envoyé à travers la connexion socket
    BufferedReader entreeSocket ;

    // Objet PrintStream pour envoyer du texte à travers la connexion socket
    PrintStream sortieSocket ;

    public ClientTCP(Socket sc, BufferedReader entreeSocket, PrintStream sortieSocket) {
        this.sc = sc;
        this.entreeSocket = entreeSocket;
        this.sortieSocket = sortieSocket;
    }

    public Socket getSc() {
        return sc;
    }

    public BufferedReader getEntreeSocket() {
        return entreeSocket;
    }

    public PrintStream getSortieSocket() {
        return sortieSocket;
    }

    public abstract void envoyerRequete() throws IOException ;

}
