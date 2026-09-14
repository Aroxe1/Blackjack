package fr.univartois.butinfo.qdev.blackjack;

public class Main {
    public static void main(String[] args){
        BlackjackGame game = new BlackjackGame();
        game.addPlayer("Alice");

        game.addPlayer("Youssef");
        game.play();
    }
}
