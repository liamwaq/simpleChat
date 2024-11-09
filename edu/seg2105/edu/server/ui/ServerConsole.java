package edu.seg2105.edu.server.ui;

import java.util.Scanner;
import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;

public class ServerConsole implements ChatIF {
	
	  //Class variables *************************************************
	  
	  /**
	   * The default port to connect on.
	   */
	final public static int DEFAULT_PORT = 5555;
	
	  //Instance variables **********************************************
	  
	// The server this console is associated to
	EchoServer server;
	
	
	// Scanner to read from the console
	Scanner fromConsole;
	
	
	
	/**
	 * Constructs an instance of the ServerConsole UI
	 * 
	 * @param port the port to connect on
	 */
	public ServerConsole(int port) {
		
		server = new EchoServer(port);
			
		fromConsole = new Scanner(System.in);
	}
	
	// Instance methods ***********************************************
	
	public void accept() {
		
		try {
			
			String message;
			
			while (true) {
				message = fromConsole.nextLine();
				
				if (message.startsWith("#")) {
					
					String command = message.substring(1);
					
					if(command.equals("stop")) {
						server.stopListening();
						
					} else if(command.equals("close")) {
						server.close();
						
					} else if(command.matches("^setport\\s+(\\d+)$")) {
						if(!server.isListening()) {
							server.setPort(Integer.parseInt(command.substring(9)));
						}
						else {
							display("Cannot change port while server is active");
						}
						
					} else if(command.equals("start")) {
						if(!server.isListening()) {
							server.listen();
						} else {
							display("Server is already listening");
						}
						
					} else if(command.equals("getPort")) {
						display(String.valueOf(server.getPort()));
					}
		} else {
			server.sendToAllClients("SERVER MSG>" + message);
		}
			}
		}
		catch(Exception ex) {
			System.out.println("Unexpected error while reading "
					+ "from server console");
		}
	}
	
	
	
	/**
	 * Overrides the method in the ChatIF interface to display a message
	 * 
	 * @param message the String to be displayed
	 */
	@Override
	public void display(String message) {
		
		System.out.println("> " + message);
	}
	
	
	
	  /**
	   * This method is responsible for the creation of the Server UI.
	   *
	   * @param args[0] The port to connect to.
	   */
	public static void main(String[] args) {
		
		int port = DEFAULT_PORT;
		try { port = Integer.parseInt(args[0]);
		
		} catch(ArrayIndexOutOfBoundsException e) {
			port = DEFAULT_PORT;
		}
		
		ServerConsole serverChat = new ServerConsole(port);
		serverChat.accept();
	}
	
	
}
