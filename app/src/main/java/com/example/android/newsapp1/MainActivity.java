package com.example.android.newsapp1;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {

    /**
     * URL to fetch the news data
     */
    private static final String REQUEST_URL =
            "https://content.guardianapis.com/search?show-fields=thumbnail&show-tags=contributor&api-key=d9d1e725-9638-4b88-89a6-b1c3704901d5";

    /**
     * Constant value for the news loader ID.
     */
    private static final int NEWS_LOADER_ID = 1;

    //Context
    private Context currentContext;

    /**
     * Adapter for newsList
     */
    private NewsAdapter newsAdapter;

    /**
     * TextView  is displayed when there are no news
     */
    private TextView emptyTextView;


    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    //News loader

    private final LoaderCallbacks<List<News>> newsLoader
            = new LoaderCallbacks<List<News>>() {
        @Override
        public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
            // Create a new loader for the given URL
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(currentContext);


            // parse breaks apart the URI string that's passed into its parameter
            Uri baseUri = Uri.parse(REQUEST_URL);

            // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
            Uri.Builder uriBuilder = baseUri.buildUpon();


            String sectionSelection = sharedPrefs.getString(
                    getString(R.string.settings_section_filter),
                    getString(R.string.settings_section_filter_default));

            // Append query parameter and its value. For example, the section=politics
            if (!sectionSelection.equals(getString(R.string.settings_section_filter_default))) {
                uriBuilder.appendQueryParameter("section", sectionSelection);
            }

            // Return the completed uri

            return new NewsLoader(currentContext,uriBuilder.toString());

        }


        @Override
        public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
            // Hide loading indicator because the data has been loaded
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Set empty state text to display "No news found."
            emptyTextView.setText(R.string.no_news);

            // If there is a valid list of {@link News}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (news != null && !news.isEmpty()) {
                newsAdapter.addAll(news);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<News>> loader) {
            // Loader reset, so we can clear out our existing data.
            newsAdapter.clear();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set context
        currentContext = this;

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = findViewById(R.id.list);

        emptyTextView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyTextView);

        // Create a new adapter that takes an empty list of news as input
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(newsAdapter);

        // Set an item click listener on the ListView, which is redirected or send intent to a web browser
        // to open a website with more information about the selected news.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news that was clicked on
                News currentNews = newsAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loaders. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, newsLoader);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyTextView.setText(R.string.no_internet_connection);
        }
    }
}