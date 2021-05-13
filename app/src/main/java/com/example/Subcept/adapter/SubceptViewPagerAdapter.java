package com.example.Subcept.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.Subcept.R;
import com.example.Subcept.model.survey_Data;

import java.util.List;

public class SubceptViewPagerAdapter extends PagerAdapter {

    Context context;
    List<survey_Data> survey_data_list;

    public SubceptViewPagerAdapter(Context context, List<survey_Data> quiz_data_list){
        this.context = context;
        this.survey_data_list = quiz_data_list;
    }

    @Override
    public int getCount(){
        return survey_data_list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object){
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){

        View view = LayoutInflater.from(context).inflate(R.layout.subcept_quiz_screen_layout, null);
        ImageView imageView;
        TextView title, desc;

        imageView = view.findViewById(R.id.imageView);
        title = view.findViewById(R.id.survey_title);
        desc = view.findViewById(R.id.survey_desc);

       imageView.setImageResource(survey_data_list.get(position).getImageURL());
       title.setText(survey_data_list.get(position).getTitle());
       desc.setText(survey_data_list.get(position).getDesc());

        container.addView(view);

        return view;

    }
}
