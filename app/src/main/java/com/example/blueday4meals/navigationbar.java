package com.example.blueday4meals;

import android.app.Activity;
import android.content.Intent;

public class navigationbar extends Activity {
    public navigationbar(Activity activity, Class<?> changingClass){
        Intent intent = new Intent(activity, changingClass);
        activity.startActivity(intent);
    }
}
