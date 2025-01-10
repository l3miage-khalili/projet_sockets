package as.clients;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientUDP {
    private String adresseMachine ;
    private int port ;

    public ClientUDP(String adresseMachine, int port) {
        this.adresseMachine = adresseMachine;
        this.port = port ;
    }

    /**
     * demarre le client et communique avec un serveur UDP
     */
    public void demarrer() {
        try {
            // Création d'un socket UDP sur un port choisi par le système
            DatagramSocket socket = new DatagramSocket();

            // communication avec un serveur UDP
            communiquer(socket);
        } catch (Exception e) {
            System.err.println("Erreur de demarrage du client ou de communication avec le serveur : " + e.getMessage());
        }
    }

    /**
     * gère la communication avec un serveur UDP
     * @param socket socket UDP
     */
    public void communiquer(DatagramSocket socket) {
        try {
            // tampon pour recevoir les données des datagrammes UDP
            final byte[] tampon = new byte[1024];

            // objet Java permettant de recevoir un datagramme UDP
            DatagramPacket reception = new DatagramPacket(tampon, tampon.length);

            // Scanner sur System.in
            Scanner scanner = new Scanner(System.in);

            // On envoie les messages à la machine à l'adresse spécifiée
            InetAddress destination = InetAddress.getByName(this.adresseMachine);

            String chaine = "";

            System.out.println("Veuillez faire votre check ou Tapez FIN pour arrêter :");

            while(!chaine.equalsIgnoreCase("FIN")) {
                // lecture clavier
                chaine = scanner.nextLine();

                // on récupère un tableau des octets de la chaîne
                byte [] octetsChaine = chaine.getBytes();

                // objet Java permettant d'envoyer un datagramme UDP vers la machine destination et le port spécifié
                DatagramPacket emission = new DatagramPacket(octetsChaine, octetsChaine.length, destination, this.port);

                // on envoie le datagramme UDP
                socket.send(emission);

                // attente et réception d'un datagramme UDP
                socket.receive(reception);

                // extraction des données
                String retourServ = new String(reception.getData(), 0, reception.getLength());

                System.out.println("Chaine reçue du serveur UDP : " + retourServ);

                // on replace la taille du tampon au max
                // elle a été modifiée lors de la réception
                reception.setLength(tampon.length);
            }
        } catch (Exception e) {
            System.err.println("Erreur de communication avec le serveur : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        /* configuration du client */
        String adresseMachine = "localhost" ;
        int port = 28414 ;

        // Instanciation d'un client
        ClientUDP clientUDP = new ClientUDP(adresseMachine, port) ;

        // demarrage du client et communication avec le serveur
        clientUDP.demarrer();
    }
}
