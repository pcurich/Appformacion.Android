package com.qubicgo.android.appformacion.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.qubicgo.android.appformacion.activities.CheckInDetailActivity;
import com.qubicgo.android.appformacion.activities.HomeActivity;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.services.def.IServiceCheckIn;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryCheckIn;
import com.qubicgo.android.appformacion.data.request.CheckInListRequest;
import com.qubicgo.android.appformacion.data.request.CheckInRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInFragment extends Fragment {

    @BindView(R.id.rvCheckIn)
    RecyclerView rvCheckIn;

    Unbinder unbinder;

    private Context mContext;
    private ProgressDialog progressDialog;
    public static ArrayList<CheckInRequest>  list;

    public CheckInFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_in, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        list = new ArrayList<>();
        mContext = getActivity();

        loadCheckIn();
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
                loadCheckIn();
                break;
        }
        return true;
    }

    private void loadCheckIn() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva",Context.MODE_PRIVATE);
        String userName = preferences.getString("username", "");

        final IServiceCheckIn invitationService = ServiceFactoryCheckIn.getCheckInListConfiguration(mContext).create(IServiceCheckIn.class);
        Call<CheckInListRequest> response = invitationService.obtenerAsistenciaXMarcar(
                mContext.getSharedPreferences("formacionbbva", mContext. MODE_PRIVATE).getString("AppJunction",""),
                userName);

        response.enqueue(new Callback<CheckInListRequest>() {
            @Override
            public void onResponse(Call<CheckInListRequest> call, Response<CheckInListRequest> response) {

                CheckInListRequest result = response.body();

                if("OK".equals( result.getStatus()) && result.getBody()!=null){
                    list.clear();
                    list.addAll(result.getBody());
                    CheckInAdapter adapter = new CheckInAdapter(mContext,list);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL);
                    dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_decoration));
                    rvCheckIn.addItemDecoration(dividerItemDecoration);
                    rvCheckIn.setLayoutManager(new LinearLayoutManager(mContext));
                    rvCheckIn.setAdapter(adapter);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CheckInListRequest> call, Throwable t) {
                t.printStackTrace();
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

    class CheckInAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;
        private List<CheckInRequest> list;

        public CheckInAdapter(Context mContext, List<CheckInRequest> list) {
            this.mContext = mContext;
            this.list = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_invitation, parent, false);
            return new CheckInAdapter.CheckInViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            CheckInAdapter.CheckInViewHolder viewHolder = (CheckInAdapter.CheckInViewHolder) holder;

            if (viewHolder != null) {
                CheckInRequest model = list.get(position);
                if ((position + 1) % 2 == 0) {
                    viewHolder.flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_pair));
                } else {
                    viewHolder.flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_no_pair));
                }

                viewHolder.tvCount.setText("CURS".equals(model.getType()) ? "C" : "P");
                String details = model.getAddress();
                viewHolder.tvCursoDetails.setText(details);

                viewHolder.tvCurso.setText(model.getName());
                viewHolder.llContenedor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Intent intent = new Intent(mContext, CheckInDetailActivity.class);
                        intent.putExtra("check_in_adapter_position", position);
                        mContext.startActivity(intent);
                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class CheckInViewHolder extends RecyclerView.ViewHolder {

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

            public CheckInViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
