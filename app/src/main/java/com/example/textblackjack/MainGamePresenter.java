package com.example.textblackjack;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.textblackjack.blackjack.BlackJack;

import org.jetbrains.annotations.NotNull;

class MainGamePresenter {

    private BlackJack game;

    private TextView textViewUserPot;
    private TextView textViewPlayerScore;
    private TextView textViewDealerScore;
    private TextView textViewPlayerBank;
    private TextView textViewPlayerScoreSplit;

    private Button[] PlayButtons;
    private Button[] BetButtons;

    private Button   btnSplit;

    private Context applicationcontext;

    MainGamePresenter(BlackJack game, TextView textViewDealerScore, TextView textViewUserPot, TextView textViewPlayerScore, TextView textViewPlayerScoreSplit, TextView textViewPlayerBank, Button btnSplit, Button[] BetButtons, Button[] PlayButtons, Context applicationcontext){
        this.applicationcontext = applicationcontext;

        this.game        = game;

        this.PlayButtons = PlayButtons;
        this.BetButtons  = BetButtons;

        this.btnSplit = btnSplit;

        this.textViewDealerScore      = textViewDealerScore;
        this.textViewUserPot          = textViewUserPot;
        this.textViewPlayerScore      = textViewPlayerScore;
        this.textViewPlayerScoreSplit = textViewPlayerScoreSplit;
        this.textViewPlayerBank       = textViewPlayerBank;

        initPlayerBank();
    }

    boolean startRound(){
        /* When starting a round, hide the Split, and update the bank textview.
        * Afterwards, call the Model function to official start the new round,
        * and if the player can split, show him the button to be able to do so.
        * At the end, update the player scores accordingly.
        * Returns true if gameround started successfully, and did not finish from a
        * start-of-the-round check. Returns false otherwise.*/
        this.textViewPlayerScoreSplit.setVisibility(View.INVISIBLE);

        this.textViewPlayerBank.setText(Integer.toString(game.getBank()));

        boolean res = this.game.startGame();

        updatePlayerDealerUI();

        return res;
    }

    boolean hitPlayer(){
        /* When hitting the player, if the player has lost by going over the limit,
        * return true (that he has lost), so the new timer can begin.
        * Afterwards, update textviews accordingly. */
        boolean flag = false;

        if (!this.game.hitPlayer()){
            flag = true;
        }

        this.game.setFirstMoveDone();

        updatePlayerDealerUI();
        this.textViewPlayerScoreSplit.setText(Integer.toString(this.game.getPlayerSecondHand()));

        return flag;
    }

    boolean stayPlayer(){
        /*If the player has split his cards, and he isn't done, finish the player split row,
        * stopping him from hitting the second hand, and return to hitting the default one.
        * Afterwards, update textviews accordingly.
        * Returns true if the round has ended, false if it's ongoing. */
        boolean rounddone = false;

        if (this.game.playerHasSplit() && !this.game.playerDoneSplitRow()){
            this.game.finishPlayerSplit();
        }else{
            this.game.switchPlayerDealerState();
            rounddone = true;
        }

        updatePlayerDealerUI();
        this.textViewPlayerBank.setText(Integer.toString(this.game.getBank()));
        return rounddone;
    }

    void splitCards(){
        /*If the player chooses to split the cards, perform check and set the split score textview
        * visible and remove the button to split again.*/
        if (this.game.splitCards()){
            this.textViewPlayerScoreSplit.setVisibility(View.VISIBLE);
            this.btnSplit.setVisibility(View.INVISIBLE);
            updatePlayerDealerUI();
        }
    }

    void Bet(int amount){
        //Handles betting, making it sure it doesn't go into the minus. Updates the pot accordingly.
        if (amount >= 0){
            this.game.addBet(amount);
        }else{
            this.game.subBet(amount);
        }

        this.textViewUserPot.setText(Integer.toString(this.game.getCurrentPot()));
    }

    void doubleBet(){
        //Doubles the current pot and updates textviews accordingly.
        this.game.doubleBet();

        updatePlayerDealerUI();
        this.textViewPlayerBank.setText(Integer.toString(this.game.getBank()));
    }

    void resetPot(){
        //Resets the current pot to 0, and updates the textview accordingly.
        this.game.resetPot();
        this.textViewUserPot.setText("0");
    }

    void turnOffPlayButtons(){ turnOffButtons(this.PlayButtons); }

    void turnOnPlayButtons(){ turnOnButtons(this.PlayButtons); }

    void turnOffBetButtons(){ turnOffButtons(this.BetButtons); }

    void turnOnBetButtons(){ turnOnButtons(this.BetButtons); }

    void saveSession(){
        //acts as a wrapper method for the model
        this.game.startThreadSavePlayingSession(this.applicationcontext);
    }

    private void initPlayerBank(){
        this.game.startThreadInitUserBank(this.applicationcontext);
        this.textViewPlayerBank.setText(Integer.toString(game.getBank()));
    }

    private void turnOffButtons(@NotNull Button[] btnarray){
        //Turns off a specified array of buttons
        this.btnSplit.setVisibility(View.INVISIBLE);

        for(Button thisbtn : btnarray){
            thisbtn.setVisibility(View.INVISIBLE);
        }
    }

    private void turnOnButtons(@NotNull Button[] btnarray){
        //Turns on a specified array of buttons.
        for(Button thisbtn : btnarray){
            thisbtn.setVisibility(View.VISIBLE);
        }
    }

    private void updatePlayerDealerUI(){
        //Function created in order to reduce repeating too much code.
        if (this.game.playerCanSplit()){
            this.btnSplit.setVisibility(View.VISIBLE);
        }else{
            this.btnSplit.setVisibility(View.INVISIBLE);
        }

        this.textViewPlayerScore.setText(Integer.toString(this.game.getPlayerHand()));
        this.textViewDealerScore.setText(Integer.toString(this.game.getDealerHand()));
        this.textViewPlayerBank.setText(Integer.toString( this.game.getBank()));
    }
}
