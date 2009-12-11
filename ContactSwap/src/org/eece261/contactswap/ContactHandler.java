package org.eece261.contactswap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Environment;

public class ContactHandler {
	
	public class Contact {
    	String name = "";
    	boolean isSearch = false;
    	ArrayList<String> numbers = new ArrayList<String>();
    }
	
	private ArrayList<Contact> alContacts;
	private File dir;
	
	public ContactHandler() {
		dir = new File(Environment.getExternalStorageDirectory().getPath(), "ContactSwap");
		dir.mkdirs();
		
		loadContactsList();
	}
	
	private void writeContactsList() {
		FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
    		File f = new File(dir, "contactsList");
            f.createNewFile();
    		fout = new FileOutputStream(f);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alContacts.size(); i++) {
				dout.writeUTF(alContacts.get(i).name);
				dout.writeBoolean(alContacts.get(i).isSearch);
				for(int j = 0; j < alContacts.get(i).numbers.size(); j++) {
					dout.writeUTF(alContacts.get(i).numbers.get(j));
				}
				dout.writeUTF("");
			}
			
			dout.close();
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadContactsList() {
    	FileInputStream fin;
    	DataInputStream din;
    	
    	alContacts = new ArrayList<Contact>();
    	
    	try {
    		File f = new File(dir, "contactsList");
    		f.createNewFile();
			fin = new FileInputStream(f);
			din = new DataInputStream(fin);
			
			while(din.available() > 0) {
				Contact temp = new Contact();
				temp.name = din.readUTF();
				temp.isSearch = din.readBoolean();
				String current = din.readUTF();
				while(!current.equalsIgnoreCase("")) {
					temp.numbers.add(current);
					current = din.readUTF();
				}
				alContacts.add(temp);
			}
			
			din.close();
			fin.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }

    public void addReceived(String name, String phone) {
    	boolean NotFound = true;
    	for(int i = 0; i < alContacts.size(); i++) {
    		if(alContacts.get(i).name.equalsIgnoreCase(name)) {
    			alContacts.get(i).numbers.add(phone);
    			NotFound = false;
    		}
    	}
    	if(NotFound) {
    		Contact temp = new Contact();
    		temp.name = name;
    		temp.numbers.add(phone);
    		alContacts.add(temp);
    	}
    	
    	writeContactsList();
    }
	 
    public ArrayList<String> getSearchNames() {
    	loadContactsList();
    	ArrayList<String> alResults = new ArrayList<String>();
    	for(int i = 0; i < alContacts.size(); i++) {
    		if (alContacts.get(i).isSearch) {
    		alResults.add(alContacts.get(i).name);
    		}
    	}
    	return alResults;
    }
    
    public ArrayList<String> getReceivedContacts() {
    	loadContactsList();
    	ArrayList<String> alResults = new ArrayList<String>();
    	for(int i = 0; i < alContacts.size(); i++) {
    		if(!alContacts.get(i).numbers.isEmpty()) {
    			alResults.add(alContacts.get(i).name);
    		}
    	}
    	return alResults;
    }
    
    public ArrayList<String> getAllNames() {
    	loadContactsList();
    	ArrayList<String> alResults = new ArrayList<String>();
    	for(int i = 0; i < alContacts.size(); i++) {
    		alResults.add(alContacts.get(i).name);
    	}
    	return alResults;
    }

    public ArrayList<String> getReceivedResults(String name) {
    	loadContactsList();
    	ArrayList<String> alResults = new ArrayList<String>();
    	for(int i = 0; i < alContacts.size(); i++) {
    		if(alContacts.get(i).name != null && alContacts.get(i).name.equalsIgnoreCase(name)){
    			return alContacts.get(i).numbers;
    		}
    	}
    	return alResults;
    }
    
    public void addSearch(String name) {
    	Contact temp = new Contact();
    	temp.name = name;
    	temp.isSearch = true;
    	alContacts.add(temp);
    	
    	writeContactsList();
    }
    
    public void removeContact(String Name) {
    	for(int i = 0; i < alContacts.size(); i++) {
			if(alContacts.get(i).name.equalsIgnoreCase(Name)) {
				alContacts.remove(i);
			}
    	}
    	
    	writeContactsList();
    }
    
}
