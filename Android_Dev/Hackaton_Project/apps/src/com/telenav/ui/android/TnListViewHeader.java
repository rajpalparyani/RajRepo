package com.telenav.ui.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.telenav.app.android.scout_us.R;

/**
 * @author jpwang
 * @date 2013-2-21
 */
public class TnListViewHeader extends LinearLayout
{
    private LinearLayout mContainer;

    private ImageView mArrowImageView;

    private ProgressBar mProgressBar;

    private TextView mHintTextView;

    private int mState = STATE_NORMAL;

    private Animation mRotateUpAnim;

    private Animation mRotateDownAnim;

    private final static int ROTATE_ANIM_DURATION = 180;

    public final static int STATE_NORMAL = 0;

    public final static int STATE_READY = 1;

    public final static int STATE_REFRESHING = 2;

    public TnListViewHeader(Context context)
    {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public TnListViewHeader(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context)
    {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tn_list_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mArrowImageView = (ImageView) findViewById(R.id.tnListHeader0Arrow);
        mHintTextView = (TextView) findViewById(R.id.tnListHeader0Hint);
        mProgressBar = (ProgressBar) findViewById(R.id.tnListHeader0Progressbar);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    public void setState(int state)
    {
        if (state == mState)
            return;

        if (state == STATE_REFRESHING)
        {
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state)
        {
            case STATE_NORMAL:
                if (mState == STATE_READY)
                {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING)
                {
                    mArrowImageView.clearAnimation();
                }
                mHintTextView.setText(R.string.tnlistHeader0HintNormal);
                break;
            case STATE_READY:
                if (mState != STATE_READY)
                {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText(R.string.tnListHeader0HintReady);
                }
                break;
            case STATE_REFRESHING:
                mHintTextView.setText(R.string.inProgress);
                break;
            default:
        }

        mState = state;
    }

    public void setVisiableHeight(int height)
    {
        if (height < 0)
            height = 0;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight()
    {
        return mContainer.getHeight();
    }

}
