package com.rs.utilities;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

public class TextWrapping {
	
	private static final char SPACE = ' ';
	private static final int DEFAULT_LENGTH = 60;

	public static String[] wrap(final String s) {
		return wrap(s, DEFAULT_LENGTH);
	}

	public static String[] wrap(final String s, int wrapLength) {
		wrapLength = Math.max(wrapLength, 1);

		final int n = s.length();
		int offset = 0;
		final ObjectList<String> w = new ObjectArrayList<>();

		while (offset < n) {
			int i = s.indexOf(SPACE, offset + wrapLength);
			if (i == -1) {
				i = n;
			}
			w.add(s.substring(offset, i));
			offset = i + 1;
		}

		return w.toArray(new String[0]);
	}
}
