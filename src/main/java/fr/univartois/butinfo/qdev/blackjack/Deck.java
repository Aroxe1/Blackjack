package fr.univartois.butinfo.qdev.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cards;

    private Deck(){
        this.cards = new ArrayList<>();
    }

    public static Deck create52CardsDeck(){
        Deck deck = new Deck();
        for(Suit suit : Suit.values()){
            for(Value value : Value.values()){
                deck.cards.add(new Card(value, suit));
            }
        }
        return deck;
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public void add(Card card){
        if(card == null){
            throw new IllegalArgumentException("La carte ne peut pas être nulle");
        }
        if(cards.contains(card)){
            throw new IllegalArgumentException("La carte existe déjà");
        }
        cards.add(card);
    }

    public Card withdraw(){
        if(cards.isEmpty()){
            throw new IllegalStateException("Le paquet est vide");
        }
        return cards.remove(cards.size() - 1);
    }

}
