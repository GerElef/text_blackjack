package com.example.textblackjack.blackjack;

public class BlackJack21 extends BlackJack {

    public BlackJack21(){
        super(21, 2);
    }

    public boolean startGame(){
        /*When starting a round, give two cards to each player. Afterwards, check if the dealer
         * has a BlackJack (ace & 10-J-Q-K) for instant win, and if so, end the round.
         * Returns true if game started and did not end because of a start-of-the-round check.
         * Returns false otherwise.*/
        for(int i = 0; i<2;i++){
            this.addCardToDealer();
            this.hitPlayer();
        }

        if (this.getDealerHasBlackjack()){
            endRound();
            return false;
        }

        if (this.getPlayerHasBlackjack()){
            switchPlayerDealerState();
            return false;
        }
        return true;
    }

    public boolean hitDealer(){
        /*If the dealer is below 17 hit him with a card.
         * Returns true if the dealer can play again, false if he can't.*/
        if (this.getDealerHand() < 17){
            this.addCardToDealer();
        }
        return this.getDealerHand() < 17;
    }

    public void switchPlayerDealerState(){
        /*When this function is called, play as the dealer, and then end the round.*/
        while(hitDealer());
        endRound();
    }

    public void handleEndOfRoundPotBank(){
        //dealer blackjack wins everything
        if (this.getDealerHasBlackjack()){
            this.setPlayerBank(this.getBank() - this.getCurrentPot());
        }
        else if (this.getPlayerHasBlackjack()){
            this.setPlayerBank(this.getBank() + this.getCurrentPot());
        }
        else if (this.getPlayerHand() > this.getCARDLIMIT()){
            this.setPlayerBank(this.getBank() - this.getCurrentPot());
        }
        else if (this.getDealerHand() > this.getCARDLIMIT()){
            this.setPlayerBank(this.getBank() + this.getCurrentPot());
        }
        else if (this.getPlayerHand() > this.getDealerHand()){
            this.setPlayerBank(this.getBank() + this.getCurrentPot());
        }
        else if (this.getPlayerHand() < this.getDealerHand()){
            this.setPlayerBank(this.getBank() - this.getCurrentPot());
        }

        if (playerHasSplit()){
            if (this.getDealerHasBlackjack()){
                this.setPlayerBank(this.getBank() - this.getCurrentPot());
            }
            else if (this.getPlayerHasSplitBlackjack()){
                this.setPlayerBank(this.getBank() + this.getCurrentPot());
            }
            else if (this.getPlayerSecondHand() > this.getCARDLIMIT()){
                this.setPlayerBank(this.getBank() - this.getCurrentPot());
            }
            else if (this.getPlayerSecondHand() > this.getCARDLIMIT()){
                this.setPlayerBank(this.getBank() + this.getCurrentPot());
            }
            else if (this.getPlayerSecondHand() > this.getDealerHand()){
                this.setPlayerBank(this.getBank() + this.getCurrentPot());
            }
            else if (this.getPlayerSecondHand() < this.getDealerHand()){
                this.setPlayerBank(this.getBank() - this.getCurrentPot());
            }
        }
    }
}
