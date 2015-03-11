package com.example.background;




import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String>{
	
	private final Activity context;
	private final ArrayList <String> web;
	private final ArrayList <String> names;
	
	public CustomList(Activity context, ArrayList <String> web,ArrayList <String> names)
	{
			super(context, R.layout.list_row,web);
			this.context = context;
			this.web=web;
			this.names=names;
			
	}
	@Override
	public View getView(int position, View view, ViewGroup parent) 
	{
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView= inflater.inflate(R.layout.list_row, null, true);
			TextView textview1 = (TextView) rowView.findViewById(R.id.TextView1);
			textview1.setText(names.get(position));
			TextView textview2 = (TextView) rowView.findViewById(R.id.TextView2);
			textview2.setText(web.get(position));
			//Button button = (Button) rowView.findViewById(R.id.button1);
		    //button.setTag(position);
		    //ImageView icon = (ImageView)rowView.findViewById(R.id.imageView1);  
		    //icon.setImageResource(im[position]);
			return rowView;
	}
}