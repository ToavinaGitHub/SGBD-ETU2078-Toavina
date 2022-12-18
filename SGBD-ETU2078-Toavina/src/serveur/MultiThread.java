package serveur;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import client.*;
import relation.Database;
import relation.Grammaire;
import serveur.*;
public class MultiThread implements Runnable {
	Serveur server;										
	Socket client;										
	static Thread serverThread;
	
	public static Thread getServerThread() {
		return serverThread;
	}
	public static void setServerThread(Thread serverThread) {
		MultiThread.serverThread = serverThread;
	}

	BufferedReader in;
	PrintWriter out;
	int clientId = 0;
	String clientName;
	String message;
	Scanner sc = new Scanner(System.in);
	static int count=0;
	static Database d;
	static Grammaire g;
	static boolean use = false;
	
	public static boolean isUse() {
		return use;
	}
	public static void setUse(boolean use) {
		MultiThread.use = use;
	}
	public static Database getD() {
		return d;
	}
	public static void setD(Database d) {
		MultiThread.d = d;
	}
	public static Grammaire getG() {
		return g;
	}
	public static void setG(Grammaire g) {
		MultiThread.g = g;
	}
	public MultiThread(Socket client, Serveur server,Database d,Grammaire g) throws ClassNotFoundException, IOException {
		
		this.client = client;
		this.server = server;
		MultiThread.setD(d);
		MultiThread.setG(g);
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream());
			clientId = server.addClient(client);
			serverThread = new Thread(this);
			serverThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		if(MultiThread.getD()==null)MultiThread.setD(Serveur.getD());
		
		try {
			MultiThread.setG(new Grammaire(MultiThread.getD()));
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		
		try {
			sc = new Scanner(client.getInputStream());
			message = sc.nextLine();
			while (message != null) {
				out.println(message);
				System.out.println(message);
				try {
					String[] tab = message.split("USE ");
					if(tab.length==2)
					{
						MultiThread.deleteSpace(tab[1]);
						Serveur.setD(new Database(tab[1]));
						MultiThread.setD(new Database(tab[1]));
						MultiThread.setG(new Grammaire(MultiThread.getD()));
					}else {
						MultiThread.getG().setDatabase(MultiThread.getD());
						MultiThread.getG().setTabRelations(MultiThread.getD().getTabRelations());
						MultiThread.getG().ExecuteQuery(message);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				message = sc.nextLine();
				count++;
			}
			
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				server.deleteClient(clientId);
				client.close();
				serverThread.join();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void deleteSpace(String s)
	{
		String res=null;
		for(int i=0;i<s.length();i++)
		{
			if(s.charAt(i)!=' ')
			{
				res+=s.charAt(i);
			}
		}
		s=res;
	}
}
