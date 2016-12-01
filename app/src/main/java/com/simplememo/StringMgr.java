package com.simplememo;

/**
 * Created by USER on 2016-12-01.
 */
public class StringMgr {

	public static String extTagData(String text, String tag) {
		if (text.contains(tag)) {
			int start = text.indexOf(tag) + tag.length();
			int end = text.indexOf(tag, start + 1);
			if (end == -1) return null;
			return text.substring(start, end);
		} else {
			return null;
		}
	}
}
