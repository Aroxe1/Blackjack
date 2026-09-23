package fr.univartois.butinfo.qdev.blackjack;

/**
 * Issue d'une partie pour un joueur.
 */
public enum Result {
    WIN("a gagné !"),
    LOSE("a perdu !"),
    PUSH("fait égalité.");

    private final String message;

    private Result(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
