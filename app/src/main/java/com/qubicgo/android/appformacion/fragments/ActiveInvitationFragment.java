package com.qubicgo.android.appformacion.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.activities.ActiveInvitationDetailActivity;
import com.qubicgo.android.appformacion.activities.HomeActivity;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.services.def.IServiceInvitation;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryInvitation;
import com.qubicgo.android.appformacion.data.request.InvitationListRequest;
import com.qubicgo.android.appformacion.data.request.InvitationRequest;
import com.qubicgo.android.appformacion.data.request.InvitationSchedulerListRequest;
import com.qubicgo.android.appformacion.data.request.InvitationSchedulerRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ActiveInvitationFragment extends Fragment {

    @BindView(R.id.rvActiveInvitations)
    RecyclerView rvActiveInvitations;

    Unbinder unbinder;

    private Context mContext;
    private ProgressDialog progressDialog;

    public static List<InvitationRequest> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_invitation, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        mContext = getActivity();
        list = new ArrayList<>();
        loadInvitations();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_home,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh:
                loadInvitations();
                break;
        }
        return true;
    }

    private void loadInvitations () {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva",Context.MODE_PRIVATE);
        String userName = preferences.getString("username", "");

        final IServiceInvitation invitationService = ServiceFactoryInvitation.getListConfiguration(mContext).create(IServiceInvitation.class);
        Call<InvitationListRequest> response = invitationService.obtenerInvitacionesActivas(
                mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                userName);

        response.enqueue(new Callback<InvitationListRequest>() {
            @Override
            public void onResponse(Call<InvitationListRequest> call, Response<InvitationListRequest> response) {

                InvitationListRequest result = response.body();

                if("OK".equals( result.getStatus()) && result.getBody()!=null){
                    list.clear();
                    list.addAll(result.getBody());

                    ActiveInvitationAdapter adapter = new ActiveInvitationAdapter(mContext,list);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL);
                    dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_decoration));
                    rvActiveInvitations.addItemDecoration(dividerItemDecoration);
                    rvActiveInvitations.setLayoutManager(new LinearLayoutManager(mContext));
                    rvActiveInvitations.setAdapter(adapter);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<InvitationListRequest> call, Throwable t) {
                progressDialog.dismiss();
                if (t.toString().contains("BEGIN_OBJECT")){
                    HomeActivity.restartSession(mContext);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class ActiveInvitationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;
        private List<InvitationRequest> list;

        public ActiveInvitationAdapter(Context context, List<InvitationRequest> list) {
            this.mContext = context;
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_invitation, parent, false);
            return new ActiveInvitationAdapter.InvitationViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ActiveInvitationAdapter.InvitationViewHolder viewHolder = (ActiveInvitationAdapter.InvitationViewHolder) holder;

            if (viewHolder != null) {
                InvitationRequest model = list.get(position);
                if ((position + 1) % 2 == 0) {
                    viewHolder.flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_pair));
                } else {
                    viewHolder.flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_no_pair));
                }

                viewHolder.tvCount.setText("CURS".equals(model.getType()) ? "C" : "P");
                String details = "Duracion: " + model.getHours() + ":" + model.getMinutes() + " - Sessiones: " + model.getnSessions();
                viewHolder.tvCursoDetails.setText(details);

                viewHolder.tvCurso.setText(model.getName());
                viewHolder.llContenedor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Intent intent = new Intent(mContext, ActiveInvitationDetailActivity.class);
                        intent.putExtra("active_invitation_adapter_position", position);

                        final IServiceInvitation invitationService = ServiceFactoryInvitation.getSchedulerConfiguration(mContext).create(IServiceInvitation.class);
                        Call<InvitationSchedulerListRequest> response = invitationService.obtenerSalaDisponibilidadInvitaciones(
                                mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                                list.get(position).getScheduleId());


                        response.enqueue(new Callback<InvitationSchedulerListRequest>() {
                            @Override
                            public void onResponse(Call<InvitationSchedulerListRequest> call, Response<InvitationSchedulerListRequest> response) {
                                InvitationSchedulerListRequest result = response.body();
                                List<InvitationSchedulerRequest> list = new ArrayList<>();

                                if ("OK".equals(result.getStatus()) && result.getBody() != null) {
                                    list.addAll(result.getBody());
                                    intent.putExtra("active_invitation_adapter_list", (Serializable) list);
                                    mContext.startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<InvitationSchedulerListRequest> call, Throwable t) {
                                if (t.toString().contains("BEGIN_OBJECT")) {
                                    HomeActivity.restartSession(mContext);
                                }
                            }
                        });


                    }
                });

            }
        }

        class InvitationViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tvCount)
            TextViewCustom tvCount;
            @BindView(R.id.tvCursoDetails)
            TextViewBoldCustom tvCursoDetails;
            @BindView(R.id.tvCurso)
            TextViewBoldCustom tvCurso;
            @BindView(R.id.llContenedor)
            LinearLayout llContenedor;
            @BindView(R.id.flBackground)
            FrameLayout flBackground;

            public InvitationViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
