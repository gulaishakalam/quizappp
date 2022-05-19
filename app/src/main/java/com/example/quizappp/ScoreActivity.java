package com.example.quizappp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {
    private TextView scoretv,timetv,totaltv,correctqtv,wrongqtv,unattemptedqtv;
   private  Button reattemptb,viewansb;
    private ImageView backB1;
   private long timetaken;
   private Dialog progressDialog;
   private Toolbar toolbar;
   private TextView dialogText;
   private int finalscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);


//        toolbar=findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setTitle("Result");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new Dialog(ScoreActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");
        progressDialog.show();

        init();

        loadData();

        viewansb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        backB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(ScoreActivity.this,MainActivity.class);
                startActivity(intent);
                ScoreActivity.this.finish();
            }
        });
       reattemptb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reattempt();
            }
        });
        saveResult();

    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    private void init()
    {
        scoretv=findViewById(R.id.score);
        timetv=findViewById(R.id.time);
        totaltv=findViewById(R.id.totalques);
        correctqtv=findViewById(R.id.correct);
        wrongqtv=findViewById(R.id.wrong);
        unattemptedqtv=findViewById(R.id.unattempted);
        backB1=findViewById(R.id.backB1);

        reattemptb=findViewById(R.id.reattempt);
       viewansb=findViewById(R.id.view_ans);
    }
    private void loadData()
    {
        int correctq=0,wrongq=0,unattempt=0;
        for(int i=0;i<DbQuery.g_quesList.size();i++)
        {
            if(DbQuery.g_quesList.get(i).getSelectedAns()==-1)
            {
                unattempt++;
            }
            else
            {
                if(DbQuery.g_quesList.get(i).getSelectedAns()==DbQuery.g_quesList.get(i).getCorrectAns())
                {
                    correctq++;
                }
                else
                {
                    wrongq++;
                }
            }
        }
        correctqtv.setText(String.valueOf(correctq));
        wrongqtv.setText(String.valueOf(wrongq));
        unattemptedqtv.setText(String.valueOf(unattempt));
        totaltv.setText(String.valueOf(DbQuery.g_quesList.size()));
        finalscore=(correctq*100)/DbQuery.g_quesList.size();
        scoretv.setText(String.valueOf(finalscore));

        timetaken=getIntent().getLongExtra("TIME TAKEN",0);
        String time=String.format("%02d:%02d min", TimeUnit.MILLISECONDS.toMinutes(timetaken),
                TimeUnit.MILLISECONDS.toSeconds(timetaken)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timetaken))
        );
        timetv.setText(time);

    }
    private void reattempt()
    {
       for(int i=0;i<DbQuery.g_quesList.size();i++)
       {
           DbQuery.g_quesList.get(i).setSelectedAns(-1);
           DbQuery.g_quesList.get(i).setStatus(DbQuery.NOT_VISITED);
       }
       Intent intent=new Intent(ScoreActivity.this,StartTestActivity.class);
       startActivity(intent);
    }
    private void saveResult()
    {
        DbQuery.saveResult(finalscore, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(ScoreActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            ScoreActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}