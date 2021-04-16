package ma.ensaj.expensemanagement;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import ma.ensaj.expensemanagement.ui.home.HomeFragment;
import ma.ensaj.expensemanagement.ui.listOfMovements.ListOfMovementsFragment;
import ma.ensaj.expensemanagement.ui.reportsByDate.ReportsByDateFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment fragmentHome;
    private Fragment fragmentListOfMovements;
    private Fragment fragmentReportsByDate;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_LIST_OF_MOVEMENTS = 1;
    private static final int FRAGMENT_REPORTS_BY_DATE = 2;

    private Toolbar toolbar;
    private TextView title;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);

        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();

        showFirstFragment();
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_home :
                this.showFragment(FRAGMENT_HOME);
                title.setText("Home");
                break;
            case R.id.nav_listOfMovements:
                this.showFragment(FRAGMENT_LIST_OF_MOVEMENTS);
                title.setText("List Of Movements");
                break;
            case R.id.nav_reportsByDate:
                this.showFragment(FRAGMENT_REPORTS_BY_DATE);
                title.setText("Reports By Date");
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(int fragmentIdentifier){
        switch (fragmentIdentifier){
            case FRAGMENT_HOME :
                this.showHomeFragment();
                break;
            case FRAGMENT_LIST_OF_MOVEMENTS:
                this.showListOfMovementsFragment();
                break;
            case FRAGMENT_REPORTS_BY_DATE:
                this.showReportsByDateFragment();
                break;
            default:
                break;
        }
    }

    private void showFirstFragment(){
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (visibleFragment == null){
            this.showFragment(FRAGMENT_HOME);
            title.setText("Home");
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    private void showHomeFragment(){
        if (this.fragmentHome == null) this.fragmentHome = new HomeFragment();
        this.startTransactionFragment(this.fragmentHome);
    }

    private void showListOfMovementsFragment(){
        if (this.fragmentListOfMovements == null) this.fragmentListOfMovements = new ListOfMovementsFragment();
        this.startTransactionFragment(this.fragmentListOfMovements);
    }

    private void showReportsByDateFragment(){
        if (this.fragmentReportsByDate == null) this.fragmentReportsByDate = new ReportsByDateFragment();
        this.startTransactionFragment(this.fragmentReportsByDate);
    }

    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }

    private void configureToolBar(){
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView(){
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}