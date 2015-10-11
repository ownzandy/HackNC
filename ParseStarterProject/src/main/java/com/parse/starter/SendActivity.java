package com.parse.starter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SendActivity extends AppCompatActivity {

    private TextView input1, choice1;
    public CharSequence dragData;
    private static final String DEBUG_TAG = "Velocity";
    private VelocityTracker mVelocityTracker = null;
    private TextView textVelocityX;
    private TextView textVelocityY;
    drags followTouch;
    velocity velocityTouch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        textVelocityX = (TextView) findViewById(R.id.velocityx);
        textVelocityY = (TextView) findViewById(R.id.velocityy);

        input1 = (TextView)findViewById(R.id.input_1);
        //views to drop onto
        choice1 = (TextView)findViewById(R.id.choice_1);
        //set touch listeners
        input1.setOnTouchListener(new ChoiceTouchListener());
        //set drag listeners
        choice1.setOnDragListener(new ChoiceDragListener());
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int index = event.getActionIndex();
//        int action = event.getActionMasked();
//        int pointerId = event.getPointerId(index);
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                if (mVelocityTracker == null) {
//                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
//                    mVelocityTracker = VelocityTracker.obtain();
//                } else {
//                    // Reset the velocity tracker back to its initial state.
//                    mVelocityTracker.clear();
//                }
//                // Add a user's movement to the tracker.
//                mVelocityTracker.addMovement(event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mVelocityTracker.addMovement(event);
//                // When you want to determine the velocity, call
//                // computeCurrentVelocity(). Then call getXVelocity()
//                // and getYVelocity() to retrieve the velocity for each pointer ID.
//                mVelocityTracker.computeCurrentVelocity(1000);
//                // Log velocity of pixels per second
//                // Best practice to use VelocityTrackerCompat where possible.
//                double xVelocity = VelocityTrackerCompat.getXVelocity(mVelocityTracker,
//                        pointerId);
//                double yVelocity = VelocityTrackerCompat.getYVelocity(mVelocityTracker,
//                        pointerId);
//                Log.d("", "X velocity: " + xVelocity);
//                Log.d("", "Y velocity: " + yVelocity);
//
//                textVelocityX.setText("X-velocity (pixel/s): " + xVelocity);
//                textVelocityY.setText("Y-velocity (pixel/s): " + yVelocity);
//
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                // Return a VelocityTracker object back to be re-used by others.
//                mVelocityTracker.recycle();
//                mVelocityTracker = null;
//                break;
//        }
//        return true;
//    }

    /**
     * ChoiceTouchListener will handle touch events on draggable views
     */
    private final class ChoiceTouchListener implements View.OnTouchListener {
        @SuppressLint("NewApi")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
//            followTouch.(view, motionEvent);
//                ClipData data = ClipData.newPlainText("", "");
//                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
//                //start dragging the item touched
//                view.startDrag(data, shadowBuilder, view, 0);
//                return true;

            velocityTouch.touches(motionEvent);

            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            //start dragging the item touched
            view.startDrag(data, shadowBuilder, view, 0);
            return true;
        }
    }


    /**
     * DragListener will handle dragged views being dropped on the drop area
     * - only the drop action will have processing added to it as we are not
     * - amending the default behavior for other parts of the drag process
     */
    @SuppressLint("NewApi")
    private class ChoiceDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            View view = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //no action necessary
                    view.setVisibility(View.INVISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DROP:
                    //view dragged item is being dropped on
                    TextView dropTarget = (TextView) v;
                    //view being dragged and dropped
                    TextView dropped = (TextView) view;
                    //checking whether first character of dropTarget equals first character of dropped
                    if (dropTarget.getText().toString().charAt(0) == dropped.getText().toString().charAt(0)) {
                        //stop displaying the view where it was before it was dragged
                        view.setVisibility(View.INVISIBLE);
                        //update the text in the target view to reflect the data being dropped
                        dropTarget.setText(dropTarget.getText().toString() + dropped.getText().toString());
                        //make it bold to highlight the fact that an item has been dropped
                        dropTarget.setTypeface(Typeface.DEFAULT_BOLD);
                        //if an item has already been dropped here, there will be a tag
                        Object tag = dropTarget.getTag();
                        //if there is already an item here, set it back visible in its original place
                        if (tag != null) {
                            //the tag is the view id already dropped here
                            int existingID = (Integer) tag;
                            //set the original view visible again
                            findViewById(existingID).setVisibility(View.VISIBLE);
                        }
                        //set the tag in the target view being dropped on - to the ID of the view being dropped
                        dropTarget.setTag(dropped.getId());
                        //remove setOnDragListener by setting OnDragListener to null, so that no further drag & dropping on this TextView can be done
                        dropTarget.setOnDragListener(null);
                    } else
                        //displays message if first character of dropTarget is not equal to first character of dropped
                        Toast.makeText(SendActivity.this, dropTarget.getText().toString() + "is not " + dropped.getText().toString(), Toast.LENGTH_LONG).show();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //no action necessary
                    break;
                default:
                    break;
            }
            return true;
        }

    }


    public void reset(View view) {
        input1.setVisibility(TextView.VISIBLE);
        choice1.setText("A for ");
        choice1.setTag(null);
        choice1.setTypeface(Typeface.DEFAULT);
        choice1.setOnDragListener(new ChoiceDragListener());
    }
}