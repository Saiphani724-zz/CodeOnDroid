package com.example.codeondroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.sql.ClientInfoStatus;

public class CardViewDataAdapter extends RecyclerView.Adapter<CardViewDataAdapter.ViewHolder> {
    public String[] mDataset;
    SharedPreferences sf;
    Context context;
    Recycleviewcommunicator comm;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardViewDataAdapter(String[] myDataset, Recycleviewcommunicator comm) {
        mDataset = myDataset;
        this.comm=comm;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardViewDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_row, null);
        context = parent.getContext();
        sf = context.getSharedPreferences("myfile1", Context.MODE_PRIVATE);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.tvtinfo_text.setText(mDataset[position].toString());
        viewHolder.tvtinfo_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor edit=sf.edit();
                edit.clear(); // remove existing entries
                edit.putString("filename",mDataset[position]);
                edit.commit();

                context.startActivity(new Intent(context,EditorActivity.class));

            }
        });
        viewHolder.del_button.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File delfile = new File(context.getFilesDir() + "/" + mDataset[position]);
                try {
                    boolean deleted = delfile.delete();
                    if (deleted) {
                        Toast.makeText(context, "File " + mDataset[position]+ " was successfully deleted", Toast.LENGTH_SHORT).show();
                        comm.load_files();
                    }
                    else {
                        //Toast.makeText(context, "Default snipets cannot be deleted"+mDataset[position], Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }));
        viewHolder.share_button.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comm.share_files(mDataset[position]);
            }
        }));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvtinfo_text;
        ImageButton share_button;
        Button del_button;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvtinfo_text = (TextView) itemLayoutView.findViewById(R.id.info_text);
            share_button = (ImageButton) itemLayoutView.findViewById(R.id.imageButton);
            del_button = (Button) itemLayoutView.findViewById(R.id.deltesnipicon);
        }
    }



}