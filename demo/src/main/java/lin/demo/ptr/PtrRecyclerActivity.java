package lin.demo.ptr;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import lin.core.ptr.PtrListView;
import lin.core.ptr.PtrRecyclerView;
import lin.core.ptr.PtrView;
import lin.demo.R;
import lin.demo.Utils;

public class PtrRecyclerActivity extends AppCompatActivity {

    private int listCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ptr_default);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptr_recyclerview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        final PtrRecyclerView ptr = (PtrRecyclerView) this.findViewById(R.id.ptr_default_view);

        RecyclerView listView = ptr.getView();

        final SimpleRecyclerAdapter mAdapter = new SimpleRecyclerAdapter();
        listView.setAdapter(mAdapter);

        ptr.setOnLoadMoreListener(new PtrView.OnLoadMoreListener() {
            @Override
            public void onLoadMore(final PtrView ptr) {
                ptr.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listCount += 20;
                        mAdapter.notifyDataSetChanged();
                        ptr.complete();
                    }
                }, 2500);
            }
        });

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

//        ptr.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ptr.autoRefresh();
//            }
//        },1000);
    }

    private class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.SimpleViewHolder> {

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView contentView = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_ptr_recyclerview_item,parent,false);
            return new SimpleViewHolder(contentView);
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            holder.itemView.setText(Utils.getDigit(position+1));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return listCount;
        }

        class SimpleViewHolder extends RecyclerView.ViewHolder{

            public SimpleViewHolder(TextView itemView) {
                super(itemView);
                this.itemView = itemView;
            }

            TextView itemView;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
