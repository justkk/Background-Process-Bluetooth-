package com.example.background;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	PendingIntent pendingIntent;
	
	//////////////////////////////////////////////////////
	
	BluetoothAdapter bluetooth;
	ToggleButton button;
	ArrayList <String> devices;
	ArrayList <String> names;
	CustomList newadapter;
	ListView list;
	Activity c;
	IntentFilter filter;
	int count=0;
	TextView t;
	
	//////////////////////////////////////////////////////
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() 
	{
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				 String action = arg1.getAction();
				 System.out.println(arg0.equals(MainActivity.this));
	             if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	                    BluetoothDevice device = arg1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); 	
	                    if (device.getBondState() != BluetoothDevice.BOND_BONDED)
	                    {
	                    	if(devices.contains(device.getAddress())==false)
	                    	{
	                    		count+=1;
	                    	}
	                    }
	                    System.out.println(device.getName());
	                    if(devices.contains(device.getAddress())==false)
	                    {
	                    	devices.add(device.getAddress());
	                    	names.add(device.getName());
	                    }
	                    newadapter=new CustomList(c,devices,names);
                    	list.setAdapter(newadapter);
                    	t.setText("Scanning in Progress");
                    	t.setText("Found "+count+" new Devices");
	                   } 
	                else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
	                	System.out.println("Started Discovering Divices");
	                	t.setText("Scanning in Progress");
	                }
	                else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	                	System.out.println("Done" + count);
	                	t.setText("Found "+devices.size()+" new Devices");
	                	newadapter=new CustomList(c,devices,names);
	                    list.setAdapter(newadapter);
	                    
	                	devices.clear();
	                    names.clear();
	                	count=0;
	                	unlink();
	                	//displayNotification();
	                } 
			}
	};
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void unlink()
	{
		try
		{
			unregisterReceiver(mReceiver);
		}
		catch(Exception e)
		{
			
		}

	}
	
	
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view1);
        /////////////   view code ////////////////////
        c=MainActivity.this;
        button = (ToggleButton) findViewById(R.id.toggleButton1);
        final Button scan = (Button) findViewById(R.id.button1);
        button.setChecked(false);
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        list = (ListView)findViewById(R.id.listView1);
        t=(TextView) findViewById(R.id.TextView01);
        boolean enable=bluetooth.isEnabled();
        if(enable==true)
        {
        	button.setChecked(true);
        	runmethod();
        	scan.setEnabled(true);
        }
        else
        {
        	button.setChecked(false);
    		scan.setEnabled(false);
        	t.setText("Please Switch on the Bluetooth");
        }
        
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	boolean value=button.isChecked();
            	if(value==true)
            	{
            		bluetooth.enable();
            		while(!bluetooth.isEnabled())
            		{
            			
            		}
            		scan.setEnabled(true);
            		System.out.println("Switched ON");
            		runmethod();
            	}
            	else
            	{
            		try
            		{
            			unregisterReceiver(mReceiver);
            		}
            		catch(Exception e)
            		{
            			
            		}
            		bluetooth.disable();
            		while(bluetooth.isEnabled())
            		{
            			
            		}
            		scan.setEnabled(false);
            		//newadapter=new CustomList(c,devices);
                	//list.setAdapter(newadapter);
                	t.setText("Please Switch on the Bluetooth");
            	}
            }
         });
        
        scan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            		try
            		{
            			unregisterReceiver(mReceiver);
            		}
            		catch(Exception e)
            		{
            			
            		}
            		devices.clear();
            		names.clear();
            		runmethod();
                	//t.setText("Please Switch on the Bluetooth");
          
         }
        });
        
        ////////////////////////////////////////////////
        
       

       /* if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
     
       
       ///////////////////////////////// Alaram Code //////////////////////////////
      
       
        
       // Intent mServiceIntent = new Intent(this, Serv.class);
       // System.out.println("K");
       // startService(mServiceIntent);
        setalaram(MainActivity.this);
    }
    public void setalaram(Context p)
    {
    	 Intent alarm = new Intent(p, Recv.class);
         boolean alarmrunning=(pendingIntent.getBroadcast(MainActivity.this, 0, alarm, PendingIntent.FLAG_NO_CREATE)!=null);
         System.out.println(alarmrunning);
         pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarm, 0);
         AlarmManager amanager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
         amanager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30000, pendingIntent);
         /////////////////////////////////////////////////////////////////////////////
         Breceiver.class.getSimpleName();
         ComponentName receiver = new ComponentName(p, Breceiver.class);
         PackageManager pm = p.getPackageManager();
         pm.setComponentEnabledSetting(receiver,
                 PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                 PackageManager.DONT_KILL_APP);
    	
    }
    
    public void runmethod()
    {
    	boolean enable=bluetooth.isEnabled();
    	if(enable==false)
    		return;
    	if (bluetooth.isDiscovering()) {
        	bluetooth.cancelDiscovery();
        }
    	count=0;
 	   	devices=new ArrayList<String>();
 	   	names=new ArrayList<String>();
        //devices.add("asd");
        newadapter=new CustomList(c,devices,names);
    	list.setAdapter(newadapter);
    	filter = new IntentFilter();
 		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
 		filter.addAction(BluetoothDevice.ACTION_FOUND);
 		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
 		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
 		registerReceiver(mReceiver, filter);
 		bluetooth.startDiscovery();
 		while(bluetooth.isDiscovering()==false)
 		{
 			
 		}
 		t.setText("Scanning in Progress");
 		
    }
    
    
    public void start() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000;
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }
    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	try
		{
			unregisterReceiver(mReceiver);
		}
		catch(Exception e)
		{
			
		}
    	super.onPause();
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	runmethod();
    	super.onResume();
    }
    @Override
    protected void onDestroy() {
    	try
		{
			unregisterReceiver(mReceiver);
		}
		catch(Exception e)
		{
			
		}
   	 super.onDestroy();
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    
    public class Breceiver extends BroadcastReceiver {
      
        @Override
        public void onReceive(Context context, Intent intent) {
        	setalaram(context);
        }
    }

}
