package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.parse.*;
import java.util.*;
import java.io.*;

public class receiveMsgActivity extends ActionBarActivity {

    private ImageView myPic;
    private TextView caption;
    private ImageButton backtoMap;
    private ParseObject parseObjGlob;
    String message = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_msg);

        myPic = (ImageView) findViewById(R.id.picture);
        caption = (TextView) findViewById(R.id.message);
        backtoMap = (ImageButton) findViewById(R.id.back);

        Intent oldIntent = getIntent();
        message = oldIntent.getStringExtra("message");
        caption.setText(message);
        final String objectid = oldIntent.getStringExtra("objectid");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Fling");
        query.getInBackground(objectid, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                parseObjGlob = object;
                if (e == null) {
                    ParseFile picture = (ParseFile) object.get("picture");
                    picture.getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    //programmatically set image and message
                                myPic.setImageBitmap(bmp);

                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
    public void backtoMap(View v){
        Intent i = new Intent(receiveMsgActivity.this, EsriActivity.class);
        startActivity(i);
        parseObjGlob.deleteInBackground();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_receive_msg, menu);
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
