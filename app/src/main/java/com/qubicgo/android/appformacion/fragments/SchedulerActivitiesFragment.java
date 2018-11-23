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
import android.widget.ProgressBar;

import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.activities.HomeActivity;
import com.qubicgo.android.appformacion.activities.SchedulerDetailActivity;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.services.def.IServiceScheduler;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryScheduler;
import com.qubicgo.android.appformacion.data.request.SchedulerListRequest;
import com.qubicgo.android.appformacion.data.request.SchedulerRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class SchedulerActivitiesFragment extends Fragment {

    @BindView(R.id.rvSchedulerActivity)
    RecyclerView rvSchedulerActivity;

    Unbinder unbinder;

    private Context mContext;
    private ProgressDialog progressDialog;
    public static ArrayList<SchedulerRequest> list;

    public SchedulerActivitiesFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_schedule_activities, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        list = new ArrayList<>();
        mContext = getActivity();

        loadActivities();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh:
                loadActivities();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_home,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void loadActivities()
    {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva",Context.MODE_PRIVATE);
        String userName = preferences.getString("username", "");

        final IServiceScheduler invitationService = ServiceFactoryScheduler.getSchedulerListConfiguration(mContext).create(IServiceScheduler.class);
        Call<SchedulerListRequest> response = invitationService.obtenerActividadesProgramadas(
                mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                userName);

        response.enqueue(new Callback<SchedulerListRequest>() {
            @Override
            public void onResponse(Call<SchedulerListRequest> call, Response<SchedulerListRequest> response) {

                SchedulerListRequest result = response.body();

                if("OK".equals( result.getStatus()) && result.getBody()!=null){

                    list.clear();
                    list.addAll(result.getBody());
                    SchedulerActivityAdapter adapter = new SchedulerActivityAdapter(mContext, result.getBody());
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL);
                    dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_decoration));
                    rvSchedulerActivity.addItemDecoration(dividerItemDecoration);
                    rvSchedulerActivity.setLayoutManager(new LinearLayoutManager(mContext));
                    rvSchedulerActivity.setAdapter(adapter);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SchedulerListRequest> call, Throwable t) {
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

    class SchedulerActivityAdapter extends RecyclerView.Adapter<SchedulerActivityAdapter.ShedulerViewHolder> {

        private Context context;
        private List<SchedulerRequest> list;

        public SchedulerActivityAdapter(Context context, List<SchedulerRequest> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public SchedulerActivityAdapter.ShedulerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_scheduler, parent, false);
            return new SchedulerActivityAdapter.ShedulerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SchedulerActivityAdapter.ShedulerViewHolder  holder, final int position) {
            holder.bind(list.get(position),position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ShedulerViewHolder extends RecyclerView.ViewHolder {

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
            @BindView(R.id.progressBar)
            ProgressBar progressBar;

            public ShedulerViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bind(final SchedulerRequest model, final int position){

                if ((position + 1) % 2 == 0) {
                    this.flBackground.setBackgroundColor(context.getResources().getColor(R.color.color_invitation_pair));
                } else {
                    this.flBackground.setBackgroundColor(context.getResources().getColor(R.color.color_invitation_no_pair));
                }

                tvCount.setText("CURS".equals( model.getType()) ? "C":"P");
                String[] sessions = model.getSessions().split("/");
                String details =  String.format("%.1f",(Float.parseFloat(sessions[0])/Float.parseFloat(sessions[1]))*100)+"%";
                progressBar.setProgress((int) ((Float.parseFloat(sessions[0])/Float.parseFloat(sessions[1]))*100));
                tvCursoDetails.setText(details);

                tvCurso.setText(model.getName());
                llContenedor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SchedulerDetailActivity.class);
                        intent.putExtra("scheduler_activity_adapter_position", position);
                        context.startActivity(intent);
                    }
                });
            }

        }
    }

}
