package com.huangtao.user.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangtao.user.R;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.model.MeetingNote;
import com.huangtao.user.model.User;
import com.huangtao.user.network.Network;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HtmlNoteActivity extends MyActivity implements View.OnClickListener {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.owner)
    TextView owner;

    @BindView(R.id.favorite)
    TextView favorite;

    @BindView(R.id.favorite_icon)
    ImageView favoriteIcon;

    @BindView(R.id.text)
    HtmlTextView mText;

    private MeetingNote meetingNote;
    private boolean isFavorite;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_html_note;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title_bar;
    }

    @Override
    protected void initView() {
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

                    title.setText(TextUtils.isEmpty(meetingNote.getTitle()) ? "无标题" : meetingNote.getTitle());
                    if(meetingNote.getCollectorIds() != null && meetingNote.getCollectorIds().size() > 0) {
                        favorite.setText(meetingNote.getCollectorIds().size() + "人收藏");
                    } else {
                        favorite.setVisibility(View.GONE);
                    }

                    isFavorite = meetingNote.isFavorite(Constants.uid);
                    favoriteIcon.setImageResource(isFavorite ? R.mipmap.ic_note_favorite_true : R.mipmap.ic_note_favorite_false);
                    favoriteIcon.setOnClickListener(HtmlNoteActivity.this);

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

                    if(!TextUtils.isEmpty(meetingNote.getNote()))
                        mText.setHtml(meetingNote.getNote(), new HtmlHttpImageGetter(mText));
                }
            }

            @Override
            public void onFailure(Call<MeetingNote> call, Throwable t) {

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
}
