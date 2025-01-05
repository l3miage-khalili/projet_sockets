package as.utilitaires;

public class ParserMessage {
    private ListeAuth listeAuth ;

    public ParserMessage(ListeAuth listeAuth) {
        this.listeAuth = listeAuth;
    }

    public String parser(String chaine, boolean estManager) {
        String bad = "BAD" ;
        String good = "GOOD" ;

        // decryptage et analyse du message du client
        String[] partiesMessage = chaine.split(" ") ;
        switch (partiesMessage[0]){
            case "CHK" :
            case "chk" :
                // On vérifie la syntaxe de la requête reçue par le client
                if(partiesMessage.length != 3){
                    return bad ; // on envoie la chaine BAD
                }
                if(listeAuth.tester(partiesMessage[1], partiesMessage[2])){
                    // la paire existe, on envoie GOOD
                    return good ;
                }
                else{
                    // la paire n'existe pas, on envoie BAD
                    return bad ;
                }
                case "ADD" :
                case "add" :
                    if(estManager){
                        if (listeAuth.creer(partiesMessage[1], partiesMessage[2])){
                            // Ajout réussi, on envoie GOOD
                            return good ;
                        }
                        else{
                            // Ajout échoué, on envoie BAD
                            return bad ;
                        }
                    }
                    else{
                        // client non manager ne peut pas effectuer l'opération Add, on envoie donc BAD
                        return bad ;
                    }
                case "DEL" :
                case "del" :
                    if(estManager){
                        if (listeAuth.supprimer(partiesMessage[1], partiesMessage[2])){
                            // Suppression réussie, on envoie GOOD
                            return good ;
                        }
                        else{
                            // La suppression a échoué, on envoie BAD
                            return bad ;
                        }
                    }
                    else{
                        // client non manager ne peut pas effectuer l'opération DEL, on envoie donc BAD
                        return bad ;
                    }
                case "MOD" :
                case "mod" :
                    if(estManager){
                        if (listeAuth.mettreAJour(partiesMessage[1], partiesMessage[2])){
                            // Mise à jour réussie, on envoie GOOD
                            return good ;
                        }
                        else{
                            // La modification a échoué, on envoie BAD
                            return bad ;
                        }
                    }
                    else{
                        // client non manager ne peut pas effectuer l'opération MOD, on envoie donc BAD
                        return bad ;
                    }
            default:
                // opération inconnue, on envoie la chaine BAD
                return bad ;
        }
    }
}
