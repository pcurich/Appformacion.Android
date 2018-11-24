package com.qubicgo.android.appformacion.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.activities.HomeActivity;
import com.qubicgo.android.appformacion.custom.TextViewBoldCustom;
import com.qubicgo.android.appformacion.custom.TextViewCustom;
import com.qubicgo.android.appformacion.data.request.CalendarListRequest;
import com.qubicgo.android.appformacion.data.request.CalendarRequest;
import com.qubicgo.android.appformacion.services.def.IServiceCalendar;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryCalendar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class CalendarFragment extends Fragment {

    @BindView(R.id.calendar)
    CalendarView calendar;

    Unbinder unbinder;

    @BindView(R.id.rvCalendarDetail)
    RecyclerView rvCalendarDetail;

    private Context mContext;
    private ProgressDialog progressDialog;
    private static List<CalendarRequest.CalendarDetailRequest> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        mContext = getActivity();
        list = new ArrayList<>();

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, final int year, final int month, final int dayOfMonth) {

                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage(getResources().getString(R.string.loading));
                progressDialog.setCancelable(false);
                progressDialog.show();

                SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva", Context.MODE_PRIVATE);
                String userName = preferences.getString("username", "");

                final IServiceCalendar calendarService = ServiceFactoryCalendar.getCalendarList(mContext).create(IServiceCalendar.class);
                Call<CalendarListRequest> response = calendarService.obtenerCalendario(
                        mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).getString("AppJunction",""),
                        userName,
                        String.valueOf(year),
                        String.format("%02d", (month + 1)));
                response.enqueue(new Callback<CalendarListRequest>() {

                    @Override
                    public void onResponse(Call<CalendarListRequest> call, Response<CalendarListRequest> response) {

                        CalendarListRequest result = response.body();

                        if ("OK".equals(result.getStatus()) && result.getBody() != null) {

                            list.clear();
                            for (CalendarRequest r : result.getBody()){
                                String tMonth = String.format("%02d", (month + 1));
                                String tday = String.format("%02d", dayOfMonth);
                                String date = String.valueOf(year)+  tMonth + tday;
                                if(r.getFechaKey().trim().equals(date.trim())){
                                    list.addAll(r.getCalendarDetailRequest());
                                }
                            }

                            CalendarAdapter adapter = new CalendarAdapter(mContext);
                            rvCalendarDetail.setLayoutManager(new LinearLayoutManager(mContext));
                            rvCalendarDetail.setAdapter(adapter);
                        }
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onFailure(Call<CalendarListRequest> call, Throwable t) {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        if (t.toString().contains("BEGIN_OBJECT")){
                            HomeActivity.restartSession(mContext);
                        }
                    }
                });


                Toast.makeText(getContext(), year + "/" + (month + 1) + "/" + dayOfMonth + "/", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    static class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

        private Context mContext;

        public CalendarAdapter(Context mContext ) {
            this.mContext = mContext;
        }

        @NonNull
        @Override
        public CalendarAdapter.CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_invitations, parent, false);
            return new CalendarAdapter.CalendarViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
            holder.bind(list.get(position), position);
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class CalendarViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tvTimeStart)
            TextViewBoldCustom tvTimeStart;
            @BindView(R.id.tvTimeEnd)
            TextViewCustom tvTimeEnd;
            @BindView(R.id.tvDate)
            TextViewBoldCustom tvDate;
            @BindView(R.id.tvAddress)
            TextViewCustom tvAddress;

            CalendarViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bind(CalendarRequest.CalendarDetailRequest  event , final int position) {
                tvTimeStart.setText(event.getStart());
                tvTimeEnd.setText(event.getEnd());
                tvDate.setText(event.getName());
                tvAddress.setText(event.getRoom());
            }
        }

    }

}
