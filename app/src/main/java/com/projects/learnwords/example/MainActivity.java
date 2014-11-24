package com.projects.learnwords.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.projects.learnwords.app.R;

/**
 * Created by Александр on 17.10.2014.
 */
public class MainActivity extends Activity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_main_activity);

        Button nextActivity = (Button) findViewById(R.id.nextActivity);
        nextActivity.setOnClickListener(this);

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nextActivity:
                Intent nextActivity = new Intent(this,NextActivity.class);
                startActivity(nextActivity);
                //push from bottom to top
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                //slide from right to left
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            // More buttons go here (if any) ...

        }
    }

}
