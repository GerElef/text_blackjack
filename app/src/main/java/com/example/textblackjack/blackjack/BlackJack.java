package com.example.textblackjack.blackjack;

import android.content.Context;
import android.os.CountDownTimer;

import androidx.room.Room;

import com.example.textblackjack.blackjack.deck.Deck;
import com.example.textblackjack.db.MatchHistoryDatabase;
import com.example.textblackjack.db.UserHistory;
import com.example.textblackjack.db.UserSessionBank;
import com.example.textblackjack.db.callable.InsertDatabaseRowsCallable;
import com.example.textblackjack.db.callable.ReturnDatabaseLatestBankCallable;
import com.example.textblackjack.user.Dealer;
import com.example.textblackjack.user.Player;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public abstract class BlackJack implements Serializable {
    /* DB Stuff Declarations */
    private volatile MatchHistoryDatabase db;
    private String timestampstart;
    private int bankstart;

    private final SimpleDateFormat DATEFORMAT;

    private Player player;
    private Dealer dealer;
    private Deck deck;

    private int     betsum;
    private int     CARDLIMIT;

    private boolean splitcards;
    private boolean splitrowdone;
    private boolean firstmove;

    BlackJack(int cardlimit, int blackjackcount){
        this.player = new Player(cardlimit, blackjackcount);
        this.dealer = new Dealer(cardlimit, blackjackcount);
        this.deck   = new Deck();

        this.CARDLIMIT = cardlimit;
        this.DATEFORMAT = new SimpleDateFormat("(yyyy-MM-dd) - HH:mm:ss", Locale.getDefault());
        this.timestampstart = getCurrentTime();
        //bankstart is initialized when startThreadInitUserBank is called

        resetPot();
        resetSplitState();
        resetFirstMove();
    }

    int getCARDLIMIT(){
        return this.CARDLIMIT;
    }

    public int getBank(){
        return this.player.getBank();
    }

    public int getPlayerHand(){
        return this.player.getCurrentHandValue();
    }

    public int getDealerHand(){
        return this.dealer.getCurrentHandValue();
    }

    public int getPlayerSecondHand(){
        return player.getCurrentSplitHandValue();
    }

    public int getCurrentPot(){
        return this.betsum;
    }

    void addCardToDealer(){
        this.dealer.addCardToHand(this.deck.pullCard());
    }

    void endRound(){
        /*When ending the round, handle the player bank and current pot, start a new 5s async timer
         * that resets both the player's and dealer's hands. At the end, reset the pot and saved
         * split state.*/
        handleEndOfRoundPotBank();

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {/* do nothing */}

            public void onFinish() {
                player.resetAllHands();
                dealer.resetHand();
            }
        }.start();

        resetPot();
        resetSplitState();
        resetFirstMove();
    }

    void setPlayerBank(int sum){
        this.player.setBank(sum);
    }

    public void doubleBet(){
        /*When the function starts, double the current pot and add the last card to his hand.
        * If the player goes over the limit, end the round, otherwise make the dealer play.*/
        doublePot();
        if (!this.player.addCardToHand(this.deck.pullCard())){
            endRound();
        }else{
            switchPlayerDealerState();
        }
    }

    public void addBet(int amount){
        this.betsum += amount;
    }

    public void subBet(int amount){
        if ((this.betsum + amount) <= 0){
            this.betsum = 0;
        }else{
            this.betsum += amount;
        }
    }

    public void finishPlayerSplit(){
        this.splitrowdone = true;
    }

    public void resetPot(){
        this.betsum = 0;
    }

    public void setFirstMoveDone(){
        this.firstmove = false;
    }

    private void doublePot(){
        //Bit shift 1 slot, equal to multiplying with 2.
        this.betsum = this.betsum << 1;
    }

    private void resetSplitState(){
        this.splitcards   = false;
        this.splitrowdone = false;
    }

    private void resetFirstMove(){
        this.firstmove = true;
    }

    public void startThreadSavePlayingSession(Context applicationcontext){
        /*Function that starts a thread to insert the current gamestate in the game.*/
        this.db = Room.databaseBuilder(applicationcontext,
                MatchHistoryDatabase.class, "match-history-database").build();

        String currentdatetime = getCurrentTime();

        UserHistory timestamp = new UserHistory(this.timestampstart, currentdatetime, this.CARDLIMIT);
        UserSessionBank bankstamp = new UserSessionBank(this.timestampstart, this.CARDLIMIT, bankstart, getBank());

        InsertDatabaseRowsCallable insertbankcallable = new InsertDatabaseRowsCallable(this.db.userDao(), timestamp, bankstamp);
        FutureTask<Boolean> insertBankTask = new FutureTask<>(insertbankcallable);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(insertBankTask);

        this.db.close();

        this.timestampstart = currentdatetime;
        this.bankstart = getBank();
    }

    public void startThreadInitUserBank(Context applicationcontext){
        /*Function that starts a thread to initialize and get the latest bank from the DB.*/
        this.db = Room.databaseBuilder(applicationcontext,
                MatchHistoryDatabase.class, "match-history-database").build();

        ReturnDatabaseLatestBankCallable latestbankcallable = new ReturnDatabaseLatestBankCallable(this.db.userDao(), this.CARDLIMIT);
        FutureTask<Integer> latestBankTask = new FutureTask<>(latestbankcallable);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(latestBankTask);

        int loadedbank = 0;

        try{
            loadedbank = latestBankTask.get();
            this.setPlayerBank(loadedbank);
            this.bankstart = loadedbank;
        }catch(InterruptedException | ExecutionException ex){
            ex.printStackTrace();
            this.setPlayerBank(loadedbank);
        }

        this.db.close();
    }

    public boolean hitPlayer(){
        /*If the player is not at or over the limit, check if he has split his cards. If he's not
         * done with the split row, hit that row with a new card, and if he's over the limit at that
         * row, end his split. If the player has not split and is playing normally, hit the default
         * row, and if he's over the limit, end the round.
         * If the player is over or at the limit, make the dealer play.
         * Function returns true if the player was hit and did not go over the limit successfully,
         * or if he's at the limit. Returns false otherwise */
        if (this.player.getCurrentHandValue() < player.getLIMIT()){

            if (playerHasSplit() && !playerDoneSplitRow()){
                if (!this.player.addCardToSplitHand(this.deck.pullCard())){
                    finishPlayerSplit();
                }
            }else{
                if (!this.player.addCardToHand(this.deck.pullCard())){
                    endRound();
                    return false;
                }
                if (this.player.getCurrentHandValue() == this.getCARDLIMIT()){
                    switchPlayerDealerState();
                    return false;
                }
            }
            return true;
        }else{
            switchPlayerDealerState();
            return false;
        }
    }

    public boolean splitCards(){
        /*If the player has doubles, he can split and have a split row, while doubling his bet.
         * Default value for the new row is the integer division of his hand
         * (even / even numbers = int) */
        if (this.playerCanSplit()){
            this.splitcards = true;
            int dblcard = (int) this.player.getCurrentHandValue()/2;
            this.player.addCardToSplitHand(dblcard);
            this.player.setCurrentHandValue(dblcard);
            this.player.setCurrentHandCount(player.getCurrentHandCount() - 1);
            //splithandcount is appended in addcardtosecondhand() method so no need to update it here
        }
        return this.splitcards;
    }

    public boolean playerCanSplit(){
        return this.player.hasDoubles() && getFirstMove();
    }

    public boolean playerHasSplit(){
        return this.splitcards;
    }

    public boolean playerDoneSplitRow(){return this.splitrowdone;}

    private boolean getFirstMove(){
        return this.firstmove;
    }

    /*Blackjack 21 specific functions*/
    boolean getPlayerHasBlackjack(){
        return this.player.hasBlackjack();
    }

    boolean getDealerHasBlackjack(){
        return this.dealer.hasBlackjack();
    }

    boolean getPlayerHasSplitBlackjack(){
        return this.player.hasSplitBlackjack();
    }

    /*Blackjack 31 specific functions*/
    boolean getPlayerHas31(){
        return this.player.has31();
    }

    boolean getPlayerHas14(){
        return this.player.has14();
    }

    boolean getPlayerHasSplit31(){
        return this.player.hasSplit31();
    }

    boolean getPlayerHasSplit14(){
        return this.player.hasSplit14();
    }

    boolean getDealerHas31(){
        return this.dealer.has31();
    }

    boolean getDealerHas14(){
        return this.dealer.has14();
    }

    @NotNull
    private String getCurrentTime(){
        return this.DATEFORMAT.format(new Date());
    }

    /*Contracts all children must fulfill*/
    public abstract boolean hitDealer();

    public abstract boolean startGame();

    public abstract void switchPlayerDealerState();

    public abstract void handleEndOfRoundPotBank();
}
