package com.example.background;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Serv extends IntentService {
	BluetoothAdapter bluetooth;
	BroadcastReceiver mReceiver;
	ArrayList <String> devices;
	int count=0;
	public Serv() {
		super("Default Service");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		//Log.v("av","av");
		devices=new ArrayList<String>();
		bluetooth=BluetoothAdapter.getDefaultAdapter();
		IntentFilter filter = new IntentFilter();
		mReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				 String action = arg1.getAction();
	             if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	                    BluetoothDevice device = arg1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); 
	                    if (device.getBondState() != BluetoothDevice.BOND_BONDED)
	                    {
	                    	if(devices.contains(device.getAddress())==false)
	                    	{
	                    		count+=1;
	                    		devices.add(device.getAddress());
	                    	}
	                    	//count+=1;
	                    }
	                    System.out.println(device.getName());
	                   } 
	                else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
	                	System.out.println("Started Discovering Divices");
	                }
	                else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	                	System.out.println(count + " Done");
	                	System.out.println(devices);
	                	
	                	if(count>0)
	                		displayNotification(count);
	                	devices.clear();
	                	count=0;
	                	onDestroy();
	                } 
			}
		};
		if(bluetooth.isEnabled()==false)
			return;
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver,filter);
		System.out.println("OK Service Working Fine");
		
		boolean val=bluetooth.startDiscovery();
		while(!bluetooth.isDiscovering())
		{
			//System.out.println("false");
		}
		while(bluetooth.isDiscovering())
		{
			//System.out.println("True");
		}
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		try
		{
			unregisterReceiver(mReceiver);
		}
		catch(Exception e)
		{
			
		}
		super.onDestroy();
	}
	@SuppressLint("NewApi") protected void displayNotification(int r) {
	      NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);	
	      mBuilder.setContentTitle("BackGround Service");
	      mBuilder.setContentText("Found "+r+" new divices, Check it out");
	      mBuilder.setTicker("New Message Alert!");
	      mBuilder.setSmallIcon(R.drawable.ic_launcher);
	      mBuilder.setAutoCancel(true);
	      Intent resultIntent = new Intent(this, MainActivity.class);
	      TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
	      stackBuilder.addParentStack(MainActivity.class);
	      stackBuilder.addNextIntent(resultIntent);
	      PendingIntent resultPendingIntent =
	         stackBuilder.getPendingIntent(
	            0,
	            PendingIntent.FLAG_UPDATE_CURRENT
	         );
	      mBuilder.setContentIntent(resultPendingIntent);
	      
	      NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	      Random randomGenerator = new Random();
	      int randomInt = randomGenerator.nextInt(1000);
	      System.out.println(randomInt);
	      mNotificationManager.notify(500, mBuilder.build());
		
	   }

}
