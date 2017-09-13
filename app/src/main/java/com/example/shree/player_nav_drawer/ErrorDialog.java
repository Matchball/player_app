package com.example.shree.player_nav_drawer;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ErrorDialog {

    public TextView text;

    public void showDialog(final Activity activity, String msg, Typeface font) {

        final Dialog dialog = new Dialog(activity);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);

        dialog.setContentView(R.layout.errordialog);

        text = (TextView) dialog.findViewById(R.id.text_dialog);

        text.setTypeface(font);

        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);

        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }

        });

        dialog.show();

    }


}