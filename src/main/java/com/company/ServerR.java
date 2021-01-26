package com.company;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class ServerR
{
	public static void main(String[] args)
	{
		SSLServerSocketFactory factory = null;
		SSLServerSocket sslserversocket = null;
		SSLSocket sslSocket = null;
		String ksName = "C:\\Users\\Luksor\\IdeaProjects\\SSLExample\\src\\main\\java\\com\\company\\keystore.jks";
		//String ksName = "D:\\Projekty\\SSLExample\\src\\main\\java\\com\\company\\keystore.jks";
		char[] ksPass = "password".toCharArray();
		char[] ctPass = "132456".toCharArray();
		KeyStore ks;

	    System.out.println("Serwer czeka na klienta");

	    try{
			ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(ksName), ksPass);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, ctPass);
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), null, null);
			factory = sc.getServerSocketFactory();
			sslserversocket = (SSLServerSocket) factory.createServerSocket(4400);
	    }catch(IOException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException | KeyManagementException e)
	    {
	    e.printStackTrace();
	    System.out.println("Server error");
		}
	
	    while(true)
	    {
	        try
			{
				sslSocket = (SSLSocket) sslserversocket.accept();
				System.out.println("connection Established");
	            ServerThread st = new ServerThread(sslSocket);
	            st.start();
	        }catch(Exception e){
	        	e.printStackTrace();
	        	System.out.println("Connection Error");
	        }
	    }	
	}	
}
	
class ServerThread extends Thread
{  
	Handler handler = new Handler();
	String line = null;
	String[] temp = null;
	ObjectInputStream is = null;
	ObjectOutputStream os = null;
	SSLSocket sslSocket = null;
	String password;
	String login;
	String tempp;
	String temppp;

	public ServerThread(SSLSocket sslSocket)
	{
		this.sslSocket = sslSocket;
	}
	public void run()
	{
		int tem;
		try
		{
			is = new ObjectInputStream (sslSocket.getInputStream());
	        os = new ObjectOutputStream(sslSocket.getOutputStream());
		}catch(IOException e)
		{
	        System.out.println("IO error in server thread");
	    }
	    try
	    {
			os.writeObject(
					"Czy masz już konto?\n\n"
							+ "Tak:                           1\n"
							+ "Nie:                           2\n"
							+ "Przypomnij haslo:              3\n"
							+ "Wyjscie:                       Wyjdz\n"
			);
			line = (String) is.readObject();

			while(line.compareTo("Wyjdz") != 0) {
				line = "";
				line = (String) is.readObject();
				switch (line) {
					case "1":
						os.writeObject("Podaj login:");
						login = (String) is.readObject();
						os.writeObject("Podaj haslo:");
						password = (String) is.readObject();
						if(handler.checkUser(login,password)) {
							line = "Wyjdz";
							break;
						}else{
							os.writeObject("Niepoprawne dane logowania");
							break;
						}

					case "2":
						os.writeObject("Podaj jaki chcesz miec login");
						login = (String) is.readObject();
						os.writeObject("Podaj jakie chcesz miec haslo");
						password = (String) is.readObject();
						os.writeObject("Podaj pytanie pomocnicze");
						tempp = (String) is.readObject();
						if(!(handler.checkLogin(login))) {
							handler.createAcc(login, password, tempp);
							line = "Wyjdz";
							break;
						}else {
							os.writeObject("Podany login jest juz zajety");
							break;
						}

					case "3":
						os.writeObject("Podaj swoj login");
						login = (String) is.readObject();
						if(handler.checkLogin(login)) {
							temppp = handler.showQuestion(login);
							os.writeObject("Twoje pytanie pomocnicze: " + temppp);
							break;
						}else {
							os.writeObject("Nie ma takiego uzytkownika");
							break;
						}

					default:
						os.writeObject(line);
						os.flush();
						System.out.println("Response to Client  :  " + line);
				}
			}

			os.writeObject(
					"Wyszukiwarka - co chcesz zrobic?\n\n"
							+ "Szukaj frazy:                  1\n"
							+ "Zmien login:                   2\n"
							+ "Zmien haslo:                   3\n"
							+ "Wyjscie:                       Wyjdz\n"
			);

			line = "";

			while(line.compareTo("Wyjdz") != 0)
	        {
	        	line = (String) is.readObject();
	            switch(line)
	            {
	            case "1":
					os.writeObject("Jaka fraze sprawdzic?");
					line = (String) is.readObject();
					long a = handler.getPhrase("dsf");
					//os.writeObject(a);
					//System.out.println(a);

					//handler.getPhrase(line);
					os.writeObject("aa");
					//String response = "";
	            	//for(String s:temp)
	            	//	response += s;
	            	//os.writeObject(response);
	            	os.flush();
	            	break;
	            	
	            case "2":
	            	os.writeObject("Jaki ma byc nowy login");
	            	tempp = (String) is.readObject();
	            	os.writeObject("Powtorz nowy login");
	            	temppp = (String) is.readObject();
	            	if(tempp.equals(temppp)) {
	            		handler.updateLogin(login, password, temppp);
	            		login=temppp;
						os.writeObject("Login zostal zmieniony");
						break;
	            	}else if(tempp.equals(login)){
						os.writeObject("Login nie rozni sie od poprzedniego");
						break;
					}
	            	else{
	            		os.writeObject("Login nie jest taki sam");
						break;
	            	}

	            	
	            case "3":
	            	String temp;
	            	os.writeObject("Jaki ma byc nowe haslo");
	            	tempp = (String) is.readObject();
	            	os.writeObject("Powtorz nowe haslo");
	            	temppp = (String) is.readObject();
					os.writeObject("Podaj nowe pytanie pomocnicze");
					temp = (String) is.readObject();
	            	if(tempp.equals(password)) {
						os.writeObject("Haslo nie rozni sie od poprzedniego");
						break;
					}else if(tempp.equals(temppp)){
						handler.updatePassword(login, password, temppp, temp);
						password = temppp;
						os.writeObject("Haslo oraz pytanie pomocnicze zostaly zmienione");

						break;
					}
	            	else{
	            		os.writeObject("Haslo nie jest takie samo");
	            		break;
	            	}
	                    
	            default:
	                os.writeObject(line);
	                os.flush();
	                System.out.println("Response to Client  :  " + line);
	            }
	        }
	    }catch (IOException e)
	    {
	    	line = this.getName(); //Name of Thread (Client)
	        System.out.println("IO Error/ Client " + line + " terminated abruptly");
	    }catch(NullPointerException e)
	    {
	        line = this.getName(); 
	        System.out.println("Client " + line + " Closed");
	    }catch (ClassNotFoundException e)
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
	    {    
		    try
		    {
		        System.out.println("Connection Closing..");
		        if (is != null)
		        {
		            is.close(); 
		            System.out.println(" Socket Input Stream Closed");
		        }
		
		        if(os != null)
		        {
		            os.close();
		            System.out.println("Socket Out Closed");
		        }
		        
		        if (sslSocket!=null)
		        {
		        	sslSocket.close();
		        	System.out.println("Socket Closed");
		        }
		
		        }catch(IOException ie)
		    	{
		        	System.out.println("Socket Close Error");
		    	}
	    }
	}
}