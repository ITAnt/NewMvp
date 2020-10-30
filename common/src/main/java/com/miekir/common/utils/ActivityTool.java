package com.miekir.common.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 *
 *
 * @author Miekir
 * @date 2020/6/21 21:32
 * Description: 关于Activity的工具
 */
public class ActivityTool {
    private ActivityTool() {}

    public static void openUrl(Activity activity, String webUrl) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri = Uri.parse(webUrl);
        intent.setData(uri);
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            ToastTool.showShort("找不到支付通道");
            e.printStackTrace();
        }
    }
}
