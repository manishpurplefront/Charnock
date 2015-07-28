package com.charnock.dev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.charnock.dev.model.Product_List_Model;
import java.util.List;

public class DisplayAdapter extends BaseAdapter {
	private Context mContext;
	private List<Product_List_Model> name;

	

	public DisplayAdapter(Context c, List<Product_List_Model> name) {
		this.mContext = c;
		this.name = name;
	}

	public int getCount() {
        return name.size();
	}

	public Object getItem(int position) {
		return name.get(position).getProduct_name();
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int pos, View child, ViewGroup parent) {
		Holder mHolder;
		LayoutInflater layoutInflater;
		if (child == null) {
			layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			child = layoutInflater.inflate(R.layout.listcell, null);
			mHolder = new Holder();
			mHolder.txt_name = (TextView) child.findViewById(R.id.txt_name);

			child.setTag(mHolder);
		} else {
			mHolder = (Holder) child.getTag();
		}
		mHolder.txt_name.setText(name.get(pos).getProduct_name());

		return child;
	}

	public class Holder {
		TextView txt_name;

	}

}