import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient{
	private String IP = "";
	private int port = 0;
	private String inFromUser;
	private Socket clientSocket;
	private DataOutputStream outToServer;
	public Scanner inFromServer;
	public String tempInputFromTCP;
	//public boolean busy = false;
	public boolean newInputFromTCP = false;
	
	TCPClient(String IP, int port){
		this.IP = IP;
		this.port = port;
	}
	
	public void waitForTCPToReceive(){
		newInputFromTCP = true;
		while(!inFromServer.hasNextLine()){}
		tempInputFromTCP =  inFromServer.nextLine();
		newInputFromTCP = false;
	}
	
	public void setup() throws Exception{
		clientSocket = new Socket(IP, port);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromServer = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
	}
	public void write(String inFromUser){
		try {
			outToServer.writeBytes(inFromUser + '\n');
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeALine() throws IOException{
		outToServer.writeBytes(inFromUser + '\n');
		//modifiedSentence = inFromServer.readLine();
		//System.out.println("FROM SERVER: " + modifiedSentence);
		clientSocket.close();
	}
	


}
