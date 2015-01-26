package com.room.desire.ccmanual;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by desire on 14/12/15.
 */
public class TickingService extends Service implements MediaPlayer.OnPreparedListener, AudioManager.OnAudioFocusChangeListener {

    private static final String TICKING_AUDIO_NAME = "cache.mp3";
    private static String sTickingAudioPath;
    private Binder mBinder;
    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        File audio = new File(getFilesDir(), TICKING_AUDIO_NAME);
        sTickingAudioPath = audio.getAbsolutePath();
        if (!audio.exists()) {
            buildVoice(5);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null) {
            mBinder = new TickBinder();
        }
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("@@@@", "----- on Unbind!");
        if (!isPlaying()) {
            releaseMedia();
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMedia();
        abandonFocus();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onAudioFocusChange(int i) {
        switch (i) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if (mMediaPlayer == null) {
                    startPlay();
                } else if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
                mMediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                if (isPlaying()) {
                    mMediaPlayer.stop();
                }
                releaseMedia();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (isPlaying()) {
                    mMediaPlayer.pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (isPlaying()) {
                    mMediaPlayer.setVolume(0.1f, 1.0f);
                }
                break;
        }
    }

    private void buildVoice(int end) {
        List<InputStream> list = new ArrayList<InputStream>();
        //TODO build voice file
    }

    private void startPlay() {
        if (!isPlaying()) {
            initMedia();

            if (requestFocus()) {
                mMediaPlayer.prepareAsync();
            }
        }
    }

    private void stopPlay() {
        if (isPlaying()) {
            mMediaPlayer.stop();
            abandonFocus();
        }
    }

    private boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    private void initMedia() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                    releaseMedia();
                    return true;
                }
            });

            setDataSourceFromResource(getResources(), mMediaPlayer);
        }
    }

    private void releaseMedia() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void setDataSourceFromResource(Resources resources, MediaPlayer player) {
        try {
//            AssetFileDescriptor afd = resources.openRawResourceFd(res);
//            if (afd != null) {
//                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//                afd.close();
//            }
            player.setDataSource(sTickingAudioPath);
            player.setLooping(true);
        } catch (Exception e) {
            releaseMedia();
        }
    }

    private boolean requestFocus() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    private boolean abandonFocus() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
    }

    class TickBinder extends Binder {

        public void start() {
            startPlay();
        }

        public void stop() {
            stopPlay();
        }

        public boolean isPlaying() {
            return TickingService.this.isPlaying();
        }
    }

}