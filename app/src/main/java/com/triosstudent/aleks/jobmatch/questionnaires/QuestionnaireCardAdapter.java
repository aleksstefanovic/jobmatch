package com.triosstudent.aleks.jobmatch.questionnaires;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.triosstudent.aleks.jobmatch.R;

import java.util.ArrayList;

public class QuestionnaireCardAdapter extends BaseAdapter {
    ArrayList list;
    LayoutInflater inflater;

    public QuestionnaireCardAdapter (ArrayList l, Context c) {
        this.list = l;
        this.inflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return this.list.size();
    }

    public long getItemId (int index){
        return index;
    }

    public QuestionnaireCard getItem (int index) {
        return (QuestionnaireCard) list.get(index);
    }

    private class ViewHolder {
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
        TextView txtFourth;
        TextView txtFifth;
    }

    public View getView(int pos, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.questionnairecard, parent, false);
            TextView q1 = (TextView) view.findViewById(R.id.q1);
            TextView q2 = (TextView) view.findViewById(R.id.q2);
            TextView q3 = (TextView) view.findViewById(R.id.q3);
            TextView q4 = (TextView) view.findViewById(R.id.q4);
            TextView title = (TextView) view.findViewById(R.id.title);
            holder = new ViewHolder();
            holder.txtFirst =  q1;
            holder.txtSecond = q2;
            holder.txtThird = q3;
            holder.txtFourth = q4;
            holder.txtFifth = title;
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtFirst.setText(((QuestionnaireCard) list.get(pos)).getQuestion1());
        holder.txtSecond.setText(((QuestionnaireCard) list.get(pos)).getQuestion2());
        holder.txtThird.setText(((QuestionnaireCard) list.get(pos)).getQuestion3());
        holder.txtFourth.setText(((QuestionnaireCard) list.get(pos)).getQuestion4());
        holder.txtFifth.setText(((QuestionnaireCard) list.get(pos)).getTitle());
        return view;
    }
}
