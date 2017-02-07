package lin.demo.ptr;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import lin.core.ptr.PtrListView;
import lin.core.ptr.PtrView;
import lin.demo.R;
import lin.demo.Utils;
import lin.demo.databinding.ActivityPtrListviewBinding;

public class PtrListActivity extends AppCompatActivity {

    private int listCount = 0;
    private ListAdapter mAdapter = null;
    private PtrListView ptr = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ptr_default);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ptr_listview);

        ActivityPtrListviewBinding binding = ActivityPtrListviewBinding.inflate(LayoutInflater.from(this));
        binding.setHandler(this);
        this.setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        ptr = (PtrListView) binding.getRoot().findViewById(R.id.ptr_default_view);

//        ptr = (PtrListView) this.findViewById(R.id.ptr_default_view);

        ListView listView = ptr.getView();

        mAdapter = new ListAdapter();
        listView.setAdapter(mAdapter);

//        ptr.setOnLoadMoreListener(new PtrView.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(final PtrView ptr) {
//                ptr.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        listCount += 20;
//                        mAdapter.notifyDataSetChanged();
//                        ptr.complete();
//                    }
//                }, 2500);
//            }
//        });

        ptr.setOnRefreshListener(new PtrView.OnRefreshListener() {
            @Override
            public void onRefresh(final PtrView ptr) {
                ptr.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listCount = 20;
                        mAdapter.notifyDataSetChanged();
                        ptr.complete();
                    }
                }, 2500);
            }
        });
    }

    public void onLoadMore() {
        ptr.postDelayed(new Runnable() {
            @Override
            public void run() {
                listCount += 20;
                mAdapter.notifyDataSetChanged();
                ptr.complete();
            }
        }, 2500);
    }

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listCount;
        }

        @Override
        public Object getItem(int position) {
            return Utils.getDigit(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(!(convertView instanceof TextView)){
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_ptr_listview_item,parent,false);
            }
            ((TextView) convertView).setText(Utils.getDigit(position+1));
            return convertView;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
