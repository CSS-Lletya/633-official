package com.rs.utilities;
import java.util.ArrayList;
import java.util.List;

public class TextWrapping {

    private static final char SPACE = ' ';
    private static final int DEFAULT_LENGTH = 60;

    public static String[] wrap(final String str) {
        return wrap(str, DEFAULT_LENGTH);
    }

    public static String[] wrap(final String str, int wrapLength) {
        if (str == null) {
            return null;
        }

        if (wrapLength < 1) {
            wrapLength = 1;
        }

        final int inputLineLength = str.length();
        int offset = 0;
        final List<String> wrappedLines = new ArrayList<>();

        while (offset < inputLineLength) {
            int spaceToWrapAt = findSpaceToWrapAt(str, offset, wrapLength);

            if (spaceToWrapAt >= offset) {
                wrappedLines.add(str.substring(offset, spaceToWrapAt));
                offset = spaceToWrapAt + 1;
            } else {
                wrappedLines.add(str.substring(offset));
                offset = inputLineLength;
            }
        }

        return wrappedLines.toArray(new String[0]);
    }

    private static int findSpaceToWrapAt(final String str, int offset, int wrapLength) {
        int spaceToWrapAt = str.indexOf(SPACE, offset + wrapLength);
        if (spaceToWrapAt == -1) {
            return str.length();
        }
        return spaceToWrapAt;
    }
}