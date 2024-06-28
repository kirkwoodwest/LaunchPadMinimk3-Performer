package com.kirkwoodwest.openwoods.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	static public boolean stringMatch(String lookup_string, String match){
		Pattern p = Pattern.compile("" + match, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(lookup_string);
		boolean b = m.find();
		return b;
	}
}
