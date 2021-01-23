package com.infowithvijay.triviaquizappwithroom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverDialog {

    private Context mContext;
    private Dialog gameOverDialog;


    public GameOverDialog(Context mContext) {
        this.mContext = mContext;
    }

    public void GameOverDialog(String correctAns){

        gameOverDialog = new Dialog(mContext);
        gameOverDialog.setContentView(R.layout.gameover_dialog);

        final Button btGameOverOk = (Button) gameOverDialog.findViewById(R.id.bt_timer);
        final TextView txtCorrectAns = (TextView) gameOverDialog.findViewById(R.id.txtGameOverCorrectAns);

        txtCorrectAns.setText("Correct Ans : " + correctAns);

        btGameOverOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gameOverDialog.dismiss();
                Intent intent = new Intent(mContext,PlayScreen.class);
                mContext.startActivity(intent);


            }
        });


        gameOverDialog.show();
        gameOverDialog.setCancelable(false);
        gameOverDialog.setCanceledOnTouchOutside(false);
        gameOverDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


}
