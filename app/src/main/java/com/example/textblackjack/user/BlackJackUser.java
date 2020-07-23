package com.example.textblackjack.user;

import java.io.Serializable;

abstract class BlackJackUser implements Serializable {

    private int LIMIT;
    private int BLACKJACK_COUNT;

    private int currenthandvalue;
    private int currenthandcount;
    private int ace;
    private int prevcard;

    private boolean doubles;

    BlackJackUser(int limit, int blackjackcount){
        this.BLACKJACK_COUNT = blackjackcount;
        this.LIMIT = limit;
    }

    private boolean useAce(){
        /*Use Ace. Returns true if there's at least 1 ace available and deincrements the ace var.*/
        boolean flag = (this.ace > 0);
        if (flag){
            this.ace--;
        }
        return flag;
    }

    private boolean hasAce(){
        return (this.ace > 0);
    }

    private void setAce(boolean bool){
        /*Increments the ace counter if the player has an ace in hand.*/
        if (bool){
            this.ace++;
        }else{
            this.ace = 0;
        }
    }

    public boolean hasDoubles(){
        return this.doubles;
    }

    public int getCurrentHandCount(){
        return this.currenthandcount;
    }

    public void setCurrentHandCount(int val){
        this.currenthandcount = val;
    }

    public int getLIMIT(){
        return this.LIMIT;
    }

    public int getCurrentHandValue(){
        return this.currenthandvalue;
    }

    public void setCurrentHandValue(int val){
        this.currenthandvalue = val;
    }

    public boolean addCardToHand(int val){
        /*Function that handles adding cards to current hand. If the card count is < 2 and there's
        * a second same value card, make doubles true, so the player can split if he chooses so.
        * If the value is above 10, make it 10, and add the hand value, increment the count.
        * Afterwards, if it's an Ace, make sure to update the hand to have an Ace, and add +10 value
        * to it (because aces can have 1 or 11 value, with 11 being the default). If the hand value
        * is above the current limit, subtract 10 hand has an ace.
        * Function returns true if hand is <= LIMIT (the user hasn't lost.)*/
        if (this.currenthandcount < 2){
            if (val == this.prevcard){
                this.doubles = true;
            }
            this.prevcard = val;
        }

        if (val > 10){ // 11-13 are J-Q-K Cards
            val = 10;
        }

        this.currenthandvalue += val;
        this.currenthandcount++;

        if(val == 1){
            setAce(true);
            this.currenthandvalue += 10;
        }

        if ((this.currenthandvalue > this.LIMIT) && useAce()){
            this.currenthandvalue -= 10;
            return true;
        }

        return (this.currenthandvalue <= this.LIMIT);
    }

    public boolean hasBlackjack(){
        //User has blackjack if he has an ace and a 10-J-Q-K card only.
        return (hasAce() && (getCurrentHandCount() == this.BLACKJACK_COUNT) && getCurrentHandValue() == getLIMIT());
    }

    public boolean has31(){
        return this.currenthandvalue == this.LIMIT;
    }

    public boolean has14(){
        return this.currenthandvalue == 14;
    }

    public void resetHand(){
        this.currenthandvalue = 0;
        this.currenthandcount = 0;
        this.ace              = 0;
        this.prevcard         = 0;
        this.doubles          = false;
    }

    int getBLACKJACK_COUNT(){
        return this.BLACKJACK_COUNT;
    }
}
