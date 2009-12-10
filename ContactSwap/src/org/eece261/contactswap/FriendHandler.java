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

public class FriendHandler {
	private ArrayList<String> alFriends;
	private ArrayList<String> alRequests;
	
	private File dir;
	
	FriendHandler(){
		dir = new File(Environment.getExternalStorageDirectory().getPath(), "ContactSwap");
		dir.mkdirs();
		
		loadFriendsList();
		loadRequestsList();
	}
	
    private void loadFriendsList() {
    	FileInputStream fin;
    	DataInputStream din;
    	
    	alFriends = new ArrayList<String>();
    	 
    	try {
    		File f = new File(dir, "friendsList");
			fin = new FileInputStream(f);
			din = new DataInputStream(fin);
			
			while(din.available() > 0) {
				alFriends.add(din.readUTF());
			}
			
			din.close();
			fin.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
    private void loadRequestsList() {
    	FileInputStream fin;
    	DataInputStream din;
    	
    	alRequests = new ArrayList<String>();
    	 
    	try {
    		File f = new File(dir, "friendrequestsList");
			fin = new FileInputStream(f);
			din = new DataInputStream(fin);
			
			while(din.available() > 0) {
				alRequests.add(din.readUTF());
			}
			
			fin.close();
		} catch (FileNotFoundException e) {
			// Assume it hasn't been created yet
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public ArrayList<String> getFriends(){
    	return alFriends;
    }
    
    public ArrayList<String> getRequests(){
    	return alRequests;
    }
    
    public void addFriend(String friend) {
    	alFriends.add(friend);
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
            File f = new File(dir, "friendsList");
            f.createNewFile();
    		fout = new FileOutputStream(f);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alFriends.size(); i++) {
				dout.writeUTF(alFriends.get(i));
			}
			
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void removeFriend(int index) {
    	
    	alFriends.remove(index);
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
            File f = new File(dir, "friendsList");
            f.createNewFile();
    		fout = new FileOutputStream(f);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alFriends.size(); i++) {
				dout.writeUTF(alFriends.get(i));
			}
			
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void addRequest(String friend) {
    	alRequests.add(friend);
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
            File f = new File(dir, "friendrequestsList");
            f.createNewFile();
    		fout = new FileOutputStream(f);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alRequests.size(); i++) {
				dout.writeUTF(alRequests.get(i));
			}
			
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void removeRequest(int index) {
    	
    	alRequests.remove(index);
    	
    	FileOutputStream fout;
    	DataOutputStream dout;
    	
    	try {
            File f = new File(dir, "friendrequestsList");
            f.createNewFile();
    		fout = new FileOutputStream(f);
			dout = new DataOutputStream(fout);
			
			for(int i = 0; i < alRequests.size(); i++) {
				dout.writeUTF(alRequests.get(i));
			}
			
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
