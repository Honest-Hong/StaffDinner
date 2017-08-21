package com.project.boostcamp.staffdinner.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.project.boostcamp.publiclibrary.data.OSLData;
import com.project.boostcamp.staffdinner.R;
import com.project.boostcamp.staffdinner.adapter.OSLAdapter;

import java.util.ArrayList;

public class OSLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osl);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.open_source_license);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        OSLAdapter adapter = new OSLAdapter(this, getOSLData());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<OSLData> getOSLData() {
        String[] names = getResources().getStringArray(R.array.osl_names);
        String[] links = getResources().getStringArray(R.array.osl_links);
        String[] copyrights = getResources().getStringArray(R.array.osl_copyrights);
        String[] licenses = getResources().getStringArray(R.array.osl_apache_licenses);

        ArrayList<OSLData> oslDatas = new ArrayList<>();
        for(int i=0; i<names.length; i++) {
            OSLData data = new OSLData();
            data.setName(names[i]);
            data.setLink(links[i]);
            data.setCopyright(copyrights[i]);
            data.setApacheLicense(licenses[i]);
            oslDatas.add(data);
        }
        return oslDatas;
    }
}
