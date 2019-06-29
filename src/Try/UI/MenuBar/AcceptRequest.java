package Try.UI.MenuBar;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Try.Network.Server;
import Try.logic.User;

public class AcceptRequest {

	public AcceptRequest(User user, String acceptedUser) {
		super();
		new Server().start();
		try(Socket socket =  new Socket("127.0.0.1", 444);
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
			// also this user (who has pushed the accept button) must be added to the second guy
			dos.writeUTF("Accepted request of");
			dos.flush();
			dos.writeUTF(acceptedUser);
			dos.flush();
			dos.writeUTF(user.getEmail());
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
