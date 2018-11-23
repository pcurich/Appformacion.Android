package com.qubicgo.android.appformacion.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.qubicgo.android.appformacion.R;
import com.qubicgo.android.appformacion.fragments.ActiveEvaluationFragment;
import com.qubicgo.android.appformacion.fragments.ActiveInvitationFragment;
import com.qubicgo.android.appformacion.fragments.ActivePollsFragment;
import com.qubicgo.android.appformacion.fragments.CalendarFragment;
import com.qubicgo.android.appformacion.fragments.CheckInFragment;
import com.qubicgo.android.appformacion.fragments.HomeFragment;
import com.qubicgo.android.appformacion.fragments.MaterialFragment;
import com.qubicgo.android.appformacion.fragments.SchedulerActivitiesFragment;
import com.qubicgo.android.appformacion.fragments.WebBrowserFragment;
import com.qubicgo.android.appformacion.services.def.IServiceLogin;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactory;
import com.qubicgo.android.appformacion.services.implementation.ServiceFactoryLogin;
import com.qubicgo.android.appformacion.utilities.FileManager;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = HomeActivity.this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        if (getIntent().hasExtra("calendar")) {
            changeScreen(R.id.nav_calendar, 1);
        } else if (savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void changeScreen(int option, int position) {
        navigationView.getMenu().performIdentifierAction(option, position);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        String title = "";

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_active_invitations) {
            fragment = new ActiveInvitationFragment();
            title = getResources().getString(R.string.text_active_invitations_large);
        }else if (id == R.id.nav_scheduler_activities) {
            fragment = new SchedulerActivitiesFragment();
            title = getResources().getString(R.string.text_scheduler_activities_large);
        }else if (id == R.id.nav_check_in) {
            fragment = new CheckInFragment();
            title = getResources().getString(R.string.text_assists_score_large);
        }else if (id == R.id.nav_drive) {
            fragment = new MaterialFragment();
            title = getResources().getString(R.string.text_drive);
        }else if (id == R.id.nav_poll) {
            fragment = new ActivePollsFragment();
            title = getResources().getString(R.string.text_active_polls_large);
        }else if (id == R.id.nav_evaluations) {
            fragment = new ActiveEvaluationFragment();
            title = getResources().getString(R.string.text_active_evaluations_large);
        }else if (id == R.id.nav_google_plus) {
            fragment = new WebBrowserFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("url", "https://plus.google.com/");
            fragment.setArguments(bundle);
            title = getResources().getString(R.string.text_google_plus);
        }else if (id == R.id.nav_campus_bbva) {
            fragment = new WebBrowserFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("url", "http://www.campusbbva.com");
            fragment.setArguments(bundle);
            title = getResources().getString(R.string.text_campus_bbva);
        }else if (id == R.id.nav_calendar) {
            fragment = new CalendarFragment();
            title = getResources().getString(R.string.text_calendar);
        }else if (id == R.id.nav_sign_out) {
            closeSession(mContext);
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_home, fragment)
                    .commit();
            setTitle(title);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void closeSession(final Context mContext)
    {
        Intent intent = new Intent(mContext, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FileManager.writeToFile("",mContext);
        Toast.makeText(mContext,"Sesión finalizada, ingresar nuevamente", Toast.LENGTH_LONG).show();
        mContext.startActivity(intent);
    }

    public static void restartSession(final Context mContext){

        final IServiceLogin loginService  = ServiceFactoryLogin.getConfiguration(mContext).create(IServiceLogin.class);
        SharedPreferences preferences = mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("pwd", "");

        final Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Toast.makeText(mContext,"Sesión finalizada, ingresar nuevamente", Toast.LENGTH_LONG).show();

        if("".equals(username) && "".equals(password)){
            intent.setClass(mContext, SplashActivity.class);
            mContext.startActivity(intent);
        }else{
            Call<ResponseBody> response = loginService.logout();
            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).edit();
                    editor.clear();
                    editor.apply();
                    intent.setClass(mContext, SignInActivity.class);
                    mContext.startActivity(intent);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("formacionbbva", MODE_PRIVATE).edit();
                    editor.clear();
                    editor.apply();
                    intent.setClass(mContext, SplashActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                Fragment homeFragment = new HomeFragment();
                FragmentManager fm = getSupportFragmentManager();
                Fragment currentFragment = fm.findFragmentById(R.id.content_home);
                currentFragment.getId();
                currentFragment.getClass();
                if(!currentFragment.getClass().toString().equals(homeFragment.getClass().toString())){
                    fm.beginTransaction()
                            .replace(R.id.content_home, homeFragment)
                            .commit();
                    setTitle("");
                }else{
                    super.onKeyDown(keyCode,event);
                }
                break;
            default:
                super.onKeyDown(keyCode,event);
                break;

        }
        return true;
    }

}
