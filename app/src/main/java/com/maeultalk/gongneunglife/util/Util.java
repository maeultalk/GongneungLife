package com.maeultalk.gongneunglife.util;

import android.content.Context;
import android.os.Build;

import java.text.DecimalFormat;

/**
 * Created by kains4 on 2015-07-26.
 */
public class Util {
    public int convertDpToPx(Context ctx, float dp) {
        float d = ctx.getResources().getDisplayMetrics().density;
        return (int)(d*dp);
    }

    public final int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static String toNumFormat(int num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }

}
