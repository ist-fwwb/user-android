package com.huangtao.user.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.huangtao.user.model.User;
import com.huangtao.user.network.FileManagement;
import com.huangtao.user.network.Network;

import java.io.File;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoiceNoteActivity extends MyActivity implements View.OnClickListener {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.owner)
    TextView owner;

    @BindView(R.id.favorite)
    TextView favorite;

    @BindView(R.id.favorite_icon)
    ImageView favoriteIcon;

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

    private MeetingNote meetingNote;
    private PlayerAdapter mPlayerAdapter;
    private boolean mUserIsSeeking = false;
    private boolean isFavorite;

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
                    meetingNote = response.body();
                    File file = new File(Constants.SAVE_RECORD_DIR + meetingNote.getVoiceFileName());
                    if(!file.exists()) {
                        FileManagement.download(VoiceNoteActivity.this, meetingNote.getVoiceFileName(), Constants.SAVE_RECORD_DIR);
                    }
                    mPlayerAdapter.loadMedia(file.getAbsolutePath());

                    title.setText(TextUtils.isEmpty(meetingNote.getTitle()) ? "无标题" : meetingNote.getTitle());
                    if(meetingNote.getCollectorIds() != null && meetingNote.getCollectorIds().size() > 0) {
                        favorite.setText(meetingNote.getCollectorIds().size() + "人收藏");
                    } else {
                        favorite.setVisibility(View.GONE);
                    }

                    isFavorite = meetingNote.isFavorite(Constants.uid);
                    favoriteIcon.setImageResource(isFavorite ? R.mipmap.ic_note_favorite_true : R.mipmap.ic_note_favorite_false);
                    favoriteIcon.setOnClickListener(VoiceNoteActivity.this);

                    Network.getInstance().queryUserById(meetingNote.getOwnerId()).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.body() != null) {
                                owner.setText(response.body().getName());
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
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

    @Override
    public void onClick(View v) {
        if (v == favoriteIcon) {
            // 收藏 or 取消收藏
            Call<MeetingNote> call;
            if (isFavorite) {
                call = Network.getInstance().removeFavoriteNote(meetingNote.getId(), Constants.uid);
            } else {
                call = Network.getInstance().addFavoriteNote(meetingNote.getId(), Constants.uid);
            }
            call.enqueue(new Callback<MeetingNote>() {
                @Override
                public void onResponse(Call<MeetingNote> call, Response<MeetingNote> response) {
                    if (isFavorite) {
                        isFavorite = false;
                        favoriteIcon.setImageResource(R.mipmap.ic_note_favorite_false);
                        toast("取消收藏成功");
                    } else {
                        isFavorite = true;
                        favoriteIcon.setImageResource(R.mipmap.ic_note_favorite_true);
                        toast("收藏成功");
                    }
                }

                @Override
                public void onFailure(Call<MeetingNote> call, Throwable t) {
                    t.printStackTrace();
                    toast("操作失败");
                }
            });
        }
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
        }

        @Override
        public void onPlaybackCompleted() {
        }
    }
}
