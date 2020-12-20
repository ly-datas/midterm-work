package com.example.notebook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    MyHelper myHelper;
    SQLiteDatabase db;
    ListView listView;
    static List<Note> noteList;
    Button addNote;
    Button searchNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


        BaseAdapter baseAdapter = new MyBaseAdapter();
        listView.setAdapter(baseAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                Note note = noteList.get(position);
                Intent intent;
                intent = new Intent(MainActivity.this,EditNote.class);
                intent.putExtra("_id",note.getId());
                intent.putExtra("title",note.getTitle());
                intent.putExtra("crDate",note.getCreateDate());
                intent.putExtra("mdDate",note.getModifiedDate());
                intent.putExtra("content",note.getContent());
                startActivity(intent);
            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this,EditNote.class);
                startActivity(intent);
            }
        });
        searchNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this,SearchNote.class);
                startActivity(intent);
            }
        });

    }
//
    private void initView(){
        addNote = findViewById(R.id.addNote);
        searchNote = findViewById(R.id.searchNote);
//        初始化ListView控件
        listView = findViewById(R.id.mylist);
        noteList = new ArrayList<Note>();
        myHelper = new MyHelper(this);
        db = myHelper.getWritableDatabase();

        String sql = "select * from notes order by mdDate desc";

//       ContentValues values = new ContentValues();
//        for (int i = 0; i < 3; i++) {
//            values.clear();
//            values.put("title", "title"+i);
//            values.put("crDate", "2020/12/19" );
//            values.put("mdDate", "2020/12/19" );
//            values.put("content", "content"+i);
//            db.insert("notes", null, values);
//        }
//
//        values.put("title", "newtitle");
//        values.put("crDate", "2020/12/20" );
//        values.put("mdDate", "2020/12/20" );
//        values.put("content", "newcontent");
//        db.insert("notes", null, values);



        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.getCount()==0){
            Toast.makeText(this,"还没有写笔记哦！赶紧来记录一下吧~",Toast.LENGTH_SHORT).show();
        }
        while (cursor.moveToNext()){
            int idCol = cursor.getColumnIndex("_id");
            int titleCol = cursor.getColumnIndex("title");
            int contentCol = cursor.getColumnIndex("content");
            int crDateCol = cursor.getColumnIndex("crDate");
            int mdDateCol = cursor.getColumnIndex("mdDate");

            String title = cursor.getString(titleCol);
            String id = cursor.getString(idCol);
            String content = cursor.getString(contentCol);
            String crDate = cursor.getString(crDateCol);
            String mdDate = cursor.getString(mdDateCol);

            Note note = new Note(id,title,content,crDate,mdDate);
            noteList.add(note);
        }
    }




    class MyBaseAdapter extends BaseAdapter{
        //  得到item的总数
        @Override
        public int getCount() {
            return noteList.size();   //返回ListItem条目的总数
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


            Note note = noteList.get(position);
            View view = null;
            if(convertView == null){
                view = View.inflate(MainActivity.this,R.layout.note_list,null);
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
