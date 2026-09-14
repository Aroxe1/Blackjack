package fr.univartois.butinfo.qdev.blackjack;

public abstract class CardOwner {
    private String name;

    protected CardOwner(String name){
        this.name = name;
    }

    public abstract boolean isHitting();

    public void receive(Card card){

    }

    public int getHandValue(){

        return 0;
    }

    public boolean isBusted(){
        return false;
    }

    @Override
    public String toString(){
        return "";
    }
}
