package com.telenav.carconnect.provider.abbrev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ConfigUtil
{
	public static final String DATA_SET = "UTF8";

	public static final String DELIMITER_2D = "\\|";

	public static final String LOOK_UP = "lookup";
	public static final String DIRECTIONS = "com.telenav.carconnect.provider.abbrev.directions";
	public static final String HIGHWAY_TOKENS = "com.telenav.carconnect.provider.abbrev.highway_tokens";
	public static final String STREET_TYPES = "com.telenav.carconnect.provider.abbrev.streettypes";
	public static final String STANDARDIZE = "com.telenav.carconnect.provider.abbrev.standardize";
	public static final String ELABORATION = "com.telenav.carconnect.provider.abbrev.elaboration";
	public static final String LOCALE_COUNTRY_DEFAULTS = "com.telenav.carconnect.provider.abbrev.locale_country_defaults";
	public static final String LOCALE_STANDARD_TAGS = "com.telenav.carconnect.provider.abbrev.locale_standard_tags";
	public static final String SPECIAL_CHAR_TABLE = "com.telenav.carconnect.provider.abbrev.special_char_regex";
	
	private static Map<String, Map<String, String[]>> directionTables
								= new HashMap<String, Map<String, String[]>>();
	private static Map<String, Map<String, String>> highwayTokenTables
								= new HashMap<String, Map<String, String>>();
	private static Map<String, Map<String, String[]>> streettypeTables
								= new HashMap<String, Map<String, String[]>>();
	
	private static Map<String, Map<String, String>> standardizeTables
								= new HashMap<String, Map<String, String>>();
	private static Map<String, Map<String, String[]>> elaborationTables
								= new HashMap<String, Map<String, String[]>>();

	// this maps the country code to the default locale for that
	// country
	private static Map<String,Locale> localeCountryDefaultsMap;
	// this maps the language code to the default locale for that
	// country
	private static Map<String,String> localeStandardLangTagMap;
	
	private static Map<String,String> specialCharRegexMap;

	static
	{
		loadLocaleCountryDefaults();
		loadLocaleStandardLangTagMap();
	}

	public static Locale parseLocale(String localeStr)
	{
		Locale locale = null;
		String[] tokens = localeStr.split("_");
		if (tokens.length == 2)
			locale = new Locale(tokens[0], tokens[1]);
		else
		{
			System.out.println("Incorrect locale format: " + localeStr
					+ ".  Skipping ...");
		}
		return locale;
	}

	public static Locale constructLocale(String lang, String country)
	{
		Locale retLocale;

		if (lang == null || lang.trim().equals(""))
		{ // get the default locale for this country
			return localeCountryDefaultsMap.get(country.trim()
					.toUpperCase());
		}
		else
		{
			retLocale = new Locale(lang.toLowerCase());
		}

		return retLocale;
	}

	public static String toStandardLangTag(String langtag)
	{
		return localeStandardLangTagMap.get(langtag);
	}

	private static Map<String, String> loadTable(String tableKey,
			Locale locale, boolean switchKeyValue) throws IOException

	{
		String suffix = ".properties";

		Map<String, String> m = new LinkedHashMap<String, String>();

		// change "." to "/" in the tableKey
		tableKey = tableKey.replaceAll("\\.", "/");

		InputStream is = ConfigUtil.class
				.getResourceAsStream(tableKey + suffix);

		if (is == null)
		{
			// try with preceding slash
			is = ConfigUtil.class.getResourceAsStream("/" + tableKey + suffix);
		}

		// if it is still null, we were unable to load it
		if (is == null)
		{
			System.err.println("Can't load properties file: " + tableKey
					+ suffix);
			return new HashMap<String, String>();
		}

		InputStreamReader isr = new InputStreamReader(is, DATA_SET);
		BufferedReader in = new BufferedReader(isr);

		String line = null;
		while ((line = in.readLine()) != null)
		{
			// Ignore comments
			if (line.startsWith("#"))
				continue;

			// tokenize by "="
			String[] kv = line.split("=");

			if (kv.length > 0)
			{
				// first element is the key
				String key = kv[0];

				if (kv.length == 2)
				{
					String value = kv[1].trim();

					// note, some files may have extra data delimited by | in
					// the value. Parse out the extra data
					String[] tokens = value.split("\\|");

					if (switchKeyValue)
						m.put(tokens[0], key.trim());
					else
						m.put(key.trim(), tokens[0]);
				}
			}
		}
		
		isr.close() ;
		in.close() ;

		return m;
	}

	private static void loadLocaleCountryDefaults()
	{
		localeCountryDefaultsMap = new HashMap<String,Locale>();
		ResourceBundle cfg = PropertyResourceBundle
				.getBundle(LOCALE_COUNTRY_DEFAULTS);
		Enumeration<String> keys = cfg.getKeys();
		while (keys.hasMoreElements())
		{
			String key = (String) keys.nextElement();
			String lang = cfg.getString(key);
			// convert value to locale
			localeCountryDefaultsMap.put(key.trim(), new Locale(lang));
		}
	}

	private static void loadLocaleStandardLangTagMap()
	{
		localeStandardLangTagMap = new HashMap<String,String>();
		ResourceBundle cfg = PropertyResourceBundle
				.getBundle(LOCALE_STANDARD_TAGS);
		Enumeration<String> keys = cfg.getKeys();
		while (keys.hasMoreElements())
		{
			String key = (String) keys.nextElement();
			String value = cfg.getString(key);
			localeStandardLangTagMap.put(key.trim(), value.trim());
		}
	}

	/**
	 * The parameter switchKeyValue will place take the first item in the value
	 * list and make it the key and replace that slot in the list by the key
	 * 
	 * E.g. if the table entry is: 12=RD|ROAD|r ao d When switchKeyValue is
	 * true, it will load the entry as:
	 * 
	 * RD=12|ROAD|r ao d
	 * 
	 * We need support for this, because we need to be able to retrieve the
	 * normalized name from the properties file to use when verifying parsed
	 * street types, directions, lexemes, etc.
	 * 
	 * @param tableKey
	 * @param locale
	 * @param switchKeyValue
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String[]> load2DTable(String tableKey,
			Locale locale, boolean switchKeyValue) throws IOException

	{
		String suffix = ".properties";

		Map<String, String[]> m = new HashMap<String, String[]>();

		// change "." to "/" in the tableKey
		tableKey = tableKey.replaceAll("\\.", "/");

		InputStream is = ConfigUtil.class
				.getResourceAsStream(tableKey + suffix);

		if (is == null)
		{
			// try with preceding slash
			is = ConfigUtil.class.getResourceAsStream("/" + tableKey + suffix);
		}

		// if it is still null, we were unable to load it
		// but return a black table to avoid null pointer exceptions.
		if (is == null)
		{
			System.err.println("Can't load properties file: " + tableKey
					+ suffix);
			return new HashMap<String, String[]>();
		}

		InputStreamReader isr = new InputStreamReader(is, DATA_SET);
		BufferedReader in = new BufferedReader(isr);

		String line = null;
		while ((line = in.readLine()) != null)
		{
			// Ignore comments
			if (line.startsWith("#"))
				continue;

			// tokenize by "="
			String[] kv = line.split("=");

			if (kv.length > 0)
			{
				// first element is the key
				String key = kv[0];

				if (kv.length == 2)
				{
					String value = kv[1].trim();

					// The value may be a list, so tokenize with the delimiter
					// and and store the array as the value
					String[] list = value.split(DELIMITER_2D);
					String newKey = key.trim();

					if (switchKeyValue)
					{
						newKey = list[0];
						list[0] = key.trim();
					}

					m.put(newKey, list);
				}

			}
		}

		isr.close();
		in.close();

		return m;
	}

	/**
	 * This is the same as the directions table, except the key and value are
	 * switched allowing look up by id
	 * 
	 * @param locale
	 * @return
	 */
	public static Map<String, String[]> getDirectionsLookUpTable(Locale locale)
	{
		// build the key
		String tableKey = DIRECTIONS + "_" + locale;
		if (!directionTables.containsKey(tableKey + "_" + LOOK_UP))
			loadDirectionsLookUpTable(locale);

		return directionTables.get(tableKey + "_" + LOOK_UP);

	}
	
	public static Map<String,String> getSpecialCharRegexTable()
	{
		// build the key
		String tableKey = SPECIAL_CHAR_TABLE;

		if (specialCharRegexMap == null)
		{
			try
			{
				specialCharRegexMap = loadTable(tableKey, null, false);
			}
			catch (Exception e)
			{
				System.err.println("Unable to load/find resource: " + tableKey);
			}
		}
		
		return specialCharRegexMap ;
	}

	public static void loadDirectionsLookUpTable(Locale locale)
	{
		// build the key
		String tableKey = DIRECTIONS + "_" + locale;

		try
		{
			Map<String, String[]> prefixMap = load2DTable(tableKey, locale,
					false);

			directionTables.put(tableKey + "_" + LOOK_UP, prefixMap);
		}
		catch (Exception e)
		{
			System.err.println("Unable to load/find resource: " + tableKey);
			directionTables.put(tableKey + "_" + LOOK_UP, null);
		}
	}

	public static Map<String, String[]> getDirectionsTable(Locale locale)
	{
		// build the key
		String tableKey = DIRECTIONS + "_" + locale;
		if (!directionTables.containsKey(tableKey))
			loadDirectionsTable(locale);

		return directionTables.get(tableKey);

	}

	public static void loadDirectionsTable(Locale locale)
	{
		// build the key
		String tableKey = DIRECTIONS + "_" + locale;

		try
		{
			Map<String, String[]> prefixMap = load2DTable(tableKey, locale,
					true);

			directionTables.put(tableKey, prefixMap);
		}
		catch (Exception e)
		{
			System.err.println("Unable to load/find resource: " + tableKey);
			directionTables.put(tableKey, null);
		}
	}

	public static Map<String, String[]> getStreetTypesLookUpTable(Locale locale)
	{
		// build the key
		String tableKey = STREET_TYPES + "_" + locale;
		if (!streettypeTables.containsKey(tableKey + "_" + LOOK_UP))
			loadStreetTypesLookUpTable(locale);

		return streettypeTables.get(tableKey + "_" + LOOK_UP);

	}

	public static void loadStreetTypesLookUpTable(Locale locale)
	{
		// build the key
		String tableKey = STREET_TYPES + "_" + locale;

		try
		{
			Map<String, String[]> prefixMap = load2DTable(tableKey, locale,
					false);

			streettypeTables.put(tableKey + "_" + LOOK_UP, prefixMap);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Unable to load/find resource: " + tableKey);
			streettypeTables.put(tableKey + "_" + LOOK_UP, null);
		}
	}

	public static Map<String, String[]> getStreetTypesTable(Locale locale)
	{
		// build the key
		String tableKey = STREET_TYPES + "_" + locale;
		if (!streettypeTables.containsKey(tableKey))
			loadStreetTypesTable(locale);

		return streettypeTables.get(tableKey);

	}

	public static void loadStreetTypesTable(Locale locale)
	{
		// build the key
		String tableKey = STREET_TYPES + "_" + locale;

		try
		{
			Map<String, String[]> prefixMap = load2DTable(tableKey, locale,
					true);

			streettypeTables.put(tableKey, prefixMap);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Unable to load/find resource: " + tableKey);
			streettypeTables.put(tableKey, null);
		}
	}

	public static Map<String,String> getHighwayTokenTable(Locale locale)
	{
		// build the key
		String tableKey = HIGHWAY_TOKENS + "_" + locale;
		if (!highwayTokenTables.containsKey(tableKey))
			loadHighwayTokenTable(locale);

		return highwayTokenTables.get(tableKey);

	}

	public static void loadHighwayTokenTable(Locale locale)
	{
		// build the key
		String tableKey = HIGHWAY_TOKENS + "_" + locale;

		try
		{
			Map<String,String> tokenMap = loadTable(tableKey, locale, false);

			highwayTokenTables.put(tableKey, tokenMap);
		}
		catch (Exception e)
		{
			System.out.println("Unable to load/find resource: " + tableKey);
			highwayTokenTables.put(tableKey, null);
		}
	}

	public static Map<String,String> getStandardizeTable(Locale locale)
	{
		// build the key
		String tableKey = STANDARDIZE + "_" + locale;
		if (!standardizeTables.containsKey(tableKey))
			loadStandardizeTable(locale);

		return standardizeTables.get(tableKey);

	}

	public static void loadStandardizeTable(Locale locale)
	{
		// build the key
		String tableKey = STANDARDIZE + "_" + locale;

		try
		{
			Map<String,String> prefixMap = loadTable(tableKey, locale, false);

			standardizeTables.put(tableKey, prefixMap);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Unable to load/find resource: " + tableKey);
			standardizeTables.put(tableKey, null);
		}
	}

	public static Map<String,String[]> getElaborationTable(Locale locale)
	{
		// build the key
		String tableKey = ELABORATION + "_" + locale;
		if (!elaborationTables.containsKey(tableKey))
			loadElaborationTable(locale);

		return elaborationTables.get(tableKey);

	}

	public static void loadElaborationTable(Locale locale)
	{
		// build the key
		String tableKey = ELABORATION + "_" + locale;

		try
		{
			Map<String, String[]> prefixMap = load2DTable(tableKey, locale,
					false);

			elaborationTables.put(tableKey, prefixMap);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Unable to load/find resource: " + tableKey);
			elaborationTables.put(tableKey, null);
		}
	}

}
