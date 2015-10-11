package com.parse.starter;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Thomas on 10/10/15.
 */
public class drags {

    private View view;
    private MotionEvent motionEvent;

    public drags(View getView, MotionEvent getMotionEvent) {
        view = getView;
        motionEvent = getMotionEvent;
    }

    public boolean handle(View view, MotionEvent motionEvent){
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        //start dragging the item touched
        view.startDrag(data, shadowBuilder, view, 0);
        return true;
    }

}
