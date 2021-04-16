package ma.ensaj.expensemanagement.ui.newMovement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ma.ensaj.expensemanagement.R;
import ma.ensaj.expensemanagement.ui.newMovement.ui.ExpenseFragment;
import ma.ensaj.expensemanagement.ui.newMovement.ui.IncomeFragment;
import ma.ensaj.expensemanagement.ui.newMovement.ui.TransferFragment;

public class NewMovementActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Intent intent;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView close_btn;
    private TextView title;
    private int[] tabIcons = {
            R.drawable.ic_action_add,
            R.drawable.ic_action_minus,
            R.drawable.ic_action_transfer
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_movement);

        intent = getIntent();
        configureToolbar();
    }

    public void configureToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String titleText = intent.getStringExtra("title");
        title = (TextView) findViewById(R.id.title);
        title.setText(titleText);

        setCloseAction();
        configureTabLayout();
    }


    public void configureTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.new_movement_tabs);
        viewPager = (ViewPager) findViewById(R.id.new_movement_viewpager);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


        int tabIndex = intent.getIntExtra("tabIndex", 0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.new_movement_tabs);
        TabLayout.Tab tab = tabLayout.getTabAt(tabIndex);
        tab.select();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tabLayout.getSelectedTabPosition();
                if(position == 0) {
                    title.setText("Add Incomes");
                }
                else if(position == 1) {
                    title.setText("Add Expenses");
                }
                else {
                    title.setText("Transfers");
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    private void setupViewPager(@NotNull ViewPager viewPager) {
        NewMovementActivity.ViewPagerAdapter adapter = new NewMovementActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new IncomeFragment(), "INCOME");
        adapter.addFragment(new ExpenseFragment(), "EXPENSE");
        adapter.addFragment(new TransferFragment(), "TRANSFER");
        viewPager.setAdapter(adapter);
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public void setCloseAction() {
        close_btn = (ImageView) findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}