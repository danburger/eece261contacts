package eece261.addressbook;

import java.util.ArrayList;

public class AddressBook {
	
	private ArrayList<Entry> entries;
	
	public AddressBook()
	{
		entries = new ArrayList<Entry>();
	}
	
	public void addEntry(String name, long number)
	{
		entries.add(new Entry(name, number));
	}
	
	public String findEntryQuery(String name)
	{
		for(int i = 0; i<entries.size(); i++)
		{
			if(entries.get(i).getName().equalsIgnoreCase(name))
			{
				return entries.toString();
			}
		}
		
		return "ContactSwap:Response:Name:" + name +
			":Not Found:";
	}
	
	public String findEntryNumber(String name)
	{
		for(int i = 0; i<entries.size(); i++)
		{
			if(entries.get(i).getName().equalsIgnoreCase(name))
			{
				return entries.get(i).getNumberAsString();
			}
		}
		
		return "Not found";
	}
	
	public boolean isEntryFound(String name)
	{
		for(int i = 0; i<entries.size(); i++)
		{
			if(entries.get(i).getName().equalsIgnoreCase(name))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void deleteEntry(String name)
	{
		for(int i = 0; i<entries.size(); i++)
		{
			if(entries.get(i).getName().equalsIgnoreCase(name))
			{
				entries.remove(i);
				return;
			}
		}
	}
}
