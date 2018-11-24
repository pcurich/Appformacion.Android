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
import android.widget.Toast;

import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.activities.HomeActivity;
import com.qubicgo.android.appformacion.activities.MaterialDetailActivity;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.services.def.IServiceMaterial;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryMaterials;
import com.qubicgo.android.appformacion.data.request.MaterialListRequest;
import com.qubicgo.android.appformacion.data.request.MaterialRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MaterialFragment extends Fragment {

    @BindView(R.id.rvMaterialDrive)
    RecyclerView rvMaterialDrive;

    Unbinder unbinder;

    private Context mContext;
    private ProgressDialog progressDialog;

    public static List<MaterialRequest> list;

    public MaterialFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_material, container, false);
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

    private void loadActivities() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva",Context.MODE_PRIVATE);
        String userName = preferences.getString("username", "");

        final IServiceMaterial materialService = ServiceFactoryMaterials.getListMaterials(mContext).create(IServiceMaterial.class);
        Call<MaterialListRequest> response = materialService.obtenerUrlDrive(
                mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                userName);

        response.enqueue(new Callback<MaterialListRequest>() {
            @Override
            public void onResponse(Call<MaterialListRequest> call, Response<MaterialListRequest> response) {

                MaterialListRequest result = response.body();

                if("OK".equals( result.getStatus()) && result.getBody()!=null){

                    list.clear();
                    list.addAll(result.getBody());
                    MaterialAdapter adapter = new MaterialAdapter(mContext, result.getBody());
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL);
                    dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_decoration));
                    rvMaterialDrive.addItemDecoration(dividerItemDecoration);
                    rvMaterialDrive.setLayoutManager(new LinearLayoutManager(mContext));
                    rvMaterialDrive.setAdapter(adapter);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MaterialListRequest> call, Throwable t) {
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

    class MaterialAdapter  extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

        private Context context;
        private List<MaterialRequest> list;

        public MaterialAdapter(Context context, List<MaterialRequest> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public MaterialAdapter.MaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_material, parent, false);
            return new MaterialAdapter.MaterialViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(MaterialAdapter.MaterialViewHolder holder, final int position) {
            holder.bind(list.get(position), position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MaterialViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.llContenedor)
            LinearLayout llContenedor;

            @BindView(R.id.tvTitle)
            TextViewBoldCustom tvTitle;

            @BindView(R.id.tvType)
            TextViewCustom tvType;

            @BindView(R.id.flBackground)
            FrameLayout flBackground;

            Context mContext;

            public MaterialViewHolder(View itemView, Context context) {
                super(itemView);
                mContext = context;
                ButterKnife.bind(this, itemView);
            }

            public void bind(final MaterialRequest model, final int position) {

                tvTitle.setText(model.getName());
                tvType.setText("CURS".equals(model.getType()) ? "C" : "P");

                if ((position + 1) % 2 == 0) {
                    flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_pair));
                } else {
                    flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_no_pair));
                }

                llContenedor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Intent intent = new Intent(mContext, MaterialDetailActivity.class);
                        intent.putExtra("material_adapter_position", position);
                        if (model.getUrlDrive() == null) {
                            Toast.makeText(context, "Materiales no disponibles", Toast.LENGTH_SHORT).show();
                        } else {
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
        }
    }
}
