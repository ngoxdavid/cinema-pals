package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GoogleSignInClient mGoogleSignInClient;
    private PopupWindow popUpInfo;
    private TinyDB tinyDB;
    private MenuItem myActionMenuItem;
    private SearchView searchView;
    private final String APP_NAME = "Cinema Pals";

    /**
     * Initiate MainActivity (Homepage)
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();
        toolbar.setTitle(APP_NAME);
        toolbar.setTitleTextColor(Color.WHITE);


        //Create a Google Sign In Client (will be used to logout)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //Creating a ViewPager that will allow us to swipe through tabs
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        initializeViewPager(viewPager);

        //Tab Layout that will have 3 tabs
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //Setting up the Tab Layout
        tabLayout.setupWithViewPager(viewPager);

        tinyDB = new TinyDB(getApplicationContext());
        viewPager.setCurrentItem(tinyDB.getInt("tabPosition"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Save User's Current Tab Position in Preferences
                tinyDB.putInt("tabPosition", tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    /**
     * Initializes the ViewPager, adds different tab fragments to an adapter
     * then sets the adapter to the ViewPager
     *
     * @param vp the ViewPager object that will hold the tabs
     */
    private void initializeViewPager(ViewPager vp) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Add Fragments (Tabs) to the View Pager (Swipe View)
        adapter.addFragment(new NowPlayingTab(), "Now Playing");
        //  adapter.addFragment(new UpcomingMoviesTab(), "Upcoming");
        adapter.addFragment(new ShowtimesTab(), "Show times");
        adapter.addFragment(new ProfileTab(), "Profile");
        vp.setAdapter(adapter);

        vp.setOffscreenPageLimit(3);
    }

    /**
     * ViewPagerAdapter Class
     * - Custom adapter for the ViewPager Tabs
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        /**
         * Returns fragment of selected tab
         *
         * @param position
         * @return fragment of selected tab
         */
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        /**
         * Returns number of tabs in ViewPager
         *
         * @return
         */
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        /**
         * Adds Fragment to ViewPager (Each Tab is a Fragment)
         *
         * @param fragment the tab
         * @param title    the title of the tab
         */
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        /**
         * Return Tab Title
         *
         * @param position position of the tab (first tab will be position = 0)
         * @return Gets a String for the Tab's Title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /**
     * Create Options Menu
     *
     * @param menu the menu in the top right that a user can select through
     * @return true if menu is created successfully
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();


        TextView textView = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textView.setTextColor(Color.WHITE);
        textView.setHint("Search a movie");

        ImageView magImage = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        magImage.setVisibility(View.GONE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print

                Intent intent = new Intent(getApplicationContext(), SearchResults.class);
                intent.putExtra("movieSearch", query);


                searchView.setQuery("", false);
                searchView.setIconified(true);

                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_about:
                popUp(this.getCurrentFocus());
                return true;
            case R.id.main_menu_logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * popUp - handles the popup window that appears when user clicks on About option
     *
     * @param view the view that the user currently sees
     */
    public void popUp(View view) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        ViewGroup container = (ViewGroup) inflater.inflate(R.layout.pop_up_about_me, null);
        LinearLayout linearL = (LinearLayout) findViewById(R.id.mainLinearLayout);
        popUpInfo = new PopupWindow(container, 800, 600, true);
        popUpInfo.showAtLocation(linearL, Gravity.CENTER, 0, 0);

        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popUpInfo.dismiss();
                return true;
            }
        });
    }

    /**
     * logOut - handles operations to log out the user from Google Client and out of the app
     */
    private void logOut() {

        TinyDB tinyDB = new TinyDB(getApplicationContext());
        tinyDB.putBoolean("logStatus", false);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
    }


}







