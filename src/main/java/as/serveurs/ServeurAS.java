package as.serveurs;

import as.clients.ClientChkUDP;
import as.utilitaires.ListeAuth;
import as.utilitaires.ParserMessage;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServeurAS {
    /* Attributs en tant que serveur AS */
    private int tailleTampon ;

    private int portChecker;

    private int portManager;

    // joue le rôle du metier
    private ListeAuth listeAuth;

    // utilise le metier pour traiter les requêtes du client
    private ParserMessage parserMessage ;

    // Socket UDP sur un port choisi par le système
    private DatagramSocket socket ;

    // tampon pour recevoir les données des datagrammes UDP
    private final byte[] tampon = new byte[tailleTampon];

    private DatagramPacket reception ;

    // On veut envoyer les messages à la même machine
    private InetAddress destination ;

    /* Attributs en tant que client du serveur Log */
    private ClientChkUDP clientChkUDP ;

    public ServeurAS(int portChecker,
                     int portManager,
                     int tailleTampon,
                     ListeAuth listeAuth,
                     ParserMessage parserMessage,
                     DatagramSocket socket,
                     DatagramPacket reception,
                     ClientChkUDP clientChkUDP
    ) {
        this.portChecker = portChecker;
        this.portManager = portManager;
        this.tailleTampon = tailleTampon;
        this.listeAuth = listeAuth;
        this.parserMessage = parserMessage;
        this.socket = socket ;
        this.reception = reception ;
        this.clientChkUDP = clientChkUDP ;
    }

    public static void main(String[] args) throws Exception {
        int tailleTampon = 1024 ;
        String adresseMachine = "localhost" ;

        /* Configuration serveur AS */
        int portChecker = 28414 ;
        int portManager = 28415 ;
        ListeAuth listeAuth = new ListeAuth() ;
        ParserMessage parserMessage = new ParserMessage(listeAuth) ;

        /* Configuration client AS */
        int portDestination = 3244 ;

        /* Configuration UDP */

        // Création d'un socket UDP sur un port choisi par le système
        DatagramSocket socket = new DatagramSocket();

        // tampon pour recevoir les données des datagrammes UDP
        final byte[] tampon = new byte[tailleTampon];

        DatagramPacket reception = new DatagramPacket(tampon, tampon.length);

        // On veut envoyer les messages à la même machine
        InetAddress destination = InetAddress.getByName(adresseMachine);

        ClientChkUDP clientChkUDP = new ClientChkUDP(socket, reception, destination, portDestination, tampon) ;

        ServeurAS serveurAS = new ServeurAS(
                portChecker,
                portManager,
                tailleTampon,
                listeAuth,
                parserMessage,
                socket,
                reception,
                clientChkUDP
        ) ;

        serveurAS.creerServeurs();
    }

    public void creerServeurs() throws Exception {
        // Gestion des clients sur le serveur TCP
        ServeurTCP serveurTCP = new ServeurTCP(portChecker, portManager, listeAuth) ;

        Thread gererClientsTCP = new Thread(serveurTCP) ;
        gererClientsTCP.start();

        // Gestion des clients sur le serveur UDP
        ServeurUDP serveurUDP = new ServeurUDP(
                portChecker,
                tailleTampon,
                socket,
                reception,
                parserMessage,
                clientChkUDP
        ) ;

        serveurUDP.gererLesClients();
    }

}
