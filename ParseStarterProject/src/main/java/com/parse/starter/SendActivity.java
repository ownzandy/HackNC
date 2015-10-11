package com.parse.starter;

import android.os.Bundle;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SendActivity extends ActionBarActivity {
    private static final String DEBUG_TAG = "Velocity";
    private VelocityTracker mVelocityTracker = null;
    TextView x;
    TextView y;
    private static ImageView bubz;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        x = (TextView) findViewById(R.id.velocityX);
        y = (TextView) findViewById(R.id.velocityY);
        bubz = (ImageView) findViewById(R.id.bubble);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);
        float xpos = event.getX();
        float ypos = event.getY();
        bubz.setX(xpos - bubz.getWidth() / 2);
        bubz.setY(ypos - bubz.getHeight() * 5 / 4);

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                bubz.setVisibility(View.VISIBLE);
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
                bubz.setVisibility(View.INVISIBLE);
                break;
        }
        return true;
    }
}