package lin.demo.main;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import lin.demo.ActivityUtils;
import lin.demo.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);

        MainFragment listFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (listFragment == null){
            listFragment = MainFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), listFragment, R.id.contentFrame);
        }

        listFragment.setPresenter(new MainPresenter(listFragment));

        // Set up the navigation drawer.
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        if (navigationView != null) {
//            setupDrawerContent(navigationView);
//        }
//
//        TasksFragment tasksFragment =
//                (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
//        if (tasksFragment == null) {
//            // Create the fragment
//            tasksFragment = TasksFragment.newInstance();
//            ActivityUtils.addFragmentToActivity(
//                    getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
//        }
//
//        // Create the presenter
//        TasksRepository repository = Injection.provideTasksRepository(getApplicationContext());
//        TasksLoader tasksLoader = new TasksLoader(getApplicationContext(), repository);
//
//        mTasksPresenter = new TasksPresenter(
//                tasksLoader,
//                getSupportLoaderManager(),
//                repository,
//                tasksFragment
//        );
//
//        // Load previously saved state, if available.
//        if (savedInstanceState != null) {
//            TasksFilterType currentFiltering =
//                    (TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
//            mTasksPresenter.setFiltering(currentFiltering);
//        }


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
