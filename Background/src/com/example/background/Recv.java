package com.example.background;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

public class Recv extends WakefulBroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		//Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
		System.out.println("Got it");
		Intent mServiceIntent = new Intent(context, Serv.class);
	    System.out.println("K");
	    startWakefulService(context, mServiceIntent);
		}
	}
	