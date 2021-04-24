package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerApplication {			
	   
	static String driver, connectionURL, dbName, username, password;
	
	Connection connection;//Creating object of Connection class
	Statement statement;
	
   public ServerApplication()
   {      
		driver = "com.mysql.cj.jdbc.Driver";
		connectionURL ="jdbc:mysql://localhost:3306/";
		dbName = "assignment2";
		username = "root";
		password = "";
   }
	
	public static void main(String[] args) {		
	
		// Server UDP socket runs at this port
		final int serverPort = 50002;
		
		while(true) {
			try {			
				// Instantiate a new DatagramSocket to receive responses from the client
			    DatagramSocket serverSocket = new DatagramSocket(serverPort);
			    
			    // Create buffers to hold receiving data.
			    byte receivingDataBuffer[] = new byte[1024];
			    byte receivingDataBuffer2[] = new byte[1024];
			    byte receivingDataBuffer3[] = new byte[1024];
			    byte receivingDataBuffer4[] = new byte[1024];
			    byte receivingDataBuffer5[] = new byte[1024];
			    
			    // Instantiate a UDP packet to store the client data using the buffer for receiving data
			    DatagramPacket iP = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
			    DatagramPacket iP2 = new DatagramPacket(receivingDataBuffer2, receivingDataBuffer2.length);
			    DatagramPacket iP3 = new DatagramPacket(receivingDataBuffer3, receivingDataBuffer3.length);
			    DatagramPacket iP4 = new DatagramPacket(receivingDataBuffer4, receivingDataBuffer4.length);
			    DatagramPacket iP5 = new DatagramPacket(receivingDataBuffer5, receivingDataBuffer5.length);
		
			    
			    System.out.println("In ready mode. Client should be here and insert card number");
			    		    
			    // Receive data from the client and store in inputPacket
			    serverSocket.receive(iP);
			    serverSocket.receive(iP2);
			    serverSocket.receive(iP3);
			    serverSocket.receive(iP4);
			    serverSocket.receive(iP5);
			    
			    // Printing out the client sent data
			    String receivedData2 = new String(iP.getData(), 0, iP.getLength());
			    String receivedData3 = new String(iP2.getData(), 0, iP2.getLength());
			    String receivedData4 = new String(iP3.getData(), 0, iP3.getLength());
			    String receivedData5 = new String(iP4.getData(), 0, iP4.getLength());
			    String receivedData6 = new String(iP5.getData(), 0, iP5.getLength());
			    
			    String cc = new String("2222");
			    
			    System.out.println("Sent from the client 2: " + receivedData2);
			    System.out.println("Sent from the client 3: " + receivedData3);
			    System.out.println("Sent from the client 4: " + receivedData4);
			    System.out.println("Sent from the client 5: " + receivedData5);
			    System.out.println("Sent from the client 6: " + receivedData6);
			    
			    if(receivedData2.equals(cc))
			    {
			    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
			    	LocalDateTime now = LocalDateTime.now();
			    	driver = "com.mysql.cj.jdbc.Driver";
					connectionURL ="jdbc:mysql://localhost:3306/";
					dbName = "assignment2";
					username = "root";
					password = "";
			    	Class.forName(driver);
			    	Connection connection = DriverManager.getConnection(connectionURL+dbName+"?serverTimezone=UTC",username,password);
					PreparedStatement preparedStmt;
				    
				    String getInsertData = "INSERT INTO ordertransaction (TransactionDate, Orders, AmountCharged, TransactionStatus, Last4Digits, OrderMode) VALUES (?,?,?,?,?,?)";
				    preparedStmt = connection.prepareStatement(getInsertData);  
				    preparedStmt.setString(1, dtf.format(now));
				    preparedStmt.setInt(2, Integer.valueOf(receivedData4));
				    preparedStmt.setFloat(3, Float.valueOf(receivedData5));
				    preparedStmt.setInt(4, 1);
				    preparedStmt.setInt(5, Integer.valueOf(receivedData2));
				    preparedStmt.setString(6, receivedData6);
				    preparedStmt.executeUpdate();
				    
				    String queryUpdate = "UPDATE orders SET status=? WHERE OrderId=?";
		    		PreparedStatement preparedStmt5 = connection.prepareStatement(queryUpdate);
		    		preparedStmt5.setString(1, "Done");
		    		preparedStmt5.setInt(2, Integer.valueOf(receivedData4));
		    		preparedStmt5.executeUpdate();
			    	
			    	// Process data - convert to upper case
				    String sendingData = "verified";
				    
				    // Creating corresponding buffer to send data
				    byte sendingDataBuffer[] = sendingData.getBytes();
				    
				    // Get client's address
				    InetAddress senderAddress = iP.getAddress();
				    int senderPort = iP.getPort();
				    
				    // Create new UDP packet with data to send to the client
				    DatagramPacket outputPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length, senderAddress,senderPort);
				    
				    // Send the created packet to client
				    serverSocket.send(outputPacket);
			    }
			    else {
			    	// Process data - convert to upper case
			    	String sendingData = "rejected";
				    
				    // Creating corresponding buffer to send data
				    byte sendingDataBuffer[] = sendingData.getBytes();
				    
				    // Get client's address
				    InetAddress senderAddress = iP.getAddress();
				    int senderPort = iP.getPort();
				    
				    // Create new UDP packet with data to send to the client
				    DatagramPacket outputPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length, senderAddress,senderPort);
				    
				    // Send the created packet to client
				    serverSocket.send(outputPacket);
			    }
			    
			    // Close the socket connection
			    serverSocket.close();
			      
			} catch (Exception ex) {			
				System.out.println("NOOOO... we got problem");
				ex.printStackTrace();
			}
		}
		
	}
}