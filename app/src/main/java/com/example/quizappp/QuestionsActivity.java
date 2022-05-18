package com.example.quizappp;

import static com.example.quizappp.DbQuery.ANSWERED;
import static com.example.quizappp.DbQuery.NOT_VISITED;
import static com.example.quizappp.DbQuery.REVIEW;
import static com.example.quizappp.DbQuery.UNANSWERED;
import static com.example.quizappp.DbQuery.g_catList;
import static com.example.quizappp.DbQuery.g_quesList;
import static com.example.quizappp.DbQuery.g_selected_cat_index;
import static com.example.quizappp.DbQuery.g_selected_test_index;
import static com.example.quizappp.DbQuery.g_testList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quizappp.Adapter.QuestionGridAdapter;
import com.example.quizappp.Adapter.QuestionsAdapter;

import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView questionsView;
    private TextView tvQuesid,timertv,catnametv;
    private Button submitB,markB,clearSelB;
    private ImageButton prevQuesB,nextquesB;
    private ImageView quesListB;
    private int quesId;
    QuestionsAdapter quesAdapter;
    private DrawerLayout drawer;
    private ImageButton drawerCloseB;
    private GridView questionListGV;
    private ImageView markImage;
    private QuestionGridAdapter gridAdapter  ;
    private CountDownTimer timer;
    private long timeleft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_list_layout);
        init();
         quesAdapter=new QuestionsAdapter(g_quesList);
        questionsView.setAdapter(quesAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionsView.setLayoutManager(layoutManager);

        gridAdapter=new QuestionGridAdapter(this,g_quesList.size());
        questionListGV.setAdapter(gridAdapter);

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
        drawer=findViewById(R.id.drawer_layout);
        drawerCloseB=findViewById(R.id.drawerCloseB);
        markImage=findViewById(R.id.marked_image);
        questionListGV=findViewById(R.id.question_list_gv);
        quesId=0;
        tvQuesid.setText("1/"+String.valueOf(g_quesList.size()));
        catnametv.setText(g_catList.get(g_selected_cat_index).getName());
        g_quesList.get(0).setStatus(UNANSWERED);
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
                if(g_quesList.get(quesId).getStatus()==NOT_VISITED)//it will check whether the answer is visite or not
                    g_quesList.get(quesId).setStatus(UNANSWERED);
                if(g_quesList.get(quesId).getStatus()==REVIEW)
                {
                    markImage.setVisibility(View.VISIBLE);
                }
                else
                {
                    markImage.setVisibility(View.GONE);
                }

                tvQuesid.setText(String.valueOf(quesId+1)+"/"+String.valueOf(g_quesList.size()));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
    private void setClickListener(){
        prevQuesB.setOnClickListener((view) -> {
            if(quesId>0)
            {
                questionsView.smoothScrollToPosition(quesId - 1);
            }

        });
        nextquesB.setOnClickListener((view) -> {
                if(quesId < g_quesList.size() - 1)
                {
                    questionsView.smoothScrollToPosition(quesId + 1);
                }
        });
//        clearSelB.setOnClickListener((view) -> {
//               g_quesList.get(quesId).setSelectedAns(-1);//deselect
//                g_quesList.get(quesId).setStatus(UNANSWERED);
//                markImage.setVisibility(View.GONE);
//                quesAdapter.notifyDataSetChanged();
//
//
//        });
        quesListB.setOnClickListener((view)-> {
                if(!drawer.isDrawerOpen(GravityCompat.END))
                {
                    gridAdapter.notifyDataSetChanged();
                    drawer.openDrawer(GravityCompat.END);
                }
        });

        drawerCloseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen(GravityCompat.END))
                {
                    drawer.closeDrawer(GravityCompat.END);
                }
            }
        });
        markB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markImage.getVisibility() != View.VISIBLE)//it was not reviewed
                {

                    markImage.setVisibility(View.VISIBLE);
                    g_quesList.get(quesId).setStatus(REVIEW);
                    gridAdapter.notifyDataSetChanged();
                }
                else
                {
                    markImage.setVisibility(View.GONE);
                    if(g_quesList.get(quesId).getSelectedAns() != -1)
                    {
                        g_quesList.get(quesId).setStatus(ANSWERED);
                    }
                    else
                    {
                        g_quesList.get(quesId).setStatus(UNANSWERED);
                    }
                }
            }
        });
        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTest();
            }
        });
    }
    public void submitTest()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(QuestionsActivity.this);
        builder.setCancelable(true);
        View view=getLayoutInflater().inflate(R.layout.alert_dialog_layout,null);
        Button cancelB=view.findViewById(R.id.cancelB);
        Button confirmB=view.findViewById(R.id.confirmB);
        builder.setView(view);
        AlertDialog alertDialog=builder.create();
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        confirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              timer.cancel();
              alertDialog.dismiss();

                Intent intent=new Intent(QuestionsActivity.this,ScoreActivity.class);
                long totaltime=g_testList.get(g_selected_test_index).getTime()*60*1000;
                intent.putExtra("Time taken",totaltime-timeleft);
                startActivity(intent);
                QuestionsActivity.this.finish();
             }
        });
        alertDialog.show();
    }
    public void goToquestion(int position)
    {
        questionsView.smoothScrollToPosition(position);
        if(drawer.isDrawerOpen(GravityCompat.END))
            drawer.closeDrawer(GravityCompat.END);
    }
    private void startTimer()
    {
        long totaltime=g_testList.get(g_selected_test_index).getTime()*60*1000;
        timer= new CountDownTimer(totaltime + 1000,1000) {
            @Override
            public void onTick(long remainingTime) {
                timeleft=remainingTime;
                String time=String.format("%02d:%02d min",TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime)-
                               TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                );

            timertv.setText(time);

            }

            @Override
            public void onFinish() {
                Intent intent=new Intent(QuestionsActivity.this,ScoreActivity.class);
                long totaltime=g_testList.get(g_selected_test_index).getTime()*60*1000;
                intent.putExtra("Time taken",totaltime-timeleft);
                startActivity(intent);
                QuestionsActivity.this.finish();
            }
        };
        timer.start();
    }
}