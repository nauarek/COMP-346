package Assignment2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 *
 * @author Kerly Titus
 */
public class DriverV2 {

    /** 
     * main class
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            PrintStream output = new PrintStream("Test_unsynchronized.txt");
            System.setOut(output);
        }catch(FileNotFoundException e){
            System.out.println("FILE NOT FOUND");
            System.exit(0);
        }


    	NetworkV2 objNetwork = new NetworkV2( );            /* Activate the network */
        objNetwork.start();

        ServerV2 server1 = new ServerV2("Server1");
        server1.start();
        ServerV2 server2 = new ServerV2("Server2");
        server2.start();

        ClientV2 objClient1 = new ClientV2("sending");          /* Start the sending client thread */
        objClient1.start();
        ClientV2 objClient2 = new ClientV2("receiving");        /* Start the receiving client thread */
        objClient2.start();

    }
    
 }
