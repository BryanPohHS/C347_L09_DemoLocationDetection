package sg.edu.rp.c346.c347_l09_demolocationdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity
{

  Button btnGetLastLoc, btnGetLocUpdate, btnRemoveLocUpdate;
  FusedLocationProviderClient client;
  LocationRequest mLocationRequest;
  LocationCallback mLocationCallback;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    client = LocationServices.getFusedLocationProviderClient(this);

    mLocationRequest = new LocationRequest();
    mLocationCallback = new LocationCallback();

    btnGetLastLoc = findViewById(R.id.btnGetLastLocation);
    btnGetLocUpdate = findViewById(R.id.btnGetLocationUpdate);
    btnRemoveLocUpdate = findViewById(R.id.btnRemoveLocationUpdate);

    checkPermission();

    // Configurations
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    mLocationRequest.setInterval(10000);
    mLocationRequest.setFastestInterval(50000);
    mLocationRequest.setSmallestDisplacement(100);

   mLocationCallback = new LocationCallback(){
      @Override
      public void onLocationResult(LocationResult locationResult)
      {
        if(locationResult != null){
          Location data = locationResult.getLastLocation();
          double lat = data.getLatitude();
          double lng = data.getLongitude();
        }
      }
    };

    btnGetLastLoc.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view)
      {
        if (checkPermission() == true){
          Task<Location> task = client.getLastLocation();
          task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>()
          {
            @Override
            public void onSuccess(Location location)
            {
              if(location != null){
                String msg = "Lat : " + location.getLatitude() + " Lng: " + location.getLatitude();
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
              } else {
                String msg = "No last known location found";
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
              }
            }
          });
        }
      }
    });

    btnGetLocUpdate.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        if (checkPermission() == true){
          client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
      }
    });

    btnRemoveLocUpdate.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        client.removeLocationUpdates(mLocationCallback);
      }
    });
  }

  private boolean checkPermission(){
    int permissionCheck_Coarse = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
    int permissionCheck_Fine = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

    if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
      return true;
    } else {
      return false;
    }
  }
}