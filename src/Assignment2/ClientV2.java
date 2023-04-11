package Assignment2;

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/** Client class
 *
 * @author Kerly Titus
 */

public class ClientV2 extends Thread {
    
    private static int numberOfTransactions;   		/* Number of transactions to process */
    private static int maxNbTransactions;      		/* Maximum number of transactions */
    private static TransactionsV2 [] transaction; 	        /* Transactions to be processed */
    private String clientOperation;    			/* sending or receiving */
       
	/** Constructor method of Client class
 	 * 
     * @return 
     * @param
     */
     ClientV2(String operation)
     { 
       if (operation.equals("sending"))
       { 
           System.out.println("\n Initializing client sending application ...");
           numberOfTransactions = 0;
           maxNbTransactions = 100;
           transaction = new TransactionsV2[maxNbTransactions];
           clientOperation = operation; 
           System.out.println("\n Initializing the transactions ... ");
           readTransactions();
           System.out.println("\n Connecting client to network ...");
           String cip = NetworkV2.getClientIP();
           if (!(NetworkV2.connect(cip)))
           {   System.out.println("\n Terminating client application, network unavailable");
               System.exit(0);
           }
       	}
       else
    	   if (operation.equals("receiving"))
           { 
    		   System.out.println("\n Initializing client receiving application ...");
    		   clientOperation = operation; 
           }
     }
           
    /** 
     * Accessor method of Client class
     * 
     * @return numberOfTransactions
     * @param
     */
     public int getNumberOfTransactions()
     {
         return numberOfTransactions;
     }
            
    /** 
     * Mutator method of Client class
     * 
     * @return 
     * @param nbOfTrans
     */
     public void setNumberOfTransactions(int nbOfTrans)
     { 
         numberOfTransactions = nbOfTrans;
     }
         
    /** 
     * Accessor method of Client class
     * 
     * @return clientOperation
     * @param
     */
     public String getClientOperation()
     {
         return clientOperation;
     }
         
    /** 
     * Mutator method of Client class
	 * 
	 * @return 
	 * @param operation
	 */
	 public void setClientOperation(String operation)
	 { 
	     clientOperation = operation;
	 }
         
    /** 
     * Reading of the transactions from an input file
     * 
     * @return 
     * @param
     */
     public void readTransactions()
     {
        Scanner inputStream = null;     	/* Transactions input file stream */
        int i = 0;                      		/* Index of transactions array */
        
        try
        {
        	inputStream = new Scanner(new FileInputStream("transaction2.txt"));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File transaction.txt was not found");
            System.out.println("or could not be opened.");
            System.exit(0);
        }
        while (inputStream.hasNextLine( ))
        {
            try
            {   transaction[i] = new TransactionsV2();
                transaction[i].setAccountNumber(inputStream.next());            /* Read account number */
                transaction[i].setOperationType(inputStream.next());            /* Read transaction type */
                transaction[i].setTransactionAmount(inputStream.nextDouble());  /* Read transaction amount */
                transaction[i].setTransactionStatus("pending");                 /* Set current transaction status */
                i++;
            }
             catch(InputMismatchException e)
            {
                System.out.println("Line " + i + "file transactions.txt invalid input");
                System.exit(0);
            }
            
        }
        setNumberOfTransactions(i);		/* Record the number of transactions processed */
        
        /* System.out.println("\n DEBUG : Client.readTransactions() - " + getNumberOfTransactions() + " transactions processed"); */
        
        inputStream.close( );

     }
     
    /** 
     * Sending the transactions to the server 
     * 
     * @return 
     * @param
     */
     public void sendTransactions()
     {
         int i = 0;     /* index of transaction array */
         
         while (i < getNumberOfTransactions())
         {  
	
        	 while (NetworkV2.getInBufferStatus().equals("full"))
        	{
         	  Thread.yield(); 	/* Yield the cpu if the network input buffer is full */
          }
                                              	
            transaction[i].setTransactionStatus("sent");   /* Set current transaction status */
           
            /* System.out.println("\n DEBUG : Client.sendTransactions() - sending transaction on account " + transaction[i].getAccountNumber()); */ 
            
            NetworkV2.send(transaction[i]);                            /* Transmit current transaction */
            i++;          
         }
         
    }
         
 	/** 
  	 * Receiving the completed transactions from the server
     * 
     * @return 
     * @param transact
     */
     public void receiveTransactions(TransactionsV2 transact)
     {
         int i = 0;     /* Index of transaction array */
         
         while (i < getNumberOfTransactions())
         {   
        	 while (NetworkV2.getOutBufferStatus().equals("empty"))
        	 {
        		 Thread.yield(); 	/* Yield the cpu if the network output buffer is full */
        		 
        	 }
                                                                            	
            NetworkV2.receive(transact);                               	/* Receive updated transaction from the network buffer */
            
            /* System.out.println("\n DEBUG : Client.receiveTransactions() - receiving updated transaction on account " + transact.getAccountNumber()); */
            
            System.out.println(transact);                               /* Display updated transaction */    
            i++;
         } 
    }
     
    /** 
     * Create a String representation based on the Client Object
     * 
     * @return String representation
     * @param 
     */
     public String toString() 
     {
    	 return ("\n client IP " + NetworkV2.getClientIP() + " Connection status" + NetworkV2.getClientConnectionStatus() + "Number of transactions " + getNumberOfTransactions());
     }
    
       
    /** Code for the run method
     * 
     * @return 
     * @param
     */
    public void run() {
    	TransactionsV2 transact = new TransactionsV2();
    	long sendClientStartTime, sendClientEndTime, receiveClientStartTime, receiveClientEndTime;

        if(getClientOperation().equals("sending")){
            sendClientStartTime = System.currentTimeMillis();
            sendTransactions();
            sendClientEndTime = System.currentTimeMillis();
            System.out.println("Terminating Client Sending Thread - Running Time is " + (sendClientEndTime - sendClientStartTime) + " milliseconds");
        }

        else if (getClientOperation().equals("receiving")) {
            receiveClientStartTime = System.currentTimeMillis();
            receiveTransactions(transact);
            receiveClientEndTime = System.currentTimeMillis();
            System.out.println("Terminating Client Receiving Thread - Running Time is " + (receiveClientEndTime - receiveClientStartTime) + " milliseconds");
            NetworkV2.disconnect(NetworkV2.getClientIP());

        } else {
            System.exit(0);
        }
    }
}
