package as.serveurs;

import as.utilitaires.ListeAuth;

public class ServeurAS {
    /* Attributs en tant que serveur AS */

    // Port des clients "checker"
    private int portChk;

    // Port des clients "manager"
    private int portMng;

    // Le métier utilisé par les serveurs
    private ListeAuth listeAuth;

    /* Attributs en tant que client AS */
    private String adresseMachineLog ;
    private int portLog ;

    public ServeurAS(int portChk, int portMng, ListeAuth listeAuth, String adresseMachineLog, int portLog) {
        this.portChk = portChk;
        this.portMng = portMng;
        this.listeAuth = listeAuth;
        this.adresseMachineLog = adresseMachineLog ;
        this.portLog = portLog ;
    }

    // demarrage d'un serveur TCP pour la gestion des clients TCP et d'un serveur UDP pour ceux d'UDP
    public void demarrer() {
        // démarrage de deux threads pour créer les serveurs TCP et UDP
        new Thread(() -> gererServeurTCP()).start();
        new Thread(() -> gererServeurUDP()).start();
    }

    // création et demarrage du serveur TCP
    private void gererServeurTCP() {
        ServeurTCP serveurTCP = new ServeurTCP(portChk, portMng, listeAuth, adresseMachineLog, portLog) ;
        serveurTCP.demarrer();
    }

    // création et demarrage du serveur UDP
    private void gererServeurUDP() {
        ServeurUDP serveurUDP = new ServeurUDP(portChk, listeAuth) ;
        serveurUDP.demarrer();
    }

    public static void main(String[] args) {
        /* configuration du serveur AS multi-protocole */
        int portChk = 28414 ;
        int portMng = 28415 ;
        ListeAuth listeAuth = new ListeAuth() ;
        String adresseMachineLog = "localhost" ;
        int portLog = 3244 ;

        // Instanciation du serveur AS multi-protocole
        ServeurAS serveurAS = new ServeurAS(portChk, portMng, listeAuth, adresseMachineLog, portLog) ;

        // demarrage du serveur AS et gestion des clients
        serveurAS.demarrer();
    }


}
