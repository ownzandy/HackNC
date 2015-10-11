package com.parse.starter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.esri.android.map.*;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.map.Graphic;
import android.view.*;
import com.esri.core.symbol.*;
import com.esri.core.geometry.*;

import java.util.*;

/**
 * This app uses the DynamicLayerMapService and the DynamicLayer to demonstrate
 * the Class break rendering. On clicking the field from the spinner it will
 * display the rendered image classifying the attribute that you have selected.
 * Please be aware that the Dynamic Layer service is not guaranteed to be
 * running.
 *
 */
public class EsriActivity extends Activity {

    MapView myMapView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esri);
        myMapView = (MapView) findViewById(R.id.map);
        GraphicsLayer graphicsLayer = new GraphicsLayer();
        SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
        Point pointGeometry = new Point(37.568536, -119.468169);
        Graphic pointGraphic = new Graphic(pointGeometry, simpleMarker);
        graphicsLayer.addGraphic(pointGraphic);
        myMapView.addLayer(graphicsLayer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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