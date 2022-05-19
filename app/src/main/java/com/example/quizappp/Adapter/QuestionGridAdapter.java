package com.example.quizappp.Adapter;

import static com.example.quizappp.DbQuery.ANSWERED;
import static com.example.quizappp.DbQuery.NOT_VISITED;
/*import static com.example.quizappp.DbQuery.REVIEW;*/
import static com.example.quizappp.DbQuery.UNANSWERED;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.quizappp.DbQuery;
import com.example.quizappp.QuestionsActivity;
import com.example.quizappp.R;

public class QuestionGridAdapter extends BaseAdapter {
        private int numofques;
        private Context context;
        public QuestionGridAdapter(Context context,int numofques)
        {
            this.numofques=numofques;
            this.context=context;
        }
        @Override
        public int getCount() {
            return numofques;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View myview;
            if(view == null)
            {
                myview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ques_grid_item,viewGroup,false);
            }
            else
            {
                myview=view;
            }
            myview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                      if(context instanceof QuestionsActivity)
                          ((QuestionsActivity)context).goToquestion(i);
                }
            });
            TextView questv=myview.findViewById(R.id.ques_num);
            questv.setText(String.valueOf(i+1));
            switch (DbQuery.g_quesList.get(i).getStatus())
            {
                case NOT_VISITED:
                    questv.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(),R.color.grey)));
                    break;
                case UNANSWERED:
                    questv.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(),R.color.red)));
                    break;
                case ANSWERED:
                    questv.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(),R.color.green)));
                    break;
               /*case REVIEW:
                    questv.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(),R.color.pink)));
                    break;*/
                default:
                    break;

            }
            return myview;
        }
    }


