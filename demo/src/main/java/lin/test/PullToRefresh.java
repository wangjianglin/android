package lin.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import lin.core.XListView;



public class PullToRefresh extends Activity {

    private XListView listView;
    private List<String> list = new ArrayList<String>();

    private void addDatas(){
        int m = list.size();
        for(int n=0;n<10;n++){
            list.add("stirng "+(n+m));
        }
    }
    private Handler mHandler = new Handler();

    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh);

        listView = (XListView) this.findViewById(R.id.xlistViewId);

        listView.setPullMode(XListView.Mode.BOTH);


//        listView.setPullLoadEnable(true);
//        listView.setPullRefreshEnable(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                addDatas();
                adapter.notifyDataSetChanged();
                listView.complete();
            }
        }, 5000);

        listView.setXListViewListener(new XListView.XListViewListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        addDatas();
                        adapter.notifyDataSetChanged();
                        listView.complete();
                    }
                }, 5000);

            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addDatas();
                        adapter.notifyDataSetChanged();
                        listView.setPullMode(XListView.Mode.BOTH);
                        listView.complete();
                    }
                }, 6000);
            }
            });
        //addDatas();
        adapter = new ArrayAdapter<String>(this, R.layout.item_view) {


            @Override
            public int getCount() { // 获取数据总数量
//                 return 10;
//                return GoodsListViewBig.listRow(mAdapter.goods);
                return list.size();
            }

            @Override
            public int getPosition(String item) { // 获取当前位置
                return list.indexOf(item);
            }

            public long getItemID(int position) {
                return list.get(position).hashCode();
            }

            @Override
            public String getItem(int position) {
                return list.get(position);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) { // 画出XListView
                TextView v = new TextView(this.getContext());

                v.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        300, 1));
                v.setText(list.get(position));
                return v;

            }
        };

        listView.setAdapter(adapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_pull_to_refresh, menu);
//        return true;
//    }


}
