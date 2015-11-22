package com.chiahao.callbacknet.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chiahao.callbacknet.R;
import com.chiahao.callbacknet.domain.Person;
import com.chiahao.callbacknet.global.Constants;
import com.chiahao.callbacknet.net.Engine;
import com.chiahao.callbacknet.net.IDataCallback;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IDataCallback {

    private static final int REQUEST_PERSON_XUTILS = 0;
    private static final int REQUEST_PERSON_ASYNC = 1;
    @Bind(R.id.btn_loaddata_Xutils) Button mBtnLoadDataXutils;
    @Bind(R.id.btn_loaddata_Async) Button mBtnLoadDataAsync;
    @Bind(R.id.btn_clean) Button mBtnClean;
    @Bind(R.id.listview) ListView mListView;
    private ArrayList<Person.PersonEntity> mPersonList = new ArrayList<>();
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        myAdapter = new MyAdapter();
        mListView.setAdapter(myAdapter);

        mBtnLoadDataXutils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataXutils();// 访问网络加载数据
            }
        });
        mBtnLoadDataAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataAsync();
            }
        });
        mBtnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanListView();
            }
        });
    }

    /**
     * 使用AsyncTask访问网络加载数据
     */
    private void getDataAsync() {
        cleanListView();// 多次调用会覆盖先前的数据
        Engine.getInstance(MainActivity.this).getServerDataAsyncTask(Constants.url, REQUEST_PERSON_ASYNC, MainActivity.this);
    }

    /**
     * 使用第三方框架Xutils访问网络加载数据
     */
    public void getDataXutils(){
        cleanListView();// 多次调用会覆盖先前的数据
        Engine.getInstance(MainActivity.this).getServerDataXutils(Constants.url, REQUEST_PERSON_XUTILS, MainActivity.this);
    }

    /**
     * 清除ListView中的数据，方便使用另一种方式显示数据
     */
    private void cleanListView() {
        mPersonList.clear();
        myAdapter.notifyDataSetChanged();
    }

    /**
     * 回调方法，处理服务器返回的数据
     */
    @Override
    public void handleServerResult(int requestCode, int errCode, Object data) {
        switch (requestCode){
            case REQUEST_PERSON_XUTILS:// 使用xutils
                if (errCode == 0){
                    if (data != null) {
                        Gson gson = new Gson();
                        Person person = gson.fromJson((String) data, Person.class);
                        mPersonList.clear();
                        mPersonList.addAll(person.getPerson());
                        myAdapter.notifyDataSetChanged();// 刷新列表
                    }
                }
                break;
            case REQUEST_PERSON_ASYNC: // 使用AsyncTask
                if (errCode == 0){
                    if (data != null) {
                        Gson gson = new Gson();
                        Person person = gson.fromJson((String) data, Person.class);
                        mPersonList.clear();
                        mPersonList.addAll(person.getPerson());
                        myAdapter.notifyDataSetChanged();// 刷新列表
                    }
                }
                break;
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mPersonList.size();
        }

        @Override
        public Object getItem(int position) {
            return mPersonList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                convertView = View.inflate(MainActivity.this,R.layout.item_listview,null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Person.PersonEntity personEntity = mPersonList.get(position);
            holder.mTvName.setText("姓名："+personEntity.getName());
            holder.mTvAge.setText("年龄："+personEntity.getAge());
            holder.mTvSex.setText("性别："+personEntity.getSex());

            return convertView;
        }
        class ViewHolder{
            @Bind(R.id.tv_name) TextView mTvName;
            @Bind(R.id.tv_age) TextView mTvAge;
            @Bind(R.id.tv_sex) TextView mTvSex;

            public ViewHolder(View convertView){
                ButterKnife.bind(this,convertView);
            }
        }
    }

}
