package com.hap.popularmovie.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hap.popularmovie.R;

/**
 * Created by luis on 9/28/17.
 */

public class EmptyScreenView extends LinearLayout {
    private TextView emptyScreenMessage;

    public EmptyScreenView(Context context) {
        this(context, null);
    }

    public EmptyScreenView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.empty_screen_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        emptyScreenMessage = findViewById(R.id.empty_screen_message);
    }

    public void setupEmptyScreen(final ScreenType screenType) {
        if (screenType == null) {
            this.setVisibility(VISIBLE);
            emptyScreenMessage.setText(R.string.error_unknown);
            return;
        }
        switch (screenType) {
            case GONE:
                this.setVisibility(GONE);
                break;
            case EMPTY_MOVIES:
                this.setVisibility(VISIBLE);
                emptyScreenMessage.setText(R.string.empty_movies_message);
                break;
            case ERROR_NO_MOVIES:
                this.setVisibility(VISIBLE);
                emptyScreenMessage.setText(R.string.error_cannot_load_movies);
                break;
        }
    }

    public enum ScreenType {
        /**
         * This type is to hide the empty screen, this means tht we don't have any error or message
         * to display
         */
        GONE,
        /**
         * This type is to show a message about not able to get any movies, is not a network error,
         * is just that we didn't get anything back.
         */
        EMPTY_MOVIES,
        /**
         * This type is to show an error about connection, so we couldn't reach the server to get the movies
         */
        ERROR_NO_MOVIES,
    }
}
