package com.example.textblackjack.user;

public class Player extends BlackJackUser {

    private int bank;

    private int splitcardvalue;
    private int splitcardcount;
    private int splitcardace;

    public Player(int cardlimit, int blackjackcount){
        super(cardlimit,blackjackcount);

        resetAllHands();
        this.bank = 0;
    }

    private boolean useSplitAce(){
        /*Use SplitAce. Returns true if there's at least 1 split ace available and deincrements the
        * split ace var.*/
        boolean flag = (this.splitcardace > 0);
        if (flag){
            this.splitcardace--;
        }
        return flag;
    }

    private void setSplitAce(boolean bool){
        /*Increments the ace counter if the player has an ace in hand.*/
        if (bool){
            this.splitcardace++;
        }else{
            this.splitcardace = 0;
        }
    }

    private boolean hasSplitAce(){
        return (this.splitcardace > 0);
    }

    public boolean addCardToSplitHand(int val){
        /*Function that handles adding cards to current split hand.
         * If the value is above 10, make it 10, and add the hand value, increment the count.
         * Afterwards, if it's an Ace, make sure to update the hand to have an Ace, and add +10 value
         * to it (because aces can have 1 or 11 value, with 11 being the default). If the hand value
         * is above the current limit, subtract 10 hand has an ace.
         * Function returns true if hand is below < 21 (the user hasn't lost.)*/
        if (val > 10){ // 11-13 are J-Q-K Cards
            val = 10;
        }

        this.splitcardvalue += val;
        this.splitcardcount++;

        if(val == 1){
            setSplitAce(true);
            this.splitcardvalue += 10;
        }

        if ((this.splitcardvalue > getLIMIT()) && useSplitAce()){
            this.splitcardvalue -= 10;
            return true;
        }

        return !(this.splitcardvalue > getLIMIT());
    }

    public void resetAllHands(){
        //Resets the hand vars inherited from BlackJackUser abstract class and the local vars too.
        resetHand();
        this.splitcardvalue = 0;
        this.splitcardcount = 0;
        this.splitcardace   = 0;

    }

    public boolean hasSplitBlackjack() {
        //Returns true if the split row has a Blackjack (Blackjack rules explained in BlackJackUser)
        return (hasSplitAce() && (getCurrentSplitHandCount() == getBLACKJACK_COUNT()) && getCurrentSplitHandValue() == getLIMIT());
    }

    public boolean hasSplit31(){
        return this.splitcardvalue == this.getLIMIT();
    }

    public boolean hasSplit14(){
        return this.splitcardvalue == 14;
    }

    public void setBank(int amount){
        this.bank = amount;
    }

    public int getBank(){
        return this.bank;
    }

    public int getCurrentSplitHandValue(){
        return this.splitcardvalue;
    }

    private int getCurrentSplitHandCount(){
        return this.splitcardcount;
    }
}
