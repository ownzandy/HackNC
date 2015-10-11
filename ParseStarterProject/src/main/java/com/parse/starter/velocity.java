package com.parse.starter;

import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Thomas on 10/10/15.
 */
public class velocity {

    private View view;
    private MotionEvent motionEvent;
    private VelocityTracker mVelocityTracker = null;
    private TextView textVelocityX;
    private TextView textVelocityY;

    public velocity(View getView, MotionEvent getMotionEvent) {
        view = getView;
        motionEvent = getMotionEvent;
    }

    public boolean touches(MotionEvent motionEvent) {
        int index = motionEvent.getActionIndex();
        int action = motionEvent.getActionMasked();
        int pointerId = motionEvent.getPointerId(index);
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                if (mVelocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker.clear();
                }
                // Add a user's movement to the tracker.
                mVelocityTracker.addMovement(motionEvent);

            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(motionEvent);
                // When you want to determine the velocity, call
                // computeCurrentVelocity(). Then call getXVelocity()
                // and getYVelocity() to retrieve the velocity for each pointer ID.
                mVelocityTracker.computeCurrentVelocity(1000);
                // Log velocity of pixels per second
                // Best practice to use VelocityTrackerCompat where possible.
                double xVelocity = VelocityTrackerCompat.getXVelocity(mVelocityTracker,
                        pointerId);
                double yVelocity = VelocityTrackerCompat.getYVelocity(mVelocityTracker,
                        pointerId);
                Log.d("", "X velocity: " + xVelocity);
                Log.d("", "Y velocity: " + yVelocity);

                textVelocityX.setText("X-velocity (pixel/s): " + xVelocity);
                textVelocityY.setText("Y-velocity (pixel/s): " + yVelocity);
                break;

            default:
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                return false;
        }
        return true;

    }
}
