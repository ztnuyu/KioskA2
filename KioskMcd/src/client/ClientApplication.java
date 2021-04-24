package client;

import kioskapp.ordertransaction.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class ClientApplication {
	
	static String driver;
	static String dbName;
	static String connectionURL;
	static String username;
	static String password;
	
	Connection connection;
    Statement statement;
	   
	public void SendCreditCardNumber(int cardNumber, int orderID, float totalAmount, String orderMode) {
		
		driver = "com.mysql.cj.jdbc.Driver";
		connectionURL ="jdbc:mysql://localhost:3306/";
		dbName = "assignment2";
		username = "root";
		password = "";
		
		OrderTransaction ot = new OrderTransaction();
		
		// The server port to which the client socket is going to connect
		final int SERVERPORT2 = 50002;

		int bufferSize = 1024;

		try {
			// Instantiate client socket
			DatagramSocket clientSocket = new DatagramSocket();

			// Get the IP address of the server
			InetAddress serverAddress = InetAddress.getByName("localhost");
			
			// Create buffer to send data2
			byte sendingDataBuffer2[] = new byte[bufferSize];
			byte sendingDataBuffer3[] = new byte[bufferSize];
			byte sendingDataBuffer4[] = new byte[bufferSize];
			byte sendingDataBuffer5[] = new byte[bufferSize];
			byte sendingDataBuffer6[] = new byte[bufferSize];
			
			// Convert data to bytes and store data in the buffer2			
			// Last4Digits
			String cn = String.valueOf(cardNumber);
			// Transaction Date
			String td = "now()";
			// Order id
			String oid = String.valueOf(orderID);
			// AmountCharged
			String ac = String.valueOf(totalAmount);
			// OrderMode
			String om = orderMode;
			
			// Last4Digits
			sendingDataBuffer2 = cn.getBytes();
			// Transaction Date
			sendingDataBuffer3 = td.getBytes();
			// Order id
			sendingDataBuffer4 = oid.getBytes();
			// AmountCharged
			sendingDataBuffer5 = ac.getBytes();
			// OrderMode
			sendingDataBuffer6 = om.getBytes();
			
			// Creating a UDP packet 2
			// Last4Digits
			DatagramPacket sendingPacket2 = new DatagramPacket(sendingDataBuffer2,
					sendingDataBuffer2.length, serverAddress, SERVERPORT2);
			// Transaction Date
			DatagramPacket sendingPacket3 = new DatagramPacket(sendingDataBuffer3,
					sendingDataBuffer3.length, serverAddress, SERVERPORT2);
			// Order id
			DatagramPacket sendingPacket4 = new DatagramPacket(sendingDataBuffer4,
					sendingDataBuffer4.length, serverAddress, SERVERPORT2);
			// AmountCharged
			DatagramPacket sendingPacket5 = new DatagramPacket(sendingDataBuffer5,
					sendingDataBuffer5.length, serverAddress, SERVERPORT2);
			// OrderMode
			DatagramPacket sendingPacket6 = new DatagramPacket(sendingDataBuffer6,
					sendingDataBuffer6.length, serverAddress, SERVERPORT2);
			
			// Sending UDP packet to the server2
			// Last4Digits
			clientSocket.send(sendingPacket2);
			// Transaction Date
			clientSocket.send(sendingPacket3);	
			// Order id
			clientSocket.send(sendingPacket4);	
			// AmountCharged
			clientSocket.send(sendingPacket5);	
			// OrderMode
			clientSocket.send(sendingPacket6);	
			
			// Create buffer to receive data
			byte receivingDataBuffer [] = new byte[bufferSize];
			
			// Receive data packet from server
		    DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer,
		    		receivingDataBuffer.length);
		    clientSocket.receive(receivingPacket);
		    
		    // Unpack packet
		    String allCapsData = new String(receivingPacket.getData(), 0, receivingPacket.getLength());
		    System.out.println("Received from server: " + allCapsData);
		    String verified = "verified";
		    
		    if(verified.equals(allCapsData)){
		    	KitchenInterface KI = new KitchenInterface();
		    	KI.retrieve();
		    	
		    	TransactionDetail td1 = new TransactionDetail();
		    	td1.TransactionDetailUI();
		    	td1.retrieve();
		    }
		    else {
		    	
		    	Connection connection = DriverManager.getConnection(connectionURL+dbName+"?serverTimezone=UTC",username,password);
		    	String qDeleteItem = "DELETE FROM ordereditem";
		    	String qDeleteTrans = "DELETE FROM ordertransaction";
		    	String qDeleteOrder = "DELETE FROM orders";
	    		PreparedStatement preparedStatement = connection.prepareStatement(qDeleteItem);
	    		PreparedStatement preparedStatement2 = connection.prepareStatement(qDeleteTrans);
	    		PreparedStatement preparedStatement3 = connection.prepareStatement(qDeleteOrder);
	    		preparedStatement.executeUpdate();	
	    		preparedStatement2.executeUpdate();	
	    		preparedStatement3.executeUpdate();	
	    		
		    	ClientInterface ui = new ClientInterface();
		    	ClientInterface.mainFrame.dispose();
		    	ui.prepareGUI();
		    	ui.showTableDemo();
		    	JOptionPane.showMessageDialog(null,"Credit Card Number does not valid");
		    }

			// Closing the socket connection with the server
			clientSocket.close();			
			
		} catch (Exception ex) {
			System.out.println("we got problem");
			ex.printStackTrace();
		}

	}

}