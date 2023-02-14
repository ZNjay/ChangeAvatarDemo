package com.example.changeavatardemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Bitmap工具类
 */
public class BitmapUtils {
    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;//对byte类型数据进行写入的类 相当于一个中间缓冲层,将类写入到文件等其他outputStream
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();// flush()方法:冲走。意思是把缓冲区的内容强制的写出。
                baos.close();//关闭baos 同时释放内存。

                byte[] bitmapBytes = baos.toByteArray();//将整数转为字节数组
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * url转bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap urlToBitmap(final String url) {
        final Bitmap[] bitmap = {null};
        new Thread(() -> {
            URL imageurl = null;
            try {
                imageurl = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                //打开一个连接，此处返回的是一个URLConnection 对象，所以需要强转;
                HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                //        Bitmap的创建 创建Bitmap的时候,Java不提供new Bitmap()的形式去创建,而是通过BitmapFactory中的静态方法去创建,
                bitmap[0] = BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        return bitmap[0];
    }
}
