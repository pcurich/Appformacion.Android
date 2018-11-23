package com.qubicgo.android.appformacion.activities;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.qubicgo.android.appformacion.NotificationRecieve;
import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.custom.TextViewJustifiedCustom;
import com.qubicgo.android.appformacion.services.def.IServiceInvitation;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryInvitation;
import com.qubicgo.android.appformacion.data.request.InvitationRequest;
import com.qubicgo.android.appformacion.data.request.InvitationSchedulerRequest;
import com.qubicgo.android.appformacion.data.response.InvitationSendResponse;
import com.qubicgo.android.appformacion.data.request.InvitationTypeListRequest;
import com.qubicgo.android.appformacion.data.request.InvitationTypeRequest;
import com.qubicgo.android.appformacion.fragments.ActiveInvitationFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActiveInvitationDetailActivity extends  AppCompatActivity {

    @BindView(R.id.tvName)
    TextViewBoldCustom tvName;
    @BindView(R.id.tvTarget)
    TextViewJustifiedCustom tvTarget;
    @BindView(R.id.tvDate)
    TextViewCustom tvDate;
    @BindView(R.id.tvTime)
    TextViewCustom tvTime;

    @BindView(R.id.recyclerActivities)
    RecyclerView recyclerActivities;


    private ProgressDialog progressDialog;
    private InvitationRequest model;
    private List<InvitationSchedulerRequest> list;
    private Context mContext;

    private  List <InvitationTypeRequest> responseInv;
    private  List <String> responseInv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_invitation_detail);

        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle(R.string.text_active_invitations_large);
        mContext = this;


        list = new ArrayList<>();

        int position = getIntent().getIntExtra("active_invitation_adapter_position",-1);
        model = ActiveInvitationFragment.list.get(position);

        list = (List<InvitationSchedulerRequest>)getIntent().getSerializableExtra("active_invitation_adapter_list" );

        if(model!=null){
            tvName.setText(model.getName());
            tvTarget.setText(model.getTarget());
            tvDate.setText(model.getDate());
            tvTime.setText(model.getHours()+":"+model.getMinutes()+" horas");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh:
                break;
        }
        return true;
    }



    @Override
    protected void onResume() {
        super.onResume();

        responseInv = new ArrayList<>();
        responseInv2 = new ArrayList<>();

        ActiveInvitationRecyclerAdapter adapter = new ActiveInvitationRecyclerAdapter(R.layout.item_recycler_invitations,list);
        recyclerActivities.setLayoutManager(new LinearLayoutManager(ActiveInvitationDetailActivity.this));
        recyclerActivities.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        final IServiceInvitation invitationService = ServiceFactoryInvitation.getListTypeResponseConfiguration(mContext).create(IServiceInvitation.class);
        Call<InvitationTypeListRequest> response = invitationService.obtenerConfiguracionInvitacionActiva(
                mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction","")
        );

        response.enqueue(new Callback<InvitationTypeListRequest>() {
            @Override
            public void onResponse(Call<InvitationTypeListRequest> call, Response<InvitationTypeListRequest> response) {
                InvitationTypeListRequest result = response.body();

                if("OK".equals( result.getStatus()) && result.getBody()!=null){
                    for (InvitationTypeRequest item : result.getBody()){
                        responseInv.add(item);
                        responseInv2.add(item.getLabel());
                    }
                }
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<InvitationTypeListRequest> call, Throwable t) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (t.toString().contains("BEGIN_OBJECT")){
                    HomeActivity.restartSession(mContext);
                }
            }
        });
    }

    @OnClick(R.id.btnReplyInvitation)
    public void onReplyInvitationClicked() {

        Dialog dialog = new Dialog(ActiveInvitationDetailActivity.this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.setContentView(R.layout.dialog_invitation_response);

        final Spinner spResponse = dialog.findViewById(R.id.spResponse);
        Button btnResponse = dialog.findViewById(R.id.btnResponse);

        ArrayAdapter adapter = new ArrayAdapter(
                ActiveInvitationDetailActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                responseInv2
        );
        spResponse.setAdapter(adapter);

        btnResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(getApplicationContext());
                    progressDialog.setMessage(getResources().getString(R.string.loading));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }

                final String respuesta = spResponse.getSelectedItem().toString();
                InvitationTypeRequest selected = null;
                for (InvitationTypeRequest item : responseInv) {
                    if (item.getLabel().equals(respuesta)) {
                        selected = item;
                        break;
                    }
                }

                SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva",Context.MODE_PRIVATE);
                String userName = preferences.getString("username", "");

                final IServiceInvitation invitationService = ServiceFactoryInvitation.setResponse(mContext).create(IServiceInvitation.class);
                Call<InvitationSendResponse> response = invitationService.recepcionRespuestInvitacion(
                        mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                        model.getGroupId(),userName,selected.getCode(),selected.getFlag());

                response.enqueue(new Callback<InvitationSendResponse>() {
                    @Override
                    public void onResponse(Call<InvitationSendResponse> call, Response<InvitationSendResponse> response) {
                        InvitationSendResponse result = response.body();
                        int moreMinute = 1;
                        int serviceId = (new Random()).nextInt((100-10)+1)+10;
                        if (result != null) {

                            for(InvitationSchedulerRequest schedulerResponse : list){
                                String[] date = schedulerResponse.getDate().split("/");
                                String[] time = schedulerResponse.getTimeStart().split(":");
                                String body = "Recordatorio para la asistencia que se llevara a cabo en: " + schedulerResponse.getRoom();
                                String title = model.getName();

                                createAlarm(date, time,title,body, moreMinute,serviceId);
                                moreMinute++;
                                serviceId ++;
                                Toast.makeText(ActiveInvitationDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActiveInvitationDetailActivity.this, HomeActivity.class);
                                startActivity(intent);

                            }

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<InvitationSendResponse> call, Throwable t) {
                        Toast.makeText(ActiveInvitationDetailActivity.this, "Ocurrió un error", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (t.toString().contains("BEGIN_OBJECT")){
                            HomeActivity.restartSession(mContext);
                        }
                    }
                });
            }
        });
    }

    private void createAlarm(String[] date, String[] time, String title, String body, int minutes, int serviceId){

        Calendar now = Calendar.getInstance();

        now.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));
        now.set(Calendar.MONTH, Integer.parseInt(date[1])-1);
        now.set(Calendar.YEAR, Integer.parseInt(date[2]));
        now.set(Calendar.HOUR,Integer.parseInt(time[0]));
        now.set(Calendar.MINUTE,Integer.parseInt(time[1]));
        now.set(Calendar.SECOND,0);
        now.add(Calendar.MINUTE,-10);

        Log.d("alarma", String.valueOf(serviceId));
        Intent intent = new Intent(mContext,NotificationRecieve.class);
        intent.putExtra("title",title);
        intent.putExtra("body",body);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,serviceId,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(),pendingIntent);

    }

    class ActiveInvitationRecyclerAdapter extends RecyclerView.Adapter<ActiveInvitationRecyclerAdapter.ViewHolder> {

        private int layout;
        private List<InvitationSchedulerRequest> list;

        public ActiveInvitationRecyclerAdapter(int layout, List<InvitationSchedulerRequest> list ) {
            this.layout = layout;
            this.list = list;

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
            holder.bind(list.get(position));
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.tvTimeStart)
            TextViewBoldCustom tvTimeStart;
            @BindView(R.id.tvTimeEnd)
            TextViewCustom tvTimeEnd;
            @BindView(R.id.tvDate)
            TextViewBoldCustom tvDate;
            @BindView(R.id.tvAddress)
            TextViewCustom tvAddress;

            Unbinder unbinder;


            public ViewHolder(View itemView) {
                super(itemView);
                unbinder = ButterKnife.bind(this, itemView);
            }

            public void bind(final InvitationSchedulerRequest model){

                tvTimeStart.setText(model.getTimeStart());
                tvTimeEnd.setText(model.getTimeEnd());
                tvDate.setText(model.getDate());
                tvAddress.setText(model.getRoom());
            }
        }

    }

}
