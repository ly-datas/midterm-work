package com.example.notebook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNote extends AppCompatActivity {
    MyHelper myHelper;
    SQLiteDatabase db;
    MyDialog myDialog;
    Button addSuccess;
    EditText editTitle;
    EditText editContent;
    String id;
    static String title;
    static String content;
    static String crDate;
    static String mdDate;
    static String isNew;
    Intent intent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //实例化菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        myHelper = new MyHelper(this);
        db = myHelper.getWritableDatabase();

        editTitle = findViewById(R.id.edit_noteTitle);

        editContent = findViewById(R.id.edit_noteContent);
        addSuccess = findViewById(R.id.addSuccess);

        intent = getIntent();
        id = intent.getStringExtra("_id");
        title = intent.getStringExtra("title");
        crDate = intent.getStringExtra("rcDate");
        mdDate = intent.getStringExtra("mdDate");
        content = intent.getStringExtra("content");
        editTitle.setText(title);
        editContent.setText(content);
        isNew = editTitle.getText().toString();

//        添加Note
        addSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();

                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("yyyy/MM/dd ");
                Date date = new Date();
                mdDate = sdf.format(date);
                if(isNew == ""){
                    crDate = sdf.format(date);
                    values.put("title",title);
                    values.put("crDate",crDate);
                    values.put("mdDate",mdDate);
                    values.put("content",content);
                    db.insert("notes",null,values);
                    Toast.makeText(EditNote.this,"Note已添加",Toast.LENGTH_SHORT).show();

                }else{
                    crDate = intent.getStringExtra("crDate");
                    values.put("title",title);
                    values.put("_id",id);
                    values.put("crDate",crDate);
                    values.put("mdDate",mdDate);
                    values.put("content",content);

                    db.update("notes",values,"id = "+id,null);
                    Toast.makeText(EditNote.this,"Note已修改",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_delete:
                myDialog = new MyDialog(EditNote.this,editTitle.getText().toString(),mdDate,editContent.getText().toString());
                myDialog.show();
                Button btnConfirm = findViewById(R.id.confirm);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db = myHelper.getWritableDatabase();
                        String sql = "delete from notes where _id="+id;
                        db.delete("notes","title=?",new String[]{id+""});
                        db.execSQL(sql,null);
                        db.close();
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
