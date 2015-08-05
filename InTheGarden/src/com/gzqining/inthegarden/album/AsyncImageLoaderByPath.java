package com.gzqining.inthegarden.album;

import java.io.IOException;  
import java.io.InputStream;  
import java.lang.ref.SoftReference;  
import java.net.MalformedURLException;  
import java.net.URL;  
import java.util.HashMap;  
  
import android.content.Context;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.graphics.BitmapFactory.Options;  
import android.graphics.drawable.Drawable;  
import android.os.Handler;  
import android.os.Message;  
import android.provider.MediaStore;  
import android.util.Log;  
import android.widget.ImageView;  
  
public class AsyncImageLoaderByPath {  
    //SoftReference是软引用，是为了更好的为了系统回收变量  
    private HashMap<String, SoftReference<Bitmap>> imageCache;  
    private Context context;  
      
    public AsyncImageLoaderByPath(Context context) {  
        this.imageCache = new HashMap<String, SoftReference<Bitmap>>();  
        this.context = context;  
    }  
    public Bitmap loadBitmapByPath(final String imagePath, final ImageView imageView, final ImageCallback imageCallback){  
        if (imageCache.containsKey(imagePath)) {  
            //从缓存中获取  
            SoftReference<Bitmap> softReference = imageCache.get(imagePath);  
            Bitmap bitmap = softReference.get();  
            if (bitmap != null) {  
                return bitmap;  
            }  
        }  
        final Handler handler = new Handler() {  
            public void handleMessage(Message message) {  
                imageCallback.imageLoaded((Bitmap) message.obj, imageView, imagePath);  
            }  
        };  
        //建立新一个获取SD卡的图片  
        new Thread() {  
            @Override  
            public void run() {  
                Bitmap bitmap = BitmapFactory.decodeByteArray(PictureUtil.decodeBitmap(imagePath), 0, PictureUtil.decodeBitmap(imagePath).length);  
                imageCache.put(imagePath, new SoftReference<Bitmap>(bitmap));  
                Message message = handler.obtainMessage(0, bitmap);  
                handler.sendMessage(message);  
            }  
        }.start();  
        return null;  
    }  
    //回调接口  
    public interface ImageCallback {  
        public void imageLoaded(Bitmap imageBitmap,ImageView imageView, String imagePath);  
    }  
}
