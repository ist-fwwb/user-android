package com.huangtao.user.common;

import android.os.Environment;

import com.huangtao.user.model.User;

import java.io.File;

public class Constants {

    public static User user;

    // arcsoft
    public static final String APP_ID = "F1vNFhZAoVSdhNXaCzpLvBvcMW9aeqH6e68aSH5GUB3C";
    public static final String SDK_KEY = "7Lq9LdfNhCJxkw4zbBPk2hBz3KhbiuBSXhy3sGq3EogE";

    public static String HEAD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "meeting" + File.separator + "head" + File.separator;

    // ali oss
    public static final String OSS_AccessKeyId = "LTAIqMIT5KX4oGAT";
    public static final String OSS_AccessKeySecret = "wYwZdNHrnvAiM9GNddiXqaeHcB4xfz";
    public static final String OSS_BUCKET = "face-file";
    public static final String OSS_DIR_FACE = OSS_BUCKET + "/face-file";
    public static final String OSS_DIR_FEATURE = "face-feature-file";
    public static final String OSS_endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    public static final String OSS_AUTH_SERVER = "http://129.211.119.51:7080/";

}
