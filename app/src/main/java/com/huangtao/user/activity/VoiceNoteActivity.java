package com.huangtao.user.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huangtao.user.MediaPlayer.MediaPlayerHolder;
import com.huangtao.user.MediaPlayer.PlaybackInfoListener;
import com.huangtao.user.MediaPlayer.PlayerAdapter;
import com.huangtao.user.R;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.model.MeetingNote;
import com.huangtao.user.network.FileManagement;
import com.huangtao.user.network.Network;

import java.io.File;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoiceNoteActivity extends MyActivity {

    @BindView(R.id.text_debug)
    TextView mTextDebug;

    @BindView(R.id.button_play)
    Button mPlayButton;

    @BindView(R.id.button_pause)
    Button mPauseButton;

    @BindView(R.id.button_reset)
    Button mResetButton;

    @BindView(R.id.seekbar_audio)
    SeekBar mSeekbarAudio;

    @BindView(R.id.scroll_container)
    ScrollView mScrollContainer;

    private PlayerAdapter mPlayerAdapter;
    private boolean mUserIsSeeking = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_voice_note;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title_bar;
    }

    @Override
    protected void initView() {
        initializeUI();
        initializeSeekbar();
        initializePlaybackController();
    }

    @Override
    protected void initData() {
        String id = getIntent().getStringExtra("id");
        if(id == null) {
            return;
        }

        Network.getInstance().getMeetingNoteById(id).enqueue(new Callback<MeetingNote>() {
            @Override
            public void onResponse(Call<MeetingNote> call, Response<MeetingNote> response) {
                if(response.body() != null) {
                    MeetingNote meetingNote = response.body();
                    File file = new File(Constants.SAVE_RECORD_DIR + meetingNote.getVoiceFileName());
                    if(file.exists()){
                        file.delete();
                    }

                    FileManagement.download(VoiceNoteActivity.this, meetingNote.getVoiceFileName(), file.getAbsolutePath());
                    mPlayerAdapter.loadMedia(file.getAbsolutePath());

                }
            }

            @Override
            public void onFailure(Call<MeetingNote> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isChangingConfigurations() && mPlayerAdapter.isPlaying()) {
        } else {
            mPlayerAdapter.release();
        }
    }

    private void initializeUI() {
        mPauseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPlayerAdapter.pause();
                    }
                });
        mPlayButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPlayerAdapter.play();
                    }
                });
        mResetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPlayerAdapter.reset();
                    }
                });
    }

    private void initializePlaybackController() {
        MediaPlayerHolder mMediaPlayerHolder = new MediaPlayerHolder(this);
        mMediaPlayerHolder.setPlaybackInfoListener(new PlaybackListener());
        mPlayerAdapter = mMediaPlayerHolder;
    }

    private void initializeSeekbar() {
        mSeekbarAudio.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int userSelectedPosition = 0;

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = true;
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            userSelectedPosition = progress;
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = false;
                        mPlayerAdapter.seekTo(userSelectedPosition);
                    }
                });
    }

    public class PlaybackListener extends PlaybackInfoListener {

        @Override
        public void onDurationChanged(int duration) {
            mSeekbarAudio.setMax(duration);
        }

        @Override
        public void onPositionChanged(int position) {
            if (!mUserIsSeeking) {
                mSeekbarAudio.setProgress(position, true);
            }
        }

        @Override
        public void onStateChanged(@State int state) {
            String stateToString = PlaybackInfoListener.convertStateToString(state);
            onLogUpdated(String.format("onStateChanged(%s)", stateToString));
        }

        @Override
        public void onPlaybackCompleted() {
        }

        @Override
        public void onLogUpdated(String message) {
//            if (mTextDebug != null) {
//                mTextDebug.append(message);
//                mTextDebug.append("\n");
//                // Moves the scrollContainer focus to the end.
//                mScrollContainer.post(
//                        new Runnable() {
//                            @Override
//                            public void run() {
//                                mScrollContainer.fullScroll(ScrollView.FOCUS_DOWN);
//                            }
//                        });
//            }
        }
    }
}
