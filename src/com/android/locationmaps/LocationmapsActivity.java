package com.android.locationmaps;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.android.locationmaps.utilities.Utilities;

public class LocationmapsActivity extends Activity implements SensorEventListener {
    /** Called when the activity is first created. */
	
	TextView tv = null;
	
	private SensorManager sensorManager = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        new DownloadTask().execute("http://graph.facebook.com/4");
        
        tv = (TextView) findViewById(R.id.test);
        
    }
    
    private class DownloadTask extends AsyncTask<String, Long, String> {

		@Override
		protected String doInBackground(String... params) {

	        String s = Utilities.getData(params[0]).toString();
			return s;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			tv.setText(result);
		}
    	
    }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		
	}

	// TODO: Not tested. May not work.
	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER: // you can add other sensor types here.
				
				final float alpha = 0.8f;
				
				float [] gravity = new float[]{0.0f, 0.0f, 0.0f};
				gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
				gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
				gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
				
				float linear_accelerationX = event.values[0] - gravity[0];
				float linear_accelerationY = event.values[1] - gravity[1];
				float linear_accelerationZ = event.values[2] - gravity[2];
				
				System.out.println("accX : " + linear_accelerationX 
						+ " accY : " + linear_accelerationY
						+ " accZ : " + linear_accelerationZ);
				
				break;
				
			case Sensor.TYPE_GRAVITY:
				break;

			default:
				break;
			}
			
		}
	}
}