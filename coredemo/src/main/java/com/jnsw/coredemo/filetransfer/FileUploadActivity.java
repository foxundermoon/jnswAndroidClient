package com.jnsw.coredemo.filetransfer;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.jnsw.coredemo.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_file_uploadctivity)
public class FileUploadActivity extends Activity {
    @ViewById(R.id.upload_message)
    TextView uploadMessage;
    @Click(R.id.upload_btn)
    void upoad() {

    }
}

