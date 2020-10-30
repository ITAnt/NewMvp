package com.miekir.common.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miekir.common.utils.ContextManager;
import com.miekir.common.utils.SizeTool;

/**
 *
 * @author Miekir
 * @date 2020/7/5 0:17
 * Description: 免手动初始化
 */
public class CommonInstaller extends ContentProvider {
    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            ContextManager.getInstance().initContext(getContext().getApplicationContext());
            SizeTool.SCREEN_WIDTH = getContext().getResources().getDisplayMetrics().widthPixels;
            SizeTool.SCREEN_HEIGHT = getContext().getResources().getDisplayMetrics().heightPixels;
        }

        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
