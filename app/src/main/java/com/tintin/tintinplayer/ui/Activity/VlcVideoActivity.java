package com.tintin.tintinplayer.ui.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tintin.tintinplayer.R;
import com.tintin.tintinplayer.module.VideoModule;
import com.tintin.tintinplayer.util.FileOperate;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.vlc.util.VLCInstance;

import io.vov.vitamio.widget.VideoView;

public class VlcVideoActivity extends Activity implements
        SurfaceHolder.Callback, IVideoPlayer {

    private final static String TAG = "[VlcVideoActivity]";

    //    private SurfaceView mSurfaceView;
    private VideoView mVideoView;
    private LibVLC mMediaPlayer;

    private SurfaceHolder mSurfaceHolder;

    private View mLoadingView;

    private int mVideoHeight;

    private int mVideoWidth;

    private int mVideoVisibleHeight;

    private int mVideoVisibleWidth;

    private int mSarNum;

    private int mSarDen;

    VideoModule videoModule;

    private AudioManager mAudioManager;
    /**
     * 最大声音
     */
    private int mMaxVolume;
    /**
     * 当前声音
     */
    private int mVolume = -1;
    /**
     * 当前亮度
     */
    private float mBrightness = -1f;
    /**
     * 当前缩放模式
     */
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;


    private View mVolumeBrightnessLayout;

    private ImageView mOperationBg;
    private ImageView mOperationPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_vlc);

        initData();
        findViews();

        //		mMediaPlayer.setMediaList();
        //		mMediaPlayer.getMediaList().add(new Media(mMediaPlayer, "http://live.3gv.ifeng.com/zixun.m3u8"), false);
        //		mMediaPlayer.playIndex(0);
        String url = "rtsp://211.139.194.251:554/live/2/13E6330A31193128/5iLd2iNl5nQ2s8r8.sdp";
        String url2 = "http://distribution.bbb3d.renderfarming.net/video/mp4/bbb_sunflower_1080p_30fps_normal.mp4";
        url2 = FileOperate.PathToURI(videoModule.getPath());
        mMediaPlayer.playMRL(url2);
    }

    private void findViews() {
        mVideoView = (VideoView) findViewById(R.id.video);
        mLoadingView = findViewById(R.id.video_loading);
        try {
            mMediaPlayer = VLCInstance.getLibVlcInstance();
        } catch (LibVlcException e) {
            e.printStackTrace();
        }

        mSurfaceHolder = mVideoView.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
        mSurfaceHolder.addCallback(this);

        mMediaPlayer.eventVideoPlayerActivityCreated(true);

        EventHandler em = EventHandler.getInstance();
        em.addHandler(mVlcHandler);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mVideoView.setKeepScreenOn(true);

        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
        mGestureDetector = new GestureDetector(this, new MyGestureListener());
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        videoModule = (VideoModule) bundle.getSerializable("video");
        // ~~~ 绑定数据
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 手势结束
     */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    /**
     * 手势控件
     */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        /**
         * 双击
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
            else
                mLayout++;
            if (mVideoView != null)
                mVideoView.setVideoLayout(mLayout, 0);
            return true;
        }

        /**
         * 滑动
         */
        @SuppressWarnings("deprecation")
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
                onVolumeSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 5.0)// 左边滑动
                onBrightnessSlide((mOldY - y) / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mVideoView.setKeepScreenOn(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.eventVideoPlayerActivityCreated(false);

            EventHandler em = EventHandler.getInstance();
            em.removeHandler(mVlcHandler);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setSurfaceSize(mVideoWidth,
                mVideoHeight,
                mVideoVisibleWidth,
                mVideoVisibleHeight,
                mSarNum,
                mSarDen);
        if (mVideoView != null)
            mVideoView.setVideoLayout(mLayout, 0);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mMediaPlayer != null) {
            mSurfaceHolder = holder;
            mMediaPlayer.attachSurface(holder.getSurface(), this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        mSurfaceHolder = holder;
        if (mMediaPlayer != null) {
            mMediaPlayer.attachSurface(holder.getSurface(), this);//, width, height
        }
        if (width > 0) {
            mVideoHeight = height;
            mVideoWidth = width;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mMediaPlayer != null) {
            mMediaPlayer.detachSurface();
        }
    }

    @Override
    public void setSurfaceSize(int width, int height, int visible_width,
                               int visible_height, int sar_num, int sar_den) {
        mVideoHeight = height;
        mVideoWidth = width;
        mVideoVisibleHeight = visible_height;
        mVideoVisibleWidth = visible_width;
        mSarNum = sar_num;
        mSarDen = sar_den;
        mHandler.removeMessages(HANDLER_SURFACE_SIZE);
        mHandler.sendEmptyMessage(HANDLER_SURFACE_SIZE);
    }

    private static final int HANDLER_BUFFER_START = 1;

    private static final int HANDLER_BUFFER_END = 2;

    private static final int HANDLER_SURFACE_SIZE = 3;

    private static final int SURFACE_BEST_FIT = 0;

    private static final int SURFACE_FIT_HORIZONTAL = 1;

    private static final int SURFACE_FIT_VERTICAL = 2;

    private static final int SURFACE_FILL = 3;

    private static final int SURFACE_16_9 = 4;

    private static final int SURFACE_4_3 = 5;

    private static final int SURFACE_ORIGINAL = 6;

    private int mCurrentSize = SURFACE_BEST_FIT;

    private Handler mVlcHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg == null || msg.getData() == null)
                return;

            switch (msg.getData().getInt("event")) {
                case EventHandler.MediaPlayerTimeChanged:
                    break;
                case EventHandler.MediaPlayerPositionChanged:
                    break;
                case EventHandler.MediaPlayerPlaying:
                    mHandler.removeMessages(HANDLER_BUFFER_END);
                    mHandler.sendEmptyMessage(HANDLER_BUFFER_END);
                    break;
                case EventHandler.MediaPlayerBuffering:
                    break;
                case EventHandler.MediaPlayerLengthChanged:
                    break;
                case EventHandler.MediaPlayerEndReached:
                    //播放完成
                    break;
            }

        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_BUFFER_START:
                    showLoading();
                    break;
                case HANDLER_BUFFER_END:
                    hideLoading();
                    break;
                case HANDLER_SURFACE_SIZE:
                    changeSurfaceSize();
                    break;
            }
        }
    };

    private void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        mLoadingView.setVisibility(View.GONE);
    }

    private void changeSurfaceSize() {
        // get screen size
        int dw = getWindowManager().getDefaultDisplay().getWidth();
        int dh = getWindowManager().getDefaultDisplay().getHeight();

        // calculate aspect ratio
        double ar = (double) mVideoWidth / (double) mVideoHeight;
        // calculate display aspect ratio
        double dar = (double) dw / (double) dh;

        switch (mCurrentSize) {
            case SURFACE_BEST_FIT:
                if (dar < ar)
                    dh = (int) (dw / ar);
                else
                    dw = (int) (dh * ar);
                break;
            case SURFACE_FIT_HORIZONTAL:
                dh = (int) (dw / ar);
                break;
            case SURFACE_FIT_VERTICAL:
                dw = (int) (dh * ar);
                break;
            case SURFACE_FILL:
                break;
            case SURFACE_16_9:
                ar = 16.0 / 9.0;
                if (dar < ar)
                    dh = (int) (dw / ar);
                else
                    dw = (int) (dh * ar);
                break;
            case SURFACE_4_3:
                ar = 4.0 / 3.0;
                if (dar < ar)
                    dh = (int) (dw / ar);
                else
                    dw = (int) (dh * ar);
                break;
            case SURFACE_ORIGINAL:
                dh = mVideoHeight;
                dw = mVideoWidth;
                break;
        }

        mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
        ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();
        lp.width = dw;
        lp.height = dh;
        mVideoView.setLayoutParams(lp);
        mVideoView.invalidate();
    }

    /**
     * 定时隐藏
     */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            // 显示
            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 显示
            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;

        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }

        return super.onTouchEvent(event);
    }

}
