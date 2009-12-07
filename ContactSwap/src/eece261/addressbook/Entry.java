package eece261.addressbook;

public class Entry {

	private String name;
	private long number;
	
	public Entry()
	{
		name = "Balloon Boy";
		number = 1234567890L;
	}
	
	public Entry(String newName, long newNumber)
	{
		name = newName;
		number = newNumber;
	}
	
	public void setName(String newName)
	{
		name = newName;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setNumber(long newNumber)
	{
		number = newNumber;
	}
	
	public void setNumber(String newNumber)
	{
		number = valueOf(newNumber);
	}
	
	public long getNumber()
	{
		return number;
	}
	
	public String getNumberAsString()
	{
		return toString(number);
	}
	
	public long valueOf(String aNumber)
	{
		String tmpStr = "";
		char tmpChar;
		
		for(int i = 0; i<aNumber.length(); i++)
		{
			tmpChar = aNumber.charAt(i);
			if(tmpChar >= '0' && tmpChar <= '9')
			{
				tmpStr += tmpChar;
			}
		}
		
		return Long.valueOf(tmpStr);
	}
	
	public String toString(long aNumber)
	{
		String tmpStr = Long.toString(aNumber);
		
		// Special case 1: 123-4567
		if(tmpStr.length()==7)
		{
			tmpStr = tmpStr.substring(0,3) + "-" 
				+ tmpStr.substring(3,4);
		}
		// Special case 2: (123) 456-7890
		else if(tmpStr.length()==10)
		{
			tmpStr = "(" + tmpStr.substring(0,3) + ") " 
				+ tmpStr.substring(3,3) + "-"
				+ tmpStr.substring(6,4);
		}
		
		return tmpStr;
	}
	
	public String toString()
	{
		return "ContactSwap:Response:Name:" +
			name + ":Phone:" + number + ":";
	}
}
