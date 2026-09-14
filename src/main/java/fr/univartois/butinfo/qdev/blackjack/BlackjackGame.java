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
        for(int i=0; i < 2; i++){
            for(Player player : players){
                player.receive(deck.withdraw());
            }
            house.receive(deck.withdraw());
        }

        house.flipFirstCard();

        for(Player player : players){
            System.out.println(player);
            while(player.isHitting() && !player.isBusted()){
                player.receive(deck.withdraw());
                System.out.println(player);
            }
        }

        System.out.println(house);
        while(house.isHitting() && !house.isBusted()){
            house.receive(deck.withdraw());
            System.out.println(house);
        }

        for(Player player : players){
            if(player.isBusted()){
                player.bust();
                player.lose();
            }
            else if (house.isBusted()){
                player.win();
            }
            else if (player.getHandValue() > house.getHandValue()){
                player.win();
            }
            else if (player.getHandValue() < house.getHandValue()){
                player.lose();
            }
            else{
                player.push();
            }
        }





    }

}
