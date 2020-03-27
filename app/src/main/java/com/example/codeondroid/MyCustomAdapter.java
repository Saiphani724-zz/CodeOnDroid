package com.example.codeondroid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Snipetcontaier> list = new ArrayList<Snipetcontaier>();
    private Context context;
    CustomAdapterCommunicator comm;
    public MyCustomAdapter(ArrayList<Snipetcontaier> arr, Context context,CustomAdapterCommunicator comm) {
        list=arr;
        this.context=context;
        this.comm=comm;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.customcodesnipets, null);
        }

        //Handle TextView and display string from your list
        TextView title= (TextView)view.findViewById(R.id.snipname);
        title.setText(list.get(position).getTitle());
        final TextView content= (TextView)view.findViewById(R.id.snipcontent);
        content.setText(list.get(position).getContent());
        Button button_del,button_paste,button_edit;
        button_del=(Button)view.findViewById(R.id.deltesnipicon);
        button_paste=(Button)view.findViewById(R.id.pastesnipicon);
        button_edit=(Button)view.findViewById(R.id.editsnipicon);
        button_paste.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        comm.customsetresult(list.get(position).getContent());
                    }
                })
        );
        button_del.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File delfile = new File(context.getFilesDir() + "/snip/" + list.get(position).getTitle()+".snip");
                try {
                    boolean deleted = delfile.delete();
                    if (deleted) {
                        Toast.makeText(context, "Snipet successfully deleted", Toast.LENGTH_SHORT).show();
                        comm.loadondelete();
                    }
                    else {
                        Toast.makeText(context, "Default snipets cannot be deleted", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
           }
        }));
        button_edit.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        comm.customstartactivity(list.get(position).getTitle());
                    }
                })
        );
        return view;
    }

}
