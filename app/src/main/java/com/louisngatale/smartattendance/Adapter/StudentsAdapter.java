package com.louisngatale.smartattendance.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.louisngatale.smartattendance.Data.Student;
import com.louisngatale.smartattendance.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentsAdapter  extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> {
    private static final String TAG = "Adapter";
    private Context mContext;
    ArrayList<Student> all_students;

    public StudentsAdapter(Context mContext, ArrayList<Student> all_students) {
        this.mContext = mContext;
        this.all_students = all_students;
    }

    @NonNull
    @NotNull
    @Override
    public StudentsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StudentsAdapter.ViewHolder holder, int position) {
        holder.student_name.setText(all_students.get(position).getId().toString());
        holder.student_percentage.setText(all_students.get(position).getPercentage().toString());
        Log.d(TAG, "Items " + all_students.toString());
    }

    @Override
    public int getItemCount() {
        return all_students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView student_name, student_percentage;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            student_name = itemView.findViewById(R.id.student_name);
            student_percentage = itemView.findViewById(R.id.student_percentage);
        }
    }
}
