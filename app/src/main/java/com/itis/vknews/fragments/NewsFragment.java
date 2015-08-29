package com.itis.vknews.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itis.vknews.R;
import com.itis.vknews.adapters.NewsAdapter;
import com.itis.vknews.model.Item;
import com.itis.vknews.utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemClickListener {

    private static final String ARG_TAG = "putSerializable";

    private RecyclerView rv_news;
    private SwipeRefreshLayout refresh_layout;

    private LinearLayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private List<Item> mItems;
    private OnRefreshFragment mRefreshFragmentListener;
    private OnNewsItemClickListener mItemClickListener;

    public interface OnRefreshFragment {
        void onRefresh(boolean pulled);
    }

    public interface OnNewsItemClickListener {
        void onNewsItemClick(Item item);
    }

    public static NewsFragment newInstance(List<Item> items) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TAG, (ArrayList<Item>) items);
        fragment.setArguments(args);
        return fragment;
    }

    public void updateList(ArrayList<Item> items) {
        if (mItems != null) {
            mItems.addAll(items);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mItems = (ArrayList<Item>) args.getSerializable(ARG_TAG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        rv_news = (RecyclerView) view.findViewById(R.id.rv_news);
        refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new NewsAdapter(mItems);
        mAdapter.setItemClickListener(this);

        rv_news.setLayoutManager(mLayoutManager);
        rv_news.setAdapter(mAdapter);
        rv_news.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                mRefreshFragmentListener.onRefresh(false);
            }
        });
        refresh_layout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mRefreshFragmentListener = (OnRefreshFragment) activity;
        mItemClickListener = (OnNewsItemClickListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRefreshFragmentListener = null;
        mItemClickListener = null;
    }

    @Override
    public void onRefresh() {
        refresh_layout.setRefreshing(true);
        if (mRefreshFragmentListener != null) {
            mRefreshFragmentListener.onRefresh(true);
        }
        refresh_layout.setRefreshing(false);
    }

    @Override
    public void onItemClick(int position) {
        if (mItemClickListener != null) mItemClickListener.onNewsItemClick(mItems.get(position));
    }
}
