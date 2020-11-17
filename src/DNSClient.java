/*
 * Tayyab Ahmad
 * 10197212
 * CISC435 Assignment 2
 */

import java.io.*;
import java.net.*;
import java.util.Scanner; 
  
public class DNSClient {
	
	static int port = 1239;
	
	static String name;
	static int DNS_port;
	static int lease_time;
	static String DHCP_GW_MAC;
	
	static String GW_MAC = "192.168.2.1";
	static String MAC = "1C-39-47-D3-86-E9";
	static String IPv4_website;
	static String domain_website;
	
    public static void main(String args[]) throws IOException { 
        
        byte[] buf = null;
        byte[] receive = new byte[512]; 
        DatagramPacket toReceive = new DatagramPacket(receive, receive.length); 
        DatagramSocket socket = new DatagramSocket(port); 
        InetAddress ip = InetAddress.getLocalHost(); 
 
        // send "connection request" to DHCP server, that will reply with an IP for 
        // the client to use
        buf = "Connection Request".getBytes();  
        sendPacket(buf, socket, ip, 7071);
        
        // receive the IP configuration from the server and set the client's name
        socket.receive(toReceive);
        System.out.println("Server configuration: " + data(receive)); 
        String config = data(receive).toString();
        String[] configArr = config.split("\\s+");
        
        // setting configuration variables
        name = configArr[1];
        DHCP_GW_MAC = configArr[3];
        DNS_port = Integer.parseInt(configArr[5]);
        lease_time = Integer.parseInt(configArr[7]);
        
        // print client name (which is the IP given by the DHCP server)
        System.out.println("My name is " + name);
        
        // connect to the DNS server
        buf = "Connection Request".getBytes();  
        sendPacket(buf, socket, ip, 9090);
        
        socket.receive(toReceive);
        System.out.println(data(receive).toString());

        Scanner sc = new Scanner(System.in); 
        String mapping;
        
        while (true) {   
            
            String inp = sc.nextLine(); 
            buf = inp.getBytes();  
            sendPacket(buf, socket, ip, 9090);
            
            socket.receive(toReceive);
            String response = data(receive).toString();
            if (response.toString().equals("www.sdxcentral.com       104.20.242.119     2606:4700:10::6814:f277") ||
            		response.toString().equals("www.lightreading.com     104.25.195.108     2606:4700:20::6819:c46c") ||
            		response.toString().equals("www.linuxfoundation.org  23.185.0.2         2620:12a:8000::2") ||
            		response.toString().equals("www.cncf.io              23.185.0.3         2620:12a:8000::3")) {
            	mapping = response;
                break;
        	} else 
            	System.out.println("You did not enter a valid domain name. The "
                 + "options are: www.sdxcentral.com, www.lightreading.com, www.linuxfoundation.org, www.cncf.io"); 
           
            receive = new byte[512];
        } //end while-loop

        String websiteInfo[] = mapping.split("\\s+");
        String websitePacket = GW_MAC + " | " + MAC + " | " + websiteInfo[1] + 
        		" | " + name + " | Dest. TCP port: 80 | Src. TCP port: 1236 | " + 
        		websiteInfo[0];
        
        System.out.println("Packet information :" + websitePacket);
        socket.close();
    } // end main method
    
    public static void sendPacket(byte[] buf, DatagramSocket socket, InetAddress ip, int port) {
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
} //end Client Class
