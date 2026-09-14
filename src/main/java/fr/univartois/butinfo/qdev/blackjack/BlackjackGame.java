package fr.univartois.butinfo.qdev.blackjack;

import java.util.ArrayList;
import java.util.List;

public class BlackjackGame{
    private Deck deck;
    private House house;
    private List<Player> players;

    public BlackjackGame() {
        this.deck = Deck.create52CardsDeck();
        this.house = new House();
        this.players = new ArrayList<>();
    }

    public void addPlayer(String name){
        players.add(new Player(name));
    }

    public void play(){

    }

}
