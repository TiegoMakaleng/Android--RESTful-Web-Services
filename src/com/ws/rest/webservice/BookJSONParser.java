package com.ws.rest.webservice;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Tiego Makaleng
 */

public class BookJSONParser {

	public static List<Book> parseFeed(String content){
		try {
			
            JSONObject jsonObject = new JSONObject(content);
		    JSONArray ar = jsonObject.getJSONArray("books");

			List<Book> books = new ArrayList<>();

			for (int i = 0; i < ar.length(); i++) {

				JSONObject obj = ar.getJSONObject(i);
				Book book = new Book();
                book.setBookId(obj.getInt("bookid"));
				book.setIsbn(obj.getLong("isbn"));
				book.setTitle(obj.getString("title"));
				book.setPhoto(obj.getString("photo"));

				books.add(book);
			}

			return books;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
