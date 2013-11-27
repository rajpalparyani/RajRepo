package com.telenav.carconnect.provider.abbrev;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class StringUtil
{
	
	public static final String TOKENIZE_PATTERN = "[ ]" ;
	
	
	public static String normalize(String text)
	{
		// remove period
		text = text.replaceAll("\\.", " ");
		
		// remove duplicate spaces
		text = text.replaceAll("\\s+", " ") ;
		
		return text ;
	}
	
	public static String[] tokenize(String name)
	{
		String[] tokens = name.split(TOKENIZE_PATTERN) ;
		return tokens ;
	}
	
	public static String standardize(String s, Locale l)
	{
		Map<String,String> standardizeTable = ConfigUtil.getStandardizeTable(l) ;
		
		if (standardizeTable == null)
		{
			// couldn't load standardize table for this locale
			// so don't try to standardize
			return s ;
		}
		
		StringBuffer sb = new StringBuffer() ;
		String[] tokens = tokenize(s) ;
		
		for (int i=0; i<tokens.length; i++)
		{
		    String stk = tokens[i].toUpperCase(l);
			if (standardizeTable.containsKey(stk))
			{
				sb.append(standardizeTable.get(stk)+" ") ;
			}
			else
			{
				sb.append(tokens[i]+" ") ;
			}
		}
		
		return sb.toString().trim() ;
	}
	
	
	/**
	 * This method compares two names.  First it just tries .equals().
	 * If that fails, it normalizes all special characters and tries to
	 * match again.
	 * 
	 * @param name1
	 * @param name2
	 * @return
	 */
	public static boolean areTheSame(String name1, String name2)
	{
		if (name1 == null || name2 == null)
			return false ;
			
		if (name1.equals(name2))
			return true ;
		
		// ok, they still don't match, so normalize special characters for
		// both name1 and name2
		return normalizeSpecialChars(name1).equals(
						normalizeSpecialChars(name2)) ;
	}
	
	public static String normalizeSpecialChars(String s)
	{
		Map<String,String> specialCharTable
						= ConfigUtil.getSpecialCharRegexTable() ;
		Iterator<Map.Entry<String, String>> iter = specialCharTable.entrySet().iterator() ;
		while (iter.hasNext())
		{
			Map.Entry<String, String> entry = iter.next() ;
			s = s.replaceAll(entry.getValue(), entry.getKey()) ;
		}
		return s ;
	}
	
}
