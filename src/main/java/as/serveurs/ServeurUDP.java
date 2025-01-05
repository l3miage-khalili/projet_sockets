package as.serveurs;

import as.clients.ClientChkUDP;
import as.utilitaires.ClientUDPRequest;
import as.utilitaires.ListeAuth;
import as.utilitaires.ParserMessage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServeurUDP implements Runnable, ClientUDPRequest {
    /* Attributs en tant que serveur UDP */
    private int port;

    private int tailleTampon ;

    // Socket UDP sur un port choisi par le système
    private DatagramSocket socket ;

    // tampon pour recevoir les données des datagrammes UDP
    private final byte[] tampon = new byte[tailleTampon];

    private DatagramPacket reception ;

    private ParserMessage parserMessage ;

    /* Attributs en tant que client du serveur Log */
    private ClientChkUDP clientChkUDP ;

    public ServeurUDP(
            int port,
            int tailleTampon,
            DatagramSocket socket,
            DatagramPacket reception,
            ParserMessage parserMessage,
            ClientChkUDP clientChkUDP
    ) {
        this.port = port;
        this.tailleTampon = tailleTampon;
        this.socket = socket;
        this.reception = reception;
        this.parserMessage = parserMessage ;
        this.clientChkUDP = clientChkUDP ;
    }

    public static void main(String[] args) throws Exception {
        int tailleTampon = 1024 ;
        int portChecker = 28414 ;
        ListeAuth listeAuth = new ListeAuth() ;
        ParserMessage parserMessage = new ParserMessage(listeAuth) ;

        int portDestination = 3244 ;
        String adresseMachine = "localhost" ;

        // Création d'un socket UDP sur un port choisi par le système
        DatagramSocket socket = new DatagramSocket();

        // tampon pour recevoir les données des datagrammes UDP
        final byte[] tampon = new byte[tailleTampon];

        DatagramPacket reception = new DatagramPacket(tampon, tampon.length);

        // On veut envoyer les messages à la même machine
        InetAddress destination = InetAddress.getByName(adresseMachine);

        ClientChkUDP clientChkUDP = new ClientChkUDP(socket,reception,destination,portDestination,tampon) ;

        ServeurUDP serveurUDP = new ServeurUDP(
                portChecker,
                tailleTampon,
                socket,
                reception,
//                destination,
//                listeAuth,
                parserMessage,
                clientChkUDP
        ) ;

        serveurUDP.gererLesClients();
    }


    @Override
    public void run() {
        try {
            this.gererLesClients();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void gererLesClients() throws Exception {

        System.out.println("Serveur UDP : EN COURS");
        System.out.println("AS prêt à logger des requêtes vers le serveur L");

        while(true) {
            // attente et réception d'un datagramme UDP
            socket.receive(reception);

            // extraction des données
            String chaine = new String(reception.getData(), 0, reception.getLength());

            // affichage de la chaine reçue
            System.out.println("Chaine reçue du client UDP : "+chaine);

            /* Analyse de la chaine */

            // on fixe le 2nd paramètre estManager à false car
            // le client manager n'utilise que le serveur TCP
            String rep = parserMessage.parser(chaine, false) ;
            byte [] repByte = rep.getBytes() ;
            reception.setData(repByte, 0, repByte.length) ;

            // on renvoie le message au client
            socket.send(reception);

            // On log la requête vers le serveur L
            envoyerRequete(chaine) ;

            // on replace la taille du tampon au max
            // elle a été modifiée lors de la réception
            reception.setData(tampon);
        }
    }

    @Override
    // Log la requête vers le serveur Log
    public void envoyerRequete(String requete) throws IOException {
        // extraction des différentes parties de la requête
        String[] partiesRequete = requete.split(" ") ;

        // contient le texte Json à envoyer au serveur L
        String chaineEnJson ;

        int tailleRequete = partiesRequete.length ;

        if (tailleRequete == 3){
            // construction de la requête sous format Json en dur
            chaineEnJson = "" ;
            chaineEnJson += "{\n" ;
            chaineEnJson += "   type : " + partiesRequete[0] ;
            chaineEnJson += ",\n" ;
            chaineEnJson += "   login : " + partiesRequete[1] ;
            chaineEnJson += ",\n" ;
            chaineEnJson += "   password : " + partiesRequete[2] ;
            chaineEnJson += "\n}" ;
        }
        else {
            chaineEnJson = "" ;
            chaineEnJson += "{ " + requete + " }\n" ;
        }

        // on récupère un tableau des octets de la chaîne
        byte [] octetsChaine = chaineEnJson.getBytes();

        // objet Java permettant d'envoyer un datagramme UDP vers la machine destination et le port de destination spécifié
        DatagramPacket emission = new DatagramPacket(octetsChaine, octetsChaine.length, this.clientChkUDP.getDestination(), this.clientChkUDP.getPortDestination());

        // on envoie le datagramme UDP
        DatagramSocket socket = this.clientChkUDP.getSocket() ;
        socket.send(emission);
        System.out.println("chaine envoyée au serveur L : " + chaineEnJson);

        // attente et réception d'un datagramme UDP
        DatagramPacket reception = this.clientChkUDP.getReception() ;
        socket.receive(reception);

        // extraction des données
        String chaine2 = new String(reception.getData(), 0, reception.getLength());

        System.out.println("Chaine reçue du serveur L : "+chaine2);

        // on replace la taille du tampon au max
        // elle a été modifiée lors de la réception
        reception.setLength(this.tampon.length);
    }

}
