/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import java.io.*;
import android.view.Gravity;
import android.content.*;


import com.parse.*;

public class MainActivity extends ActionBarActivity {

  private Button writeMsg;
  private Button seeMsg;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ParseAnalytics.trackAppOpenedInBackground(getIntent());

    //temp testing buttons
    writeMsg = (Button) findViewById(R.id.writeMsg);
    seeMsg = (Button) findViewById(R.id.seeMsg);
    writeMsgOnClick();
    seeMsgOnClick();

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  public void writeMsgOnClick() {
    writeMsg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(getBaseContext(), EsriActivity.class);
        startActivity(i);
      }
    });
  }

  public void seeMsgOnClick() {
    seeMsg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

//s        Intent intent = new Intent(getBaseContext(), receiveMsgActivity.class);

        Intent intent = new Intent(getBaseContext(), EsriActivity.class);

        startActivity(intent);
      }
    });
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
