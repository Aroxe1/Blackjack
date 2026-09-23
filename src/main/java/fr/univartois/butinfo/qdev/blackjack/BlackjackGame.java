package fr.univartois.butinfo.qdev.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Déroulement d'une partie de Blackjack.
 * La partie peut être jouée d'un seul coup avec {@link #play()} (version console),
 * ou étape par étape ({@link #deal()}, {@link #hit(CardOwner)}, {@link #playHouse()},
 * {@link #settle()}) pour être pilotée par une interface graphique.
 */
public class BlackjackGame{
    private Deck deck;
    private House house;
    private List<Player> players;

    public BlackjackGame() {
        this.deck = Deck.create52CardsDeck();
        this.deck.shuffle();
        this.house = new House();
        this.players = new ArrayList<>();
    }

    public void addPlayer(String name){
        players.add(new Player(name));
    }

    public House getHouse(){
        return house;
    }

    public List<Player> getPlayers(){
        return Collections.unmodifiableList(players);
    }

    /**
     * Distribue deux cartes à chaque joueur et à la maison.
     * La première carte de la maison reste face cachée jusqu'à son tour.
     */
    public void deal(){
        for(int i=0; i < 2; i++){
            for(Player player : players){
                hit(player);
            }
            Card card = deck.withdraw();
            if(i > 0){
                card.flip();
            }
            house.receive(card);
        }
    }

    /**
     * Donne une carte face visible au joueur (ou à la maison) donné.
     */
    public void hit(CardOwner owner){
        Card card = deck.withdraw();
        card.flip();
        owner.receive(card);
    }

    /**
     * Retourne la carte cachée de la maison, puis la fait tirer tant qu'elle le doit.
     */
    public void playHouse(){
        house.flipFirstCard();
        while(house.isHitting() && !house.isBusted()){
            hit(house);
        }
    }

    /**
     * Compare les mains et attribue le résultat de chaque joueur.
     */
    public void settle(){
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

    public void play(){
        deal();

        for(Player player : players){
            System.out.println(player);
            while(player.isHitting() && !player.isBusted()){
                hit(player);
                System.out.println(player);
            }
        }

        System.out.println(house);
        playHouse();
        System.out.println(house);

        settle();
    }

}
