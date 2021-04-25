package client;

import kioskapp.order.Order;
import kioskapp.itemproduct.*;
import kioskapp.ordertransaction.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;    
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Random;
import java.awt.Component;

public class ClientInterface {
	
   public static JFrame mainFrame;
   private JPanel orderModePanel, controlPanel, quantityPanel, creditCardPanel, listPanel;
   private JLabel headerLabel, orderModeLabel, itemQuantityLabel, creditCardLabel, quantityTotalLabel, quantityLabel, priceTotalLabel, priceLabel, IdLabel, productIdLabel;
   private JComboBox quantitySelection;
   private JTextField creditCardField;
   private JButton addButton, payButton;
   private JRadioButton eatInButton, takeAwayButton;
   private ButtonGroup group;
   private JTable table;   
   private JScrollPane scrollPane;   
   private DefaultTableModel tableModel;
   public String Data, Price, ID;
   private DefaultListModel<String> list1;
   private JList<String> list;
   public int x, y, orderId = 0;
   public float z, a = 0;
   
   Order myOrder = new Order();
   ItemProduct itemprod = new ItemProduct();
      
	static String driver, dbName, connectionURL, username, password;
	
   
   Connection connection;
   Statement statement;
   private JLabel lblNewLabel;

   public ClientInterface()
   {      
		driver = "com.mysql.cj.jdbc.Driver";
		connectionURL ="jdbc:mysql://localhost:3306/";
		dbName = "assignment2";
		username = "root";
		password = "";
		
		list1 = new DefaultListModel<>();		
		list = new JList<>(list1);
		list.setBackground(Color.pink);
		list.setBounds(100,100,75,75);
   }
   
   public static void main(String[] args) throws ClassNotFoundException{
	   ClientInterface UI = new ClientInterface();      
	   UI.prepareGUI();
	   UI.showTableDemo();
   }
   
   @SuppressWarnings("unchecked")
   public void prepareGUI(){
	  //Frame 
      mainFrame = new JFrame("McDonalds Kiosk Application");
      mainFrame.setSize(1009,846);
      mainFrame.getContentPane().setLayout(new BorderLayout());
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      mainFrame.setVisible(true);    
      mainFrame.getContentPane().setBackground(Color.YELLOW);
      mainFrame.getContentPane().setForeground(Color.WHITE);
      mainFrame.setBounds(500,200,800,800);
      
      //RadioButton Group
      group = new ButtonGroup();
      
      //tableModel
      String[] columnNames = {"Name", "Price"};          
      tableModel = new DefaultTableModel(columnNames, 0);
      quantityTotalLabel = new JLabel("Total Quantity: "); 
      quantityLabel = new JLabel(); 
      priceTotalLabel = new JLabel("Total Price: RM"); 
      priceLabel = new JLabel();
      IdLabel = new JLabel();
      
      //ComboBox
      String[] quantity = {"1","2","3","4"};
      
      //North Panel 
      orderModePanel = new JPanel();
      orderModePanel.setLayout(new FlowLayout());
      orderModePanel.setBackground(Color.PINK);
      
      //Center Panel
      controlPanel = new JPanel();
      controlPanel.setPreferredSize(new Dimension(100,100));
      controlPanel.setLayout(new GridLayout(4,2));   
      controlPanel.setBackground(Color.PINK);
      
      //ScrollPane
      scrollPane = new JScrollPane();
      scrollPane.setSize(1000, 1000);
      
      controlPanel.add(scrollPane);
      
      //table
      table = new JTable(tableModel);
      scrollPane.setViewportView(table);
      table.setCellSelectionEnabled(true);
      table.setFillsViewportHeight(true);
      table.setBackground(Color.PINK);
      table.setForeground(Color.black);
      table.setSelectionBackground(Color.orange);
      table.setGridColor(Color.orange);
      table.setFont(new Font("Tahoma", Font.PLAIN,17));
      table.setRowHeight(30);
      table.setAutoCreateRowSorter(true);
      
      //West Panel
      quantityPanel = new JPanel();
      quantityPanel.setBackground(Color.PINK);
      quantityPanel.setPreferredSize(new Dimension(250, 50));
      quantityPanel.setLayout(new FlowLayout());   
      
      //South Panel
      creditCardPanel = new JPanel();
      creditCardPanel.setPreferredSize(new Dimension(250,250));
      creditCardPanel.setLayout(new FlowLayout());
      creditCardLabel = new JLabel("Enter Credit Card Number: "); 
      creditCardPanel.add(creditCardLabel);
      
      //TextField
      creditCardField = new JTextField(50);
      creditCardPanel.add(creditCardField);   
      
      payButton = new JButton("Pay");
      payButton.setBackground(Color.WHITE);
      payButton.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String cc = creditCardField.getText().toString();
			
			OrderTransaction ot = new OrderTransaction();
			ot.setLast4Digits(Integer.valueOf(cc));
			
			ClientApplication CA = new ClientApplication();
			CA.SendCreditCardNumber(ot.getLast4Digits(), orderId, myOrder.getTotalAmount(), group.getSelection().getActionCommand());
			
		}
    	  
      });
      creditCardPanel.add(payButton);
      creditCardPanel.add(IdLabel);
      creditCardPanel.setBackground(Color.PINK);
      
      //East Panel
      listPanel = new JPanel();
      listPanel.setBackground(Color.PINK);
      listPanel.setPreferredSize(new Dimension(250, 250));
      listPanel.setLayout(new FlowLayout());
      listPanel.add(list);
      listPanel.add(quantityTotalLabel);
      listPanel.add(quantityLabel);
      listPanel.add(priceTotalLabel);
      listPanel.add(priceLabel);
      
      mainFrame.getContentPane().add(orderModePanel, BorderLayout.NORTH);
      
      lblNewLabel = new JLabel("WELCOME TO MCDONALDS <3");
      lblNewLabel.setForeground(Color.DARK_GRAY);
      lblNewLabel.setBackground(Color.DARK_GRAY);
      lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
      orderModePanel.add(lblNewLabel);
      mainFrame.getContentPane().add(controlPanel, BorderLayout.CENTER);
      
      //Button
      addButton = new JButton("Insert to Cart");
      controlPanel.add(addButton);
      addButton.setPreferredSize(new Dimension(160, 30));
      addButton.setBackground(Color.ORANGE);
      addButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
      addButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {	
			DecimalFormat df = new DecimalFormat("###.##");
			x = Integer.valueOf((String) quantitySelection.getSelectedItem());
			y = y + x;
			z = Float.valueOf(Price);
			a = a + (x * z);
			float totalOneItem = x * z;
			myOrder.setTotalAmount(a);
			float tot = myOrder.getTotalAmount();
			list1.addElement(Data + " \t" + String.valueOf(x) + " \t" + String.valueOf(df.format(x * z)));
			
			quantityLabel.setText(String.valueOf(y));
			priceLabel.setText(String.valueOf(df.format(tot)));			
			IdLabel.setText(ID);			
			//itemprod.setItemProduct(Integer.valueOf(ID));

			
			try {
				Random r = new Random();
				int random_ref_num = r.nextInt(100);
				Connection connection = DriverManager.getConnection(connectionURL+dbName+"?serverTimezone=UTC",username,password);
				PreparedStatement preparedStmt;
			    
			    String getValidation = "SELECT COUNT(*) FROM orders WHERE status = 'To Pay' LIMIT 1";
			    PreparedStatement preparedStmts = connection.prepareStatement(getValidation);
			    ResultSet result = preparedStmts.executeQuery(getValidation);
			    orderId = 0;
			    
			    if (result.next()) {
			    	orderId = result.getInt(1);
			    	if (orderId == 1)
			    	{
			    		String getValidations = "SELECT OrderId FROM orders WHERE status = 'To Pay' LIMIT 1";
					    PreparedStatement preparedStmtss = connection.prepareStatement(getValidations);
					    ResultSet results = preparedStmtss.executeQuery(getValidations);
					    
					    if (results.next()) {
					    	orderId = results.getInt(1);
					    	
					    	String query2 = "INSERT INTO ordereditem (ItemProduct, Quantity, SubTotalAmount, Orders) VALUES (?,?,?,?)";
				    		PreparedStatement preparedStmt4 = connection.prepareStatement(query2);  
				    		preparedStmt4.setInt(1, itemprod.getItemProduct());
				    		preparedStmt4.setInt(2, x);
				    		preparedStmt4.setFloat(3, totalOneItem);
				    		preparedStmt4.setInt(4, orderId);
				    		preparedStmt4.executeUpdate();
				    		
				    		String queryUpdate = "UPDATE orders SET TotalAmount=? WHERE OrderId=?";
				    		PreparedStatement preparedStmt5 = connection.prepareStatement(queryUpdate);
				    		preparedStmt5.setFloat(1, myOrder.getTotalAmount());
				    		preparedStmt5.setInt(2, orderId);
				    		preparedStmt5.executeUpdate();				    						    	
					    }			    		
			    	}
			    	else
			    	{
			    		String query3 = "INSERT INTO orders (TotalAmount, OrderReferenceNumber, status) VALUES (?,?,?)";
					    
					    preparedStmt = connection.prepareStatement(query3, Statement.RETURN_GENERATED_KEYS);  
					    preparedStmt.setFloat(1, myOrder.getTotalAmount());
					    preparedStmt.setFloat(2, random_ref_num);
					    preparedStmt.setString(3, "To Pay");
					    preparedStmt.executeUpdate();
					    
					    String getValidations = "SELECT OrderId FROM orders WHERE status = 'To Pay' LIMIT 1";
					    PreparedStatement preparedStmtss = connection.prepareStatement(getValidations);
					    ResultSet results = preparedStmtss.executeQuery(getValidations);
					    
					    if (results.next()) {
					    	orderId = results.getInt(1);
					    	
					    	String query2 = "INSERT INTO ordereditem (ItemProduct, Quantity, SubTotalAmount, Orders) VALUES (?,?,?,?)";
				    		PreparedStatement preparedStmt4 = connection.prepareStatement(query2);  
				    		preparedStmt4.setInt(1, itemprod.getItemProduct());
				    		preparedStmt4.setInt(2, x);
				    		preparedStmt4.setFloat(3, myOrder.getTotalAmount());
				    		preparedStmt4.setInt(4, orderId);
				    		preparedStmt4.executeUpdate();
					    }
			    	}
			    }			   			    
			    //preparedStmt.close();
			    connection.close();				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}			
		}    	  
      });
      headerLabel = new JLabel("Selected Item: ");
      controlPanel.add(headerLabel);
      productIdLabel = new JLabel("");
      controlPanel.add(productIdLabel);
      mainFrame.getContentPane().add(creditCardPanel, BorderLayout.SOUTH);
      mainFrame.getContentPane().add(listPanel, BorderLayout.EAST);      
      mainFrame.getContentPane().add(quantityPanel, BorderLayout.WEST);  
      
      //Label
      orderModeLabel = new JLabel("Order Mode: ");
      quantityPanel.add(orderModeLabel);
      
      //RadioButton
      eatInButton = new JRadioButton("Eat-In", true);
      quantityPanel.add(eatInButton);
      eatInButton.setBackground(Color.PINK);
      eatInButton.setActionCommand("Eat-In");
      group.add(eatInButton);
      takeAwayButton = new JRadioButton("Take-Away");
      quantityPanel.add(takeAwayButton);
      takeAwayButton.setBackground(Color.PINK);
      takeAwayButton.setActionCommand("Take-Away");
      group.add(takeAwayButton);  
      itemQuantityLabel = new JLabel("Select Quantity: ");
      quantityPanel.add(itemQuantityLabel);
      quantitySelection = new JComboBox(quantity);
      quantityPanel.add(quantitySelection);
      quantitySelection.setBackground(Color.WHITE);
      quantitySelection.setSelectedIndex(0);
      
   }
   
        
   public void showTableDemo() throws ClassNotFoundException{
      
      try {
    	  Class.forName(driver);
  		  Connection connection = DriverManager.getConnection(connectionURL+dbName+"?serverTimezone=UTC",username,password); //Creating connection with database
          statement = connection.createStatement();
          String query = "select * from itemproduct";
          ResultSet resultSet = statement.executeQuery(query);
                              
          while (resultSet.next()) 
          {          
	    	  int id = resultSet.getInt("ItemProduct");
	          String name = resultSet.getString("Name");
	          float price = resultSet.getFloat("Price");
	          itemprod.setItemProduct(id);
	          itemprod.setName(name);
	          itemprod.setPrice(price);
	          
	          Object[] populateData = {itemprod.getName(), itemprod.getPrice()}; 
	          tableModel.addRow(populateData);
          }  
          
          ListSelectionModel select = table.getSelectionModel();
          select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
          select.addListSelectionListener(new ListSelectionListener() {  
              public void valueChanged(ListSelectionEvent e) 
              {
                Data = null;
                Price = null;
                ID = null;
                int[] row = table.getSelectedRows();
                int[] columns = table.getSelectedColumns();
                for (int i = 0; i < row.length; i++) 
                {            
                  for (int j = 0; j < columns.length; j++) 
                  {                	
                    Data = String.valueOf(table.getValueAt(row[i], columns[j]));
                    Price = String.valueOf(table.getValueAt(row[i], 1));
                    //ID = String.valueOf(table.getValueAt(row[i], 0));
                  }                                  
                }                
                headerLabel.setText(Data);                
              }
            });
      } catch (SQLException throwables) {
          throwables.printStackTrace();
      }      
   }
}