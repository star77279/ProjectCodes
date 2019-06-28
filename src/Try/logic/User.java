package Try.logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;

import Try.Network.Server;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String email;
	private String password;
	private Library library;
	private HashSet<Playlist> playlists = new HashSet<Playlist>();
	private SharedPlayList share = new SharedPlayList("Shared Playlist", this);
	private HashSet<User> friends = new HashSet<User>();
	private HashSet<User> requests = new HashSet<User>();
	
	
	public User(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		library = new Library(this);
		updateInfo();
	}


	public SharedPlayList getShare() {
		return share;
	}



	public Library getLibrary() {
		return library;
	}


	public String getName() {
		return name;
	}


	public String getEmail() {
		return email;
	}


	public String getPassword() {
		return password;
	}
	
	
	public Playlist[] getPlaylists() {
		Playlist[] allPlaylists = new Playlist[playlists.size()];
		int i = 0;
		Iterator<Playlist> it = playlists.iterator(); 
        while (it.hasNext()) {
        	allPlaylists[i] = it.next();
        	i++;
        }
        return allPlaylists;
	}
	
	
	public void addPlaylist(Playlist playlist) {
		playlists.add(playlist);
		updateInfo();
	}
	
	
	public void removePlaylist(Playlist playlist) {
		playlists.remove(playlist);
		updateInfo();
	}
	
	
	public void requestTo(User u) {
		new Server().start();
		try(Socket socket =  new Socket("127.0.0.1", 444);
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
			dos.writeUTF("requesting");
			dos.flush();
			dos.writeUTF(u.email);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void  acceptRequestOf(User u) {
		requests.remove(u);
		friends.add(u);
		updateInfo();
	}
	
	
	public User[] requests() {
		User[] allRequests = new User[requests.size()];
		int i = 0;
		Iterator<User> it = requests.iterator(); 
        while (it.hasNext()) {
        	allRequests[i] = it.next();
        	i++;
        }
        return allRequests;
	}
	
	public void updateInfo() {
		File file = new File("D:\\Jpotify\\SampelCode\\Client\\" + email + ".txt");
		file.delete();
		try {
			file.createNewFile();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		try(FileOutputStream fout = new FileOutputStream("D:\\Jpotify\\SampelCode\\Client\\" + email + ".txt");
				ObjectOutputStream out = new ObjectOutputStream(fout);) {
			out.writeObject(this);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		  
		  
	}


	@Override
	public String toString() {
		String lib = "";
		for(int i=0; i<library.getLibrarySongs().length ; i++)
			lib += library.getLibrarySongs()[i].getTitle();
		return "User [name=" + name + ", email=" + email + ", password=" + password + ", library=" + lib
				+ ", playlists=" + playlists + ", share=" + share + ", friends=" + friends + ", requests=" + requests
				+ "]";
	}
	
	
}
