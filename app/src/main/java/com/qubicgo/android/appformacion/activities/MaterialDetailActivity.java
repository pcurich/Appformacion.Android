package com.qubicgo.android.appformacion.activities;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.data.request.MaterialRequest;
import com.qubicgo.android.appformacion.fragments.MaterialFragment;
import com.qubicgo.android.appformacion.fragments.WebBrowserFragment;

import butterknife.ButterKnife;

public class MaterialDetailActivity extends AppCompatActivity {

    private MaterialRequest model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_detail);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle(R.string.text_drive);

        int position = getIntent().getIntExtra("material_adapter_position",-1);

        if(position>-1){
            model = MaterialFragment.list.get(position);
            WebBrowserFragment fragment = new WebBrowserFragment();
            fragment.setmContext(this);
            Bundle bundle = new Bundle();
            bundle.putString("title", getResources().getString(R.string.text_drive));
            bundle.putString("url", model.getUrlDrive());
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
