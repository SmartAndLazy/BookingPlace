package ru.nutscoon.mapsproject;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class CommentDialog extends DialogFragment {

    private RatingBar rb;
    private EditText commentEt;
    private Button postBtn;

    private Dialog dialog;
    private PostCommentCallback postCommentCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.post_comment_dialog, container);
        rb = v.findViewById(R.id.ratingBar);
        commentEt = v.findViewById(R.id.comment);
        postBtn = v.findViewById(R.id.post_comment_btn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postCommentCallback.onPost((double) rb.getRating(), commentEt.getText().toString());
                dialog.cancel();
            }
        });

        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return dialog;
    }

    public void setPostCommentCallback(PostCommentCallback postCommentCallback) {
        this.postCommentCallback = postCommentCallback;
    }

    public interface PostCommentCallback{
        void onPost(double rate, String comment);
    }
}
