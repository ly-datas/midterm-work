package com.example.notebook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchNote extends AppCompatActivity {
    MyHelper myHelper;
    SQLiteDatabase db;
    ListView listView;

    EditText searchText;
    ImageView searchImg;
    String filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list);

        initView();
        filter = searchText.getText().toString();
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(filter);

            }
        });

    }

    private void initView(){
        searchText = findViewById(R.id.filter);

        searchImg = findViewById(R.id.search_icon);
//        初始化ListView控件
        listView = findViewById(R.id.resultList);

        myHelper = new MyHelper(this);

    }

    private void setData(String filter){
        db = myHelper.getReadableDatabase();
        List<Note> resultlist;
        resultlist = new ArrayList<>();
//        String sql = "select * from notes where title like '%"+filter+"%'"+"or title like '%"+filter+"%';";
        String sql = "select * from notes where _id > 3";
        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.getCount()==0){
            Toast.makeText(SearchNote.this,"还没有与"+filter+"相关的内容哦~",Toast.LENGTH_SHORT).show();
        }
        while (cursor.moveToNext() && cursor.getCount()<=2){
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            String mdDate = cursor.getString(4);

            Note note = new Note(title,content,mdDate);
            resultlist.add(note);

        }
        cursor.close();
        db.close();
//                创建一个Adapter的实例
        BaseAdapter baseAdapter = new MyBaseAdapter(resultlist);
        listView.setAdapter(baseAdapter);

    }

    class MyBaseAdapter extends BaseAdapter {
        List<Note> resultList;
        //  得到item的总数

        public MyBaseAdapter(List<Note> resultList) {
            this.resultList = resultList;
        }

        @Override
        public int getCount() {
            return resultList.size();   //返回ListItem条目的总数
        }
        //得到item代表的对象
        @Override
        public Object getItem(int position) {
            return null;
        }
        //得到Item的id
        @Override
        public long getItemId(int position) {
            return 0;   //返回ListView Item的id
        }
        //得到Item的View视图
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            Note note = resultList.get(position);
            View view = null;
            if(convertView == null){
                view = View.inflate(SearchNote.this,R.layout.note_list,null);
            }else {
                view = convertView;
            }

            TextView noteTitle = view.findViewById(R.id.note_title);
            noteTitle.setText(note.getTitle());
            TextView noteTime = view.findViewById(R.id.note_time);
            noteTime.setText(note.getModifiedDate());
            TextView noteContent = view.findViewById(R.id.note_content);
            noteContent.setText(note.getContent());

            return view;

        }
    }
}
