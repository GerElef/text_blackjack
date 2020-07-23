package com.example.textblackjack.blackjack;

public class BlackJack31 extends BlackJack {

    public BlackJack31(){
        super(31, 3);
    }

    public boolean startGame(){
        /*When starting a round, give two cards to each player. Afterwards, check if the dealer
         * has a BlackJack (ace & 10-J-Q-K) for instant win, and if so, end the round.
         * Returns true if game started and did not end because of a start-of-the-round check.*/
        for(int i = 0; i<2;i++){
            this.addCardToDealer();
            this.hitPlayer();
        }
        return true;
    }

    public boolean hitDealer(){
        /*Dealer will only play if:
         * - Player has 14 and he doesn't or
         * - He doesn't have 31 and
         * - His hand value is less than that of the player's (and he doesn't have 14)
         * - if his hand is equal to the players, and below a safe amount(23 in this case)
         * If all these are false, or if dealer is above cardlimit, return false
         * (Returns true if dealer can play again, false if he can't)*/
        if ((!this.getDealerHas14() && !this.getDealerHas31()) && this.getPlayerHas14()){
            addCardToDealer();
        }
        else if ((this.getDealerHand() < this.getPlayerHand()) && !this.getDealerHas14()){
            addCardToDealer();
        }
        else if (this.getDealerHand() == this.getPlayerHand()
                && this.getDealerHand() < 23){
            addCardToDealer();
        }

        return (this.getDealerHand() <= this.getCARDLIMIT()
                && (((!this.getDealerHas14() && !this.getDealerHas31()) && this.getPlayerHas14())
                || ((this.getDealerHand() < this.getPlayerHand()) && !this.getDealerHas14())
                || (this.getDealerHand() == this.getPlayerHand()
                && this.getDealerHand() < 23)));
    }

    public void switchPlayerDealerState(){
        /*When this function is called, play as the dealer, and then end the round.
        * Check hitDealer for detailed explanation of the if statement*/
        while(hitDealer());
        endRound();
    }

    public void handleEndOfRoundPotBank(){
        if (this.getDealerHas31()){
            this.setPlayerBank(this.getBank() - this.getCurrentPot());
        }
        else if (this.getPlayerHas31()){
            this.setPlayerBank(this.getBank() + this.getCurrentPot());
        }
        else if (this.getDealerHas14()){
            this.setPlayerBank(this.getBank() - this.getCurrentPot());
        }
        else if (this.getPlayerHas14()){
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
            if (this.getDealerHas31()){
                this.setPlayerBank(this.getBank() - this.getCurrentPot());
            }
            else if (this.getPlayerHasSplit31()){
                this.setPlayerBank(this.getBank() + this.getCurrentPot());
            }
            else if (this.getDealerHas14()){
                this.setPlayerBank(this.getBank() - this.getCurrentPot());
            }
            else if (this.getPlayerHasSplit14()){
                this.setPlayerBank(this.getBank() + this.getCurrentPot());
            }
            else if (this.getPlayerSecondHand() > this.getCARDLIMIT()){
                this.setPlayerBank(this.getBank() - this.getCurrentPot());
            }
            else if (this.getDealerHand() > this.getCARDLIMIT()){
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