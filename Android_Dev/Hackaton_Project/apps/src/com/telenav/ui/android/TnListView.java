/**
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * This is an user-defined ListView which is added the effect of Drag up/down to refresh list and switching unfocused view and focused view once list item is clicked  
 * For example, the ancestral item view in the list is like this:
 *      1.xxxxxxxxx
 * -------------------------
 *      2.xxxxxxxxx
 * ------------------------
 *      3.xxxxxxxxx
 *    
 *      
 * After Drag up/down, the list will like this:
 *      2.xxxxxxxxx
 * -------------------------
 *      1.xxxxxxxxx
 * ------------------------
 *      3.xxxxxxxxx
 *   
 *      
 * After switching, you can make your list like this: (The old '1.xxxxxxxx' view now is instead by 'A.++++')
 *      A.+++++++++
 *        +++++++++
 *        +++++++++
 * -------------------------
 *      2.xxxxxxxxx
 * ------------------------
 *      3.xxxxxxxxx
 */
package com.telenav.ui.android;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.telenav.app.android.scout_us.R;

/**
 * @author jpwang
 * @date 2013-2-21
 */
public class TnListView extends ListView implements OnScrollListener
{

    private float mLastY = -1; // save event y

    private Scroller mScroller; // used for scroll back

    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private ITnListViewListener mListViewListener;

    // header view
    private TnListViewHeader mHeaderView;

    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;

    private TextView mHeaderTimeView;

    private int mHeaderViewHeight; // header view's height

    private boolean mEnablePullRefresh = false;

    private boolean mPullRefreshing = false; // is refreashing.

    // footer view
    private TnListViewFooter mFooterView;

    private boolean mEnablePullLoad = true;
    
    private boolean mEnableHitBottomLoad = false;

    private boolean mPullLoading;

    private boolean mIsFooterReady = false;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;

    private final static int SCROLLBACK_HEADER = 0;

    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400; // scroll back duration

    private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
                                                        // at bottom, trigger
                                                        // load more.

    private final static float OFFSET_RADIO = 1.8f;

    private final static String REFRESH_TIME_FORMAT = "MM/dd/yyyy hh:mm a";

    protected Animation fadeInAnimation;

    private OnTnItemSelectedChangeListener itemSelectedChangeListener;

    /**
     * @param context
     */
    public TnListView(Context context)
    {
        super(context);
        initWithContext(context);
    }

    public TnListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initWithContext(context);
    }

    public TnListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context)
    {
        fadeInAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.fade_in);
        this.setTag(R.id.TAG_SELECTED_ITEM_INDEX, -1);
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // TnListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new TnListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.tnListHeader0Content);
        mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.tnListHeader0Time);
        setPullRefreshEnable(true);
        addHeaderView(mHeaderView);

        // init footer view
        mFooterView = new TnListViewFooter(context);
        setPullLoadEnable(true);

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                mHeaderViewHeight = mHeaderViewContent.getHeight();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        
        this.setDivider(null);
    }

    @Override
    public void setAdapter(ListAdapter adapter)
    {
        // make sure TnListViewFooter is the last footer view, and only add once.
        if (mIsFooterReady == false)
        {
            mIsFooterReady = true;
            addFooterView(mFooterView);
        }
        super.setAdapter(adapter);
    }

    /**
     * enable or disable pull down refresh feature.
     * 
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable)
    {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh)
        { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        }
        else
        {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     * 
     * @param enable
     */
    public void setPullLoadEnable(boolean enable)
    {
        if ((mEnablePullLoad || mEnableHitBottomLoad) && !enable)
        {
            mFooterView.hide();
            mFooterView.setOnClickListener(null);
        }
        else if (!mEnablePullLoad && enable)
        {
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(TnListViewFooter.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    startLoadMore();
                }
            });
        }
        mEnableHitBottomLoad = false;
        mEnablePullLoad = enable;
    }
    
    public void setHitBottomLoadEnable(boolean enable)
    {
        if ((mEnableHitBottomLoad || mEnablePullLoad) &&!enable)
        {
            mFooterView.hide();
            mFooterView.setOnClickListener(null);
        }
        else if (!mEnableHitBottomLoad && enable)
        {
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(TnListViewFooter.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    startLoadMore();
                }
            });
        }
        mEnablePullLoad = false;
        mEnableHitBottomLoad = enable;
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh()
    {
        if (this.getTag(R.id.TAG_LAST_SELECTED_ITEM_VIEW) instanceof View)
        {
            View lastItemView = (View) this.getTag(R.id.TAG_LAST_SELECTED_ITEM_VIEW);
            ((View) lastItemView.getTag(R.id.TAG_SELECTED_VIEW)).setVisibility(View.GONE);
            ((View) lastItemView.getTag(R.id.TAG_UNSELECTED_VIEW)).setVisibility(View.VISIBLE);
        }
        this.setTag(R.id.TAG_SELECTED_ITEM_INDEX, -1);
        if (mPullRefreshing == true)
        {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore()
    {
        if (mPullLoading == true)
        {
            mPullLoading = false;
            mFooterView.setState(TnListViewFooter.STATE_NORMAL);
        }
    }

    /**
     * set last refresh time
     * 
     * @param time
     */
    public void setRefreshTime(Date time)
    {
        if (time != null)
        {
            mHeaderTimeView.setText(new SimpleDateFormat(REFRESH_TIME_FORMAT).format(time));
        }
    }


    private void updateHeaderHeight(float delta)
    {
        mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing)
        {
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight)
            {
                mHeaderView.setState(TnListViewHeader.STATE_READY);
            }
            else
            {
                mHeaderView.setState(TnListViewHeader.STATE_NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight()
    {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight)
        {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight)
        {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    private void updateFooterHeight(float delta)
    {
        int height = mFooterView.getBottomMargin() + (int) delta;
        if (mEnablePullLoad && !mPullLoading)
        {
            if (height > PULL_LOAD_MORE_DELTA)
            { // height enough to invoke load
              // more.
                mFooterView.setState(TnListViewFooter.STATE_READY);
            }
            else
            {
                mFooterView.setState(TnListViewFooter.STATE_NORMAL);
            }
        }
        mFooterView.setBottomMargin(height);

        // setSelection(mTotalItemCount - 1); // scroll to bottom
    }

    private void resetFooterHeight()
    {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0)
        {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore()
    {
        if (mPullLoading)
        {
            return;
        }
        mPullLoading = true;
        mFooterView.setState(TnListViewFooter.STATE_LOADING);
        if (mListViewListener != null)
        {
            mListViewListener.onLoadMore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (mLastY == -1)
        {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0))
                {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                }
                else if (getLastVisiblePosition() == mTotalItemCount - 1 && (mFooterView.getBottomMargin() > 0 || deltaY < 0))
                {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0)
                {
                    // invoke refresh
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight)
                    {
                        startRefresh();
                    }
                    resetHeaderHeight();
                }
                if (getLastVisiblePosition() == mTotalItemCount - 1)
                {
                    // invoke load more.
                    if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA)
                    {
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void startRefresh()
    {
        if (mPullRefreshing)
        {
            return;
        }
        mPullRefreshing = true;
        mHeaderView.setState(TnListViewHeader.STATE_REFRESHING);
        if (mListViewListener != null)
        {
            mListViewListener.onRefresh();
        }
    }

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            if (mScrollBack == SCROLLBACK_HEADER)
            {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            }
            else
            {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l)
    {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        if (mScrollListener != null)
        {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        // send to user's listener
        mTotalItemCount = totalItemCount;        
        if( mEnableHitBottomLoad && (firstVisibleItem + visibleItemCount) == totalItemCount)
        {
            startLoadMore();
        }
        if (mScrollListener != null)
        {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }


    public void setTnListViewListener(ITnListViewListener l)
    {
        mListViewListener = l;
    }

    /**
     * Make the list item switching between focused and unfocused view, and add a fade in animation to make it more smooth
     */
    public boolean performItemClick(final View view, final int position, long id)
    {
        if (!super.performItemClick(view, position, id))
        {
            if (view.getTag(R.id.TAG_SELECTED_VIEW) instanceof View && view.getTag(R.id.TAG_UNSELECTED_VIEW) instanceof View)
            {
                View selectedView = (View) view.getTag(R.id.TAG_SELECTED_VIEW);
                View unselectedView = (View) view.getTag(R.id.TAG_UNSELECTED_VIEW);
                if (this.getTag(R.id.TAG_SELECTED_ITEM_INDEX).equals(view.getTag(R.id.TAG_ITEM_INDEX)))
                {
                    selectedView.setVisibility(View.GONE);
                    if (fadeInAnimation != null)
                    {
                        unselectedView.startAnimation(fadeInAnimation);
                    }
                    unselectedView.setVisibility(View.VISIBLE);
                    setNewSelectedItemIndex(-1);
                }
                else
                {
                    if (this.getTag(R.id.TAG_LAST_SELECTED_ITEM_VIEW) instanceof View)
                    {
                        View lastItemView = (View) this.getTag(R.id.TAG_LAST_SELECTED_ITEM_VIEW);
                        if (lastItemView != null && lastItemView != view
                                && this.getTag(R.id.TAG_SELECTED_ITEM_INDEX).equals(lastItemView.getTag(R.id.TAG_ITEM_INDEX))
                                && lastItemView.getTag(R.id.TAG_SELECTED_VIEW) instanceof View
                                && lastItemView.getTag(R.id.TAG_UNSELECTED_VIEW) instanceof View)
                        {
                            ((View) lastItemView.getTag(R.id.TAG_SELECTED_VIEW)).setVisibility(View.GONE);
                            if (fadeInAnimation != null)
                            {
                                ((View) lastItemView.getTag(R.id.TAG_UNSELECTED_VIEW)).startAnimation(fadeInAnimation);
                            }
                            ((View) lastItemView.getTag(R.id.TAG_UNSELECTED_VIEW)).setVisibility(View.VISIBLE);
                        }
                    }
                    unselectedView.setVisibility(View.GONE);
                    if (fadeInAnimation != null)
                    {
                        selectedView.startAnimation(fadeInAnimation);
                    }
                    selectedView.setVisibility(View.VISIBLE);
                    setNewSelectedItemIndex((Integer) view.getTag(R.id.TAG_ITEM_INDEX));
                    view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
                    {
                        
                        @Override
                        public void onGlobalLayout()
                        {
                            TnListView.this.smoothScrollToPosition(position);
                            getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    });
                }

                this.setTag(R.id.TAG_LAST_SELECTED_ITEM_VIEW, view);
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    private void setNewSelectedItemIndex(int itemIndex)
    {
        Integer oldItemIndex = (this.getTag(R.id.TAG_SELECTED_ITEM_INDEX) instanceof Integer) ? ((Integer) this
                .getTag(R.id.TAG_SELECTED_ITEM_INDEX)) : -1;
        this.setTag(R.id.TAG_SELECTED_ITEM_INDEX, itemIndex);
        if (this.itemSelectedChangeListener != null)
        {
            this.itemSelectedChangeListener.OnTnItemSelectedChange(oldItemIndex, itemIndex);
        }
    }


    /**
     * implements this interface to get refresh/load more event.
     */
    public interface ITnListViewListener
    {
        public void onRefresh();

        public void onLoadMore();
    }

    public interface OnTnItemSelectedChangeListener
    {
        public void OnTnItemSelectedChange(int oldItemIndex, int newItemIndex);
    }

    /**
     * Tell the invoker the item selecting changed
     * @param itemSelectedChangeListener
     */
    public void setOnTnItemSelectedChangeListener(OnTnItemSelectedChangeListener itemSelectedChangeListener)
    {
        this.itemSelectedChangeListener = itemSelectedChangeListener;
    }

    /**
     * @deprecated please use {@link #setOnTnItemClickListener(OnTnItemClickListener)} instead
     */
    @Deprecated
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        if (listener instanceof OnTnItemClickListener)
        {
            super.setOnItemClickListener(listener);
        }
        else
        {
            super.setOnItemClickListener(new OnTnItemClickListener(listener)
            {
                protected void onTnItemClick(AdapterView<?> parent, View view, int itemIndex, long id)
                {
                }
            });
        }
    }

    public void setOnTnItemClickListener(OnTnItemClickListener tnItemClickListener)
    {
        super.setOnItemClickListener(tnItemClickListener);
    }
    
    /**
     * 
     * @deprecated please use {@link #setOnTnItemLongClickListener(OnTnItemLongClickListener)} instead
     */
    @Deprecated
    public void setOnItemLongClickListener(OnItemLongClickListener listener)
    {
        if (listener instanceof OnTnItemLongClickListener)
        {
            super.setOnItemLongClickListener(listener);
        }
        else
        {
            super.setOnItemLongClickListener(new OnTnItemLongClickListener(listener){

                protected boolean onTnItemLongClick(AdapterView<?> parent, View view, int position, long id)
                {
                    return false;
                }
                
            });
        }
    }
    
    public void setOnTnItemLongClickListener(OnTnItemLongClickListener listener)
    {
        super.setOnItemLongClickListener(listener);
    }

}
