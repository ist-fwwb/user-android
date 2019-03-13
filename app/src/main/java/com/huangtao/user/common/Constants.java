package com.huangtao.user.common;

import android.os.Environment;

import com.huangtao.user.model.User;

import java.io.File;

public class Constants {

    public static User user;
    public static String uid;

    public static String KEY_UID = "uid";

    // arcsoft
    public static final String APP_ID = "F1vNFhZAoVSdhNXaCzpLvBvcMW9aeqH6e68aSH5GUB3C";
    public static final String SDK_KEY = "7Lq9LdfNhCJxkw4zbBPk2hBz3KhbiuBSXhy3sGq3EogE";

    public static final String ROOT_DIR = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "meeting" + File.separator;

    public static final String SAVE_IMG_DIR = ROOT_DIR + "imgs" + File.separator;
    public static final String SAVE_FEATURE_DIR = ROOT_DIR + "features" + File.separator;
    public static final String HEAD_DIR = ROOT_DIR + "head" + File.separator;
    public static final String SAVE_RECORD_DIR = ROOT_DIR + "records" + File.separator;
    public static final String MEETING_ROOM_DIR = ROOT_DIR + "meetingroom" + File.separator;

    // ali oss
    public static final String OSS_AccessKeyId = "LTAIqMIT5KX4oGAT";
    public static final String OSS_AccessKeySecret = "wYwZdNHrnvAiM9GNddiXqaeHcB4xfz";
    public static final String OSS_BUCKET = "face-file";
    public static final String OSS_DIR_FACE = OSS_BUCKET + "/face-file";
    public static final String OSS_DIR_FEATURE = "face-feature-file";
    public static final String OSS_endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    public static final String OSS_AUTH_SERVER = "http://47.106.8.44:7080/";

}
