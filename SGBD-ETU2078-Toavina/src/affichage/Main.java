package affichage;
import java.awt.Color;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import Design.Couleur;
import relation.*;
public class Main {
	public static Database d;
	public static Database getD() {
		return d;
	}
	public static void setD(Database d) {
		Main.d = d;
	}
	public static void main(String[] args) throws Exception
	{
//		Relation r = new Relation();
//		r.setNom("Relation 1");
//		//String[] col = {"id","nom","prenom"};
//		Vector<String> col = new Vector<String>();
//		col.add("id");
//		col.add("nom");
//		col.add("prenom");
//		col.add("age");
//		r.setColonne(col);
//		
		Vector<Vector<Object>> ligne = new Vector<Vector<Object>>();
		Vector<Object> l1 = new Vector<Object>();
		l1.add(3);
		l1.add("Rabe");
		l1.add("B");
		l1.add("15");
		Vector<Object> l2 = new Vector<Object>();
		l2.add(3);	
		l2.add("Rabe");
		l2.add("B");
		l2.add("15");
		Vector<Object> l3 = new Vector<Object>();
		l3.add(2);	
		l3.add("Rasoa");
		l3.add("C");
		l3.add("15");
		Vector<Object> l4 = new Vector<Object>();
		l4.add(3);	
		l4.add("Rabe");
		l4.add("B");
		l4.add("15");
		Vector<Object> l5 = new Vector<Object>();
		l5.add(1);	
		l5.add("Rakoto");
		l5.add("A");
		l5.add("15");
//		
		ligne.add(l1);
		ligne.add(l2);
		ligne.add(l3);
		ligne.add(l4);
		ligne.add(l5);
//		r.setDonne(ligne);
////		
//		Relation r2 = new Relation();
//		r2.setNom("age");
//		Vector<String> col2 = new Vector<String>();
//		col2.add("age");
//		r2.setColonne(col2);
//		Vector<Vector<Object>> ligne2 = new Vector<Vector<Object>>();
//		Vector<Object> l12 = new Vector<Object>();
//		l12.add("13");
//		Vector<Object> l22 = new Vector<Object>();
//		l22.add("15");
//		ligne2.add(l12);
//		ligne2.add(l22);
//		r2.setDonne(ligne2);
		
		//System.out.println(r.DiffOfCol(r2));
//		Relation r2 = new Relation();
//		r.setNom("Relation 2");
//		//String[] col = {"id","nom","prenom"};
//		Vector<String> col2 = new Vector<String>();
//		col2.add("id");
//		col2.add("nom");
//		col2.add("prenom");
//		r2.setColonne(col2);
//		Vector<Vector<Object>> ligne2 = new Vector<Vector<Object>>();
//		Vector<Object> l12 = new Vector<Object>();
//		l12.add(1);
//		l12.add("Rakoto");
//		l12.add("A");
//		Vector<Object> l22 = new Vector<Object>();
//		l22.add(2);
//		l22.add("Rabe");
//		l22.add("C");
//		ligne2.add(l12);
//		ligne2.add(l22);
//		r2.setDonne(ligne2);
//		
//		Vector<String> att = new Vector<String>();
//		att.add("id");
//		att.add("nom");
//		Vector<String> att = new Vector<String>();
//		att.add("age");
//		//Relation res = r2.Projection(att);
//		//Relation res = r2.Selection("prenom", "C");
//		Relation res = r.produit(r2);
//		System.out.println(res.getDonne());
		//Relation res = r.division(r2);
		//Relation res = r.join(r2,"id","id");
		//Vector<String> diff = r.DiffOfCol(r2);
		//System.out.println(diff);
		//Relation res = r.Intersection(r2);
		//Relation res = r2.Difference(r);
//		for(int i=0;i<res.getDonne().size();i++)
//		{
//			for(int j=0;j<res.getDonne().get(i).size();j++)
//			{
//				System.out.println(res.getDonne().get(i).get(j));
//			}
//		}
//		Vector<Object> temp = Relation.concatObj(l5, l22);
//		System.out.println(temp);
		//res.DisplayRelation();
		//g.ExecuteQuery("INSERT INTO Relation4 VALUES 1,3,DEPT13");
		//g.ExecuteQuery("UPDATE Relation2 SET nom=Rasoa WHERE id=2");
		//g.ExecuteQuery("DELETE FROM Relation2 WHERE prenom=C");
//		g.ExecuteQuery("CREATE TABLE Relation4 id,mgr,dept");
//		g.ExecuteQuery("SHOW TABLE");
//		g.ExecuteQuery("SELECT * FROM Relation2");
		Database d = new Database("Ecole");
		Main.setD(d);
		Grammaire g = new Grammaire(Main.getD());
		boolean etat = true;
		System.out.println( "Base de donné "+d.getDbName());
		while(Grammaire.isState())
		{
			Scanner sc = new Scanner(System.in);
			System.out.print(">>>>>");
			g.ExecuteQuery(sc.nextLine());
		}
		
		//Grammaire.getAllDB();
//		Vector<Vector<Object>> vect = Relation.manalaDoublon(ligne);
//		System.out.println(vect);
	}
	public static void run() throws Exception
	{
		Grammaire g = new Grammaire(Main.getD());
		Grammaire.setState(true);
		while(Grammaire.isState())
		{
			Scanner sc = new Scanner(System.in);
			System.out.print(">>>>>");
			g.ExecuteQuery(sc.nextLine());
		}
	}
}
