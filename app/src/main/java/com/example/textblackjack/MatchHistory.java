package com.example.textblackjack;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.textblackjack.db.MatchHistoryDatabase;
import com.example.textblackjack.db.UserHistoryAndBank;
import com.example.textblackjack.db.callable.ReturnDatabaseAllRowsCallable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MatchHistory extends AppCompatActivity {
    /* DB Stuff */
    private volatile MatchHistoryDatabase db;
    private ExecutorService executor;

    private LinearLayout layoutToBeInflated;

    private Button btnHistory21;
    private Button btnHistory31;
    private TextView textViewHistoryBank;
    private ImageView imageViewBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchhistory);

        this.btnHistory21        = findViewById(R.id.btnHistory21);
        this.btnHistory31        = findViewById(R.id.btnHistory31);
        this.textViewHistoryBank = findViewById(R.id.textViewHistoryBank);
        this.imageViewBtnBack    = findViewById(R.id.imageViewBtnBack);

        this.layoutToBeInflated = findViewById(R.id.LinearLayoutHistory);
        this.executor = Executors.newSingleThreadExecutor();

        this.db = Room.databaseBuilder(getApplicationContext(),
                MatchHistoryDatabase.class, "match-history-database").build();
        this.db.close();

        setupListeners();
    }

    private void setupListeners(){
        this.btnHistory21.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loadHistory(21);
            }
        });

        this.btnHistory31.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loadHistory(31);
            }
        });

        this.imageViewBtnBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                MatchHistory.super.finish();
            }
        });
    }

    void loadHistory(int num){
        /*Loads history for a specified gametype (num). If the database is not open, get an instance
        * of the db. Resets the layout that's going to be inflated, and initializes +
        * instantiates the appropriate Callable and futuretask to setup the query. Afterwards, calls
        * the executor to execute the task. Gets the output, and it's size. In each loop,
        * creates a new inflatable from the layout inflater, then iterates over the
        * entire array, sets the values and passes the view object to the inflater to inflate the
        * layoutToBeInflated ad nauseum until the iteration ends. At the end, sets the bank thingy
        * to the correct value. Closes the db when the function ends. */

        if (!this.db.isOpen()){
            this.db = Room.databaseBuilder(getApplicationContext(),
                    MatchHistoryDatabase.class, "match-history-database").build();
        }

        this.layoutToBeInflated.removeAllViews();

        ReturnDatabaseAllRowsCallable allgamerowscallable = new ReturnDatabaseAllRowsCallable(db.userDao(), num);
        FutureTask<UserHistoryAndBank[]> allgamerowsTask = new FutureTask<>(allgamerowscallable);

        this.executor.execute(allgamerowsTask);

        try{
            UserHistoryAndBank[] userhistoryandbank = allgamerowsTask.get();
            int len = userhistoryandbank.length;
            //This is not a foreach loop for performance reasons when there are thousands of data
            for(int i = 0; i < len; i++){
                View inflatable = getLayoutInflater().inflate(
                        R.layout.match_history_inflater,
                        this.layoutToBeInflated,
                        false);

                TextView timeStart = inflatable.findViewById(R.id.textViewHistoryStart);
                TextView timeEnd   = inflatable.findViewById(R.id.textViewHistoryEnd);
                TextView bankStart = inflatable.findViewById(R.id.textViewBankStart);
                TextView bankEnd   = inflatable.findViewById(R.id.textViewBankEnd);

                timeStart.setText(userhistoryandbank[i].getUserHistory().getTimestampStart());
                timeEnd.setText(userhistoryandbank[i].getUserHistory().getTimestampEnd());
                bankStart.setText(Integer.toString(userhistoryandbank[i].getUserBank().getStartingBalance()));
                bankEnd.setText(Integer.toString(userhistoryandbank[i].getUserBank().getEndingBalance()));

                this.layoutToBeInflated.addView(inflatable);
            }
            if (len > 0){
                this.textViewHistoryBank.setText(Integer.toString(userhistoryandbank[len - 1].getUserBank().getEndingBalance()));
            }
        }catch(InterruptedException | ExecutionException ex){
            ex.printStackTrace();
        }

        this.db.close();
    }
}
