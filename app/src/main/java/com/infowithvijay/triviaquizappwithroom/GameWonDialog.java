package com.infowithvijay.triviaquizappwithroom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameWonDialog {

    private Context mContext;
    private Dialog gameWonDialog;


    public GameWonDialog(Context mContext) {
        this.mContext = mContext;
    }

    public void GameWonDialog(String moneyWonByUser){

        gameWonDialog = new Dialog(mContext);
        gameWonDialog.setContentView(R.layout.gameover_won_dialog);

        final Button btGameWonOk = (Button) gameWonDialog.findViewById(R.id.bt_game_won);
        final TextView txtMoneyWonByUser = (TextView) gameWonDialog.findViewById(R.id.txtMoneyWonByUser);
        txtMoneyWonByUser.setText(moneyWonByUser);

        

        btGameWonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gameWonDialog.dismiss();
                Intent intent = new Intent(mContext,PlayScreen.class);
                mContext.startActivity(intent);


            }
        });


        gameWonDialog.show();
        gameWonDialog.setCancelable(false);
        gameWonDialog.setCanceledOnTouchOutside(false);
        gameWonDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


}
