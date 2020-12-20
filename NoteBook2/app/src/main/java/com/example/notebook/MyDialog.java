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
//    private Button btnConfirm;
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

//        String dialogName = makesure.getText().toString();
//        String title = mksrTitle.getText().toString();
//        String dmDate = mksrMdDate.getText().toString();
//        String content = mksrContent.getText().toString();

        btnConfirm = findViewById(R.id.confirm);
        btnCancel = findViewById(R.id.cancel);

//        makesure.setText(dialogName);
//        btnConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
