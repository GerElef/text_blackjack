package com.example.textblackjack.user;

public class Dealer extends BlackJackUser {

    public Dealer(int cardlimit, int blackjackcount){
        super(cardlimit,blackjackcount);

        this.resetHand();
    }
}
