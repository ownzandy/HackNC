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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Space;
import android.widget.Toast;
import android.graphics.Color;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.core.map.Graphic;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.MapGeometry;
import com.esri.core.symbol.*;
import com.esri.core.geometry.*;
import com.esri.android.runtime.ArcGISRuntime.License;
import com.parse.*;

import java.io.UnsupportedEncodingException;
import java.util.*;
import android.content.*;

public class EsriActivity extends Activity {

    MapView mapView;
    GraphicsLayer graphicsLayer = null;
    SimpleMarkerSymbol simpleMarker = null;
    ArrayList<ParseObject> parseList = new ArrayList<ParseObject>();
    int layerID;
    boolean flag;
    boolean serverFlag;
    int count;
    boolean messageFlag;
    ParseObject p = null;
    private int x1;
    private int y1;
    private ImageButton home;
    private ImageButton recenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //buttons
        home = (ImageButton) findViewById(R.id.HomeButton);
        recenter = (ImageButton) findViewById(R.id.recenter);


        //set up map and variables
        final Context context = getApplicationContext();
        count = 0;
        ParseACL defaultACL= new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL,true);
        serverFlag = true;
        flag = true;
        messageFlag = true;
        mapView = (MapView)findViewById(R.id.map);
        mapView.addLayer(new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
        graphicsLayer = new GraphicsLayer();
        mapView.addLayer(graphicsLayer);
        simpleMarker = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE);


        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {

            public void onStatusChanged (Object source, STATUS status){

                //get device location, etc.
                if (OnStatusChangedListener.STATUS.LAYER_LOADED == status && (source instanceof ArcGISTiledMapServiceLayer)) {
                    LocationDisplayManager ldm = mapView.getLocationDisplayManager();
                    Point initial = ldm.getPoint();
                    x1 = (int) initial.getX();
                    y1 = (int) initial.getY();
                    mapView.zoomTo(ldm.getPoint(), 10000);
                }
                if (source == mapView && status == STATUS.INITIALIZED) {
                    LocationDisplayManager ldm = mapView.getLocationDisplayManager();
                    ldm.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                    ldm.start();
                }

                //set local list to server list, only runs when servercall is done
                if (serverFlag) {
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Fling");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> markers, ParseException e) {
                            if (e == null) {
                                parseList = (ArrayList) markers;
                                serverFlag = true;
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                //allows first server call
                count++;
                if (count % 5 == 0) {
                    graphicsLayer.removeAll();
                }
                serverFlag = false;
                for (ParseObject po : parseList) {
                    p = po;
                    int[] posArr = calculateCurrPos(po);
                    int[] initArr = new int[2];
                    LocationDisplayManager ldm = mapView.getLocationDisplayManager();
                    Point initial = ldm.getPoint();
                    initArr[0] = (int) initial.getX();
                    initArr[1] = (int) initial.getY();
                    boolean justSent = p.getBoolean("justSent");
                    if (calcDistance(posArr, initArr) < 150 && !justSent) {
                        String pid = p.getObjectId();
                        String message = p.getString("message");
                        Intent receive = new Intent(EsriActivity.this, receiveMsgActivity.class);
                        receive.putExtra("objectid", pid);
                        receive.putExtra("message", message);
                        startActivity(receive);

                        /*ParseFile picture = (ParseFile)p.get("picture");
                        picture.getDataInBackground(new GetDataCallback() {
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    try {
                                        String picture = new String(data, "UTF-8");
                                        parseList.remove(p);
                                        p.deleteInBackground();
                                        Intent main = new Intent(EsriActivity.this, MainActivity.class);
                                        startActivity(main);
                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    }
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        }); */
                    }

                    if (System.currentTimeMillis() - p.getCreatedAt().getTime() > 2000) {
                        p.put("justSent", false);
                        p.saveInBackground();
                    }

                    Point pointGeometry = new Point(posArr[0], posArr[1]);
                    Graphic pointGraphic = new Graphic(pointGeometry, simpleMarker);
                    graphicsLayer.addGraphic(pointGraphic);
                }

                GraphicsLayer useless = new GraphicsLayer();

                if (flag) {
                    mapView.removeLayer(layerID);
                    flag = false;
                }

                layerID = mapView.addLayer(useless);
            }
        });
    }


//    public void test(View v){
//        Intent s = new Intent(EsriActivity.this, receiveMsgActivity.class);
//        startActivity(s);
//    }


    public int[] calculateCurrPos(ParseObject o) {
        int[] posArr = new int[2];
        LocationDisplayManager ldm = mapView.getLocationDisplayManager();
        int initX = o.getInt("xpos");
        int initY = o.getInt("ypos");
        int velX = o.getInt("xvel");
        int velY = o.getInt("yvel");
        long time = System.currentTimeMillis() - o.getCreatedAt().getTime();
        posArr[0] = (int) initX + velX * (int) time / 100000;
        posArr[1] = (int) initY + velY * (int) time / 100000;
        return posArr;
    }

    //recenter the map to your position
    public void recenterMap(View v){
        Intent send = new Intent(EsriActivity.this, SendMsgActivity.class);
        send.putExtra("xpos", x1);
        send.putExtra("ypos", y1);
        startActivity(send);
        // LocationDisplayManager ldm = mapView.getLocationDisplayManager();
        // mapView.centerAt(ldm.getPoint(), true);
    }
    public void trueRecenter(View v){
         LocationDisplayManager ldm = mapView.getLocationDisplayManager();
         mapView.centerAt(ldm.getPoint(), true);
    }

    public int calcDistance(int[] p1, int[] p2) {
        return (int) Math.sqrt(Math.pow(p1[0]-p2[0], 2) + Math.pow(p1[1]-p2[1], 2));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_activity, menu);
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
