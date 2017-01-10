//package lin.core;
//
//import android.content.Context;
//import android.graphics.drawable.ColorDrawable;
//import android.util.AttributeSet;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * Created by lin on 11/10/15.
// */
//public class GroupListView extends XListView {
//    public GroupListView(Context context) {
//        super(context);
//        this.init();
//    }
//
//
//    public GroupListView(Context context,AttributeSet attrs){
//        super(context,attrs);
//        this.init();
//    }
//
////    private List<Category<SV,RV>> datas = null;
//    private List<Object> datasList = null;
////    private List<Int> datasType = null;
//    private List<Integer> datasSection = null;
//    private List<Integer> datasRow = null;
//    private ArrayAdapter mAdapter;
//
////    private int rowLayout;
////    private int seletionHeaderLayout;
////    private int seletionFooterLayout;
//    private GroupListViewAdapter adapter;
//
//
//    private void init(){
//        final LayoutInflater inflater = LayoutInflater.from(this.getContext());
//        mAdapter = new ArrayAdapter<Object>(this.getContext(),0){
//
//            private View getHeaderView(int position, View convertView, ViewGroup parent) {
//                if(convertView == null && adapter.getSeletionHeaderLayout() > 0){
//                    convertView = inflater.inflate(adapter.getSeletionHeaderLayout(),parent,false);
//                }
//                return adapter.getSelectionHeaderView(position,datasList.get(position),convertView,parent);
//            }
//
//            private View getFooterView(int position, View convertView, ViewGroup parent) {
//                if(convertView == null && adapter.getSeletionFooterLayout() > 0){
//                    convertView = inflater.inflate(adapter.getSeletionFooterLayout(),parent,false);
//                }
//                return adapter.getSelectionFooterView(position,datasList.get(position),convertView,parent);
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                //return super.getView(position, convertView, parent);
//
//                View view = null;
//                switch (getItemViewType(position)){
//                    case 1:
////                        if(convertView == null && adapter.getSeletionHeaderLayout() > 0){
////                            convertView = inflater.inflate(adapter.getSeletionHeaderLayout(),parent,false);
////                        }
////                        view = adapter.getSelectionHeaderView(position,datasList.get(position),convertView,parent);
//                        view = getHeaderView(0,convertView,parent);
//                        break;
//                    case 2:
//                        View headerView = null;
//                        View footerView = null;
//                        ViewHolder viewHolder = null;
//                        LinearLayout itemView = null;
//                        if(convertView != null && convertView.getTag() instanceof ViewHolder){
//                            viewHolder = (ViewHolder)convertView.getTag();
//                            headerView = viewHolder.headerView;
//                            footerView = viewHolder.footerView;
//                            itemView = (LinearLayout)convertView;
//                        }else{
//                            itemView = new LinearLayout(parent.getContext());
//                            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                    ViewGroup.LayoutParams.WRAP_CONTENT, 1));
//                            itemView.setOrientation(LinearLayout.VERTICAL);
//                            viewHolder = new ViewHolder();
//                            itemView.setTag(viewHolder);
//                        }
//                        viewHolder.footerView = getFooterView(datasSection.get(position)-1, viewHolder.footerView, parent);
//                        viewHolder.headerView = getHeaderView(datasSection.get(position),viewHolder.headerView,parent);
//
//                        if(footerView == null){
//                            itemView.addView(viewHolder.footerView);
//                        }else if(footerView != viewHolder.footerView){
//                            itemView.removeViewAt(1);
//                            itemView.addView(viewHolder.footerView);
//                        }
//
//                        if(headerView == null){
//                            itemView.addView(viewHolder.headerView);
//                        }else if(headerView != viewHolder.headerView){
//                            itemView.removeViewAt(0);
//                            itemView.addView(viewHolder.headerView,0);
//                        }
//                        view = itemView;
//                        break;
//                    case 3:
////                        if(convertView == null && adapter.getSeletionFooterLayout() > 0){
////                            convertView = inflater.inflate(adapter.getSeletionFooterLayout(),parent,false);
////                        }
////                        view = adapter.getSelectionFooterView(position,datasList.get(position),convertView,parent);
//                        view = getFooterView(datasSection.get(position), convertView, parent);
//                        break;
//
//                    default:
//                        if(convertView == null && adapter.getRowLayout() > 0){
//                            convertView = inflater.inflate(adapter.getRowLayout(),parent,false);
//                        }
//                        view = adapter.getRowView(datasSection.get(position),datasRow.get(position),datasList.get(position),convertView,parent);
//                        break;
//                }
//                return view;
//            }
//
//            @Override
//            public int getItemViewType(int position) {
//                //return datasType.get(position);
//                if(datasRow.get(position) < 0){
//                    return -datasRow.get(position);
//                }
//                return 0;
//            }
//
//            @Override
//            public int getViewTypeCount() {
//                return 4;
//            }
//
//            @Override
//            public int getCount() {
//                if(adapter == null){
//                    return 0;
//                }
//                List<Category> datas = adapter.getDatas();
//
//                if(datas == null || datas.size() == 0){
//                    return 0;
//                }
//
//                int count = 0;
//                datasList = new ArrayList<Object>();
//                datasSection = new ArrayList<Integer>();
//                datasRow = new ArrayList<Integer>();
////                for (Category category : datas){
//                int sectionIndex = 0;
//                int rowIndex = 0;
//                Category category = null;
//                for(;sectionIndex<datas.size();sectionIndex++){
//                    category = datas.get(sectionIndex);
//                    if(category == null){
//                        continue;
//                    }
//
//                    datasList.add(category.getSectionValue());
//                    datasSection.add(sectionIndex);
//                    if(sectionIndex == 0) {
//                        datasRow.add(-1);
//                    }else{
//                        datasRow.add(-2);
//                    }
//
//                    count += 1;
//                    if(category.getRowValues() != null){
//                         for(rowIndex = 0;rowIndex<category.getRowValues().size();rowIndex++){
//                            datasList.add(category.getRowValues().get(rowIndex));
//                            datasRow.add(rowIndex);
//                             datasSection.add(sectionIndex);
//                        }
//                        count += category.getRowValues().size();
//                    }
//
////                    datasList.add(category.getSectionValue());
////                    datasType.add(2);
//                }
//                datasRow.add(-3);
//                datasList.add(category.getSectionValue());
//                datasSection.add(sectionIndex);
//                count += 1;
//                return count;
//            }
//        };
//        super.setAdapter(mAdapter);
//    }
//
//    public <SV,RV> void setGroupAdapter(GroupListViewAdapter<SV,RV> adapter){
//        this.adapter = adapter;
//        //this.getRefreshableView().setOnItemSelectedListener();
//    }
//
//    public void setOnItemSelectedListener(OnItemSelectedListener listener){
//        this.listener = listener;
//        if(listener != null){
//            this.getRefreshableView().setOnItemSelectedListener(listListener);
//        }else{
//            this.getRefreshableView().setOnItemSelectedListener(null);
//        }
//    }
//    private OnItemSelectedListener listener = null;
//    private ListView.OnItemSelectedListener listListener = new ListView.OnItemSelectedListener(){
//
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            if(listener == null || datasRow.get(position) < 0){
//                return;
//            }
//            listener.onItemSelected(parent,view,datasSection.get(position),datasRow.get(position),id);
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//            if(listener != null){
//                listener.onNothingSelected(parent);
//            }
//        }
//    };
//
//    public static interface OnItemSelectedListener{
//
//        void onItemSelected(AdapterView<?> parent, View view, int sectionIndex,int row, long id);
//        void onNothingSelected(AdapterView<?> parent);
//    }
//
//
//
////
////    public void setDatas(List<Category<SV,RV>> datas){
////        this.datas = datas;
////        mAdapter.notifyDataSetChanged();
////    }
//
//    public static class Category<SV,RV>{
//        private SV sectionValue;
//        private List<RV> rowValues;
//
//        public SV getSectionValue() {
//            return sectionValue;
//        }
//
//        public void setSectionValue(SV sectionValue) {
//            this.sectionValue = sectionValue;
//        }
//
//        public List<RV> getRowValues() {
//            return rowValues;
//        }
//
//        public void setRowValues(List<RV> rowValues) {
//            this.rowValues = rowValues;
//        }
//    }
//
//    public static abstract class GroupListViewAdapter<SV,RV> {
//
//        private GroupListView groupListView;
//        final public void notifyDataSetChanged(){
//            if(groupListView != null && groupListView.mAdapter != null) {
//                groupListView.mAdapter.notifyDataSetChanged();
//            }
//        }
//        public abstract List<Category<SV,RV>> getDatas();
//
//        private View getDefaultView(String value, View convertView, ViewGroup parent,int height,int background){
//            TextView textView = null;
//            if(convertView instanceof TextView){
//                textView = (TextView)convertView;
//            }else{
//                textView = new TextView(parent.getContext());
//                textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        (int) (textView.getContext().getResources().getDisplayMetrics().density * height), 1));
//                textView.setPadding((int) (textView.getContext().getResources().getDisplayMetrics().density * 10), 0, 0, 0);
////                textView.setTextAlignment(0);
//                textView.setGravity(Gravity.CENTER_VERTICAL);
//                textView.setBackgroundDrawable(new ColorDrawable(background));
//            }
////            if(sectionValue != null) {
////                textView.setText(sectionValue.toString());
////            }else{
//                textView.setText(value);
////            }
//            return textView;
//        }
//        public View getSelectionHeaderView(int sectionIndex,SV sectionValue, View convertView, ViewGroup parent){
//            return getDefaultView(sectionValue==null?"":sectionValue.toString(),convertView,parent,30,0x88aaaaaa);
//
//        }
//        public View getSelectionFooterView(int sectionIndex,SV sectionValue, View convertView, ViewGroup parent){
//            return getDefaultView("",convertView,parent,10,0x88aaaaaa);
//        }
//        public View getRowView(int sectionIndex,int row,RV rowValue, View convertView, ViewGroup parent){
//            return getDefaultView(rowValue==null?"":rowValue.toString(),convertView,parent,40,0xffffffff);
//        }
//
//        public int getRowLayout() {
//            return 0;
//        }
//
//        public int getSeletionHeaderLayout() {
//            return 0;
//        }
//
//        public int getSeletionFooterLayout() {
//            return 0;
//        }
//    }
//
//    private class ViewHolder{
//        View headerView;
//        View footerView;
//    }
//
////    public static interface GroupListViewAdapter{
////        int getSectionCount();
////        int getRowAtSection(int section);
////
////
////    }
//}
