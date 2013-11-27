package com.telenav.module.about;

/**
 * 
 * @author chliu
 *
 */
public class DiagnosticItem 
{
	private String name;
	private String value;
	private String detailLines;
	private int id;
	
	public DiagnosticItem(String name, String value, int id)
	{
		this.name = name;
		this.value = value;
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public int getId()
	{
		return id;
	}
	
	public boolean equals(Object obj)
	{
		if (!(obj instanceof DiagnosticItem))
		{
			return false;
		}
		
		DiagnosticItem dItem = (DiagnosticItem) obj;
		return dItem.id == this.id;
	}
	
	public int hashCode()
	{
		return this.id;
	}
	
	public void setDetailLines(String lines)
	{
		this.detailLines = lines;
	}
	
	public String getDetailLines()
	{
		return this.detailLines;
	}
	
	public void updateValue(String value)
	{
		if (value != null)
		{
			this.value = value;
		}
	}
}