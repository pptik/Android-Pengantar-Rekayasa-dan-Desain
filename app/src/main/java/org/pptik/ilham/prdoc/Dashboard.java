package org.pptik.ilham.prdoc;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


import org.pptik.ilham.prdoc.TabConfigurations.SlidingTabLayout;
import org.pptik.ilham.prdoc.TabConfigurations.TabAdapter;

/**
 * Created by Ilham on 4/11/17.
 * emilhamep@icloud.com
 * PPTIK Intitut Teknologi Bandung
 * Kelas ini digunakan sebagai halaman awal ketika berhasil masuk
 */

public class Dashboard extends AppCompatActivity {
    Toolbar toolbar;
    String email, username, profilePicture;
    Integer idPengguna, universitas, peran;
    SlidingTabLayout slidingTabLayout;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Mengambil shared preferences pengguna
        SharedPreferences settings = getSharedPreferences(MainActivity.SessionPengguna, MODE_PRIVATE);
        email = settings.getString("email", null);
        idPengguna = settings.getInt("idPengguna", 0);
        universitas = settings.getInt("universitas", 0);
        peran = settings.getInt("peran", 0);
        username = settings.getString("username", null);
        profilePicture = settings.getString("profilePicture", "http://blog.ramboll.com/fehmarnbelt/wp-content/themes/ramboll2/images/profile-img.jpg");
        //Pengaturan toolbar
        setTitle("PRD");


        //Menginisialisasi widget
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setIcon(R.mipmap.toolbar_logo);

        //Menambahkan drawer
        //Header drawer
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.mipmap.account_header_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(username).withEmail(email).withIcon(Uri.parse(profilePicture)).withIdentifier(100)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();


        PrimaryDrawerItem item0 = new PrimaryDrawerItem().withIdentifier(1).withName("Materi").withIcon(R.mipmap.ic_dashboard_black_18dp);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Lakukan Bimbingan").withIcon(R.mipmap.ic_account_switch);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(1).withName("Ubah Profil").withIcon(R.mipmap.ic_person_black_24dp);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(2).withName("Keluar").withIcon(R.mipmap.ic_exit_to_app_black_18dp);

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item0,
                        item1,
                        item2,
                        new DividerDrawerItem(),
                        item3
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if(position == 3){
                            Intent intent = new Intent(getApplication(), ProfileEdit.class);
                            startActivity(intent);
                        }
                        else if(position == 5){
                            SharedPreferences pref = getApplicationContext().getSharedPreferences(MainActivity.SessionPengguna, MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.clear();
                            editor.commit();
                            finish();
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            startActivity(intent);
                        }
                        return false;
                    }
                })
                .build();

        result.setSelection(1);

        //Pengaturan tab
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager(), this));


        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTabLayout);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        slidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tv_tab);
        slidingTabLayout.setViewPager(viewPager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.menuLogOut:
                // User chose the "Settings" item, show the app settings UI...
                Log.d("Tombol Keluar", "Ditekan");

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
