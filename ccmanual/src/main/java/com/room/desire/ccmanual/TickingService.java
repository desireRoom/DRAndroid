package com.room.desire.ccmanual;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by desire on 14/12/15.
 */
public class TickingService extends Service implements MediaPlayer.OnPreparedListener, AudioManager.OnAudioFocusChangeListener {
    public static final String ACTION_REBUILD_AUDIO = "com.room.desire.ccm.action_REBUILD_AUDIO";
    public static final String EXTRA_REBUILD_AUDIO_END = "room.desire.ccm.extra_REBUILD_AUDIO_END";

    //    private static final String TICKING_AUDIO_CACHE_NAME = "cache.mp3";
//    private static final String TICKING_AUDIO_NAME = "audio.mp3";
//    private static String sTickingAudioPath;
    private Binder mBinder;
    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
//        File cache = new File(getFilesDir(), TICKING_AUDIO_CACHE_NAME);
//        File audio = new File(getFilesDir(), TICKING_AUDIO_NAME);
//        sTickingAudioPath = audio.getAbsolutePath();
//        if (!audio.exists()) {
//        buildVoice(5, cache, audio);
//        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_REBUILD_AUDIO.equals(action)) {
                stopPlay();
                releaseMedia();
//                int end = intent.getIntExtra(EXTRA_REBUILD_AUDIO_END, 0);
//                File cache = new File(getFilesDir(), TICKING_AUDIO_CACHE_NAME);
//                File audio = new File(getFilesDir(), TICKING_AUDIO_NAME);
//                buildVoice(end, cache, audio);
            }
        }
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

    private synchronized void buildVoice(int end, File cache, File audio) {
        if (cache == null || audio == null) {
            return;
        }

        if (end > 9) {
            end = 9;
        } else if (end < 0) {
            end = 0;
        }
        //TODO build voice file
        List<InputStream> list = new ArrayList<InputStream>();
        InputStream is;
        for (int i = 0; i <= end; i++) {
            try {
                is = getResources().openRawResource(getRawFileResId(i));
                Log.d("@@@@", "----- build voice && index = " + i + " && is = " + is);
                list.add(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.d("@@@@", "----- build voice && size = " + list.size());
        if (list.size() > 0) {
            cache.deleteOnExit();
            FileOutputStream fos = null;
            try {
                cache.createNewFile();
                fos = new FileOutputStream(cache);
                int bytesRead;
                byte[] buf = new byte[4 * 1024];
                byte[] headBuf = new byte[44];
                for (int i = 0; i < list.size(); i++) {
                    is = list.get(i);
                    if (is != null && is.read(headBuf) != -1) {
                        while ((bytesRead = is.read(buf)) != -1) {
                            fos.write(buf, 0, bytesRead);
                        }
                    }

                    Log.d("@@@@", "----- write voice && i = " + i + " && is = " + is);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                for (InputStream input : list) {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        try {
            convertAudioFiles(cache, audio);
        } catch (Exception e) {

        }
    }

    private int getRawFileResId(int index) {
        int resId = 0;
//        switch (index) {
//            case 0:
//                resId = R.raw.ccm_0;
//                break;
//            case 1:
//                resId = R.raw.ccm_1;
//                break;
//            case 2:
//                resId = R.raw.ccm_2;
//                break;
//            case 3:
//                resId = R.raw.ccm_3;
//                break;
//            case 4:
//                resId = R.raw.ccm_4;
//                break;
//            case 5:
//                resId = R.raw.ccm_5;
//                break;
//            case 6:
//                resId = R.raw.ccm_6;
//                break;
//            case 7:
//                resId = R.raw.ccm_7;
//                break;
//            case 8:
//                resId = R.raw.ccm_8;
//                break;
//            case 9:
//                resId = R.raw.ccm_9;
//                break;
//        }
        return resId;
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
//            player.setDataSource(sTickingAudioPath);
            AssetFileDescriptor afd = resources.openRawResourceFd(R.raw.ccm0_5);
            if (afd != null) {
                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
            }
            player.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
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

    // ////////////////////////////////////////////////
    private void convertAudioFiles(File src, File target) throws Exception {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(target);

        // ���㳤��
        byte[] buf = new byte[1024 * 4];
        int size = fis.read(buf);
        int PCMSize = 0;
        while (size != -1) {
            PCMSize += size;
            size = fis.read(buf);
        }
        fis.close();

        // �����������ʵȵȡ������õ���8λ����� 44100 hz
        WaveHeader header = new WaveHeader();
        // �����ֶ� = ���ݵĴ�С��PCMSize) +
        // ͷ���ֶεĴ�С(������ǰ��4�ֽڵı�ʶ��RIFF�Լ�fileLength�����4�ֽ�)
        header.fileLength = PCMSize + (44 - 8);
        header.FmtHdrLeth = 16;
        // header.BitsPerSample = 8;
        header.BitsPerSample = 16;
        header.Channels = 1;
        header.FormatTag = 0x0001;
        // header.SamplesPerSec = 44100;
        header.SamplesPerSec = 22050;
        header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
        header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
        header.DataHdrLeth = PCMSize;

        byte[] h = header.getHeader();

        assert h.length == 44; // WAV��׼��ͷ��Ӧ����44�ֽ�
        // write header
        fos.write(h, 0, h.length);
        // write data stream
        fis = new FileInputStream(src);
        size = fis.read(buf);
        while (size != -1) {
            fos.write(buf, 0, size);
            size = fis.read(buf);
        }
        fis.close();
        fos.close();
        // System.out.println("Convert OK!");
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

    public class WaveHeader {
        public final char fileID[] = {
                'R', 'I', 'F', 'F'
        };

        public int fileLength;

        public char wavTag[] = {
                'W', 'A', 'V', 'E'
        };
        ;

        public char FmtHdrID[] = {
                'f', 'm', 't', ' '
        };

        public int FmtHdrLeth;

        public short FormatTag;

        public short Channels;

        public int SamplesPerSec;

        public int AvgBytesPerSec;

        public short BlockAlign;

        public short BitsPerSample;

        public char DataHdrID[] = {
                'd', 'a', 't', 'a'
        };

        public int DataHdrLeth;

        public byte[] getHeader() throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            WriteChar(bos, fileID);
            WriteInt(bos, fileLength);
            WriteChar(bos, wavTag);
            WriteChar(bos, FmtHdrID);
            WriteInt(bos, FmtHdrLeth);
            WriteShort(bos, FormatTag);
            WriteShort(bos, Channels);
            WriteInt(bos, SamplesPerSec);
            WriteInt(bos, AvgBytesPerSec);
            WriteShort(bos, BlockAlign);
            WriteShort(bos, BitsPerSample);
            WriteChar(bos, DataHdrID);
            WriteInt(bos, DataHdrLeth);
            bos.flush();
            byte[] r = bos.toByteArray();
            bos.close();
            return r;
        }

        private void WriteShort(ByteArrayOutputStream bos, int s) throws IOException {
            byte[] mybyte = new byte[2];
            mybyte[1] = (byte) ((s << 16) >> 24);
            mybyte[0] = (byte) ((s << 24) >> 24);
            bos.write(mybyte);
        }

        private void WriteInt(ByteArrayOutputStream bos, int n) throws IOException {
            byte[] buf = new byte[4];
            buf[3] = (byte) (n >> 24);
            buf[2] = (byte) ((n << 8) >> 24);
            buf[1] = (byte) ((n << 16) >> 24);
            buf[0] = (byte) ((n << 24) >> 24);
            bos.write(buf);
        }

        private void WriteChar(ByteArrayOutputStream bos, char[] id) {
            for (int i = 0; i < id.length; i++) {
                char c = id[i];
                bos.write(c);
            }
        }
    }

}
