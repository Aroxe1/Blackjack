package fr.univartois.butinfo.qdev.blackjack;

import java.util.ArrayList;
import java.util.List;

public abstract class CardOwner {
    protected String name;
    protected List<Card> hand;

    protected CardOwner(String name){
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public abstract boolean isHitting();


    public void receive(Card card){
        hand.add(card);
    }

    public int getHandValue(){
        int total = 0;
        for(Card card : hand){
            total += card.getNumericalValue();
        }
        return total;
    }

    public boolean isBusted(){
        return getHandValue() > 21;
    }

    @Override
    public String toString(){
        return name + ": " + hand;
    }
}
