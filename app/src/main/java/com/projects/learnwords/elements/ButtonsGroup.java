package com.projects.learnwords.elements;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Александр on 25.11.2014.
 */
public class ButtonsGroup extends LinearLayout {
    Button btn;
    public ButtonsGroup(Context context) {
        super(context);
        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public ButtonsGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void addView(View child) {
        if(!child.getClass().equals(Button.class))   {
            throw new ClassCastException("Not Button class");
        }

        child.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        btn = (Button)child;

        android.view.ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = generateDefaultLayoutParams();
            if (params == null) {
                throw new IllegalArgumentException("cannot return null");
            }
        }

        super.addView(child, -1, params);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
            btn.setOnClickListener(l);
    }
}
