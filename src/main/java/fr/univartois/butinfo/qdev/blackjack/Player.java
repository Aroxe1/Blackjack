package fr.univartois.butinfo.qdev.blackjack;

import java.util.Scanner;

public class Player extends CardOwner{

    private Result result;

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

    /**
     * Résultat du joueur, ou {@code null} tant que la partie n'est pas terminée.
     */
    public Result getResult(){
        return result;
    }

    public void win(){
        result = Result.WIN;
        System.out.println(name + " a gagné !");
    }

    public void push(){
        result = Result.PUSH;
        System.out.println(name + " fait égalité.");
    }

    public void lose(){
        result = Result.LOSE;
        System.out.println(name + " a perdu !");
    }

    public void bust(){
        System.out.println(name + " a dépassé 21 !");
    }

}
