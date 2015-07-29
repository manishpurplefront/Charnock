package com.charnock.dev;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.charnock.dev.model.Sub_Category_Model;

import java.util.List;

public class Sub_Category_Adapter extends BaseAdapter {

    Context context;
    private List<Sub_Category_Model> image;
    private static LayoutInflater inflater=null;
    public Sub_Category_Adapter(Activity mainActivity, List<Sub_Category_Model> image) {

        this.context=mainActivity;
        this.image=image;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.category_list_cell, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

        holder.tv.setText(image.get(position).getName());
        try {
            holder.img.setImageResource(Integer.parseInt(image.get(position).getImage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowView;
    }

}