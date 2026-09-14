package fr.univartois.butinfo.qdev.blackjack;

public class Card {
    private boolean isFaceUp;
    private Value value;
    private Suit suit;

    public Card(Value value, Suit suit){
        this.value = value;
        this.suit = suit;
        this.isFaceUp = false;
    }

    public Value getValue(){
        return value;
    }

    public int getNumericalValue(){
        return value.getNumericalValue();
    }

    public Suit getSuit(){
        return suit;
    }

    public void flip(){
        this.isFaceUp = !this.isFaceUp;
    }

    @Override
    public String toString(){
        return value.name() + suit.getSymbol();
    }


}
