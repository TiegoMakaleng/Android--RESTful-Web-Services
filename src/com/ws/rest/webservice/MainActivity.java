package com.ws.rest.webservice;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 *
 * @author Tiego Makaleng
 */

public class MainActivity extends ListActivity {
	public static final String PHOTOS_BASE_URL = "http://10.0.2.2/images/";

	private ProgressBar progressBar;
//	private List<MyTask> tasks;

	private List<Book> books;
    private GridView mGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.INVISIBLE);

//		tasks = new ArrayList<>();
		
        mGridView = (GridView) findViewById(R.id.gridView);
	}

	public void updateDisplay() {
		// Use BookAdapter to display data
		BookAdapter adapter = new BookAdapter(this, R.layout.book_list, books);
	    setListAdapter(adapter);
	 // mGridView.setAdapter(adapter);
	 // output.setAdapter(adapter);
	}

	// Check if the network connection is available 
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo    =  cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			/**
    		if (netInfo.getType() != ConnectivityManager.TYPE_WIFI) {
			    Toast.makeText(MainActivity.this, "This app doesn't work without Wi-Fi", Toast.LENGTH_LONG).show();
			    return false;
			}*/
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_task) {
			if (isOnline()) {
				  requestData("http://10.0.2.2/books.json");
			} else {
				Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void requestData(String uri) {
		
	   StringRequest request = new StringRequest(uri,
			new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					books = BookJSONParser.parseFeed(response);
					updateDisplay();
				}
			   },
			new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError ex) {
				   Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
				}
			  });
	    RequestQueue queue = Volley.newRequestQueue(this);
	    queue.add(request);
	}
}

