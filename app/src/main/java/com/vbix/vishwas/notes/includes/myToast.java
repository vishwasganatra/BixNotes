package com.vbix.vishwas.notes.includes;

import android.content.Context;
import android.widget.Toast;

public class myToast {
    public static void show(Context context, String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
