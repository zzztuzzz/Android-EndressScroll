package com.pratap.endlessrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<Student> studentList;

    //TODO:handlerって何？
    //メインスレッド以外にウラで処理を走らせたいときに、
    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //変数を置くよ
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        //データを置くよ
        studentList = new ArrayList<Student>();
        handler = new Handler();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Android Students");

        }
        //データのセット。
        loadData();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        mAdapter = new DataAdapter(studentList, mRecyclerView);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);
        //	 mAdapter.notifyDataSetChanged();


        // studentlistが空だと、visible,gone,でオンオフするのかな。
        if (studentList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        //adapterに、用意。
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //remove progress item これで、プログラスが出た後、消える。
                        studentList.remove(studentList.size() - 1);
                        mAdapter.notifyItemRemoved(studentList.size());

                        //add items one by one // for文ぶん回して、値の挿入。
                        // List<student>　みたいに、add要素をする部分。
                        int start = studentList.size();
                        int end = start + 20;


                        for (int i = start + 1; i <= end; i++) {
                            studentList.add(new Student("Student " + i, "AndroidStudent" + i + "@gmail.com"));
                            //TODO: notifyiteminserted?
                            //
                            mAdapter.notifyItemInserted(studentList.size());
                        }

                        mAdapter.setLoaded();
                       //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });

    }


    // load initial data
    private void loadData() {

        for (int i = 1; i <= 20; i++) {
            studentList.add(new Student("Student " + i, "androidstudent" + i + "@gmail.com"));

        }


    }


}
