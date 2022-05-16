package com.example.quizappp;

import static com.example.quizappp.DbQuery.g_catList;
import static com.example.quizappp.DbQuery.g_quesList;
import static com.example.quizappp.DbQuery.g_selected_cat_index;
import static com.example.quizappp.DbQuery.g_selected_test_index;
import static com.example.quizappp.DbQuery.g_testList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView questionsView;
    private TextView tvQuesid,timertv,catnametv;
    private Button submitB,markB,clearSelB;
    private ImageButton prevQuesB,nextquesB;
    private ImageView quesListB;
    private int quesId;
    QuestionsAdapter quesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        init();
         quesAdapter=new QuestionsAdapter(g_quesList);
        questionsView.setAdapter(quesAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionsView.setLayoutManager(layoutManager);

        setSnapHelper();

        setClickListener();
        startTimer();
    }
    private void init()
    {
        questionsView = findViewById(R.id.questions_view);
        tvQuesid=findViewById(R.id.tv_questionID);
        timertv=findViewById(R.id.tv_timer);
        catnametv=findViewById(R.id.qa_catname);
        submitB=findViewById(R.id.submit);
        markB=findViewById(R.id.clear_selB);
        prevQuesB=findViewById(R.id.prev_quesB);
        nextquesB=findViewById(R.id.next_quesB);
        quesListB=findViewById(R.id.ques_list_gridB);
        quesId=0;
        tvQuesid.setText("1/"+String.valueOf(g_quesList.size()));
        catnametv.setText(g_catList.get(g_selected_cat_index).getName());
    }
    private void setSnapHelper()
    {
        SnapHelper snapHelper =new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionsView);

        questionsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View view =snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesId=recyclerView.getLayoutManager().getPosition(view);

                tvQuesid.setText(String.valueOf(quesId+1)+"/"+String.valueOf(g_quesList.size()));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
    private void setClickListener(){
        prevQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(quesId>0)
            {
                questionsView.smoothScrollToPosition(quesId - 1);
            }
            }
        });
        nextquesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quesId < g_quesList.size() - 1)
                {
                    questionsView.smoothScrollToPosition(quesId + 1);
                }
            }
        });
        clearSelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               g_quesList.get(quesId).setSelectedAns(-1);//deselect
                quesAdapter.notifyDataSetChanged();
            }
        });
    }
    private void startTimer()
    {
        long totaltime=g_testList.get(g_selected_test_index).getTime()*60*1000;
        CountDownTimer timer= new CountDownTimer(totaltime + 1000,1000) {
            @Override
            public void onTick(long remainingTime) {
                String time=String.format("%02d:%02d min",TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime)-
                               TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                );

            timertv.setText(time);

            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }
}