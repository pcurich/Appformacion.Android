package com.qubicgo.android.appformacion.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.custom.EditTextCustom;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.data.request.CheckIn;
import com.qubicgo.android.appformacion.data.request.CheckInDetailListRequest;
import com.qubicgo.android.appformacion.data.request.CheckInDetailRequest;
import com.qubicgo.android.appformacion.data.request.CheckInRequest;
import com.qubicgo.android.appformacion.fragments.CheckInFragment;
import com.qubicgo.android.appformacion.services.def.IServiceCheckIn;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryCheckIn;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInDetailActivity extends AppCompatActivity {

    @BindView(R.id.tvRoom)
    TextViewBoldCustom tvRoom;
    @BindView(R.id.etCode)
    EditTextCustom etCode;

    @BindView(R.id.recyclerCheckIn)
    RecyclerView recyclerCheckIn;

    private ProgressDialog progressDialog;
    private CheckInRequest model;
    private List<CheckInDetailRequest> list;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_detail);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle(R.string.text_assists_score_large);
        mContext = CheckInDetailActivity.this;

        list = new ArrayList<>();

        int position = getIntent().getIntExtra("check_in_adapter_position", -1);
        model = CheckInFragment.list.get(position);

        if (model != null) {
            tvRoom.setText(model.getName()); //nombre de la sala
        }
        details();

    }

    @OnClick(R.id.btnCheckIn)
    public void onViewClicked() {

        progressDialog = new ProgressDialog(CheckInDetailActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();


        String codigoAsistencia = etCode.getText().toString();

        if(codigoAsistencia.toUpperCase().equals(model.getCode())){
            final IServiceCheckIn checkInService = ServiceFactoryCheckIn.checkIn(mContext).create(IServiceCheckIn.class);
            Call<CheckIn> response = checkInService.marcarAsistencia(
                    mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                    model.getGrpdId(), codigoAsistencia);

            response.enqueue(new Callback<CheckIn>() {
                @Override
                public void onResponse(Call<CheckIn> call, Response<CheckIn> response) {
                    CheckIn result = response.body();

                    if("OK".equals( result.getStatus())){
                        Toast.makeText(mContext,result.getMessage(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        startActivity(intent);
                    }

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<CheckIn> call, Throwable t) {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    if (t.toString().contains("BEGIN_OBJECT")) {
                        HomeActivity.restartSession(mContext);
                    }
                }
            });
        }else
        {
            Toast.makeText(mContext,"El código no es correcto",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }


        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }


        private void details () {

            SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva", Context.MODE_PRIVATE);
            String userName = preferences.getString("username", "");

            final IServiceCheckIn checkInService = ServiceFactoryCheckIn.getCheckInDetailConfiguration(mContext).create(IServiceCheckIn.class);
            Call<CheckInDetailListRequest> response = checkInService.obtenerAsistenciaXMarcarDetalle(
                    mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                    userName, model.getRoomId());


            response.enqueue(new Callback<CheckInDetailListRequest>() {
                @Override
                public void onResponse(Call<CheckInDetailListRequest> call, Response<CheckInDetailListRequest> response) {
                    CheckInDetailListRequest result = response.body();
                    list.addAll(result.getBody());

                    CheckInRecyclerAdapter adapter = new CheckInRecyclerAdapter(list);
                    recyclerCheckIn.setLayoutManager(new LinearLayoutManager(CheckInDetailActivity.this));
                    recyclerCheckIn.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<CheckInDetailListRequest> call, Throwable t) {
                    t.printStackTrace();
                    if (t.toString().contains("BEGIN_OBJECT")) {
                        HomeActivity.restartSession(mContext);
                    }
                }
            });
        }

        class CheckInRecyclerAdapter extends RecyclerView.Adapter<CheckInRecyclerAdapter.ViewHolder> {

            private int layout;
            private List<CheckInDetailRequest> list;

            public CheckInRecyclerAdapter(List<CheckInDetailRequest> list) {
                this.layout =  R.layout.item_recycler_check_in;
                this.list = list;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
                ViewHolder holder = new ViewHolder(v);
                return holder;
            }

            @Override
            public int getItemCount() {
                return list.size();
            }


            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.bind(list.get(position));
            }


            class ViewHolder extends RecyclerView.ViewHolder {

                @BindView(R.id.tvTime)
                TextViewCustom tvTime;

                @BindView(R.id.tvTitle)
                TextViewBoldCustom tvTitle;

                Unbinder unbinder;


                public ViewHolder(View itemView) {
                    super(itemView);
                    unbinder = ButterKnife.bind(this, itemView);
                }

                public void bind(final CheckInDetailRequest model) {

                    if (model.getProgram() != null && model.getProgram().length() > 0) {
                        tvTitle.setText(model.getProgram());
                    } else {
                        tvTitle.setText(model.getCourse());
                    }

                    tvTime.setText(model.getStartTime() + " - " + model.getEndTime());
                }
            }
        }

    }
