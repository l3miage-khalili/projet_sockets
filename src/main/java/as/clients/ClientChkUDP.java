package as.clients;

import as.utilitaires.ClientUDPRequest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientChkUDP implements ClientUDPRequest {
    // Socket UDP sur un port choisi par le système
    private DatagramSocket socket ;

    // objet Java permettant de recevoir un datagramme UDP
    private DatagramPacket reception ;

    // Machine à laquelle on veut envoyer des messages
    private InetAddress destination ;

    // port de destination
    private int portDestination ;

    // tampon pour recevoir les données des datagrammes UDP
    private final byte[] tampon ;

    public ClientChkUDP(DatagramSocket socket,
                        DatagramPacket reception,
                        InetAddress destination,
                        int portDestination,
                        final byte[] tampon)
    {
        this.socket = socket;
        this.reception = reception;
        this.destination = destination;
        this.portDestination = portDestination ;
        this.tampon = tampon ;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public DatagramPacket getReception() {
        return reception;
    }

    public InetAddress getDestination() {
        return destination;
    }

    public int getPortDestination() {
        return portDestination;
    }

    public byte[] getTampon() {
        return tampon;
    }

    public static void main(String[] args) throws Exception {
        int tailleTampon = 1024 ;
        String adresseMachine = "localhost" ;
        int portDestination = 28414 ;

        // Création d'un socket UDP sur un port choisi par le système
        DatagramSocket socket = new DatagramSocket();

        // tampon pour recevoir les données des datagrammes UDP
        final byte[] tampon = new byte[tailleTampon];

        DatagramPacket reception = new DatagramPacket(tampon, tampon.length);

        // On veut envoyer les messages à la même machine
        InetAddress destination = InetAddress.getByName(adresseMachine);

        ClientChkUDP clientChkUDP = new ClientChkUDP(socket, reception, destination, portDestination, tampon) ;

        // Envoi des requêtes au serveur
        clientChkUDP.envoyerRequete(null);
    }

    @Override
    public void envoyerRequete(String requete) throws IOException {
        // Scanner sur System.in
        Scanner scanner = new Scanner(System.in);

        String chaine = "";
        System.out.println("Client checker UDP : EN COURS");
        System.out.println("Veuillez faire votre check ou Tapez FIN pour arrêter :");

        while(!chaine.equalsIgnoreCase("FIN")) {
            // lecture clavier
            chaine = scanner.nextLine();

            // on récupère un tableau des octets de la chaîne
            byte [] octetsChaine = chaine.getBytes();

            // objet Java permettant d'envoyer un datagramme UDP vers la machine destination et le port de destination spécifié
            DatagramPacket emission = new DatagramPacket(octetsChaine, octetsChaine.length, destination, portDestination);

            // on envoie le datagramme UDP
            socket.send(emission);

            // attente et réception d'un datagramme UDP
            socket.receive(reception);

            // extraction des données
            String chaine2 = new String(reception.getData(), 0, reception.getLength());

            System.out.println("Chaine reçue : "+chaine2);

            // on replace la taille du tampon au max
            // elle a été modifiée lors de la réception
            reception.setLength(tampon.length);
        }
    }

}
