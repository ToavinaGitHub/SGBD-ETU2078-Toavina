package serveur;
import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
import client.*;
import relation.Database;
import relation.Grammaire;

public class Serveur {
	Vector<Socket> Allclients = new Vector<Socket>();  ///Les clients connecté
	static Serveur server = new Serveur();
	static ServerSocket serverSocket;
	static Socket sock = null;;
	FileOutputStream fos=null;
	BufferedOutputStream bos=null;
	int nbClient = 0;
	static boolean state=true;
	static Database d;
	
	
	public static Database getD() {
		return d;
	}
	public static void setD(Database d) {
		Serveur.d = d;
	}
	/*------------------------Getters and setters------------------------- */
	public Vector<Socket> getAllclients() {
		return Allclients;
	}
	public void setAllclients(Vector<Socket> allclients) {
		Allclients = allclients;
	}
	public int getNbClient() {
		return nbClient;
	}
	public void setNbClient(int nbClient) {
		this.nbClient = nbClient;
	}
	
	public static ServerSocket getServerSocket() {
		return serverSocket;
	}
	public static void setServerSocket(ServerSocket serverSocket) {
		Serveur.serverSocket = serverSocket;
	}
	/*---------------------------------------All fonctions-------------------------------------------- */
	public int addClient(Socket client) {									///Ajouter nouveau client
		Allclients.add(client);
		this.setNbClient(getAllclients().size());
		return Allclients.size() - 1;
	}
	public void deleteClient(int i) {										///Supprimer un client
		Allclients.removeElementAt(i);
		this.setNbClient(getAllclients().size());
	}
	public void sendToAll(String mess) {									///Envoyer un message a tout les clients
		for (int i = 0; i < Allclients.size(); i++) {
			try {
				PrintWriter out = new PrintWriter(Allclients.get(i).getOutputStream());   
				out.println(mess);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isState() {
		return state;
	}
	public static void setState(boolean state) {
		Serveur.state = state;
	}
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		
		Serveur.setD(new Database("Ecole"));
		if(isState())
		{
			Serveur.setServerSocket(new ServerSocket(5000));
			
			try {
				Grammaire g = new Grammaire(Serveur.getD());
				while (true) {
					new MultiThread(serverSocket.accept(), server,Serveur.getD(),g);
				}
			} catch (Exception e) {
				setState(false);
			}
		}else {
			Serveur.getServerSocket().close();
			Serveur.setServerSocket(null);
		}
	}
	public static void run() throws IOException
	{
		Serveur.setState(true);
		MultiThread.setUse(true);
		
		if(isState()) {
			try {
				Grammaire g = new Grammaire(Serveur.getD());
				Serveur.setServerSocket(new ServerSocket(5000));
				while (true) {
					new MultiThread(Serveur.getServerSocket().accept(), server,Main.getD(),g);
				}
			} catch (Exception e) {
				
			}
		}else {
			Serveur.getServerSocket().close();
			Serveur.setServerSocket(null);
		}
	}
}
