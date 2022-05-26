package com.test.test3app.glide;

import android.widget.ImageView;

/**
 * created by zhaoyuntao
 * on 25/05/2022
 * description:
 */
public class ChatImageLoader {
    public static void loadImage(ImageView imageView, int placeholderRes) {
//        ChatImageModule chatImageModule = new ChatImageModule(imageMessageForUI.getSessionId(), imageMessageForUI.getUUID());
//        chatImageModule.setLocalPath(imageMessageForUI.getFileLocalPath());
//        RequestBuilder<Bitmap> requestBuilder = Glide.with(imageView).asBitmap().load(chatImageModule).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(placeholderRes);
//        if (imageView.getScaleType() == ImageView.ScaleType.FIT_CENTER) {
//            requestBuilder = requestBuilder.fitCenter();
//        } else {
//            requestBuilder = requestBuilder.centerCrop();
//        }
//        requestBuilder=requestBuilder.addListener(new RequestListener<Bitmap>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
//                if (e != null) {
//                    S.e("onLoadFailed:"+e.getMessage());
//                }else{
//                    S.e("onLoadFailed:null");
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
//                S.s("onResourceReady:"+bitmap);
//                return false;
//            }
//        });
//        requestBuilder.into(imageView);
    }
}
