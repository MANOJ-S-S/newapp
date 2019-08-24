package com.app.mahindra;

import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ListAdapt extends BaseAdapter {
    LayoutInflater inf;
    Context c;

    public ListAdapt(Context c) {
        this.c = c;
    }

    ArrayList<String> file_list = new ArrayList<>(Arrays.asList("File number one", "File Number two", "File Number three"));
    int[] bullet_array ={R.drawable.ic_loc_bubble,R.drawable.ic_loc_bubble,R.drawable.ic_loc_bubble};
    int[] ic_filedownld ={android.R.drawable.arrow_down_float,android.R.drawable.arrow_down_float,android.R.drawable.arrow_down_float};
    @Override
    public int getCount() {
        return file_list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

       inf = (LayoutInflater) c.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       view =inf.inflate(R.layout.filelist_layout,viewGroup,false);
        TextView file=view.findViewById(R.id.filee);
        ImageView bull=view.findViewById(R.id.bullet);
        ImageView ic_dwn=view.findViewById(R.id.ic_dwn);
        //CheckBox che=view.findViewById(R.id.check);
        file.setText(file_list.get(0));
        bull.setImageResource(bullet_array[i]);
        ic_dwn.setImageResource(ic_filedownld[i]);


        return view;
    }
}
