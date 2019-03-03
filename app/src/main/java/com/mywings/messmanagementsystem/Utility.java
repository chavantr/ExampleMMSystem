package com.mywings.messmanagementsystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class Utility {

    public void questionPaperSelectConfirmation(Context context, final String[]strPopUpData ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("Select").setIcon(
                context.getResources().getDrawable(R.mipmap.ic_launcher));
        builder.setSingleChoiceItems(strPopUpData, 0,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String language = strPopUpData[which];

                        dialog.dismiss();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

}
