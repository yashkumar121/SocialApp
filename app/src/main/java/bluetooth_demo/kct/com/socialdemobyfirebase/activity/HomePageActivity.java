package bluetooth_demo.kct.com.socialdemobyfirebase.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import bluetooth_demo.kct.com.socialdemobyfirebase.R;
import bluetooth_demo.kct.com.socialdemobyfirebase.adapter.ViewPagerAdapter;
import bluetooth_demo.kct.com.socialdemobyfirebase.fragment.HomePageFragment;
import bluetooth_demo.kct.com.socialdemobyfirebase.util.AppPrefernce;

public class HomePageActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AppPrefernce(HomePageActivity.this).clearData();
                startActivity(new Intent(HomePageActivity.this,LogInActivity.class));
                finish();
            }
        });
    }
    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(HomePageFragment.newInstance("home"), "Home");
        adapter.addFragment(HomePageFragment.newInstance("myProfile"), "My Profile");

        viewPager.setAdapter(adapter);
    }


}
