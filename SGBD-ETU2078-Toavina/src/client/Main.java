package client;
import java.io.IOException;
import java.util.Scanner;


import relation.Database;
import relation.Grammaire;
public class Main {
    public static Database d;
    static Client client=new Client("localhost",5000);
	public static Database getD() {
		return d;
	}
	public static void setD(Database d) {
		Main.d = d;
	}
    public static void main(String[] args) throws Exception {
    	
    	Database d = new Database("Ecole");
		if(Main.getD()==null)Main.setD(d);
        Grammaire g = new Grammaire(Main.getD());
		Grammaire.setState(true);
		while(Grammaire.isState())
		{
			Scanner sc = new Scanner(System.in);
			System.out.print(">>>>>");
			String req = sc.nextLine();
			String reponse=client.sendMessage(req);
            if(reponse.equals("sortie")){break;}
            g.ExecuteQuery(req);
		}
    }
    public static void run(Database d) throws Exception
	{
    	Main.setD(d);
		Grammaire g = new Grammaire(Main.getD());
		
		Grammaire.setState(true);
		while(Grammaire.isState())
		{
			Scanner sc = new Scanner(System.in);
			System.out.print(">>>>>");
			String req = sc.nextLine();
			String reponse=client.sendMessage(req);
            if(reponse.equals("exit")){break;}
            g.ExecuteQuery(req);
		}
	}
}
