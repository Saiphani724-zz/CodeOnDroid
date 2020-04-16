package codeondroid.codeondroid;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import codeondroid.codeondroid.R;

public class FeaturesCardViewAdapter extends RecyclerView.Adapter<FeaturesCardViewAdapter.ViewHolder> {
    public String[] mDataset;
    SharedPreferences sf;

    Context context;
    Recycleviewcommunicator comm;

    // Provide a suitable constructor (depends on the kind of dataset)
    public FeaturesCardViewAdapter(String[] myDataset, Recycleviewcommunicator comm) {
        mDataset = myDataset;
        this.comm=comm;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FeaturesCardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.features_cardview_row, null);
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
//            share_button = (ImageButton) itemLayoutView.findViewById(R.id.imageButton);
//            del_button = (Button) itemLayoutView.findViewById(R.id.deltesnipicon);
        }
    }



}