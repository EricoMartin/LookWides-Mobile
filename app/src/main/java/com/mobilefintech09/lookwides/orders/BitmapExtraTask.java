package com.mobilefintech09.lookwides.orders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewParent;

import java.util.concurrent.Callable;

public class BitmapExtraTask implements Callable<Bitmap> {
        private String bitmap;
        private Bitmap mBmp;

    public BitmapExtraTask(String bitmap) {

            this.bitmap = bitmap;
        }

        @Override
        public Bitmap call() {
            // Some long running task

            //mBmp = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            return mBmp;
        }

}
