package com.example.statuslayouttest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zk.statuslayout.widgets.StatusLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 自定义状态切换布局页面
 * @author: zhukai
 * @date: 2019/10/28 11:56
 */
public class MyStatusLayoutActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> mData;
    private MyAdapter mAdapter;
    private StatusLayout mStatusLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_status_layout);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStatusLayout = new StatusLayout.Builder(this)
                .into(mRecyclerView)
                .onRetryListener(new StatusLayout.OnRetryClickListener() {
                    @Override
                    public void onRetry() {
                        Toast.makeText(MyStatusLayoutActivity.this, "点击了重试", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }

    private void initData() {
        mData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                mData.add("咸鱼");
            } else if (i % 3 == 0) {
                mData.add("桃子");
            } else {
                mData.add("腌鹿肉");
            }
        }
        mAdapter = new MyAdapter(MyStatusLayoutActivity.this, mData);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initEvent() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_status_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_show_content:
                mStatusLayout.showContent();
                break;
            case R.id.menu_show_loading:
                mStatusLayout.showLoading();
                break;
            case R.id.menu_show_empty:
                mStatusLayout.showEmpty();
                break;
            case R.id.menu_show_error:
                mStatusLayout.showError();
                break;
            case R.id.menu_show_no_network:
                mStatusLayout.showNoNetwork();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private Context mContext;
        private List<String> mData;

        public MyAdapter(Context context, List<String> data) {
            this.mContext = context;
            this.mData = data;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
            holder.tvName.setText(mData.get(i));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvName;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_name);
            }
        }
    }
}
