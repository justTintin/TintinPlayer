package com.tintin.tintinplayer.controller;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.tintin.tintinplayer.module.VideoModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alive on 2016/1/3.
 */
public class FileOperateController
{
    private static final String TAG = FileOperateController.class.getSimpleName();

    public static final String[] IMAGE_COLUMN = { MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE };

    public static final String[] AUDIO_COLUMN = { MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ALBUM_ID };

    public static final String[] VIDEO_COLUMN = { MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.SIZE, MediaStore.Audio.Media.DURATION,
            MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.ALBUM,
            MediaStore.Video.Media.WIDTH, MediaStore.Video.Media.HEIGHT };

    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

    ////查询视频文件
    public static List<VideoModule> getVideoFile(Context context)
    {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_COLUMN,
                null,
                null,
                null);
        String tp = "video}";

        List<VideoModule> list = new ArrayList<>();
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                VideoModule videoModule1 = new VideoModule();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
                String width = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH));
                String height = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT));
                //String thumbnail = Base64Util.bitmaptoString(getVideoThumbnail(path, 96, 96, MediaStore.Images.Thumbnails.MICRO_KIND));
                //tp = tp+Base64Util.encodeBase64(path)+","+size+","+duration+","+thumbnail+"}";
                //                tp = tp+ Base64Util.encodeBase64(path)+","+size+","+duration+"}";

                videoModule1.setPath(path);
                videoModule1.setDuration(duration);
                videoModule1.setSize(size);
                videoModule1.setAlbum(album);
                videoModule1.setDisplayName(display_name);
                videoModule1.setWidth(width);
                videoModule1.setHeight(height);
                Log.d("aaa", videoModule1.toString());
                list.add(videoModule1);
                while (cursor.moveToNext())
                {
                    VideoModule videoModule = new VideoModule();
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                    size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                    duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                    display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                    album = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
                    width = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH));
                    height = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT));
                    //tp = tp+Base64Util.encodeBase64(path)+","+size+","+duration+","+thumbnail+"}";
                    //                    tp = tp+ Base64Util.encodeBase64(path)+","+size+","+duration+"}";
                    videoModule.setPath(path);
                    videoModule.setDuration(duration);
                    videoModule.setSize(size);
                    videoModule.setAlbum(album);
                    videoModule.setDisplayName(display_name);
                    videoModule.setWidth(width);
                    videoModule.setHeight(height);
                    Log.d("aaa", videoModule.toString());
                    list.add(videoModule);
                }
            }
        }
        return list;
    }
}
