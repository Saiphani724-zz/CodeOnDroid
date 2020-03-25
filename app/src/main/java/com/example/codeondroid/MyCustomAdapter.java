package com.example.codeondroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Snipetcontaier> list = new ArrayList<Snipetcontaier>();
    private Context context;
    public MyCustomAdapter(ArrayList<Snipetcontaier> arr, Context context) {
        list=arr;
        this.context=context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.customcodesnipets, null);
        }

        //Handle TextView and display string from your list
        TextView title= (TextView)view.findViewById(R.id.snipname);
        title.setText(list.get(position).getTitle());
        TextView content= (TextView)view.findViewById(R.id.snipcontent);
        content.setText(list.get(position).getContent());
        return view;
    }
}
