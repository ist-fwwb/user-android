package com.huangtao.user.common;

import android.os.Environment;

import com.huangtao.user.model.User;

import java.io.File;

public class Constants {

    public static User user;

    public static final String APP_ID = "F1vNFhZAoVSdhNXaCzpLvBvcMW9aeqH6e68aSH5GUB3C";
    public static final String SDK_KEY = "7Lq9LdfNhCJxkw4zbBPk2hBz3KhbiuBSXhy3sGq3EogE";

    public static String HEAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "meeting" + File.separator + "head";
}
