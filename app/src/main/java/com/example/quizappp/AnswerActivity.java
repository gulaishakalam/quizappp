package com.example.quizappp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.example.quizappp.Adapter.AnswersAdapter;

public class AnswerActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView answersView;
    private ImageView backB2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
 //       toolbar=findViewById(R.id.aa_toolbar);
         answersView=findViewById(R.id.aa_recycler_view);
        backB2=findViewById(R.id.backB2);
//        setActionBar(toolbar);
//      setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//
//        getSupportActionBar().setTitle("ANSWERS");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        backB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(AnswerActivity.this,ScoreActivity.class);
                startActivity(intent);
                AnswerActivity.this.finish();
            }
        });

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        answersView.setLayoutManager(layoutManager);
        AnswersAdapter adapter=new AnswersAdapter(DbQuery.g_quesList);
        answersView.setAdapter(adapter);


    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            AnswerActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}