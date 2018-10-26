package com.ws.rest.webservice;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 *
 * @author Tiego Makaleng
 */

public class BookXMLParser {

	boolean isDataItemTag = false;
	String currentTagName = "";
	Book book = null;
	List<Book> books = new ArrayList<>();

	public static List<Book> parseFeed(String content){

		try {

			boolean isDataItemTag = false;
			String currentTagName = "";
			Book book = null;
			List<Book> books = new ArrayList<>();

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(new StringReader(content));

			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {

				switch (eventType) {
				case XmlPullParser.START_TAG:
					currentTagName = parser.getName();
					if (currentTagName.equals("programming")) {
						isDataItemTag = true;
						book = new Book();
						books.add(book);
					}
					break;

				case XmlPullParser.END_TAG:
					if (currentTagName.equals("programming")) {
						isDataItemTag = false;
					}
					currentTagName = "";
					break;

				case XmlPullParser.TEXT:
					if (isDataItemTag && book != null) {
						switch (currentTagName) {
						case "bookId":
							book.setBookId(Integer.parseInt(parser.getText()));
							break;
						case "isbn":
							book.setIsbn(Long.parseLong(parser.getText()));
							break;
						case "title":
							book.setTitle(parser.getText());
							break;
						case "photo":
							book.setPhoto(parser.getText());
							break;	
						default:
							break;
						}
					}
					break;
				}

				eventType = parser.next();
			}

			return books;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
