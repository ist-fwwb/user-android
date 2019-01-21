package com.huangtao.user.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CommonUtils {

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getByteFromFile(File file) {
        // 需要读取的文件，参数是文件的路径名加文件名
        if (file.isFile()) {
            // 以字节流方法读取文件

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                // 设置一个，每次 装载信息的容器
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                // 开始读取数据
                int len = 0;// 每次读取到的数据的长度
                while ((len = fis.read(buffer)) != -1) {// len值为-1时，表示没有数据了
                    // append方法往sb对象里面添加数据
                    outputStream.write(buffer, 0, len);
                }
                // 输出字符串
                return outputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("文件不存在！");
        }
        return null;
    }

    public static List<String> getDayOfWeek(long curTime) {
        List<String> result = new ArrayList<>();
        String[] days = new String[]{"Error", "日", "一", "二", "三", "四", "五", "六"};

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(curTime));

        int today = cal.get(Calendar.DAY_OF_WEEK);

        while (true) {
            if (result.size() >= 5)
                break;
            if (today != 1 && today != 7) {
                // 跳过周六周日
                result.add(days[today]);
            }
            today = today % 7 + 1;
        }

        return result;
    }

    public static List<String> getDateOfWeek(long curTime) {
        List<String> result = new ArrayList<>();
        long day = 1000 * 60 * 60 * 24;

        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            cal.setTime(new Date(curTime + i * day));
            int week = cal.get(Calendar.DAY_OF_WEEK);

            if (week == 1 || week == 7) {
                // 跳过周六周日
                continue;
            }

            String mMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);// 获取当前月份
            if(mMonth.length() == 1){
                mMonth = 0 + mMonth;
            }
            String mDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
            if(mDay.length() == 1){
                mDay = 0 + mDay;
            }
            result.add(mMonth + "." + mDay);
        }
        return result;
    }

    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static void saveSharedPreference(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("meeting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringFromSharedPreference(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("meeting", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static String getFormatTime(int start, int end) {
        String first = start / 2 + ":" + ((start + 1) % 2 == 0 ? "30" : "00");
        String last = end / 2 + ":" + ((end + 1) % 2 == 0 ? "30" : "00");
        return first + "-" + last;
    }

}
