package com.parse.starter;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.reimaginebanking.api.java.NessieClient;
import com.reimaginebanking.api.java.NessieException;
import com.reimaginebanking.api.java.NessieResultsListener;
import com.reimaginebanking.api.java.models.Account;
import com.reimaginebanking.api.java.models.Deposit;
import com.reimaginebanking.api.java.models.Purchase;

import java.util.Calendar;


public class DollaActivity extends ActionBarActivity {
    private static final String DEBUG_TAG = "Velocity";
    private VelocityTracker mVelocityTracker = null;
    TextView x;
    TextView y;
    private static ImageView cashmoney;
    private EditText bankIDEditText;
    private TextView balanceTextView;
    private NessieClient nessieClient = NessieClient.getInstance();
    private Account resultAcc;
    private double balance;
    private MediaPlayer mp;

    protected void onCreate(Bundle savedInstanceState) {
        balance = -1.0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dolla);


        x = (TextView) findViewById(R.id.velocityX);
        y = (TextView) findViewById(R.id.velocityY);

        cashmoney = (ImageView) findViewById(R.id.cashmoney);
        bankIDEditText = (EditText) findViewById(R.id.BankID);
        balanceTextView = (TextView) findViewById(R.id.BalanceView);
        balanceTextView.setText(String.valueOf(balance));
        nessieClient.setAPIKey("a4f40a3696028d61b73adc39165ce8a1");

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);
        float xpos = event.getX();
        float ypos = event.getY();
        cashmoney.setX(xpos - cashmoney.getWidth() / 2);
        cashmoney.setY(ypos - cashmoney.getHeight() * 5 / 4);

        String bankID = bankIDEditText.getText().toString();


        switch (action) {

            case MotionEvent.ACTION_DOWN:
                cashmoney.setVisibility(View.VISIBLE);
                if (mVelocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker.clear();
                }
                // Add a user's movement to the tracker.
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                // When you want to determine the velocity, call
                // computeCurrentVelocity(). Then call getXVelocity()
                // and getYVelocity() to retrieve the velocity for each pointer ID.
                mVelocityTracker.computeCurrentVelocity(1000);
                // Log velocity of pixels per second
                // Best practice to use VelocityTrackerCompat where possible.
                Log.d("", "X velocity: " +
                        VelocityTrackerCompat.getXVelocity(mVelocityTracker,
                                pointerId));
                Log.d("", "Y velocity: " +
                        VelocityTrackerCompat.getYVelocity(mVelocityTracker,
                                pointerId));

                x.setText("X Velocity = " + VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId) + "\n X position: " + xpos);
                y.setText("Y Velocity = " + VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId) + "\n Y position: " + ypos);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                cashmoney.setVisibility(View.INVISIBLE);
                //new LongRunningGetIO().execute();



                nessieClient.getCustomerAccounts(bankID, new NessieResultsListener() {
                    @Override
                    public void onSuccess(Object o, NessieException e) {
                        if (e == null) {
                            //There is no error, do whatever you need here.
                            // Cast the result object to the type that you are requesting and you are good to go
                            resultAcc = (Account) o;
                            balance = resultAcc.getBalance();
                        } else {
                            //There was an error. Handle it here
                            Log.e("Error", e.toString());
                        }
                    }
                });

                Purchase bling = new Purchase("123456", Calendar.getInstance().toString(), "completed", null, resultAcc.get_id(), "FLING", 100.0, "MAKE IT RAIN");
                nessieClient.createPurchase(bankID, bling, new NessieResultsListener() {
                            @Override
                            public void onSuccess(Object o, NessieException e) {
                                if(e == null){

                                } else {
                                    //There was an error. Handle it here
                                    Log.e("Error", e.toString());
                                }
                            }
                        }



                );

                balanceTextView.setText(String.valueOf(balance));
                mp = MediaPlayer.create(this, R.raw.chaching_mp3);
                mp.start();

                break;
        }
        return true;
    }

    public void payDay(View v){

        bankIDEditText = (EditText) findViewById(R.id.BankID);
        nessieClient.createDeposit(bankIDEditText.getText().toString(), new Deposit("12345",Calendar.getInstance().toString(),"complete", null, null, bankIDEditText.getText().toString(),500.0, "$$$$$$$"),new NessieResultsListener() {
            @Override
            public void onSuccess(Object o, NessieException e) {
                if(e == null){

                } else {
                    //There was an error. Handle it here
                    Log.e("Error", e.toString());
                }
            }
        } );


    }
}
