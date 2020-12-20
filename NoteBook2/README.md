## 期中NoteBook开发报告

### 基本构思：

创建一个名为NoteBook的应用程序，指定包名为com.example.notebook。

NoteBook设置三个基本页面：

	MainActivity：用于显示当前所有的笔记；
	
	SearchNote：用于查询笔记；
	
	EditNote：用于编辑笔记。

整体项目结构：

<img src="https://i.loli.net/2020/12/20/5ORwcCBe8kIz2pM.png" alt="CSW84G1LTKJZK481BP3RU_2.png" width="230" height="400" />

接下来依次解析重点类、方法，布局文件。

### 一、数据库创建

#### 1、创建SQLIte数据库，这里使用MyHelper类，继承SQLiteOpenHelper，并重写该类中的onCreate()方法和onUpgrade()方法。

```java
package com.example.notebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {

    public MyHelper(Context context) {
        super(context,"notesdb.db",null,1);
    }

    //  数据库第一次被创建时调用该方法
    @Override
    public void onCreate(SQLiteDatabase db) {
//        初始化数据库的表结构，执行一条建表的SQL语句
        db.execSQL("create table notes(_id integer primary key autoincrement, title char(40), content char(40), crDate char(20),mdDate char(20))");
    }

    //    当数据库的版本号增加时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

```



### 二、各个类解析

#### 1、Note类

Note对象是整个Notebook中最重要最基本的对象。Note类中主要定义每一个Note的基本属性，对应notes数据库相关字段。

```java
package com.example.notebook;

public class Note {
    private String id;
    private String title;
    private String content;
    private String createDate;
    private String modifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }


    public Note(String title, String content, String modifiedDate) {
        this.title = title;
        this.content = content;
        this.modifiedDate = modifiedDate;
    }
    public Note(String title, String content, String createDate, String modifiedDate) {
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
    }

    public Note(String id, String title, String content, String createDate, String modifiedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
    }
}

```

#### 2、MainActivity页面的实现

2.1、MainActivity主要用于展示已有笔记。效果如下：
没有添加笔记前：<br/>
<img src="https://i.loli.net/2020/12/20/gSbMnusH52dmEv8.jpg" alt="46F796FA09F86355F31401A8EC72FF6B.jpg" width="230" height="410" />
<br/>添加笔记后：<br/>
<img src="https://i.loli.net/2020/12/20/oM8C4StFk2r7xu9.jpg" alt="EAC10314BCA607596DABF926A83C95A7.jpg" width="230" height="410"/>
<br/>
2.2、activity_main.xml布局设置（布局方式不再细说，主要多用相对布局和线性布局，附重要代码）：

在MainActivity主要的布局控件有**ListView**和**Button**，通过ListView以在MainActivity中以列表的形式展示具体笔记内容，此外，在页面下方左右分别设置了一个Button，左边Button用于跳转SearchNote页面，右边Button用于跳转到EditNote页面。

```
    <ListView
        android:id="@+id/mylist"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:divider="#FDFDFD"
        android:dividerHeight="5dp"/>

    <Button
        android:id="@+id/addNote"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:text="+"
        android:textSize="33dp"
        android:textColor="#FDFDFD"
        android:gravity="center"
        android:background="@drawable/shape_btn"
        android:layout_alignParentBottom="true"
        android:layout_alignParentRight="true"
        android:layout_marginBottom="28dp"
        android:layout_marginRight="28dp"
        android:layout_alignBottom="@id/webMainLayout"
        tools:ignore="NotSibling" />

    <Button
        android:id="@+id/searchNote"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:text="@string/searchNote"
        android:textColor="#FDFDFD"
        android:textSize="14dp"
        android:gravity="center"
        android:background="@drawable/shape_searchbtn"
        android:layout_alignParentBottom="true"
        android:layout_alignParentLeft="true"
        android:layout_marginBottom="28dp"
        android:layout_marginLeft="28dp"
        android:layout_alignBottom="@id/webMainLayout"
        tools:ignore="NotSibling" />
```

另外，为了设置每一listItem的布局，定义一个**note_list.xml**布局文件，设置ListView中每一个ListItem的基本布局。

**note_list.xml代码如下：**

```
<RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"

       >
        <LinearLayout
            android:id="@+id/uplayout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">
            <TextView
                android:id="@+id/note_title"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="25dp"
                android:textColor="#F14444"
                android:textStyle="bold"/>
            <TextView
                android:id="@+id/note_time"
                android:layout_width="370dp"
                android:layout_height="wrap_content"
                android:textSize="15dp"
                android:layout_marginLeft="155dp"
                android:textColor="#818080"
                android:textStyle="italic"/>
        </LinearLayout>
        <LinearLayout
            android:id="@+id/lowlayout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_below="@id/uplayout"
            android:orientation="horizontal">
            <TextView
                android:id="@+id/note_content"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textSize="22dp"
                android:textColor="#3c3c3c"/>
        </LinearLayout>
    </RelativeLayout>
```

2.3、在MainActivity中，通过自定义的**MyBaseAdapter**把笔记数据填充到ListView中进行展示：

```java
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

```

2.4、初始化MainActivity页面，查询数据库已有的笔记信息，进行展示：

```java
noteList = new ArrayList<Note>();
        myHelper = new MyHelper(this);
        db = myHelper.getWritableDatabase();

        String sql = "select * from notes order by mdDate desc";
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
```



#### 3、EditNote页面的实现

3.1、EditNote页中，用户可以编辑每一条笔记的标题和对应的内容，并进行保存。也可以在这个页面对笔记进行删除。在删除操作中，显示一个自定义对话框，进行再次确认删除操作。先看效果：

<img src="https://i.loli.net/2020/12/20/xn8mcFMHjTsBQXA.jpg" alt="A406BD88FE010512E2D77E0C34F39E5C.jpg" width="220" height="410" />

<br/>

3.2、EditNote的布局edit_note.xml设置如下：

```
<LinearLayout
        android:id="@+id/edit_title"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        <EditText
            android:id="@+id/edit_noteTitle"
            android:layout_width="match_parent"
            android:layout_height="65dp"
            android:hint="@string/edit_noteTitle"
            android:background="#D5ECD5"
            android:textSize="23dp"
            android:gravity="top"
            android:maxLines="3"/>
    </LinearLayout>
    <LinearLayout
        android:layout_below="@id/edit_title"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <EditText
            android:id="@+id/edit_noteContent"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:hint="@string/edit_noteContent"
            android:gravity="top"
            android:textSize="21dp"
            android:background="#DAE4F8"
            />
    </LinearLayout>
    <Button
        android:id="@+id/addSuccess"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:text="@string/addSuccess"
        android:textSize="40dp"
        android:textColor="#FDFDFD"
        android:gravity="center"
        android:background="@drawable/shape_btn"
        android:layout_alignParentBottom="true"
        android:layout_alignParentRight="true"
        android:layout_marginBottom="28dp"
        android:layout_marginRight="28dp"
        android:layout_alignBottom="@id/webMainLayout"
         />
```

3.3、EditNote中主要逻辑实现代码：

```java
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
                        db.delete("notes","id=?",new String[]{id+""});
                        db.close();
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }
```

3.4、自定义对话框MyDialog的实现：

MyDialog继承**Dialog**类，主要用于在用户选择要删除某一条note时显示，进行再次询问确认：

效果如图示：

<img src="https://i.loli.net/2020/12/20/N4Ay82zkYEM65Iu.jpg" alt="23C0CA16602C27464DE4DB7E29EF9AD5.jpg" width="210" height="410" />

<img src="https://i.loli.net/2020/12/20/bFxwH83NemI45R6.jpg" alt="A1B972A839B4A2E9905320D33933073E.jpg" width="210" height="410"/>
<br/>

MyDialog主要java代码实现：

```java
package com.example.notebook;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MyDialog extends Dialog {
    public Button btnConfirm;
    private TextView makesure;
    private TextView mksrTitle;
    private TextView mksrMdDate;
    private TextView mksrContent;
    private Button btnConfirm;
    private Button btnCancel;
    private String title;
    private String mdDate;
    private String content;

    public MyDialog(Context context,String mksrTitle,String mksrMdDate,String mksrContent){
        super(context);
        this.title = mksrTitle;
        this.mdDate = mksrMdDate;
        this.content = mksrContent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog);


        mksrTitle = findViewById(R.id.mksrTitle);
        mksrMdDate = findViewById(R.id.mksrMdDate);
        mksrContent = findViewById(R.id.mksrContent);

        if (mksrTitle.getText() != null && mksrMdDate.getText() != null && mksrContent.getText() != null){
            mksrTitle.setText("标题: "+title);
            mksrMdDate.setText("时间: "+mdDate);
            mksrContent.setText("内容: "+content);
        }

        btnConfirm = findViewById(R.id.confirm);
        btnCancel = findViewById(R.id.cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}

```





#### 4、SearchNote页面的实现

4.1、SearchNote页面目的是为了对笔记标题及内容进行模糊查询。

先看效果：

<img src="https://i.loli.net/2020/12/20/8rXeox43GRZd6tc.jpg" alt="66EED437493FC4D441EA702C828FD2A6.jpg" width="210" height="410" /><br/>
<img src="https://i.loli.net/2020/12/20/vDaP13EQAn6rV9G.jpg"  width="210" height="410" /><br/>

4.2、页面布局search_list.xml代码示例：

```
<LinearLayout
        android:id="@+id/searchBar"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:orientation="horizontal"
        >

        <EditText
            android:id="@+id/filter"
            android:layout_width="0dp"
            android:layout_weight="0.7"
            android:layout_height="40dp"
            android:hint="@string/filter"
            android:background="@drawable/search_corner"
            android:textSize="20dp"/>
        <ImageView
            android:id="@+id/search_icon"
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:layout_gravity="center"
            android:src="@drawable/search"
            android:background="@drawable/shape_searchbtn"/>
    </LinearLayout>
    <LinearLayout
        android:id="@+id/searchlist"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:layout_below="@id/searchBar">
        <ListView
            android:id="@+id/resultList"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"></ListView>
    </LinearLayout>
```

4.3、SearchNote类主要逻辑代码的实现：（其基本原理跟MainActivity基本类似，把查询到的符合条件的notes通过适配器MyBaseAdapter填充其结果list）

```java
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
        String sql = "select * from notes where title like '%"+filter+"%'"+"or content like '%"+filter+"%';";
        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.getCount()==0){
            Toast.makeText(SearchNote.this,"还没有与"+filter+"相关的内容哦~",Toast.LENGTH_SHORT).show();
        }
        while (cursor.moveToNext()){
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
```

### 三、工程打包上传至远程仓库Github

1、在github个人账户中，新建一个repository，名为：

2、在工程所在目录的上一级目录，用GitBash打开，通过命令行方式上传工程，具体步骤：

2.1：git init 

<img src="https://i.loli.net/2020/10/06/QEC2s6VlkgA5u9W.png" alt="init.png" style="zoom:50%;" />

2.2：git config --global user.name 'your username'

<img src="https://i.loli.net/2020/10/06/yVefZGK6Tw4vFjY.png" alt="username.png" style="zoom:50%;" />

2.3：git config --global user.email 'your email'

<img src="https://i.loli.net/2020/10/06/PoJECKF1QmB9sgI.png" alt="email.png" style="zoom:50%;" />

2.4：git add dest(你的项目文件名，这里是NoteBook2)

<img src="https://i.loli.net/2020/12/20/L125hfE4olGMpVj.png" alt="email.png" style="zoom:50%;" />


2.5：git commit -m 'brief introduction for your program'(备注)

<img src="https://i.loli.net/2020/12/20/CmsRgP2vcz3SuZi.png" alt="email.png" style="zoom:50%;" />

2.6：git branch -M master

<img src="https://i.loli.net/2020/10/06/mgI5AFs6Oe3hWYa.png" alt="branch.png" style="zoom:50%;" />

2.7：git remote add origin https://......(github上的仓库地址)
<img src="https://i.loli.net/2020/12/20/bzMayxLSTdBEqQC.png" alt="branch.png" style="zoom:50%;" />

2.8：git push -u origin master

<img src="https://i.loli.net/2020/10/06/DN5gQUuB9MRjECf.png" alt="push.png" style="zoom:50%;" />

3、项目上传成功后，github上创新页面，会看到仓库中多了前面上传好的项目。
<img src="https://i.loli.net/2020/12/20/Od9Z2vJR6D1BUcC.png" alt="push.png" height="120" />
