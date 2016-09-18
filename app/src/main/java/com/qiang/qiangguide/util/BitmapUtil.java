package com.qiang.qiangguide.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Qiang on 2016/8/1.
 */
public class BitmapUtil {

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 从文件加载图片
     * 根据图片控件的大小压缩图片，以减少内存占用
     * @param path 图片文件路径
     * @param reqWidth 控件宽度
     * @param reqHeight 控件高度
     * @return bm
     */
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }


    private static final String TAG = LogUtil.makeLogTag(BitmapUtil.class);

    // Max read limit that we allow our input stream to mark/reset.
    private static final int MAX_READ_LIMIT_PER_IMG = 1024 * 1024;


    public static final int MAX_ART_WIDTH_ICON = 128;  // pixels
    public static final int MAX_ART_HEIGHT_ICON = 128;  // pixels


    public static Bitmap scaleBitmap(Bitmap src, int maxWidth, int maxHeight) {
        double scaleFactor = Math.min(
                ((double) maxWidth)/src.getWidth(), ((double) maxHeight)/src.getHeight());
        return Bitmap.createScaledBitmap(src,
                (int) (src.getWidth() * scaleFactor), (int) (src.getHeight() * scaleFactor), false);
    }

    public static Bitmap scaleBitmap(int scaleFactor, InputStream is) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(is, null, bmOptions);
    }

    public static int findScaleFactor(int targetW, int targetH, InputStream is) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, bmOptions);
        int actualW = bmOptions.outWidth;
        int actualH = bmOptions.outHeight;

        // Determine how much to scale down the image
        return Math.min(actualW/targetW, actualH/targetH);
    }

    @SuppressWarnings("SameParameterValue")
    public static Bitmap fetchAndRescaleBitmap(String uri, int width, int height)
            throws IOException {
        URL url = new URL(uri);
        BufferedInputStream is = null;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(urlConnection.getInputStream());
            is.mark(MAX_READ_LIMIT_PER_IMG);
            int scaleFactor = findScaleFactor(width, height, is);
            LogUtil.d(TAG, "Scaling bitmap ", uri, " by factor ", scaleFactor, " to support ",
                    width, "x", height, "requested dimension");
            is.reset();
            return scaleBitmap(scaleFactor, is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }



    //生成圆角图片
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            final float roundPx = 14;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            LogUtil.i("","图片已经圆角化处理");
            return output;
        } catch (Exception e) {
            LogUtil.e("","图片圆角化处理异常");
            return bitmap;
        }
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(Bitmap bkg,Context context) {
        long startMs = System.currentTimeMillis();
        float radius = 20;
        bkg = small(bkg);
        Bitmap bitmap = bkg.copy(bkg.getConfig(), true);
        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, bkg, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        rs.destroy();
        bitmap = big(bitmap);
        Log.d("","blur take away:" + (System.currentTimeMillis() - startMs )+ "ms");
        return bitmap;
    }

    private static Bitmap big(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(4f,4f); //长和宽放大缩小的比例
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }

    private static Bitmap small(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.25f,0.25f); //长和宽放大缩小的比例
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }



}
