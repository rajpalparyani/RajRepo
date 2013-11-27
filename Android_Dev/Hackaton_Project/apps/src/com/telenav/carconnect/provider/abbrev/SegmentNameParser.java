package com.telenav.carconnect.provider.abbrev;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SegmentNameParser
{		
	protected static Pattern digitPattern ;
	protected static Pattern ordinalPattern ;
	protected static Pattern alphaPartPattern ;
	
	public static final int NAME_SLOT = 0 ;
	public static final int HIGHWAY_DIRECTIONAL_SLOT = 1 ;
	
	public static Pattern highwayDirectionalPattern;

	
	static
	{
		String digitPatternStr = "[0-9]+";
		digitPattern = Pattern.compile(digitPatternStr);
		
		String ordinalPatternStr = "^[0-9]*(1[Ss][Tt]|2[Nn][Dd]|3[Rr][Dd]|[04-9][Tt][Hh])$";
		ordinalPattern = Pattern.compile(ordinalPatternStr);
				
		String alphaPartPatternStr = "(-|\\b)?([A-Z&]+)([0-9\\-\\/]|\\b)";
		alphaPartPattern = Pattern.compile(alphaPartPatternStr);
		
		// The TeleAtlas highway directional pattern is: "//"
		// For some reason, the segment will have two highway directionals
		// in this case, we ignore the second one.
		String directionalPattern = "(.*?)\\/\\/([A-Za-z]+)(\\/\\/[A-Za-z]+)?$";
		highwayDirectionalPattern = Pattern.compile(directionalPattern);

	}
	
	protected static final int ID_SLOT = 0 ;
	
	public Locale getLocale()
	{
	    //Only support English
		return new Locale("en") ;
	}
	
	public SegmentName parse(String name)
	{
		Locale locale = getLocale() ;
		
		if (name == null
				|| name.length() == 0)
			return null ;
		
		SegmentName segment = new SegmentName() ;
		segment.setOfficialName(name) ;
		
		Map<String,String[]> directionsTable = ConfigUtil.getDirectionsTable(locale) ;
		Map<String,String[]> streetTypesTable = ConfigUtil.getStreetTypesTable(locale) ;
		
		// =============================
		// PARSE  HIGHWAY DIRECTIONAL
		// =============================
		// Check to see if there are any highway directionals before we start parsing
		boolean parsePostfix = true ;
		String[] parsed = parseHighwayDirection(name, locale) ;
		
		// If the highway directional slot is not null, then we have a match.
		// In that case, we continue parsing parsed[NAME_SLOT] as the name
		if (parsed[HIGHWAY_DIRECTIONAL_SLOT] != null)
		{
			name = parsed[NAME_SLOT] ;
			
			String idStr = ((String[]) directionsTable
							.get(parsed[HIGHWAY_DIRECTIONAL_SLOT]))[ID_SLOT] ;
			if (idStr != null)
			{
				segment.setPostfix(idStr) ;
				 // the postfix slot is already full so don't try to parse it
				parsePostfix = false ;
			}
			else
				System.err.println("Parsed directional: \""
								+parsed[HIGHWAY_DIRECTIONAL_SLOT]+"\" but couldn't" +
										" find in cache");
		}
		
		// ==================
		// ALIGN NAME TOKENS 
		// ==================
		String[][] aligned = align(name, locale) ;
		
		//create a copy of the name array
		String[] tokens = new String[aligned[0].length] ;
		System.arraycopy(aligned[0], 0, tokens, 0, aligned[0].length) ;
		
		//==================================
		// PARSE STREET NUMBER
		//==================================
		int prefixPos = 0;
		if (tokens[0].matches("\\d+"))
		{
		    segment.setStreetNumber(tokens[0]);
		    prefixPos = 1;
		    tokens[0] = null;
		}
		
				
		// ==================================
		// PARSE PREFIX
		// ==================================
		if (directionsTable.containsKey(tokens[prefixPos]))
		{
			String[] fields = (String[]) directionsTable.get(tokens[prefixPos]) ;
			
			String prefix = fields[ID_SLOT];
			
			segment.setPrefix(prefix) ;
			
			if (tokens.length==(prefixPos+1))
				// If this is the only token we are done parsing
				return segment ;
			
			// set the parsed prefix to null
			tokens[prefixPos] = null ;
			
		}
		
		// ==================================
		// PARSE POSTFIX
		// ==================================
		// Don't parse the postfix if we already filled the slot
		// with a highway directional
		if (parsePostfix
				&& directionsTable.containsKey(tokens[tokens.length-1]))
		{
			String[] fields = (String[]) directionsTable
										.get(tokens[tokens.length-1]) ;

			String postfix = fields[ID_SLOT];
			
			segment.setPostfix(postfix) ;
			
			// set the parsed postfix to null
			tokens[tokens.length-1] = null ;
			
		}
		
		// ==================================
		// PARSE STREET TYPE
		// ==================================
		// Find the correct index to check.  If the last token is null then
		// we already parsed it as a postfix, so check the penultimate
		// token, otherwise check the last token.
		int tokenToCheck = 0 ;
		int bound = 2 + prefixPos;
		if (tokens[tokens.length-1] != null)
			tokenToCheck = tokens.length-1 ; // there is no postfix 
		else if (tokens.length < bound 
				|| (tokens.length == bound && tokens[prefixPos] == null))
			return segment ; // we're done parsing
		else
			tokenToCheck = tokens.length-2 ;
		
		if (streetTypesTable.containsKey(tokens[tokenToCheck]))
		{
			String[] fields = (String[]) streetTypesTable
										.get(tokens[tokenToCheck]) ;

			String streetType = fields[ID_SLOT];
			segment.setStreettype(streetType) ;
				
				// set the parsed streettype to null
			tokens[tokenToCheck] = null ;
		}
		
		// ==================================
		// PARSE STREET NAME
		// ==================================
		// Anything we have parsed already will be null, so check for null and
		// ignore it.
		//
		for (int i=0; i<tokens.length; i++)
		{
			if (tokens[i] != null)
			{
				if(segment.getNamePart() == null)
					segment.setNamePart(tokens[i]);
				else{
					StringBuffer sb = new StringBuffer();
					sb.append(segment.getNamePart()).append(" ").append(tokens[i]);
					segment.setNamePart(sb.toString());
				}
			}
		}
		if (segment.getNamePart() == null)
		{
		    //In case the name part is null, the prefix should not be used
		    if (segment.getPrefix() != null)
		    {
		        segment.setPrefix(null);
		        segment.setNamePart(aligned[0][prefixPos]);
		    }
		}

		
		return segment ;
	}
	

	public static String[] parseHighwayDirection(String name, Locale locale)
	{
		String[] parsed = new String[2];
		
		// parse out the highway direction if any
		// Highway directions are flagged with a "//" or "/" followed by a
		// direction
		// This direction will be expanded and translated into the user's
		// language locale
		Matcher m = highwayDirectionalPattern.matcher(name);

		if (m.matches())
		{
			// get direction and name part
			parsed[NAME_SLOT] = m.group(1);
			parsed[HIGHWAY_DIRECTIONAL_SLOT] = m.group(2);
		}
		else
		{
			parsed[NAME_SLOT] = name ;
			parsed[HIGHWAY_DIRECTIONAL_SLOT] = null;
		}
		

		return parsed ;
	}
	
	public static String[][] align(String name, Locale locale)
	{ 
		
		// First normalize the name and norm name
		name = StringUtil.normalize(name) ;
		
		// then standardize the official name
		name = StringUtil.standardize(name, locale) ;
		
		String[] nameTokens = name.split(" ") ;
		
		String[][] alignWithNoPron = new String[3][nameTokens.length] ;
		for (int i=0; i<nameTokens.length; i++)
		{
			alignWithNoPron[0][i] = nameTokens[i] ;
		}
		
		return alignWithNoPron ;
	}
}
