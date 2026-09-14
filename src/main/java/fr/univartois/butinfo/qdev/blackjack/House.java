package fr.univartois.butinfo.qdev.blackjack;


public class House extends CardOwner{

    public House(){
        super("House");
    }

    public boolean isHitting(){
        return getHandValue() < 17;
    }

    public void flipFirstCard(){
        if (!hand.isEmpty()){
            hand.get(0).flip();
        }
    }

}
