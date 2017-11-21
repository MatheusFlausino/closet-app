package com.matheusflausino.closetapp.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

import static android.content.ContentValues.TAG;

/**
 * Created by matheus on 01/11/17.
 */

public class PrefixTokenizer implements MultiAutoCompleteTextView.Tokenizer {
    private static final char NEW_LINE = '\n';

    private final char delimiter;
    private int text;
    public PrefixTokenizer(char prefix) {
        delimiter = prefix ;
    }

    @Override
    public int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;

        while (i > 0 && text.charAt(i - 1) != delimiter) {
            i--;
        }
        while (i < cursor && text.charAt(i) == delimiter) {
            i++;
        }
        this.text = i;
        Log.d(TAG, "findTokenStart: "+String.valueOf(i));
        return i;
    }

    @Override
    public int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        int len = text.length();

        while (i < len) {
            if (text.charAt(i) == delimiter) {
                return i == 0 ? 0 : i - 1;
            } else {
                i++;
            }
        }

        return len;
    }

    @Override
    public CharSequence terminateToken(CharSequence text) {

        int i = text.length();
        char separator = getDefaultSeparator();

        while (i > 0 && text.charAt(i - 1) == ' ') {
            i--;
        }

        // the text is empty or already has a trailing space
        if (i == 0 || (i > 0 && text.charAt(i - 1) == separator)) {
            return text;
        }

        // append a trailing space to the string
        if (text instanceof Spanned) {
            SpannableString sp = new SpannableString(text + String.valueOf(separator));
            TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);

            return sp;
        } else {
            if(this.text == 0) {
                text = "#"+text;
            }

            return text + String.valueOf(separator);
        }
    }

    protected char getDefaultSeparator() {
        return ' ';
    }
}
