package as_bis.serveurs;

import as_bis.utilitaires.AnalyseurRequete;
import as_bis.utilitaires.ListeAuth;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServeurUDP {
    private int port ;

    // Le métier utilisé pour traiter les requêtes clients
    private ListeAuth listeAuth ;

    // objet utilisant le métier pour analyser les requêtes clients
    private AnalyseurRequete analyseurRequete ;

    public ServeurUDP(int port, ListeAuth listeAuth) {
        this.port = port;
        this.listeAuth = listeAuth ;
        this.analyseurRequete = new AnalyseurRequete(this.listeAuth) ;
    }

    public void demarrer() {
        try {
            // Création d'un socket UDP sur le port du serveur
            DatagramSocket socket = new DatagramSocket(this.port);
            System.out.println("Serveur UDP demarré sur le port " + this.port);
            gererClients(socket) ;
        } catch (Exception e) {
            System.err.println("Erreur de demarrage du serveur UDP ou lors de gestion d'un client : " + e.getMessage());
        }
    }

    private void gererClients(DatagramSocket socket) {
        try {
            int tailleTampon = 1024 ;
            // tampon pour recevoir les données des datagrammes UDP
            final byte[] tampon = new byte[tailleTampon];

            // objet Java permettant de recevoir un datagramme UDP
            DatagramPacket reception = new DatagramPacket(tampon, tampon.length);

            while(true) {
                // attente et réception d'un datagramme UDP
                socket.receive(reception);

                // extraction des données
                String chaine = new String(reception.getData(), 0, reception.getLength());

                System.out.println("Chaine reçue du client UDP : " + chaine);

                // ce serveur gère uniquement des clients UDP checker
                boolean estManager = false ;

                // analyse de requête du client
                String retourMetier = analyseurRequete.analyser(chaine, estManager) ;
                byte[] retourMetierBytes = retourMetier.getBytes() ;
                reception.setData(retourMetierBytes, 0, retourMetierBytes.length);

                // on renvoie le retour au client
                socket.send(reception);

                // on replace la taille du tampon au max
                // elle a été modifiée lors de la réception
                reception.setData(tampon);
                //reception.setLength(tailleTampon);
            }
        } catch (Exception e) {
            System.err.println("Erreur de gestion de clients : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        /* configuration du serveur */
        int port = 28414 ;
        ListeAuth listeAuth = new ListeAuth() ;

        // Instanciation du serveur
        ServeurUDP serveurUDP = new ServeurUDP(port, listeAuth) ;

        // demarrage du serveur et gestion des clients
        serveurUDP.demarrer();
    }
}
