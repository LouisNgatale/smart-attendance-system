package com.louisngatale.smartattendance.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.louisngatale.smartattendance.Data.Courses;
import com.louisngatale.smartattendance.R;

public class CoursesAdapter extends FirestoreRecyclerAdapter<Courses, CoursesAdapter.ViewHolder> {
    private static final String TAG = "Home";
    private  Context mContext;
    private OnItemClickListener listener;

    public CoursesAdapter(FirestoreRecyclerOptions<Courses> options, Context mContext) {
        super(options);
        this.mContext = mContext;

    }

    @Override
    protected void onBindViewHolder(@NonNull CoursesAdapter.ViewHolder holder, int position, @NonNull Courses model) {
        holder.subject.setText(model.getCourse());

        try{
            Glide.with(mContext)
                    .load(model.getImage())
                    .into(holder.imageView);
        }catch (Exception e){
            Log.d(TAG, "onBindViewHolder: " + e);
        }
    }

    @NonNull
    @Override
    public CoursesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_course_item,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subject;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subject= itemView.findViewById(R.id.subjectName);
            imageView = itemView.findViewById(R.id.subjectIcon);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null){
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
