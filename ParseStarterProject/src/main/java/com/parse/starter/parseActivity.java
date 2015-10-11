/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import java.io.*;
import android.view.Gravity;

import com.parse.*;

public class parseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse);
      /*

      String message = "thisisamessage";
      ParseObject fling = new ParseObject("Fling");
      //save x position
      fling.put("xpos", 10);
      //save y position
      fling.put("ypos", 20);
      //save x velocity
      fling.put("xvel", 30);
      //save y velocity
      fling.put("yvel", 40);
      //save message
      fling.put("message", message);
      //save picture
      byte[] pic = "thisisthepicture".getBytes();
      ParseFile file = new ParseFile("picture.txt", pic);
      file.saveInBackground();
      fling.put("picture", file);
      //saves to parse
      fling.saveInBackground();

      */

        //get x position
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Fling");
        query.getInBackground("EW8CyPIWSW", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    //get x position
                    Toast.makeText(context, String.valueOf(object.getInt("xpos")), Toast.LENGTH_LONG).show();

                    //get y position
                    Toast.makeText(context, String.valueOf(object.getInt("ypos")), Toast.LENGTH_LONG).show();

                    //get x velocity
                    Toast.makeText(context, String.valueOf(object.getInt("xvel")), Toast.LENGTH_LONG).show();

                    //get y velocity
                    Toast.makeText(context, String.valueOf(object.getInt("yvel")), Toast.LENGTH_LONG).show();

                    //get message
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();

                    //get picture
                    ParseFile picture = (ParseFile)object.get("picture");
                    picture.getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                try {
                                    Toast toast = Toast.makeText(context, new String(data, "UTF-8"), Toast.LENGTH_LONG);
                                    toast.show();

                                }
                                catch (UnsupportedEncodingException e1) {
                                    e1.printStackTrace();
                                }
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
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
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
