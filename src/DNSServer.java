/*
 * Tayyab Ahmad
 * 10197212
 * CISC435 Assignment 2
 */

import java.io.*;
import java.net.*;

public class DNSServer {
	
    public static void main(String[] args) throws IOException { 
    	
        DatagramSocket socket = new DatagramSocket(9090); 
        InetAddress ip = InetAddress.getLocalHost(); 
        byte[] receive = new byte[512]; 
        byte buf[] = null; 
        DatagramPacket toReceive = new DatagramPacket(receive, receive.length); 
        
        System.out.println("DNS Server is running...");
        
        for (int i = 0; i < 4; i++) {
        	
        	socket.receive(toReceive); 
        	new Thread(new ClientHandler(socket, ip, 1239)).start(); 
        } // end for-loop
        System.out.println("Server shutting down");
        socket.close();
    } // end main method
} // end Class DNSServer

//ClientHandler class 
class ClientHandler implements Runnable { 
	
	public static final String[] domain_names = {"www.sdxcentral.com", "www.lightreading.com", "www.linuxfoundation.org", "www.cncf.io"};
	public static final String[] mappings = {"www.sdxcentral.com       104.20.242.119     2606:4700:10::6814:f277",
											 "www.lightreading.com     104.25.195.108     2606:4700:20::6819:c46c",
											 "www.linuxfoundation.org  23.185.0.2         2620:12a:8000::2",
											 "www.cncf.io              23.185.0.3         2620:12a:8000::3"};

    final DatagramSocket socket;
    final InetAddress ip;
    final int port;
   
    public ClientHandler(DatagramSocket socket, InetAddress ip, int port) { 
    	this.socket = socket;
    	this.ip = ip;
    	this.port = port;
    } // end ClientHandler constructor

    @Override
    public void run() { 
    	
    	byte[] receive = new byte[512]; 
        byte buf[] = null; 
        DatagramPacket toReceive = new DatagramPacket(receive, receive.length); 
        
        buf = "Enter a domain number to access it: (1)www.sdxcentral.com, (2)www.lightreading.com, (3)www.linuxfoundation.org, (4)www.cncf.io".getBytes(); 
        sendPacket(buf, socket, ip);
    	
    	while (true) { 
    		
    		try { socket.receive(toReceive); }
			catch (IOException e) { e.printStackTrace(); } 
    		
    		String domain = data(receive).toString(); //.toString();
    		int dom = Integer.parseInt(domain);
    		System.out.println(domain);
    			
    			if (dom == 0)//domain.equals("www.sdxcentral.com"))
    				buf = mappings[0].getBytes();
    			else if (dom == 1)//domain.equals("www.lightreading.com"))
    				buf = mappings[1].getBytes();
    			else if (dom == 2)//domain.equals("www.linuxfoundation.org"))
    				buf = mappings[2].getBytes();
    			else if (dom == 3)//domain.equals("www.cncf.io"))
    				buf = mappings[3].getBytes();
    			else 
    				buf = "Invalid Domain".getBytes();
    			
    		sendPacket(buf, socket, ip);
        } // end while-loop
    } // end thread run
    
    public void sendPacket(byte[] buf, DatagramSocket socket, InetAddress ip) {
    	DatagramPacket toSend = new DatagramPacket(buf, buf.length, ip, port); 
		try { socket.send(toSend); } 
		catch (IOException e) { e.printStackTrace(); }
    } // end sendPacket method
    
    // A utility method to convert the byte array data into a string representation. 
    public static StringBuilder data(byte[] a) { 
        if (a == null)  return null; 
        StringBuilder ret = new StringBuilder(); int i = 0; 
        while (a[i] != 0) { ret.append((char) a[i]); i++; 
        } return ret; 
    } // end data method
} // end Class clientHandler
