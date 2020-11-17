/*
 * Tayyab Ahmad
 * 10197212
 * CISC435 Assignment 2
 */

import java.io.*;
import java.net.*; 
  
public class DHCPServer { 
	
	//IP configuration to send to client {IP, GW IP, DNS port, lease time (seconds)}
	static String[] IPv4 = {"192.168.1.2/24", "192.168.1.3/24", "192.168.1.4/24", "192.168.1.5/24"};
	static String[] IPconfig = {IPv4[0], "192.168.1.1", "9090", "60"};
	
    public static void main(String[] args) throws IOException { 
    	
        DatagramSocket socket = new DatagramSocket(7071); 
        InetAddress ip = InetAddress.getLocalHost(); 
        byte[] receive = new byte[512]; 
        byte buf[] = null; 
        int clientPort = 1239;
        DatagramPacket toReceive = new DatagramPacket(receive, receive.length); 
        
        System.out.println("DHCP Server is running...");
        
        for (int i = 0; i < 4; i++) {
        	
        	socket.receive(toReceive);  // after receiving a packet from the client, send them an IP address to use
        	String configuration = "IP: " + IPv4[i] + " GW: 192.168.1 DNSServerPort#: " + 9090 + 
        						   " Lease: " + 60 + " (seconds)"; 
        	buf = configuration.getBytes(); 
        	DatagramPacket toSend = new DatagramPacket(buf, buf.length, ip, clientPort); 
        	socket.send(toSend); 
  
        	// clear the buffer after every message. 
        	receive = new byte[65535]; 
        } // end for-loop
        
        System.out.println("Server shutting down");
        socket.close();
    } // end main method
} // end Server Class