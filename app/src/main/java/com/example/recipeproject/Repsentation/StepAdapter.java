package com.example.recipeproject.Repsentation;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;


import com.example.recipeproject.R;
import com.example.recipeproject.listener.SelectListener;
import com.example.recipeproject.model.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StepAdapter  extends RecyclerView.Adapter<StepAdapter.MyView> {
    private ArrayList<Step> list;
    private Context mContext;
    private Activity mActivity;
    private SelectListener listener;


    public class MyView extends  RecyclerView.ViewHolder {
        TextView stepNumber;
        TextView stepText;
        ImageView stepImg;
        ConstraintLayout constraintLayout;
        public MyView(View view){
            super(view);
            stepNumber = view.findViewById(R.id.detail_step_number);
            stepText = view.findViewById(R.id.detail_step_text);
            constraintLayout = view.findViewById(R.id.step_constraint_layout);
            //stepImg = view.findViewById(R.id.detail_step_img);
        }


    }
    public StepAdapter( ArrayList<Step> horizontalist,Context mContext,Activity mActivity){
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.list = horizontalist;
    }
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent,
                                     int viewType)
    {

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_detail_step,
                        parent,
                        false);
        // return itemView
        return new MyView(itemView);
    }
    @Override
    public void onBindViewHolder(final MyView holder,
                                 int position)
    {
        Step step = list.get(position);
        holder.stepNumber.setText(String.valueOf(position+1));
        holder.stepText.setText(step.getText());
        String img = step.getImage();
        if(img != null && !img.isEmpty()){
            float dpRatio = mContext.getResources().getDisplayMetrics().density;
            ConstraintSet set = new ConstraintSet();
            ImageView imageView = new ImageView(mContext);
            imageView.setId(View.generateViewId());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                    (int) (200*dpRatio));
            params.setMargins(0, (int) (5*dpRatio), 0,50);
            imageView.setLayoutParams(params);
            holder.constraintLayout.addView(imageView);

            set.clone(holder.constraintLayout);
            set.connect(imageView.getId(), ConstraintSet.TOP, holder.stepText.getId(), ConstraintSet.BOTTOM, (int) (7*dpRatio));
            set.connect(imageView.getId(), ConstraintSet.START, holder.stepText.getId(), ConstraintSet.START, (int) (32*dpRatio));
            set.applyTo(holder.constraintLayout);

            Picasso.with(mContext).load(step.getImage()).into(imageView);
        }
    }
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
