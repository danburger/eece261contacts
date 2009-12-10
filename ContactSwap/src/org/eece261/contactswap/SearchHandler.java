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

public class SearchHandler {
	
	public class Search {
    	String name = "";
    	ArrayList<String> responses = new ArrayList<String>();
    }
	
	private ArrayList<Search> alSearches;
	private ArrayList<Search> alUnsolicitedContacts;
	private File dir;
	
	public SearchHandler() {
		dir = new File(Environment.getExternalStorageDirectory().getPath(), "ContactSwap");
		dir.mkdirs();
		
		loadSearchesList();
		loadUnsolicitedContactsList();
	}
	
	private void loadSearchesList() {
    	FileInputStream fin;
    	DataInputStream din;
    	
    	alSearches = new ArrayList<Search>();
    	
    	try {
    		File f = new File(dir, "searchesList");
    		f.createNewFile();
			fin = new FileInputStream(f);
			din = new DataInputStream(fin);
			
			while(din.available() > 0) {
				Search temp = new Search();
				temp.name = din.readUTF();
				String current = din.readUTF();
				while(!current.equalsIgnoreCase("")) {
					temp.responses.add(current);
					current = din.readUTF();
				}
				alSearches.add(temp);
			}
			
			din.close();
			fin.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
	
	private void loadUnsolicitedContactsList() {
    	FileInputStream fin;
    	DataInputStream din;
    	
    	alUnsolicitedContacts = new ArrayList<Search>();
    	
    	try {
    		File f = new File(dir, "unsolicitedContactList");
    		f.createNewFile();
			fin = new FileInputStream(f);
			din = new DataInputStream(fin);
			
			while(din.available() > 0) {
				Search temp = new Search();
				temp.name = din.readUTF();
				String current = din.readUTF();
				while(!current.equalsIgnoreCase("")) {
					temp.responses.add(current);
					current = din.readUTF();
				}
				alUnsolicitedContacts.add(temp);
			}
			
			din.close();
			fin.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
    public boolean addSearchResult(String name, String phone) {
    	boolean NotFound = true;
    	for(int i = 0; i < alSearches.size(); i++) {
    		if(alSearches.get(i).name.equalsIgnoreCase(name)) {
    			alSearches.get(i).responses.add(phone);
    			NotFound = false;
    		}
    	}
    	if(NotFound) {
    		return false;
    	}
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
    		File f = new File(dir, "searchesList");
            f.createNewFile();
    		fout = new FileOutputStream(f);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alSearches.size(); i++) {
				dout.writeUTF(alSearches.get(i).name);
				for(int j = 0; j < alSearches.get(i).responses.size(); j++) {
					dout.writeUTF(alSearches.get(i).responses.get(j));
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
		return true;
    }
    
    public void addUnsolicitedContact(String name, String phone) {
    	boolean NotFound = true;
    	for(int i = 0; i < alUnsolicitedContacts.size(); i++) {
    		if(alUnsolicitedContacts.get(i).name.equalsIgnoreCase(name)) {
    			alUnsolicitedContacts.get(i).responses.add(phone);
    			NotFound = false;
    		}
    	}
    	if(NotFound) {
    		Search temp = new Search();
    		temp.name = name;
    		temp.responses.add(phone);
    		alUnsolicitedContacts.add(temp);
    	}
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
    		File f = new File(dir, "unsolicitedContactList");
            f.createNewFile();
    		fout = new FileOutputStream(f);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alUnsolicitedContacts.size(); i++) {
				dout.writeUTF(alUnsolicitedContacts.get(i).name);
				for(int j = 0; j < alUnsolicitedContacts.get(i).responses.size(); j++) {
					dout.writeUTF(alUnsolicitedContacts.get(i).responses.get(j));
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
    
    public ArrayList<String> getSearchNames() {
    	loadSearchesList();
    	ArrayList<String> alResults = new ArrayList<String>();
    	for(int i = 0; i < alSearches.size(); i++) {
    		alResults.add(alSearches.get(i).name);
    	}
    	return alResults;
    }
    
    public ArrayList<String> getSearchResults(String name) {
    	loadSearchesList();
    	ArrayList<String> alResults = new ArrayList<String>();
    	for(int i = 0; i < alSearches.size(); i++) {
    		if(alSearches.get(i).name.equalsIgnoreCase(name)){
    			return alSearches.get(i).responses;
    		}
    	}
    	return alResults;
    }
    
    public ArrayList<String> getUnsolicitedContactNames() {
    	loadSearchesList();
    	ArrayList<String> alResults = new ArrayList<String>();
    	for(int i = 0; i < alUnsolicitedContacts.size(); i++) {
    		alResults.add(alUnsolicitedContacts.get(i).name);
    	}
    	return alResults;
    }
    
    public ArrayList<String> getUnsolicitedContactNumbers(String name) {
    	loadSearchesList();
    	ArrayList<String> alResults = new ArrayList<String>();
    	for(int i = 0; i < alUnsolicitedContacts.size(); i++) {
    		if(alSearches.get(i).name.equalsIgnoreCase(name)){
    			return alUnsolicitedContacts.get(i).responses;
    		}
    	}
    	return alResults;
    }
    
    public void addSearch(String name) {
    	Search temp = new Search();
    	temp.name = name;
    	alSearches.add(temp);
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
    		File f = new File(dir, "searchesList");
            f.createNewFile();
    		fout = new FileOutputStream(f);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alSearches.size(); i++) {
				dout.writeUTF(alSearches.get(i).name);
				for(int j = 0; j < alSearches.get(i).responses.size(); j++) {
					dout.writeUTF(alSearches.get(i).responses.get(j));
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
    
    public void removeSearch(String Name) {
    	for(int i = 0; i < alSearches.size(); i++) {
			if(alSearches.get(i).name.equalsIgnoreCase(Name)) {
				alSearches.remove(i);
			}
    	}
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
    		File f = new File(dir, "searchesList");
            f.createNewFile();
    		fout = new FileOutputStream(f);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alSearches.size(); i++) {
				dout.writeUTF(alSearches.get(i).name);
				for(int j = 0; j < alSearches.get(i).responses.size(); j++) {
					dout.writeUTF(alSearches.get(i).responses.get(j));
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
    
    public void removeUnsolicitedContact(String Name) {
    	for(int i = 0; i < alUnsolicitedContacts.size(); i++) {
			if(alUnsolicitedContacts.get(i).name.equalsIgnoreCase(Name)) {
				alUnsolicitedContacts.remove(i);
			}
    	}
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
    		File f = new File(dir, "unsolicitedContactList");
            f.createNewFile();
    		fout = new FileOutputStream(f);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alUnsolicitedContacts.size(); i++) {
				dout.writeUTF(alUnsolicitedContacts.get(i).name);
				for(int j = 0; j < alUnsolicitedContacts.get(i).responses.size(); j++) {
					dout.writeUTF(alUnsolicitedContacts.get(i).responses.get(j));
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
}
