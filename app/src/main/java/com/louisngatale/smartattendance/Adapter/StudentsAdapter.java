package com.louisngatale.smartattendance.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.louisngatale.smartattendance.Data.Courses;
import com.louisngatale.smartattendance.Data.Students;

import org.jetbrains.annotations.NotNull;

public class StudentsAdapter  extends FirestoreRecyclerAdapter<Students, StudentsAdapter.ViewHolder> {
    private Context mContext;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public StudentsAdapter(@NonNull @NotNull FirestoreRecyclerOptions<Students> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull StudentsAdapter.ViewHolder holder, int position, @NonNull @NotNull Students model) {

    }

    @NonNull
    @NotNull
    @Override
    public StudentsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
