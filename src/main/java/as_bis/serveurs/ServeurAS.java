package as_bis.serveurs;

import as_bis.utilitaires.ListeAuth;

public class ServeurAS {
    /* Attributs du serveur AS */
    // Port des clients "checker"
    private int portChk;

    // Port des clients "manager"
    private int portMng;

    // Le métier utilisé par les serveurs
    private ListeAuth listeAuth;

    public ServeurAS(int portChk, int portMng, ListeAuth listeAuth) {
        this.portChk = portChk;
        this.portMng = portMng;
        this.listeAuth = listeAuth;
    }

    // demarrage d'un serveur TCP pour la gestion des clients TCP et d'un serveur UDP pour ceux d'UDP
    public void demarrer() {
        // Démarrage de deux threads pour créer les serveurs TCP et UDP
        new Thread(() -> gererServeurTCP()).start();
        new Thread(() -> gererServeurUDP()).start();
    }

    // création et demarrage du serveur TCP
    private void gererServeurTCP() {
        ServeurTCP serveurTCP = new ServeurTCP(this.portChk, this.portMng, this.listeAuth) ;
        serveurTCP.demarrer();
    }

    // création et demarrage du serveur UDP
    private void gererServeurUDP() {
        ServeurUDP serveurUDP = new ServeurUDP(this.portChk, this.listeAuth) ;
        serveurUDP.demarrer();
    }

    public static void main(String[] args) {
        /* configuration du serveur AS multi-protocole */
        int portChk = 28414 ;
        int portMng = 28415 ;
        ListeAuth listeAuth = new ListeAuth() ;

        // Instanciation du serveur AS multi-protocole
        ServeurAS serveurAS = new ServeurAS(portChk, portMng, listeAuth) ;

        // demarrage du serveur AS et gestion des clients
        serveurAS.demarrer();
    }


}
