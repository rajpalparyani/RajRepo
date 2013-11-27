package com.telenav.carconnect.provider.abbrev;

import java.util.Locale;
import java.util.Map;


public class SegmentName
{	
	public static final byte FIRST_BIT_MASK = (byte) Math.pow(2, 7) ; // 1000 0000
	
	private String prefix ;
	private String postfix ;
	private String streettype ;
	private String namePart;
	private String officialName ;
	private String streetNumber;

	public SegmentName()
	{
		prefix = null ;
		postfix = null ;
		streettype = null ;
		officialName = null ;
		streetNumber = null;
	}
	
	public String getPrefix()
	{
		return prefix;
	}

	public String getPostfix()
	{
		return postfix;
	}

	public String getStreettype()
	{
		return streettype;
	}

	
	public void setNamePart(String name)
	{
		this.namePart = name;
	}

	public String getNamePart()
	{
		return namePart;
	}
	
	
	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public void setPostfix(String postfix)
	{
		this.postfix = postfix;
	}

	public void setStreettype(String streettype)
	{
		this.streettype = streettype;
	}
	
	public void setOfficialName(String name)
	{
		this.officialName = name ;
	}

	public void setStreetNumber(String number)
    {
        this.streetNumber = number ;
    }
	
	public String getStreetNumber()
    {
        return this.streetNumber;
    }
	
	public String getOfficialName()
	{
		return officialName;
	}
	
	public String getExpandedName()
	{
	    StringBuffer res = new StringBuffer();
	    if (streetNumber != null)
	    {
	        res.append(streetNumber);
            res.append(" ");
	    }
	    if (prefix != null)
	    {
	        res.append(prefix);
	        res.append(" ");
	    }
	    res.append(namePart);
	    if (streettype != null)
	    {
	        res.append(" ");
	        res.append(streettype);
	    }
	    
	    if (postfix != null)
	    {
	        res.append(" ");
	        res.append(postfix);
	    }
	    return res.toString();
	}
	
	public void getSegmentString(StringBuffer display, StringBuffer tts, String language)
	{
		Locale locale = new Locale(language) ;
		Map<String,String[]> directions = ConfigUtil.getDirectionsLookUpTable(locale) ;
		Map<String,String[]> streetTypes = ConfigUtil.getStreetTypesLookUpTable(locale) ;
		
		if (prefix != null)
		{
			String[] prefixToken = (String[]) directions.get(""+prefix) ;
			display.append(prefixToken[0]+" ") ;
			
			if (prefixToken.length == 2 &&
					prefixToken[1].length() > 0)
				tts.append(prefixToken[1]+" & ") ;
			else
				tts.append(prefixToken[0]+" & ") ;
		}
		
		if (streettype != null)
		{
			String[] streetTypeToken = (String[]) streetTypes.get(""+streettype) ;
			display.append(streetTypeToken[0]+" ") ;
			
			if (streetTypeToken.length == 2 &&
					streetTypeToken[1].length() > 0)
				tts.append(streetTypeToken[1]+" & ") ;
			else
				tts.append(streetTypeToken[0]+" & ") ;
		}
		
		if (postfix != null)
		{
			String[] postfixToken = (String[]) directions.get(""+postfix) ;
			display.append(postfixToken[0]+" ") ;
			
			if (postfixToken.length == 2 &&
					postfixToken[1].length() > 0)
				tts.append(postfixToken[1]+" & ") ;
			else
				tts.append(postfixToken[0]+" & ") ;
		}
		
		// remove trailing boundary
		display.setLength(display.length()-1) ; // removes trailing space
		tts.setLength(tts.length()-3) ; // removes trailing " & "
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer() ;
		
		sb.append("NUM: ").append(streetNumber).append("\n") ;
		sb.append("PREF: ").append(prefix).append("\n") ;
		sb.append("NAME: ").append(namePart).append("\n");
		sb.append("STYPE: ").append(streettype).append("\n") ;
		sb.append("POST: ").append(postfix).append("\n") ;
		
		return sb.toString() ;
	}

	
}
