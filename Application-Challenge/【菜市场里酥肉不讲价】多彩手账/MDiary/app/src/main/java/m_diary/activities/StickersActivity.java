package m_diary.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;

import java.io.Serializable;

import m_diary.controls.Sticker;
import m_diary.controls.StickerLayout;
import m_diary.utils.Protocol;

import com.example.myapplication.R;

import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;

public class StickersActivity extends AppCompatActivity {
    private DialogInterface.OnClickListener confirm;
    private DialogInterface.OnClickListener cancel;

    public int stickerSrcNum = 16;
    Bitmap stickerBitmap = null;
    private StickerLayout mStickerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickers);

        for (int stickerIndex = 1; stickerIndex <= stickerSrcNum; stickerIndex++) {
            @SuppressLint("DefaultLocale") String ivStickerStr = "iv_sticker_" + String.format("%02d", stickerIndex);
            @SuppressLint("DefaultLocale") String stickerStr = "sticker_" + String.format("%02d", stickerIndex);
            int resIVStickerID = getResources().getIdentifier(ivStickerStr, "id", getPackageName());
            int resStickerID = getResources().getIdentifier(stickerStr, "drawable", getPackageName());
            findViewById(resIVStickerID).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stickerBitmap = BitmapFactory.decodeResource(StickersActivity.this.getResources(), resStickerID);
                    getStickerBitmap(stickerBitmap);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mStickerLayout.removeAllSticker();
    }

    public void getStickerBitmap(Bitmap stickerBitmap) {
        if (stickerBitmap != null) {
            Intent i = new Intent();
            i.putExtra(Protocol.STICKER_BITMAP, stickerBitmap);
            i.putExtra(Protocol.CHANGED, true);
            this.setResult(Protocol.ADD_STICKER, i);
            finish();
        } else {
            Toast.makeText(this, "你没有选择任何贴纸!", Toast.LENGTH_SHORT).show();
        }
    }

    public void back_sticker(View view) {
        if (stickerBitmap != null) {
            AlertDialog.Builder alert_dialog_builder = new AlertDialog.Builder(this);
            alert_dialog_builder.setMessage("放弃添加贴纸吗?");
            alert_dialog_builder.setPositiveButton("是", confirm);
            alert_dialog_builder.setNegativeButton("否", cancel);
            AlertDialog alert_dialog = alert_dialog_builder.create();
            alert_dialog.show();
        } else {
            Intent i = new Intent();
            i.putExtra(Protocol.CHANGED, false);
            StickersActivity.this.setResult(Protocol.ADD_STICKER, i);
            finish();
        }
    }
}