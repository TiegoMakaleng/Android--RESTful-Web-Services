package com.ws.rest.webservice;

import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 * @author Tiego Makaleng
 */

@SuppressLint("ViewHolder")
public class BookAdapter extends ArrayAdapter<Book> {
	
	private Context context;
	private List<Book> books;
	
    private LruCache<Integer, Bitmap> imageCache;
    private RequestQueue queue;
    
	private static final String TAG = BookAdapter.class.getSimpleName();

	public BookAdapter(Context context, int resource, List<Book> books) {
		super(context, resource, books);
		this.context  = context;
		this.books    = books;
		
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	    final int cacheSize = maxMemory / 8;
	    imageCache = new LruCache<>(cacheSize);
	    
	    queue = Volley.newRequestQueue(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.book_list, parent, false);

		// Display book title in the TextView widget
		final Book book = books.get(position);
		TextView tvTitle = (TextView) view.findViewById(R.id.textViewBook);
		tvTitle.setText(book.getTitle());

		// Display image in the ImageView widget
		Bitmap bitmap = imageCache.get(book.getBookId());
		final ImageView image = (ImageView) view.findViewById(R.id.imageViewBook);
		if (bitmap != null) {
		//if (book.getBitmap() != null) {
			image.setImageBitmap(book.getBitmap());
		} else {
			String imageUrl = MainActivity.PHOTOS_BASE_URL + book.getPhoto();
			ImageRequest request = new ImageRequest(imageUrl, 
					new Response.Listener<Bitmap>() {

						@Override
						public void onResponse(Bitmap arg0) {
							image.setImageBitmap(arg0);
							imageCache.put(book.getBookId(), arg0); 
						}
					}, 
					80, 80, 
					Bitmap.Config.ARGB_8888, 
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							Log.d(TAG, arg0.getMessage());
						}
					});
			queue.add(request);
		}		

		return view;
	}
}
