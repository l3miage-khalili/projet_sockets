package logs;

import java.io.*;
import java.net.Socket;
import java.util.Date;

import javax.json.*;

/**
 * Classe Singleton qui permet de logger des requêtes vers un serveur de log sur le port 3244 de la machine locale
 *
 * @author torguet
 *
 */
public class JsonLogger {

    // JsonWriter pour envoyer du Json à travers la connexion socket
    private JsonWriter sortieSocketJson ;

    // JsonReader pour lire du json envoyé à travers la connexion socket
    private JsonReader entreeSocketJson ;

    /**
     * Constructeur à compléter
     */
    private JsonLogger() {
        try {
            // Attributs à compléter
            String adreseeMachine = "localhost";
            int port = 3244;
            // Socket client
            Socket socket = new Socket(adreseeMachine, port);
            this.sortieSocketJson = Json.createWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.entreeSocketJson = Json.createReader(new InputStreamReader(socket.getInputStream())) ;
        } catch (Exception e) {
            System.err.println("Erreur d'instanciation du client JsonLogger : " + e.getMessage());
        }
    }

    /**
     * Transforme une requête en Json
     *
     * @param host machine client
     * @param port port sur la machine client
     * @param proto protocole de transport utilisé
     * @param type type de la requête
     * @param login login utilisé
     * @param result résultat de l'opération
     * @return un objet Json correspondant à la requête
     */
    private JsonObject reqToJson(String host, int port, String proto, String type, String login, String result) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("host", host)
                .add("port", port)
                .add("proto", proto)
                .add("type", type)
                .add("login", login)
                .add("result", result)
                .add("date", new Date().toString());

        return builder.build();
    }

    /**
     *  singleton
     */
    private static JsonLogger logger = null;

    /**
     * récupération du logger qui est créé si nécessaire
     *
     * @return le logger
     */
    public static JsonLogger getLogger() {
        if (logger == null) {
            logger = new JsonLogger();
        }
        return logger;
    }

    /**
     * méthode pour logger
     *
     * @param host machine client
     * @param port port sur la machine client
     * @param proto protocole de transport utilisé
     * @param type type de la requête
     * @param login login utilisé
     * @param result résultat de l'opération
     */
    public void log(String host, int port, String proto, String type, String login, String result) {
        JsonLogger logger = getLogger();
        // à compléter
        try {
            // transformation de la requête en Json
            JsonObject jsonRequete = logger.reqToJson(host, port, proto, type, login, result) ;

            // on envoie la requête sous format Json au serveur
            sortieSocketJson.writeObject(jsonRequete);
            System.out.println("Requête Json envoyée au serveur L : " + jsonRequete);

            // lecture d'une réponse envoyée à travers la connexion socket
            JsonObject retourServ = entreeSocketJson.readObject() ;
            System.out.println("réponse du serveur L : " + retourServ);

        } catch (Exception e) {
            System.err.println("Erreur de log vers le serveur L : " + e.getMessage());
        }

    }

}
