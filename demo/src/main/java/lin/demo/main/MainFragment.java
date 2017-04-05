package lin.demo.main;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lin.core.Nav;
import lin.core.ResFragment;
import lin.core.annotation.MenuId;
import lin.core.annotation.OptionsMenu;
import lin.core.annotation.ResId;
import lin.core.ptr.PtrRecyclerView;
import lin.core.recyclerview.RecyclerItemClickListener;
import lin.core.recyclerview.headers.StickyHeadersAdapter;
import lin.core.recyclerview.headers.StickyHeadersDecoration;
import lin.core.recyclerview.headers.StickyHeadersTouchListener;
import lin.demo.R;
import lin.demo.databinding.ActivityMainHeaderBinding;
import lin.demo.databinding.ActivityMainItemBinding;

/**
 * Created by lin on 23/11/2016.
 */
@lin.core.mvvm.View
@lin.core.mvvm.Presenter(MainPresenter.class)
//@lin.core.mvm.
@ResId(R.layout.activity_main_frag)
@OptionsMenu
@MenuId(R.menu.tasks_fragment_menu)
public class MainFragment extends ResFragment implements MainContract.View {

    private MainContract.Presenter mPresenter;
    private SimpleRecyclerAdapter mAdapter;

    public MainFragment() {
        // Requires empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected void onCreateView() {

        View root = this.getView();

        PtrRecyclerView ptrView = (PtrRecyclerView) root.findViewById(R.id.activity_list_frag_recyclerview);
        RecyclerView recyclerView = ptrView.getView();
                //recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new DividerDecoration(this.getContext()));

        mAdapter = new SimpleRecyclerAdapter(this.getContext());
        recyclerView.setAdapter(mAdapter);

        final StickyHeadersDecoration headersDecor = new StickyHeadersDecoration(mAdapter);
        recyclerView.addItemDecoration(headersDecor);

        // Add touch listeners
        StickyHeadersTouchListener touchListener =
                new StickyHeadersTouchListener(recyclerView, headersDecor);
        touchListener.setOnHeaderClickListener(
                new StickyHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {

                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Object obj = mAdapter.mDatas.get(position).getIntent();
                if(obj instanceof Intent){
                    MainFragment.this.startActivity((Intent) obj);
                }else if(obj instanceof Class<?>){
                    Class<?> cls = (Class<?>) obj;
                    if(Activity.class.isAssignableFrom(cls)) {
                        MainFragment.this.startActivity(new Intent(MainFragment.this.getContext(), (Class<?>) obj));
                    }else if(Fragment.class.isAssignableFrom(cls) ||
                            lin.core.ContentView.class.isAssignableFrom(cls) ||
                            lin.core.ViewHolder.class.isAssignableFrom(cls)){
                        Nav.push(MainFragment.this.getActivity(),cls,null);
                    }
                }else if(obj instanceof Integer){
                    Nav.push(MainFragment.this.getActivity(),(Integer) obj,null);
                }else if(obj instanceof Long){
                    Nav.push(MainFragment.this.getActivity(),((Long) obj).intValue(),null);
                }

            }
        }));
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });

    }


    private class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.SimpleViewHolder>
            implements StickyHeadersAdapter<SimpleRecyclerAdapter.SimpleHeaderViewHolder> {

        private Context mContext;
        private List<Data> mDatas = new ArrayList<>();

        SimpleRecyclerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ActivityMainItemBinding binding = ActivityMainItemBinding.inflate(inflater,parent,false);

            return new SimpleViewHolder(binding);
        }

        public void setDatas(List<Data> datas){
            mDatas.clear();
            mDatas.addAll(datas);
            this.notifyDataSetChanged();
        }

        @Override
        public long getHeaderId(int position) {
//            if (position == 0) {
//                return -1;
//            } else {
//                return mDatas.get(position).getName().charAt(0);
//            }
            return Math.abs(mDatas.get(position).getGroup().hashCode());
        }

        @Override
        public SimpleHeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            ActivityListHeaderBinding binding = ActivityListHeaderBinding.inflate(inflater);
            ActivityMainHeaderBinding binding = ActivityMainHeaderBinding.inflate(inflater,parent,false);

            return new SimpleHeaderViewHolder(binding);
        }

        @Override
        public void onBindHeaderViewHolder(SimpleHeaderViewHolder holder, int position) {
            holder.binding.setData(mDatas.get(position));
            DataBindingUtil a;
            //holder.vContent.setText(mDatas.get(position).getGroup());
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
//            holder.vContent.setText(String.valueOf(mData.get(position)));
            holder.binding.setData(mDatas.get(position));
        }

        class SimpleViewHolder extends RecyclerView.ViewHolder{

            SimpleViewHolder(ActivityMainItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            ActivityMainItemBinding binding;
        }
        class SimpleHeaderViewHolder extends RecyclerView.ViewHolder{

            SimpleHeaderViewHolder(ActivityMainHeaderBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
//                vContent = (TextView) itemView.findViewById(android.R.id.text1);
                vContent = (TextView)binding.getRoot().findViewById(R.id.list_header_textview);
            }

            TextView vContent;
            ActivityMainHeaderBinding binding;
        }
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
////            case R.id.menu_clear:
//////                mPresenter.clearCompletedTasks();
////                break;
//            case R.id.menu_filter:
//                showFilteringPopUpMenu();
////                AlertDialog mDialog = new AlertDialog.Builder(this.getContext())
////                        .setCancelable(false)
////                        .create();
////                mDialog.show();
//                break;
//            case R.id.menu_refresh:
//////                mPresenter.loadTasks(true);
//                mPresenter.loadData();
//                break;
//        }
//        return true;
//    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.tasks_fragment_menu, menu);
//    }

//    public void setViewModel(TasksViewModel viewModel) {
//        mTasksViewModel = viewModel;
//    }

    @MenuId(R.id.menu_filter)
    private void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active:
//                        mPresenter.setFiltering(TasksFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.completed:
//                        mPresenter.setFiltering(TasksFilterType.COMPLETED_TASKS);
                        break;
                    default:
//                        mPresenter.setFiltering(TasksFilterType.ALL_TASKS);
                        break;
                }
//                mPresenter.loadTasks(false);
                return true;
            }
        });

        popup.show();
    }


    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void show(List<Data> datas) {
//        mAdapter.setDatas(datas);
//        List<Data> datas = new ArrayList<>();
//
//        for(String item : this.getContext().getResources().getStringArray(R.array.animals)){
//            datas.add(new Data(item,null));
//        }
        mAdapter.setDatas(datas);

    }

    //    @Override
//    public boolean isActive() {
//        return isAdded();
//    }


    public void onPrepareOptionsMenu(Menu menu) {
//        if (mIsEditStatus) {
//            menu.findItem(R.id.action_share).setVisible(false);
//            menu.findItem(R.id.action_edit).setVisible(true);
//        } else {
//            menu.findItem(R.id.action_share).setVisible(true);
//            menu.findItem(R.id.action_edit).setVisible(false);
//        }
        //menu.findItem(R.id.menu_refresh).setShowAsAction(R.drawable.ic_filter_list);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}

//private static class DatasAdapter extends BaseAdapter {
//
//    private List<Data> mDatas = new ArrayList<>();
//
//    public DatasAdapter(){
//
//    }
//
//    public void setDatas(List<Data> datas){
//        mDatas.clear();
//        mDatas.addAll(datas);
//        this.notifyDataSetChanged();
//    }
//
//    @Override
//    public int getCount() {
//        return mDatas.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return mDatas.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//
////            lin.test.databinding.CustomeViewBinding
//        ActivityListItemBinding binding;
//
//        if(view == null){
//            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
//            binding = ActivityListItemBinding.inflate(inflater,viewGroup,false);
//        }else{
//            binding = DataBindingUtil.getBinding(view);
//        }
//
//        binding.setData(mDatas.get(i));
//
//        return binding.getRoot();
//    }
//
//}
