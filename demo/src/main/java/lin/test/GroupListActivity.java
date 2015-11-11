package lin.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lin.core.GroupListView;

public class GroupListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        GroupListView groupListView = (GroupListView) this.findViewById(R.id.groupListId);

        GroupListView.Category<String,String> category = new GroupListView.Category<String,String>();

        final List<GroupListView.Category<String,String>> datas = new ArrayList<GroupListView.Category<String,String>>();
        datas.add(category);
        category.setSectionValue("第一行");
        category.setRowValues(new ArrayList<String>());
        for(int n=0;n<20;n++) {
            category.getRowValues().add("第"+(n+1)+"项");
        }
//        category.getRowValues().add("第一项");
//        category.getRowValues().add("第二项");
//        category.getRowValues().add("第三项");

        category = new GroupListView.Category<String,String>();

        datas.add(category);
        category.setSectionValue("第二行");
        category.setRowValues(new ArrayList<String>());
        for(int n=0;n<20;n++) {
            category.getRowValues().add("第"+(n+1)+"项");
        }
//        category.getRowValues().add("第二项");
//        category.getRowValues().add("第三项");

        final LayoutInflater inflater = LayoutInflater.from(this.getApplicationContext());

        groupListView.setGroupAdapter(new GroupListView.GroupListViewAdapter<String, String>() {
            @Override
            public List<GroupListView.Category<String, String>> getDatas() {
                return datas;
            }

//            @Override
//            public View getSelectionHeaderView(int position, String sectionValue, View convertView, ViewGroup parent) {
////                if(convertView != null){
////                    System.out.println("*********************");
////                }
////                if(convertView instanceof GroupHeaderView){
////                    return convertView;
////                }
////                convertView = inflater.inflate(R.layout.content_group_list_header,parent, false);
//                return convertView;
//            }

//            @Override
//            public View getSelectionFooterView(int position, String sectionValue, View convertView, ViewGroup parent) {
////                return super.getSelectionFooterView(position, sectionValue, convertView, parent);
////                if(convertView instanceof GroupFooterView){
////                    return convertView;
////                }
////                convertView = inflater.inflate(R.layout.content_group_list_footer,parent, false);
//                return convertView;
//            }


//            @Override
//            public View getRowView(int position, String rowValue, View convertView, ViewGroup parent) {
////                if(convertView != null){
////                    System.out.println("===================");
////                }
////                if(convertView instanceof GroupRowView){
////                    return convertView;
////                }
////                convertView = inflater.inflate(R.layout.content_group_list,parent, false);
//                return convertView;
//            }

//            @Override
//            public int getRowLayout() {
//                return R.layout.content_group_list;
//            }

//            @Override
//            public int getSeletionHeaderLayout() {
//                return R.layout.content_group_list_header;
//            }

//            @Override
//            public int getSeletionFooterLayout() {
//                return R.layout.content_group_list_footer;
//            }
        });
    }

    //public class DataCato extends lin.core.GroupListView.Category


}
