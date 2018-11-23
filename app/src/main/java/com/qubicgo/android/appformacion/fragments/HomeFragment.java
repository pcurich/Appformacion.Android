package com.qubicgo.android.appformacion.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.activities.HomeActivity;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.services.def.IServiceDashboard;
import com.qubicgo.android.appformacion.data.request.DashboardDetailRequest;
import com.qubicgo.android.appformacion.data.request.DashboardRequest;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryDashboard;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    @BindView(R.id.tvActiveInvitation)
    TextViewCustom tvActiveInvitation;
    @BindView(R.id.tvActivitiesProgram)
    TextViewCustom tvActivitiesProgram;
    @BindView(R.id.tvAvailableMaterials)
    TextViewCustom tvAvailableMaterials;
    @BindView(R.id.tvAssistsToScore)
    TextViewCustom tvAssistsToScore;
    @BindView(R.id.tvActiveEvaluations)
    TextViewCustom tvActiveEvaluations;
    @BindView(R.id.tvActivePolls)
    TextViewCustom tvActivePolls;

    Unbinder unbinder;
    private Context mContext;
    private ProgressDialog progressDialog;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        loadInfo();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                loadInfo();
                break;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.llActiveInvitation)
    public void onLlActiveInvitationClicked() {
        ((HomeActivity) getActivity()).changeScreen(R.id.nav_active_invitations, 1);
    }

    @OnClick(R.id.llActivitiesProgram)
    public void onLlActivitiesProgramClicked() {
        ((HomeActivity) getActivity()).changeScreen(R.id.nav_scheduler_activities, 2);
    }

    @OnClick(R.id.llAvailableMaterials)
    public void onLlAvailableMaterialsClicked() {
        ((HomeActivity) getActivity()).changeScreen(R.id.nav_drive, 3);
    }

    @OnClick(R.id.llAssistsToScore)
    public void onLlAssistsToScoreClicked() {
        ((HomeActivity) getActivity()).changeScreen(R.id.nav_check_in, 4);
    }

    @OnClick(R.id.llActiveEvaluations)
    public void onLlActiveEvaluationsClicked() {
        ((HomeActivity) getActivity()).changeScreen(R.id.nav_evaluations, 5);
    }

    @OnClick(R.id.llActivePolls)
    public void onLlActivePollsClicked() {
        ((HomeActivity) getActivity()).changeScreen(R.id.nav_poll, 6);
    }

    private void loadInfo() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE);

        final IServiceDashboard dashboardService = ServiceFactoryDashboard.getConfiguration(mContext).create(IServiceDashboard.class);
        String userName = preferences.getString("username", "");
        Call<DashboardRequest> responseCall = dashboardService.getDashboard(
                mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                userName);

        responseCall.enqueue(new Callback<DashboardRequest>() {
            @Override
            public void onResponse(Call<DashboardRequest> call, Response<DashboardRequest> response) {

                DashboardRequest result = response.body();
                List<String> x_mule = response.raw().headers().values("x-mule_session");

                if (x_mule != null && !x_mule.get(0).equals("")) {

                    for (DashboardDetailRequest item : result.getDashboard()) {
                        switch (item.getKey()) {
                            case "INV_ACT":
                                tvActiveInvitation.setText(String.valueOf(item.getCantidad()));
                                break;
                            case "ACT_PRG":
                                tvActivitiesProgram.setText(String.valueOf(item.getCantidad()));
                                break;
                            case "MAT_DIS":
                                tvAvailableMaterials.setText(String.valueOf(item.getCantidad()));
                                break;
                            case "ASI_MAR":
                                tvAssistsToScore.setText(String.valueOf(item.getCantidad()));
                                break;
                            case "EVA_ACT":
                                tvActiveEvaluations.setText(String.valueOf(item.getCantidad()));
                                break;
                            case "ENC_ACT":
                                tvActivePolls.setText(String.valueOf(item.getCantidad()));
                                break;
                        }
                    }
                } else {
                    Toast.makeText(mContext, "Problemas con los servicios, contactar al administrador", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<DashboardRequest> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                //java.lang.IllegalStateException: Not a JSON Object: "<html>"
                HomeActivity.closeSession(mContext);

            }
        });
    }


}
