package com.zk.statuslayout.widgets;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zk.statuslayout.R;

/**
 * @description: 自定义状态切换布局
 * @author: zhukai
 * @date: 2018/12/3 13:49
 */
public class StatusLayout extends FrameLayout {

    private Context mContext;

    // 内容视图
    private View mContentView;
    // 加载中视图
    private View mLoadingView;
    // 空数据视图
    private View mEmptyView;
    // 错误视图
    private View mErrorView;
    // 无网络视图
    private View mNoNetworkView;

    // 空数据提示文字
    private String mEmptyText;
    // 错误提示文字
    private String mErrorText;
    // 点击重试按钮文字
    private String mRetryText;
    // 无网络提示文字
    private String mNoNetworkText;
    // 点击重试监听
    private OnRetryClickListener mListener;

    // 当前状态
    private int currentStatus = -1;
    // 状态——正常显示内容
    public final static int TYPE_STATUS_CONTENT = 0;
    // 状态——加载中
    public final static int TYPE_STATUS_LOADING = 1;
    // 状态——空数据
    public final static int TYPE_STATUS_EMPTY = 2;
    // 状态——发生错误
    public final static int TYPE_STATUS_ERROR = 3;
    // 状态——无网络
    public final static int TYPE_STATUS_NO_NETWORK = 4;

    public StatusLayout(@NonNull Context context) {
        this(context, null);
    }

    public StatusLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    /**
     * 获取加载中视图
     *
     * @return 加载中视图
     */
    public View getLoadingView() {
        return mLoadingView;
    }

    /**
     * 空数据视图
     *
     * @return 空数据视图
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * 设置错误视图
     *
     * @return 错误视图
     */
    public View getErrorView() {
        return mErrorView;
    }

    /**
     * 设置无网络视图
     *
     * @return 无网络视图
     */
    public View getNoNetworkView() {
        return mNoNetworkView;
    }

    /**
     * 获取当前状态
     *
     * @return 当前状态
     */
    public int getCurrentStatus() {
        return currentStatus;
    }

    /**
     * 显示内容视图
     */
    public void showContent() {
        showStatusView(TYPE_STATUS_CONTENT);
    }

    /**
     * 显示加载中视图
     */
    public void showLoading() {
        // 设置默认状态视图
        if (mLoadingView == null) {
            mLoadingView = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_view, this, false);
            addView(mLoadingView);
        }
        showStatusView(TYPE_STATUS_LOADING);
    }

    /**
     * 显示空数据视图
     */
    public void showEmpty() {
        // 设置默认状态视图
        if (mEmptyView == null) {
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view, this, false);
            // 设置空数据提示文字
            TextView tvEmpty = mEmptyView.findViewById(R.id.tv_empty);
            if (!TextUtils.isEmpty(mEmptyText)) {
                tvEmpty.setText(mEmptyText);
            }
            addView(mEmptyView);
        }
        showStatusView(TYPE_STATUS_EMPTY);
    }

    /**
     * 显示错误视图
     */
    public void showError() {
        // 设置默认状态视图
        if (mErrorView == null) {
            mErrorView = LayoutInflater.from(mContext).inflate(R.layout.layout_error_view, this, false);
            // 设置错误提示文字
            TextView tvError = mErrorView.findViewById(R.id.tv_error);
            if (!TextUtils.isEmpty(mErrorText)) {
                tvError.setText(mErrorText);
            }
            // 设置点击重试按钮文字
            TextView tvRetry = mErrorView.findViewById(R.id.tv_retry);
            if (!TextUtils.isEmpty(mRetryText)) {
                tvRetry.setText(mRetryText);
            }
            tvRetry.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击重试按钮
                    if (mListener != null) {
                        mListener.onRetry();
                    }
                }
            });
            addView(mErrorView);
        }
        showStatusView(TYPE_STATUS_ERROR);
    }

    /**
     * 显示无网络视图
     */
    public void showNoNetwork() {
        // 设置默认状态视图
        if (mNoNetworkView == null) {
            mNoNetworkView = LayoutInflater.from(mContext).inflate(R.layout.layout_no_network_view, this, false);
            // 设置无网络提示文字
            TextView tvNoNetwork = mNoNetworkView.findViewById(R.id.tv_no_network);
            if (!TextUtils.isEmpty(mNoNetworkText)) {
                tvNoNetwork.setText(mNoNetworkText);
            }
            addView(mNoNetworkView);
        }
        showStatusView(TYPE_STATUS_NO_NETWORK);
    }

    /**
     * 显示状态视图
     *
     * @param status 要显示的状态
     */
    private void showStatusView(int status) {
        if (currentStatus == status) {
            return;
        }
        // 更新当前状态
        currentStatus = status;
        // 隐藏所有视图
        hideAllStatusView();
        switch (status) {
            case TYPE_STATUS_CONTENT:
                mContentView.setVisibility(VISIBLE);
                break;
            case TYPE_STATUS_LOADING:
                mLoadingView.setVisibility(VISIBLE);
                break;
            case TYPE_STATUS_EMPTY:
                mEmptyView.setVisibility(VISIBLE);
                break;
            case TYPE_STATUS_ERROR:
                mErrorView.setVisibility(VISIBLE);
                break;
            case TYPE_STATUS_NO_NETWORK:
                mNoNetworkView.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏所有视图
     */
    private void hideAllStatusView() {
        if (mContentView != null) {
            mContentView.setVisibility(GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(GONE);
        }
        if (mNoNetworkView != null) {
            mNoNetworkView.setVisibility(GONE);
        }
    }

    /**
     * 建造者模式创建StatusLayout
     */
    public static class Builder {

        private LayoutInflater mInflater;
        private StatusLayout mStatusLayout;

        public Builder(Context context) {
            mInflater = LayoutInflater.from(context);
            mStatusLayout = new StatusLayout(context);
        }

        /**
         * 将状态布局绑定到Activity
         *
         * @param activity 目标Activity
         * @return Builder
         */
        public Builder into(Activity activity) {
            // 获取contentView
            ViewGroup contentView = activity.findViewById(android.R.id.content);
            into(contentView.getChildAt(0));
            return this;
        }

        /**
         * 将状态布局绑定到Fragment
         *
         * @param fragment 目标Fragment
         * @return Builder
         */
        public Builder into(Fragment fragment) {
            into(fragment.getView());
            return this;
        }

        /**
         * 将状态布局绑定到指定View
         *
         * @param view 目标View
         * @return Builder
         */
        public Builder into(View view) {
            if (view == null) {
                throw new IllegalArgumentException("view can not be null");
            }
            // 给内容视图赋值
            mStatusLayout.mContentView = view;
            // 移除StatusLayout的所有子View
            mStatusLayout.removeAllViews();
            // 将目标View外嵌套一层StatusLayout
            ViewGroup parent = (ViewGroup) view.getParent();
            int index = parent.indexOfChild(view) < 0 ? 0 : parent.indexOfChild(view);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            parent.removeView(view);
            parent.addView(mStatusLayout, index, layoutParams);
            mStatusLayout.addView(view);
            return this;
        }

        /**
         * 设置加载中视图
         *
         * @param loadingResId 加载中布局资源id
         * @return Builder
         */
        public Builder loadingView(int loadingResId) {
            View loadingView = mInflater.inflate(loadingResId, mStatusLayout, false);
            mStatusLayout.mLoadingView = loadingView;
            mStatusLayout.addView(loadingView);
            return this;
        }

        /**
         * 设置加载中视图
         *
         * @param loadingView 加载中View
         * @return Builder
         */
        public Builder loadingView(View loadingView) {
            mStatusLayout.mLoadingView = loadingView;
            mStatusLayout.addView(loadingView);
            return this;
        }

        /**
         * 设置空数据视图
         *
         * @param emptyResId 空数据布局资源id
         * @return Builder
         */
        public Builder emptyView(int emptyResId) {
            View emptyView = mInflater.inflate(emptyResId, mStatusLayout, false);
            mStatusLayout.mEmptyView = emptyView;
            mStatusLayout.addView(emptyView);
            return this;
        }

        /**
         * 设置空数据视图
         *
         * @param emptyView 空数据View
         * @return
         */
        public Builder emptyView(View emptyView) {
            mStatusLayout.mEmptyView = emptyView;
            mStatusLayout.addView(emptyView);
            return this;
        }

        /**
         * 设置空数据提示文字
         *
         * @param emptyText 空数据提示文字
         * @return Builder
         */
        public Builder emptyText(String emptyText) {
            mStatusLayout.mEmptyText = emptyText;
            return this;
        }

        /**
         * 设置错误视图
         *
         * @param errorResId 错误布局资源id
         * @return Builder
         */
        public Builder errorView(int errorResId) {
            View errorView = mInflater.inflate(errorResId, mStatusLayout, false);
            mStatusLayout.mErrorView = errorView;
            mStatusLayout.addView(errorView);
            return this;
        }

        /**
         * 设置错误视图
         *
         * @param errorView 错误View
         * @return Builder
         */
        public Builder errorView(View errorView) {
            mStatusLayout.mErrorView = errorView;
            mStatusLayout.addView(errorView);
            return this;
        }

        /**
         * 设置错误提示文字
         *
         * @param errorText 错误提示文字
         * @return Builder
         */
        public Builder errorText(String errorText) {
            mStatusLayout.mErrorText = errorText;
            return this;
        }

        /**
         * 设置点击重试按钮文字
         *
         * @param retryText 点击重试按钮文字
         * @return Builder
         */
        public Builder retryText(String retryText) {
            mStatusLayout.mRetryText = retryText;
            return this;
        }

        /**
         * 设置无网络视图
         *
         * @param noNetworkResId 无网络布局资源id
         * @return Builder
         */
        public Builder noNetworkView(int noNetworkResId) {
            View noNetworkView = mInflater.inflate(noNetworkResId, mStatusLayout, false);
            mStatusLayout.mNoNetworkView = noNetworkView;
            mStatusLayout.addView(noNetworkView);
            return this;
        }

        /**
         * 设置无网络视图
         *
         * @param noNetworkView 无网络View
         * @return Builder
         */
        public Builder noNetworkView(View noNetworkView) {
            mStatusLayout.mNoNetworkView = noNetworkView;
            mStatusLayout.addView(noNetworkView);
            return this;
        }

        /**
         * 设置无网络提示文字
         *
         * @param noNetworkText 无网络提示文字
         * @return Builder
         */
        public Builder noNetworkText(String noNetworkText) {
            mStatusLayout.mNoNetworkText = noNetworkText;
            return this;
        }

        /**
         * 设置点击重试监听
         *
         * @param listener 点击重试监听
         * @return Builder
         */
        public Builder onRetryListener(OnRetryClickListener listener) {
            mStatusLayout.mListener = listener;
            return this;
        }

        /**
         * 创建状态布局
         *
         * @return StatusLayout
         */
        public StatusLayout create() {
            // 默认显示内容视图
            mStatusLayout.showContent();
            return mStatusLayout;
        }
    }

    /**
     * 点击重试监听
     */
    public interface OnRetryClickListener {
        void onRetry();
    }
}
