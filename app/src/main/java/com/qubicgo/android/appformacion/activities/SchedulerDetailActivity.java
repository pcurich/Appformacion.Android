package com.qubicgo.android.appformacion.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.custom.TextViewJustifiedCustom;
import com.qubicgo.android.appformacion.services.def.IServiceScheduler;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryScheduler;
import com.qubicgo.android.appformacion.data.request.SchedulerDetailListRequest;
import com.qubicgo.android.appformacion.data.request.SchedulerDetailRequest;
import com.qubicgo.android.appformacion.data.request.SchedulerRequest;
import com.qubicgo.android.appformacion.fragments.SchedulerActivitiesFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchedulerDetailActivity extends AppCompatActivity {

    @BindView(R.id.tvName)
    TextViewBoldCustom tvName;
    @BindView(R.id.tvTarget)
    TextViewJustifiedCustom tvTarget;

    @BindView(R.id.recyclerActivities)
    RecyclerView recyclerActivities;


    private ProgressDialog progressDialog;
    private SchedulerRequest model;
    private List<SchedulerDetailRequest> list;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler_detail);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle(R.string.text_scheduler_activities_large);
        mContext = this;

        getSupportActionBar().show();

        list = new ArrayList<>();

        int position = getIntent().getIntExtra("scheduler_activity_adapter_position",-1);
        model = SchedulerActivitiesFragment.list.get(position);

        if(model!=null){
            tvName.setText(model.getName());
            tvTarget.setText(model.getTarget());
        }

        details();

    }

    private void details() {

        final IServiceScheduler schedulerService= ServiceFactoryScheduler.getSchedulerDetailListConfiguration(mContext).create(IServiceScheduler.class);
        Call<SchedulerDetailListRequest> response = schedulerService.obtenerActividadesProgramadasDetalle(
                mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                model.getGroupPersonId());

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        response.enqueue(new Callback<SchedulerDetailListRequest>() {
            @Override
            public void onResponse(Call<SchedulerDetailListRequest> call, Response<SchedulerDetailListRequest> response) {

                SchedulerDetailListRequest result = response.body();
                list.addAll(result.getBody());

                SchedulerRecyclerAdapter adapter = new SchedulerRecyclerAdapter(mContext, list);
                recyclerActivities.setLayoutManager(new LinearLayoutManager(SchedulerDetailActivity.this));
                recyclerActivities.setAdapter(adapter);

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SchedulerDetailListRequest> call, Throwable t) {
                t.printStackTrace();
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (t.toString().contains("BEGIN_OBJECT")){
                    HomeActivity.restartSession(mContext);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,  model.getHashTag());
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        startActivity(Intent.createChooser(sharingIntent, "Compartir"));
        return super.onOptionsItemSelected(item);
    }

    class SchedulerRecyclerAdapter extends RecyclerView.Adapter<SchedulerRecyclerAdapter.ViewHolder> {

        private int layout;
        private List<SchedulerDetailRequest> list;
        private  Context mContext;

        public SchedulerRecyclerAdapter(Context context, List<SchedulerDetailRequest> list) {
            this.layout = R.layout.item_recycler_scheduler;
            this.list = list;
            this.mContext=context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(mContext,list.get(position));
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.ivStatus)
            ImageView ivStatus;

            @BindView(R.id.tvTimeStart)
            TextViewBoldCustom tvTimeStart;

            @BindView(R.id.tvTimeEnd)
            TextViewCustom tvTimeEnd;

            @BindView(R.id.tvDate)
            TextViewBoldCustom tvDate;

            @BindView(R.id.tvRoom)
            TextViewCustom tvRoom;

            @BindView(R.id.tvAddress)
            TextViewCustom tvAddress;

            Unbinder unbinder;


            public ViewHolder(View itemView) {
                super(itemView);
                unbinder = ButterKnife.bind(this, itemView);
            }

            public void bind(final Context context, final SchedulerDetailRequest model){

                Drawable drawable =  ivStatus.getBackground();
                drawable = DrawableCompat.wrap(drawable);

                if("NO".equals(model.getMarkAssistence())){
                    DrawableCompat.setTint(drawable, context.getResources().getColor(R.color.custom_darkred));
                    ivStatus.setBackground(drawable);
                }else {
                    DrawableCompat.setTint(drawable, context.getResources().getColor(R.color.custom_darkgreen));
                    ivStatus.setBackground(drawable);
                }

                tvTimeStart.setText(model.getStartTime());
                tvTimeEnd.setText(model.getEndTime());

                tvDate.setText(model.getDate());
                tvRoom.setText(model.getRoom());
                tvAddress.setText(model.getAddress());
            }
        }

    }

}
