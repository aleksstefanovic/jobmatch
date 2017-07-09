package com.triosstudent.aleks.jobmatch.responses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.triosstudent.aleks.jobmatch.R;
import com.triosstudent.aleks.jobmatch.questionnaires.QuestionnaireCard;

import java.util.ArrayList;

public class ResponseCardAdapter extends BaseAdapter {
    ArrayList list;
    LayoutInflater inflater;

    public ResponseCardAdapter(ArrayList l, Context c) {
        this.list = l;
        this.inflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return this.list.size();
    }

    public long getItemId (int index){
        return index;
    }

    public ResponseCard getItem (int index) {
        return (ResponseCard) list.get(index);
    }

    private class ViewHolder {
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
        TextView txtFourth;
    }

    public View getView(int pos, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.responsecard, parent, false);
            TextView q1 = (TextView) view.findViewById(R.id.q1);
            TextView q2 = (TextView) view.findViewById(R.id.q2);
            TextView q3 = (TextView) view.findViewById(R.id.q3);
            TextView q4 = (TextView) view.findViewById(R.id.q4);

            holder = new ViewHolder();
            holder.txtFirst =  q1;
            holder.txtSecond = q2;
            holder.txtThird = q3;
            holder.txtFourth = q4;
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtFirst.setText(((ResponseCard) list.get(pos)).getResponse1());
        holder.txtSecond.setText(((ResponseCard) list.get(pos)).getResponse2());
        holder.txtThird.setText(((ResponseCard) list.get(pos)).getResponse3());
        holder.txtFourth.setText(((ResponseCard) list.get(pos)).getResponse4());
        return view;
    }
}
