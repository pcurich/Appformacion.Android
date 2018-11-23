package com.qubicgo.android.appformacion.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.qubicgo.android.appformacion.activities.HomeActivity;
import com.qubicgo.android.appformacion.activities.PollListActivity;
import com.qubicgo.android.appformacion.activities.PollOneActivity;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.data.request.PollActiveListRequest;
import com.qubicgo.android.appformacion.data.request.PollActiveRequest;
import com.qubicgo.android.appformacion.data.request.PollAnswerSettingRequest;
import com.qubicgo.android.appformacion.services.def.IServicePoll;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryPoll;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class ActivePollsFragment extends Fragment {

    @BindView(R.id.rvPollActive)
    RecyclerView rvPollActive;

    Unbinder unbinder;

    private Context mContext;
    private ProgressDialog progressDialog;
    public static List<PollActiveRequest> list;

    public ActivePollsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_polls, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        list = new ArrayList<>();
        mContext = getActivity();

        loadActivePolls();
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
                loadActivePolls();
                break;
        }
        return true;
    }

    private  void loadActivePolls(){
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        final IServicePoll pollAnswerService = ServiceFactoryPoll.getListPollAnswer(mContext).create(IServicePoll.class);
        Call<PollAnswerSettingRequest> answerSettingResponseCall = pollAnswerService.buscarConfiguracionEncuestas(
                mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction","")
        );

        answerSettingResponseCall.enqueue(new Callback<PollAnswerSettingRequest>() {
            @Override
            public void onResponse(Call<PollAnswerSettingRequest> call, Response<PollAnswerSettingRequest> response) {
                final PollAnswerSettingRequest answerSettingResponse  = response.body();


                SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva",Context.MODE_PRIVATE);
                String userName = preferences.getString("username", "");

                final IServicePoll pollService = ServiceFactoryPoll.getListPolls(mContext).create(IServicePoll.class);
                Call<PollActiveListRequest> result = pollService.buscarEncuestasPendientes(
                        mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                        userName);

                result.enqueue(new Callback<PollActiveListRequest>() {
                    @Override
                    public void onResponse(Call<PollActiveListRequest> call, Response<PollActiveListRequest> response) {

                        PollActiveListRequest result = response.body();

                        if("OK".equals( result.getStatus()) && result.getBody()!=null){

                            list.clear();
                            list.addAll(result.getBody());

                            for (PollActiveRequest active : list){
                                for (PollActiveRequest.PollAspect aspect : active.getAspects()){
                                    for (PollActiveRequest.PollAspect.PollQuestion question : aspect.getQuestions()){
                                        List<PollActiveRequest.PollAspect.PollQuestion.PollAnswer> responseList = answerSettingResponse.getAnswerTypeByKey( aspect.getResposeType());
                                        for (PollActiveRequest.PollAspect.PollQuestion.PollAnswer r : responseList){
                                            r.setQuestionId(question.getQuestionId());
                                        }
                                        question.setResponseList(responseList);
                                    }
                                }
                            }

                            SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
                            Integer position = 1;
                            for (PollActiveRequest element : list){
                                sectionAdapter.addSection(new PollSection(element.getName(), element.getAspects(),element ,mContext,position,element.getType()));
                                position++;
                            }

                            rvPollActive.setLayoutManager(new LinearLayoutManager(mContext));
                            rvPollActive.setAdapter(sectionAdapter);
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<PollActiveListRequest> call, Throwable t) {
                        progressDialog.dismiss();
                        if (t.toString().contains("BEGIN_OBJECT")){
                            HomeActivity.restartSession(mContext);
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<PollAnswerSettingRequest> call, Throwable t) {
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class PollSection extends StatelessSection {

        String title;
        String type;
        PollActiveRequest pollActive;
        List<PollActiveRequest.PollAspect> list;
        Integer position;
        Context mContext;

        public PollSection(String title,
                           List<PollActiveRequest.PollAspect> list,
                           PollActiveRequest pollActive,
                           Context context,
                           Integer position,
                           String type)
        {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.item_active_section_item)
                    .headerResourceId(R.layout.item_active_section_header)
                    .build());

            this.title = title;
            this.list = list;
            this.pollActive = pollActive;
            this.mContext = context;
            this.position = position;
            this.type = type;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view,mContext,pollActive );
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            PollActiveRequest.PollAspect  item  = list.get(position);
            itemHolder.bind(item,title);
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.bind(position,type,title,mContext);
        }
    }

    class ItemViewHolder  extends RecyclerView.ViewHolder {

        @BindView(R.id.llContenedor)
        LinearLayout llContenedor;

        @BindView(R.id.tvTitle)
        TextViewCustom  tvTitle;

        @BindView(R.id.tvSubTitle)
        TextViewCustom  tvSubTitle;

        Context mContext;
        PollActiveRequest pollActive;

        public ItemViewHolder(View itemView, Context context, PollActiveRequest pollActive) {
            super(itemView);
            mContext = context;
            this.pollActive = pollActive;
            ButterKnife.bind(this, itemView);
        }

        public void bind(final PollActiveRequest.PollAspect aspect , final String title){

            tvTitle.setText(aspect.getDescription());
            if(aspect.getTeacherName().length()>0) {
                tvSubTitle.setVisibility(View.VISIBLE);
                tvSubTitle.getLayoutParams().height=25;
                tvSubTitle.setText(aspect.getTeacherName());
            }

            llContenedor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Intent intent;

                    if(aspect.getQuestions().size()==1 &&  aspect.getQuestions().get(0).getResponseList().size()==1){
                        intent = new Intent(mContext,PollOneActivity.class);
                    }else{
                        intent = new Intent(mContext,PollListActivity.class);
                    }
                    intent.putExtra("active_poll_fragment_groupPersonId", pollActive.getGroupPersonId());
                    intent.putExtra("active_poll_fragment_aspect", aspect);
                    intent.putExtra("active_poll_fragment_name",title);
                    mContext.startActivity(intent);
                }
            });
        }

    }

    class HeaderViewHolder  extends RecyclerView.ViewHolder{

        @BindView(R.id.tvTitle)
        TextViewBoldCustom tvTitle;

        @BindView(R.id.tvType)
        TextViewCustom tvType;

        @BindView(R.id.flBackground)
        FrameLayout flBackground;


        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position, String type, String title, Context mContext) {
            tvTitle.setText(title);
            tvType.setText("CURS".equals(type) ? "C":"P");

            if ((position + 1) % 2 == 0) {
                flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_pair));
            } else {
                flBackground.setBackgroundColor(mContext.getResources().getColor(R.color.color_invitation_no_pair));
            }
        }
    }

}
