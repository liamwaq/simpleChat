// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param loginID the ID of the user logging in
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.loginID = loginID;
    this.clientUI = clientUI;
    sendToServer("#login " + loginID);
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI
   * If the message is a command, the command is executed,
   * Otherwise the message is sent to the server
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
	  if (message.startsWith("#")) {
			
			String command = message.substring(1);
			
			if(command.equals("quit")) {
				quit();
				
			}else if(command.equals("logoff")) {
				try { 
					closeConnection();
				} catch(IOException e) {}
				
			}else if(command.matches("^sethost\\s+([\\w.-]+)$")) {
				if(!isConnected()) {
					setHost(command.substring(8));
					
				} else { 
					clientUI.display("Cannot change host while connected to a server");
				}
				
			}else if(command.matches("^setport\\s+(\\d+)$")) {
				if(!isConnected()) {
					setPort(Integer.parseInt(command.substring(9)));
					
				} else {
					clientUI.display("Cannot change port while connected to a server");
				}
				
			} else if(command.equals("login")) {
				if(!isConnected()) {
					try {
						openConnection();
					} catch (IOException e) {}
				}else {
					clientUI.display("Already logged in");
				}
				
			}else if(command.equals("gethost")) {
				clientUI.display(getHost());
				
			}else if(command.equals("getport")) {
				clientUI.display(String.valueOf(getPort()));
				
			}else {
				clientUI.display("Invalid command");
			}
			
		} else {
	try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  }
 
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  
  /**
   * 
   * Method overrides the connectionClosed method. Displays a message that
   * the client is quitting
   */
  
  @Override
  protected void connectionClosed() {
	  clientUI.display("Quitting");
  }
  
  /*
   * 
   * Method overrides the connectionException method
   * 
   */
  @Override
  protected void connectionException(Exception exception) {
	  clientUI.display("Server has shut down");
  }
}
//End of ChatClient class
