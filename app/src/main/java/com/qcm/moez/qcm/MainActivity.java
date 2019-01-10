package com.qcm.moez.qcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String url = "http://192.168.1.25:80/qcm";
    SharedPreferences sharedPref;
    TextView tvUser, tvUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*Intent in = new Intent(this,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),5555,in,0);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this);
        b
                .setContentTitle("mon titre")
                .setContentText("mon message")
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.ic_menu_camera)
                .build();

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(5555, b.build());*/



        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        try {
            tvUser = (TextView) hView.findViewById(R.id.tvUser);
            tvUserType = (TextView) hView.findViewById(R.id.tvTypeUser);
            System.out.println(tvUser.getText());
            System.out.println(tvUserType.getText());
            System.out.println(sharedPref.getString("Nom", "Nom")+ sharedPref.getString("Prenom", "prenom"));
            tvUser.setText(sharedPref.getString("Nom", "Nom")+" "+ sharedPref.getString("Prenom", "prenom"));
            if(sharedPref.getString("Type", "Type").equalsIgnoreCase("1"))
            {
                tvUserType.setText("Etudiant");
                // find MenuItem you want to change
                // get menu from navigationView
                Menu menu = navigationView.getMenu();
                MenuItem nav_valid = menu.findItem(R.id.nav_valider);

                // set new title to the MenuItem
                nav_valid.setVisible(false);

                MenuItem nav_cree = menu.findItem(R.id.nav_creer);

                // set new title to the MenuItem
                nav_cree.setVisible(false);





            }
            else
            if (sharedPref.getString("Type", "Type").equalsIgnoreCase("0"))
            {
                tvUserType.setText("Enseignant");
                // get menu from navigationView
                Menu menu = navigationView.getMenu();

                // find MenuItem you want to change
                MenuItem nav_hist = menu.findItem(R.id.nav_hist);

                // set new title to the MenuItem
                nav_hist.setTitle("Consulter Historique");

                // find MenuItem you want to change
                MenuItem nav_valid = menu.findItem(R.id.nav_valider);

                // set new title to the MenuItem
                nav_valid.setVisible(false);


            }
            else
                if(sharedPref.getString("Type", "Type").equalsIgnoreCase("3")){

                    tvUserType.setText("Administrateur");
                    // find MenuItem you want to change
                    // get menu from navigationView
                    Menu menu = navigationView.getMenu();
                    MenuItem nav_cree = menu.findItem(R.id.nav_creer);
                    MenuItem nav_hist = menu.findItem(R.id.nav_hist);
                    nav_hist.setVisible(false);
                    MenuItem nav_consult = menu.findItem(R.id.nav_consult);

                    // set new title to the MenuItem
                    nav_consult.setVisible(false);

                    // set new title to the MenuItem
                    nav_cree.setVisible(false);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this,"Crée par BENSELEM MOEZ",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_consult) {

            fragmentClass = ConsultFragment.class;
            //Toast.makeText(getApplicationContext(),"connssss",Toast.LENGTH_LONG).show();


        } else if (id == R.id.nav_hist) {
            fragmentClass = HistoryFragment.class;

        } else if (id == R.id.nav_creer) {
            fragmentClass = CreateFragment.class;

        } else if (id == R.id.nav_valider) {
            fragmentClass = VerifFragment.class;


        } else if (id == R.id.nav_config) {

            fragmentClass = ConfigFragment.class;

        } else if (id == R.id.nav_disconnect) {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("connected", false);
            editor.putString("Nom", null);
            editor.putString("Prenom", null);
            editor.putString("Password", null);
            editor.putString("idUser", null);
            editor.apply();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
