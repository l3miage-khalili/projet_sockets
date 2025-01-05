package logs;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class ServeurLog {
    private int port ;
    private int tailleTampon ;
    private ArrayList<String> listeRequetes ;

    public ServeurLog(int port, int tailleTampon) {
        this.port = port;
        this.tailleTampon = tailleTampon;
        listeRequetes = new ArrayList<>() ;
    }

    public static void main(String[] args) throws Exception {
        /* Configuration serveur Log */
        int port = 3244 ;
        int tailleTampon = 1024 ;

        ServeurLog serveurLog = new ServeurLog(port, tailleTampon) ;
        serveurLog.gererLesClients();
    }


    public void gererLesClients() throws Exception {
        System.out.println("Serveur L : EN COURS");

        // Création d'un socket UDP sur le port attribut de la classe
        DatagramSocket socket = new DatagramSocket(port);

        // tampon pour recevoir les données des datagrammes UDP
        final byte[] tampon = new byte[tailleTampon];

        // objet Java permettant de recevoir un datagramme UDP
        DatagramPacket dgram = new DatagramPacket(tampon, tampon.length);

        while(true) {
            // attente et réception d'un datagramme UDP
            socket.receive(dgram);

            // extraction des données
            String chaine = new String(dgram.getData(), 0, dgram.getLength());

            // affichage de la chaine reçue
            System.out.println("Chaine reçue du client AS : "+chaine);

            // Traitement de la chaine
            traitement(chaine) ;
            String retour = "requête ajoutée à l'historique des opérations avec succès" ;
            byte [] repByte = retour.getBytes() ;
            dgram.setData(repByte, 0, repByte.length) ;

            // on renvoie le message au client
            socket.send(dgram);

            // on replace la taille du tampon au max
            // elle a été modifiée lors de la réception
            dgram.setData(tampon);
        }
    }


    private void traitement(String chaine) {
        this.listeRequetes.add(chaine) ;
    }
}
