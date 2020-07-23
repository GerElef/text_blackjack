package com.example.textblackjack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.textblackjack.blackjack.BlackJack21;
import com.example.textblackjack.blackjack.BlackJack31;

/*Application Launcher Activity*/
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnplaymain    = findViewById(R.id.btnPlayMain);
        final Button btnplayalt     = findViewById(R.id.btnPlayAlt);
        final Button btnviewhistory = findViewById(R.id.btnViewHistory);

        btnplaymain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Sets up Intent and passes as extra the Serializable model the next activity will use*/
                BlackJack21 game = new BlackJack21();

                Intent blackjack21mainintent = new Intent(btnplaymain.getContext(), MainGame.class);
                blackjack21mainintent.putExtra("game", game);
                startActivity(blackjack21mainintent);
            }
        });

        btnplayalt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                /*Sets up Intent and passes as extra the Serializable model the next activity will use*/
                BlackJack31 game = new BlackJack31();

                Intent blackjack31mainintent = new Intent(btnplayalt.getContext(), MainGame.class);
                blackjack31mainintent.putExtra("game", game);
                startActivity(blackjack31mainintent);
            }
        });

        btnviewhistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                /*Sets up Intent*/
                Intent matchhistoryintent = new Intent(btnviewhistory.getContext(), MatchHistory.class);
                startActivity(matchhistoryintent);
            }
        });

    }

}
