package com.example.textblackjack.blackjack.deck;

import java.io.Serializable;
import java.security.SecureRandom;

public class Deck implements Serializable {

    private SecureRandom random;

    private int[] deck;
    private int   cardcount;

    public Deck(){
        this.random = new SecureRandom();

        this.cardcount = 51;
        initializeDeck();
    }

    private void initializeDeck(){
        /*Initializes deck as a hashmap (for the constant get() and put() speed and adds 4 for each
         * colour to string keys 1 to 13 (Ace(1) to King(13)).*/
        this.cardcount  = 51;
        this.deck       = new int[52];

        int[] cards = new int[13];
        int i;
        int gen;

        for(i = 0; i < 13; i++){ cards[i] = 0; } // initializes the count

        for(i = 0; i < 52; i++){
            gen = this.random.nextInt(13); //generate integers from 0 to 12 (10-11-12 are JQK)

            while (cards[gen] == 4){ // do while loop until there's a card whose count we haven't finished
                gen = this.random.nextInt(13);
            }

            //range is 1 to 13 inclusive now and JQK are +1, this was made to play nice with the values
            this.deck[i] = 1 + gen;
        }

    }

    public int pullCard(){
        /*Returns an integer card from 1 to 13. Uses SecureRandom import for good %. If deck has
         *only 1 card remaining(0 index), re-initialize the deck.*/
        if (this.cardcount == 0){
            initializeDeck();
        }

        this.cardcount--;

        return this.deck[cardcount];
    }

    public int getRemainingCardCount(){
        return this.cardcount;
    }
}
