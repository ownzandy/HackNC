package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;


public class MapActivity extends Activity {

    MapView mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(MapActivity.this, "start", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = (MapView)findViewById(R.id.map);

        mapView.addLayer(new ArcGISTiledMapServiceLayer(
                "http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));

        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {

            public void onStatusChanged(Object source, STATUS status) {


                if (OnStatusChangedListener.STATUS.LAYER_LOADED == status && (source instanceof ArcGISTiledMapServiceLayer)) {
                    LocationDisplayManager ldm = mapView.getLocationDisplayManager();
//                    Point esri4326 = new Point(ldm.getLocation().getLongitude(), ldm.getLocation().getLatitude());
//                    Toast.makeText(MapActivity.this, String.valueOf(ldm.getLocation().getLatitude()), Toast.LENGTH_SHORT).show();
//                    Point esri102100 = (Point) GeometryEngine.project(esri4326, SpatialReference.create(4326), SpatialReference.create(102100));
                    mapView.centerAndZoom(ldm.getLocation().getLatitude(), ldm.getLocation().getLongitude(), 18);

                }

                if (source == mapView && status == STATUS.INITIALIZED) {

                    LocationDisplayManager ldm = mapView.getLocationDisplayManager();

                    ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                    ldm.start();

                }

            }

        });


    }

    public void recenterMap(View v){

        LocationDisplayManager ldm = mapView.getLocationDisplayManager();
//                    Point esri4326 = new Point(ldm.getLocation().getLongitude(), ldm.getLocation().getLatitude());
//                    Toast.makeText(MapActivity.this, String.valueOf(ldm.getLocation().getLatitude()), Toast.LENGTH_SHORT).show();
//                    Point esri102100 = (Point) GeometryEngine.project(esri4326, SpatialReference.create(4326), SpatialReference.create(102100));
        mapView.centerAndZoom(ldm.getLocation().getLatitude(), ldm.getLocation().getLongitude(), 18);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
