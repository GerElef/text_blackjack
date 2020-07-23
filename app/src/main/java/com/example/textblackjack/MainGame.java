package com.example.textblackjack;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.textblackjack.blackjack.BlackJack;

public class MainGame extends AppCompatActivity{

    private MainGamePresenter presenter;

    /*------------ All Screen Objects Declarations Here -------------- */

    private TextView textViewPlayerScore;
    private TextView textViewPlayerScoreSplit;
    private TextView textViewDealerScore;
    private TextView textViewPlayerBank;
    private TextView textViewUserPot;
    private TextView textViewCountdown;

    private Button btnAdd5K;
    private Button btnAdd500;
    private Button btnResetPot;
    private Button btnRemove500;
    private Button btnRemove5K;

    private Button btnHit;
    private Button btnStay;
    private Button btnDouble;
    private Button btnSplit;
    private Button btnForfeit;

    private boolean dbsavedflag;

    /*------------ All Screen Objects Declarations Here -------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maingame);

        /*------------ All Screen Objects Initializations Here -------------- */

        this.textViewDealerScore        = findViewById(R.id.textViewDealerScore);
        this.textViewUserPot            = findViewById(R.id.textViewUserPot);
        this.textViewPlayerScore        = findViewById(R.id.textViewPlayerScore);
        this.textViewPlayerScoreSplit   = findViewById(R.id.textViewPlayerScoreSplit);
        this.textViewCountdown          = findViewById(R.id.textViewCountdown);
        this.textViewPlayerBank         = findViewById(R.id.textViewPlayerBank);

        this.btnAdd5K     = findViewById(R.id.btnAdd5K);
        this.btnAdd500    = findViewById(R.id.btnAdd500);
        this.btnResetPot  = findViewById(R.id.btnResetPot);
        this.btnRemove500 = findViewById(R.id.btnRemove500);
        this.btnRemove5K  = findViewById(R.id.btnRemove5K);

        this.btnHit      = findViewById(R.id.btnHit);
        this.btnStay     = findViewById(R.id.btnStay);
        this.btnDouble   = findViewById(R.id.btnDouble);
        this.btnSplit    = findViewById(R.id.btnSplit);
        this.btnForfeit  = findViewById(R.id.btnForfeit);

        this.presenter = new MainGamePresenter((BlackJack) getIntent().getSerializableExtra("game"),
                this.textViewDealerScore,
                this.textViewUserPot,
                this.textViewPlayerScore,
                this.textViewPlayerScoreSplit,
                this.textViewPlayerBank,
                this.btnSplit,
                new Button[] {btnAdd5K, btnAdd500, btnResetPot, btnRemove5K, btnRemove500},
                new Button[] {btnHit, btnStay, btnDouble, btnForfeit},
                getApplicationContext());

        /*------------ All Screen Objects Initializations Here -------------- */

        startTimer();
        setupListeners();
    }

    public void setupListeners(){
        /*------------ All Listener Initializations Here -------------- */

        this.btnAdd5K.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.Bet(5000);
            }
        });

        this.btnAdd500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.Bet(500);
            }
        });

        this.btnResetPot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.resetPot();
            }
        });

        this.btnRemove500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.Bet(-500);
            }
        });

        this.btnRemove5K.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.Bet(-5000);
            }
        });

        this.btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter.hitPlayer()){
                    //if player has lost by going over the limit and lost, start timer starting new round
                    startTimer();
                }
            }
        });

        this.btnStay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(presenter.stayPlayer()){
                    //if player has stayed on his main hand(not the second hand), start timer
                    startTimer();
                }
            }
        });

        this.btnDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.doubleBet();
                startTimer();
            }
        });


        this.btnForfeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exits the Activity and goes back to the Activity that called MainGame
                endGame();
            }
        });

        this.btnSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.splitCards();
            }
        });
        /*------------ All Listener Initializations Here -------------- */
    }

    public void startTimer(){
        //At the start of each round, turn on only the bet buttons and the countdown timer
        this.presenter.turnOffPlayButtons();
        this.presenter.turnOnBetButtons();
        this.textViewUserPot.setText("0");

        this.textViewCountdown.setVisibility(View.VISIBLE);

        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                String str = millisUntilFinished / 1000 + "";
                textViewCountdown.setText(str);
            }

            public void onFinish() {
                /*if the round started successfully, turn off betting and start the new round,
                * otherwise, start a new round (new timer)*/
                if(!presenter.startRound()){
                    startTimer();
                }else{
                    textViewCountdown.setVisibility(View.INVISIBLE);
                    presenter.turnOffBetButtons();
                    presenter.turnOnPlayButtons();
                }
            }
        }.start();
    }

    @Override
    public void onStart(){
        //resets the db check flag that saves into the db
        this.dbsavedflag = false;
        super.onStart();
    }

    @Override
    public void onStop(){
        //sets the flag to true, and saves into the db
        this.dbsavedflag = true;
        this.presenter.saveSession();
        super.onStop();
    }

    @Override
    public void onDestroy(){
        //if the flag isnt true, saves into the db and sets it to true
        if (!this.dbsavedflag){
            this.dbsavedflag = true;
            this.presenter.saveSession();
        }
        super.onDestroy();
    }

    public void endGame(){
        super.finish();
    }
}
