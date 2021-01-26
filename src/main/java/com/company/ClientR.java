package com.company;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;

public class ClientR
{

	public static void main(String[] args) throws IOException
	{	
	    InetAddress address = InetAddress.getLocalHost();
		SSLSocketFactory sslsocketfactory = null;
		String line = "";
	    BufferedReader br = null;
	    SSLSocket sslsocket = null;

	        
	    ObjectInputStream is = null;
	    ObjectOutputStream os = null;
	
	    try 
        {
			System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\Luksor\\IdeaProjects\\SSLExample\\src\\main\\java\\com\\company\\keystore.jks");
			//System.setProperty("javax.net.ssl.trustStore", "D:\\Projekty\\SSLExample\\src\\main\\java\\com\\company\\keystore.jks");
            sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            sslsocket = (SSLSocket) sslsocketfactory.createSocket(address, 4400);
			sslsocket.startHandshake();
            br = new BufferedReader(new InputStreamReader(System.in));
			os = new ObjectOutputStream(sslsocket.getOutputStream());
			is = new ObjectInputStream(sslsocket.getInputStream());


	    }catch (IOException e)
	    {
	        e.printStackTrace();
	        System.err.print("IO Exception");
	    }

	    String response = null;
	    try
	    {
	        while(line.compareTo("Wyjdz") != 0)
	        {
	        	os.writeObject(line);
	        	os.flush();
	        	response = (String) is.readObject();
	        	System.out.println("Server Response : \n" + response);
	        	line = br.readLine();
	        }
	    }catch(IOException | ClassNotFoundException e)
	    {
	        e.printStackTrace();
	        System.out.println("Socket read Error");
	    }finally
	    {
	        is.close();
	        os.close();
	        br.close();
	        sslsocket.close();
	        System.out.println("Connection Closed");
	    }
	}
}