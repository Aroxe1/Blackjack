package fr.univartois.butinfo.qdev.blackjack;

import java.util.Scanner;

public class Player extends CardOwner{

    public Player(String name){
        super(name);
    }

    public boolean isHitting(){
        if(isBusted()){
            return false;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println(name + " Voulez vous tirer une carte ? (o/n)");
        String response = scanner.nextLine();
        return response.equalsIgnoreCase("o");

    }

    public void win(){
        System.out.println(name + " a gagné !");
    }

    public void push(){
        System.out.println(name + " fait égalité.");
    }

    public void lose(){
        System.out.println(name + " a perdu !");
    }

    public void bust(){
        System.out.println(name + " a dépassé 21 !");
    }

}
