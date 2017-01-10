package lin.demo.swiperefresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lin.core.annotation.ResId;
import lin.core.annotation.ViewById;
import lin.core.recyclerview.headers.StickyHeadersAdapter;
import lin.core.swiperefresh.DefaultSwipeLayout;
import lin.demo.R;
import lin.demo.databinding.ActivitySwiperefreshItemBinding;
import lin.demo.main.DividerDecoration;

/**
 * Created by lin on 06/01/2017.
 */

@ResId(R.layout.activity_swiperefresh_frag)
public class SwiperefreshFragment extends lin.core.Fragment implements SwiperefreshContract.View {

    @ViewById(R.id.activity_swiperefresh_frag_swipe)
    private DefaultSwipeLayout swipeLayout;

    @ViewById(R.id.activity_swiperefresh_frag_listview)
    private RecyclerView recyclerView;

    public static SwiperefreshFragment newInstance(){
        return new SwiperefreshFragment();
    }

    private SimpleRecyclerAdapter mAdapter;
    @Override
    protected void onCreateView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new DividerDecoration(this.getContext()));

        mAdapter = new SimpleRecyclerAdapter(this.getContext());

        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void show(List<Data> datas) {
        mAdapter.setDatas(datas);
    }

    @Override
    public void setPresenter(SwiperefreshContract.Presenter presenter) {
        this.mPresenter = presenter;
    }


    private SwiperefreshContract.Presenter mPresenter;
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    private class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.SimpleViewHolder>{

        private Context mContext;
        private List<Data> mDatas = new ArrayList<>();

        SimpleRecyclerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public SimpleRecyclerAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ActivitySwiperefreshItemBinding binding = ActivitySwiperefreshItemBinding.inflate(inflater,parent,false);

            return new SimpleRecyclerAdapter.SimpleViewHolder(binding);
        }

        public void setDatas(List<Data> datas){
            mDatas.clear();
            mDatas.addAll(datas);
            this.notifyDataSetChanged();
        }


        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        @Override
        public void onBindViewHolder(SimpleRecyclerAdapter.SimpleViewHolder holder, int position) {
//            holder.vContent.setText(String.valueOf(mData.get(position)));
            holder.binding.setData(mDatas.get(position));
        }

        class SimpleViewHolder extends RecyclerView.ViewHolder{

            SimpleViewHolder(ActivitySwiperefreshItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            ActivitySwiperefreshItemBinding binding;
        }

    }
}
