package com.arth.calorytracker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Utility {
    Context context;
    public  Utility(Context context){
        this.context=context;
    }

    public boolean checkInternetConnectionALL() {
        ConnectivityManager conmag = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // for all active Network connection

        NetworkInfo info = conmag.getActiveNetworkInfo();
        if (info != null && info.isConnected())
            return true;
        else
            Toast.makeText(context.getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

        return false;
    }
    public void toast(String msg){
        Toast.makeText(context.getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

    public Dialog showdialog(int resId) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(resId);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
       // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;



        dialog.getWindow().setAttributes(lp);
        return dialog;
    }
}
