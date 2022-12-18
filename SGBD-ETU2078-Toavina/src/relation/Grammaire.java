package relation;


import java.io.*;


import java.util.Iterator;
import java.util.Vector;

import client.Main;
import serveur.MultiThread;
import serveur.Serveur;

public class Grammaire {
	Database database;
	Vector<Relation> tabRelations;
	static boolean state=true;
	public static boolean isState() {
		return state;
	}
	public static void setState(boolean state) {
		Grammaire.state = state;
	}
	public Database getDatabase() {
		return database;
	}
	public void setDatabase(Database database) {
		this.database = database;
	}
	public Vector<Relation> getTabRelations() {
		return tabRelations;
	}
	public void setTabRelations(Vector<Relation> tabRelations) {
		this.tabRelations = tabRelations;
	}
	public Grammaire(Database database) throws IOException, ClassNotFoundException {
		if(database==null)database = new Database("Ecole");
		this.setDatabase(database);
		this.setTabRelations(database.getTabRelations());
		initFromFile();
		//initRelation();
		//WriteToFile();
	}
	public void WriteToFile() throws IOException
	{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(getDatabase().getDbName()+".txt",false));
		out.writeObject(this.getTabRelations());
		out.close();
	}
	public static Vector<String> getAllDB()
	{
		 File directoryPath = new File("G:\\ITU\\Projet\\Algebre Relationnel\\Bdd"); //Chemin vers le dossier du projet pour mettre les databases créer
	      FilenameFilter textFilefilter = new FilenameFilter(){
	         public boolean accept(File dir, String name) {
	            String lowercaseName = name.toLowerCase();
	            if (lowercaseName.endsWith(".txt")) {
	               return true;
	            } else {
	               return false;
	            }
	         }
	      };
	      //List of all the text files
	      String filesList[] = directoryPath.list(textFilefilter);
	      Vector<String> tab = new Vector<String>();
	      for(String fileName : filesList) {
	    	  String res[] = fileName.split(".txt");
	    	  tab.add(res[0]);
	     }
	     return tab;
	}
	public static boolean DBexist(String db)
	{
		Vector<String> allDb = Grammaire.getAllDB();
		boolean res = false;
		for(String n:allDb) {
			if(n.compareTo(db)==0)
			{
				return true;
			}
		}
		return res;
	}
	@SuppressWarnings("deprecation")
	public void use(String db) throws Exception
	{
		
		if(DBexist(db))
		{
			Grammaire.setState(false);
			this.setDatabase(new Database(db));
			Main.setD(getDatabase());
			Serveur.setD(getDatabase());
			System.out.println("#"+this.getDatabase().getDbName());
			if(Serveur.getServerSocket()!=null)Serveur.getServerSocket().close();
			Serveur.setState(true);
			Serveur.run();
			Main.run(Main.getD());
			Grammaire.setState(true);
		}else {
			System.out.println("Database non existante");
		}
	}
	public void initFromFile() 
	{
		if(Grammaire.DBexist(getDatabase().getDbName())) {
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(getDatabase().getDbName()+".txt"));
				
				Vector<Relation> tabTemp = null;
				try {
					tabTemp = (Vector<Relation>)in.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
				}
				this.setTabRelations(tabTemp);	
				try {
					WriteToFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
				}finally {
					in.close();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
			}
			
			
		}
		
	}
	public void initRelation()
	{
		Relation r = new Relation();
		r.setNom("Relation1");
		//String[] col = {"id","nom","prenom"};
		Vector<String> col = new Vector<String>();
		col.add("id");
		col.add("nom");
		col.add("prenom");
		col.add("age");
		r.setColonne(col);
		
		Vector<Vector<Object>> ligne = new Vector<Vector<Object>>();
		Vector<Object> l1 = new Vector<Object>();
		l1.add(1);
		l1.add("Rakoto");
		l1.add("A");
		l1.add("13");
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
		l4.add(2);	
		l4.add("Rasoa");
		l4.add("C");
		l4.add("13");
		Vector<Object> l5 = new Vector<Object>();
		l5.add(1);	
		l5.add("Rakoto");
		l5.add("A");
		l5.add("15");
		
		ligne.add(l1);
		ligne.add(l2);
		ligne.add(l3);
		ligne.add(l4);
		ligne.add(l5);
		r.setDonne(ligne);
		
		Relation r2 = new Relation();
		r2.setNom("age");
		Vector<String> col2 = new Vector<String>();
		col2.add("age");
		r2.setColonne(col2);
		Vector<Vector<Object>> ligne2 = new Vector<Vector<Object>>();
		Vector<Object> l12 = new Vector<Object>();
		l12.add("13");
		Vector<Object> l22 = new Vector<Object>();
		l22.add("15");
		ligne2.add(l12);
		ligne2.add(l22);
		r2.setDonne(ligne2);
		
		this.setTabRelations(new Vector<Relation>());
		this.getTabRelations().add(r);
		this.getTabRelations().add(r2);
		
	}
	public void ExecuteQuery(String req) throws Exception {
		Grammaire g = new Grammaire(this.getDatabase());
		String[] requete = Grammaire.splitQuery(req);
		int indexOfString = 0;
		if(requete[indexOfString].compareTo("SELECT")==0)
		{
			indexOfString++;
			if(Grammaire.canContinue(requete, indexOfString))g.select(requete, indexOfString);
			else {
				System.out.println("Syntax non complete");
			}
		}
		else if(requete[indexOfString].compareTo("INSERT")==0) {
			indexOfString++;
			if(Grammaire.canContinue(requete, indexOfString))g.Insert(requete, indexOfString);
			else {
				System.out.println("Syntax non complete");
			}
		}
		else if(requete[indexOfString].compareTo("DELETE")==0) {
			indexOfString++;
			if(Grammaire.canContinue(requete, indexOfString))g.delete(requete, indexOfString);
			else {
				
				System.out.println("Syntax non complete");
			}
		}
		else if(requete[indexOfString].compareTo("UPDATE")==0) {
			indexOfString++;
			if(Grammaire.canContinue(requete, indexOfString))g.update(requete, indexOfString);
			else {
				
				System.out.println("Syntax non complete");
			}
		}
		else if(requete[indexOfString].compareTo("CREATE")==0) {
			indexOfString++;
			if(Grammaire.canContinue(requete, indexOfString))
				{
					if(requete[indexOfString].compareTo("TABLE")==0)
					{
						indexOfString++;
						g.CreateTable(requete, indexOfString);
					}else if(requete[indexOfString].compareTo("DATABASE")==0)
					{
						indexOfString++;
						g.CreateDatabase(requete, indexOfString);
					}
				}
			else {
				
				System.out.println("Syntax non complete");
			}
		}
		else if(requete[indexOfString].compareTo("DROP")==0) {
			indexOfString++;
			if(Grammaire.canContinue(requete, indexOfString))
				{
					if(requete[indexOfString].compareTo("TABLE")==0)
					{
						indexOfString++;
						g.DropRelation(requete, indexOfString);
					}
				}
			else {
				
				System.out.println("Syntax non complete");
			}
		}
		else if(requete[indexOfString].compareTo("SHOW")==0) {
			indexOfString++;
			if(Grammaire.canContinue(requete, indexOfString))
				{
					if(requete[indexOfString].compareTo("TABLES")==0)
					{
						
						indexOfString++;
						g.ShowTable();
					}else if(requete[indexOfString].compareTo("DATABASES")==0)
					{
						indexOfString++;
						g.ShowDatabase();
					}
					else {
						System.out.println("Syntax "+requete[indexOfString]+" non valide");
					}
				}
			else {
				
				System.out.println("Syntax non complete");
			}
		}
		else if(requete[indexOfString].compareTo("DESC")==0) {
			indexOfString++;
			if(Grammaire.canContinue(requete, indexOfString))g.Desc(requete, indexOfString);
			else {
				
				System.out.println("Syntax non complete");
			}
		}else if(requete[indexOfString].compareTo("USE")==0) {
			indexOfString++;
			if(Grammaire.canContinue(requete, indexOfString))g.use(requete[indexOfString]);
			else {
				
				System.out.println("Syntax non complete");
			}
		}
		else {
			System.out.println("Non reconnue");
		}
	}
	public void where(String[] req,int index,Relation relation,Vector<String> attr)
	{
		index++;
		if(Grammaire.canContinue(req, index))
		{
			String[] condition = splitCondition(req[index]);
			if(CheckAttribut(splitArgument(condition[0]),relation))
			{
				if(attr==null)
				{
					relation = relation.Selection(condition[0], condition[1]);
					relation.DisplayRelation();
				}else {
					
					relation = relation.Selection(condition[0], condition[1]);
					relation = relation.Projection(attr);
					relation.DisplayRelation();
				}
				
			}
			else {
				System.out.println("Attribut condition non valide");
			}
		}	
	}
	public static String[] splitQuery(String req)
	{
		String[] res = req.split(" ");
		
		return res;
	}
	public void From(String[] req,int index,Vector<String> attr)
	{
		index++;
		if(Grammaire.canContinue(req, index))
		{
			if(req[index].compareTo("FROM")==0)
			{
				index++;
				if(Grammaire.canContinue(req, index)) {
					if(ifTableExist(req[index]))
					{
	//Mametraka condition
						Relation relation = getRelationByName(req[index]);
						index++;
						if(Grammaire.canContinue(req, index))
						{
							if(req[index].compareTo("WHERE")==0)
							{
								where(req, index, relation, attr);
							}
							else if (req[index].compareTo("UNION")==0) {
								union(req, index, relation, attr);
							}
							else if (req[index].compareTo("INTERSECT")==0) {
								intersection(req, index, relation, attr);
							}
							else if (req[index].compareTo("DIFFERENT")==0) {
								difference(req, index, relation, attr);
							}
							else if (req[index].compareTo("DIVISE")==0) {
								division(req, index, relation, attr);
							}
							else if (req[index].compareTo("PRODUIT")==0) {
								produit(req, index, relation, attr);
							}
							else if (req[index].compareTo("JOIN")==0) {
								join(req, index, relation, attr);
							}
							else if (req[index].compareTo("ORDER")==0) {
								if(canContinue(req, index+1))
								{
									index++;
									if(req[index].compareTo("BY")==0)
									{
										if(canContinue(req, index+1))
										{
											index++;
											Order(req, index, relation,attr);
										}
									}
								}
							}
							
						}
						else{
							if(attr!=null)
							{
								relation = relation.Projection(attr);
							}
							relation.DisplayRelation();
						}
					}
					else {
						System.out.println("La table "+req[index]+" n'existe pas");
					}
				}
				
			}
			else {
				System.out.println("Invalid syntax next to"+req[index-1]);
			}
		}
	}
	public void join(String[] req, int index, Relation relation, Vector<String> attr) {
		// TODO Auto-generated method stub
		index++;
		if(Grammaire.canContinue(req, index))
		{
			if(ifTableExist(req[index]))
			{
				Relation second = getRelationByName(req[index]);
				if(relation!=second)
				{
					
					if(Grammaire.canContinue(req, index+1))
					{
						index++;
						if(req[index].compareTo("ON")==0)
						{
							if(Grammaire.canContinue(req, index+1))
							{
								index++;
								String[] condition = splitCondition(req[index]);
								if(Grammaire.isAttribut(condition[0], relation)&& Grammaire.isAttribut(condition[1], second))
								{
									Relation r = relation.join(second,condition[0],condition[1]);

									if(attr!=null)
									{
										r = r.Projection(attr);
									}
									r.DisplayRelation();
								
								}
								else {
									System.out.println("Attribut non valide");
								}
							}
						}
					}
					
				}else {
					System.out.println("Meme relation");
				}
			}
			else {
				System.out.println("Table "+req[index]+" non valide");
			}
		}
	}
	public void intersection(String[] req,int index,Relation relation,Vector<String> attr)
	{
		index++;
		if(Grammaire.canContinue(req, index))
		{
			if(ifTableExist(req[index]))
			{
				Relation second = getRelationByName(req[index]);
				if(relation!=second)
				{
					Relation r = relation.Intersection(second);
					if(Grammaire.canContinue(req, index+1))
					{
						index++;
						if(req[index].compareTo("WHERE")==0)
						{
							where(req, index, r, attr);
						}
					}
					else {
						if(attr!=null)
						{
							r = r.Projection(attr);
						}
						r.DisplayRelation();
					}
				}else {
					System.out.println("Meme relation");
				}
			}
			else {
				System.out.println("Table "+req[index]+" non valide");
			}
		}
	}
	public void difference(String[] req,int index,Relation relation,Vector<String> attr)
	{
		index++;
		if(Grammaire.canContinue(req, index))
		{
			if(ifTableExist(req[index]))
			{
				Relation second = getRelationByName(req[index]);
				if(relation!=second)
				{
					Relation r = relation.diff(second);
					if(Grammaire.canContinue(req, index+1))
					{
						index++;
						if(req[index].compareTo("WHERE")==0)
						{
							where(req, index, r, attr);
						}
					}
					else {
						if(attr!=null)
						{
							r = r.Projection(attr);
						}
						r.DisplayRelation();
					}
				}else {
					System.out.println("Meme relation");
				}
			}
			else {
				System.out.println("Table "+req[index]+" non valide");
			}
		}
		
	}
	public void produit(String[] req,int index,Relation relation,Vector<String> attr)
	{
		index++;
		if(Grammaire.canContinue(req, index))
		{
			if(ifTableExist(req[index]))
			{
				Relation second = getRelationByName(req[index]);
				if(relation!=second)
				{
					Relation r = relation.produit(second);
					if(Grammaire.canContinue(req, index+1))
					{
						index++;
						if(req[index].compareTo("WHERE")==0)
						{
							where(req, index, r, attr);
						}
					}
					else {
						if(attr!=null)
						{
							r = r.Projection(attr);
						}
						r.DisplayRelation();
					}
				}else {
					System.out.println("Meme relation");
				}
			}
			else {
				System.out.println("Table "+req[index]+" non valide");
			}
		}
		
	}
	public void division(String[] req,int index,Relation relation,Vector<String> attr)
	{
		index++;
		if(Grammaire.canContinue(req, index))
		{
			if(ifTableExist(req[index]))
			{
				Relation second = getRelationByName(req[index]);
				if(relation!=second)
				{
					System.out.println("haha");
					Relation r = relation.division(second);
					if(Grammaire.canContinue(req, index+1))
					{
						index++;
						if(req[index].compareTo("WHERE")==0)
						{
							where(req, index, r, attr);
						}
					}
					else {
						if(attr!=null)
						{
							r = r.Projection(attr);
						}
						r.DisplayRelation();
					}
					
				}else {
					System.out.println("Meme relation");
				}
			}
			else {
				System.out.println("Table "+req[index]+" non valide");
			}
		}
		
	}
	public void union(String[] req,int index,Relation relation,Vector<String> attr)
	{
		index++;
		if(Grammaire.canContinue(req, index))
		{
			if(ifTableExist(req[index]))
			{
				Relation second = getRelationByName(req[index]);
				if(relation!=second && relation.getColonne().size()==second.getColonne().size())
				{
					Relation r = relation.Union(second);
					if(Grammaire.canContinue(req, index+1))
					{
						index++;
						if(req[index].compareTo("WHERE")==0)
						{
							where(req, index, r, attr);
						}
					}
					else {
						if(attr!=null)
						{
							r = r.Projection(attr);
						}
						r.DisplayRelation();
					}
				}else {
					System.out.println("Meme relation");
				}
			}
			else {
				System.out.println("Table "+req[index]+" non valide");
			}
		}
	}
	public void select(String[]req,int index)
	{
		if(Grammaire.canContinue(req, index)) {
			if(req[index].compareTo("*")==0)
			{
				From(req, index, null);
			}
			else {
				Vector<String> attr = this.splitArgument(req[index]);
				From(req, index, attr);
	 		}
		}
		
	}
	public boolean ifTableExist(String table)
	{
		boolean res = false;
		for(int i=0;i<this.getTabRelations().size();i++)
		{
			if(this.getTabRelations().get(i).getNom().compareTo(table)==0)
			{
				res=true;
				break;
			}
		}
		return res;
	}
	public Relation getRelationByName(String name)
	{
		Relation res = null;
		if(ifTableExist(name))
		{
			for(int i=0;i<this.getTabRelations().size();i++)
			{
				if(this.getTabRelations().get(i).getNom().compareTo(name)==0)
				{
					res = this.getTabRelations().get(i);
					break;
				}
			}
		}
		return res;
	}
	public Vector<String> splitArgument(String argument)
	{
		String[] arg = argument.split(",");
		Vector<String> temp = new Vector<String>();
		for(int i=0;i<arg.length;i++)
		{
			temp.add(arg[i]);
		}
		return temp;
	}
	public boolean CheckAttribut(Vector<String> att,Relation r)
	{
		int count=0;
		boolean res = false;
		for(int i=0;i<r.getColonne().size();i++)
		{
			for(int j=0;j<att.size();j++)
			{
				if(att.get(j).compareTo(r.getColonne().get(i))==0)
				{
					count++;
				}
			}
		}
		if(count==att.size())
		{
			return true;
		}
		else {
			return false;
		}
	}
	public String[] splitCondition(String condition)
	{
		String[] res = condition.split("=");
		return res;
	}
	public static boolean canContinue(String[] req,int index)
	{
		if(index<req.length)
		{
			return true;
		}
		else {
			System.out.println("Syntax terminï¿½");
			return false;
		}
	}
	//insertion de donne
	public void AppendToTab(Relation relation,Vector<String> tabValues) throws IOException
	{
		Vector<Object> tab = new Vector<Object>();
		for(int i=0;i<tabValues.size();i++)
		{
			tab.add(tabValues.get(i));
		}
		relation.getDonne().add(tab);
		System.out.println("Ligne inserï¿½(1)");
		this.WriteToFile();
	}
	//insertion
	public void Insert(String[] req,int index) throws IOException
	{
		if(req[index].compareTo("INTO")==0)
		{
			if(Grammaire.canContinue(req, index+1))
			{
				index++;
				if(ifTableExist(req[index]))
				{
					Relation relation = this.getRelationByName(req[index]);
					if(Grammaire.canContinue(req, index+1))
					{
						index++;
						if(req[index].compareTo("VALUES")==0)
						{
							if(Grammaire.canContinue(req, index+1))
							{
								index++;
								Vector<String> tabValues = this.splitArgument(req[index]);
								if(tabValues.size()==relation.getColonne().size())
								{
									AppendToTab(relation, tabValues);
								}
								else {
									System.out.println("manque d'argument");
								}
							}
						}
					}
					else {
						System.out.println("syntax non complete");
					}
				}
				else {
					System.out.println("La table "+req[index]+" n'existe pas");
				}
			}
			else {
				System.out.println("syntax non complete");
			}
		}else {
			System.out.println("Syntax"+req[index]+" non valide");
		}
	}
	public void delete(String[] req,int index) throws IOException
	{
		if(req[index].compareTo("FROM")==0)
		{
			if(Grammaire.canContinue(req, index+1))
			{
				
				index++;
				if(ifTableExist(req[index]))
				{
					Relation r = getRelationByName(req[index]);
					if(canContinue(req, index+1))
					{
						index++;
						if(req[index].compareTo("WHERE")==0)
						{
							if(canContinue(req, index+1))
							{
								index++;
								String[] con = this.splitCondition(req[index]); 
								DeleteToTab(r, con);
								System.out.println("Ligne(s) supprimï¿½(s)");
							}
						}
					}
				}
			}
		}
	}
	public int getIndexOfColumn(Relation r,String col)
	{
		int res = 0;
		for(int i=0;i<r.getColonne().size();i++)
		{
			if(r.getColonne().get(i).compareTo(col)==0)
			{
				res = i;
			}
		}
		return res;
	}
	public void RemakeVector(int index,Vector<Vector<Object>> vect)
	{
		for(int i=index;i<vect.size();i++)
		{
			if(i!=vect.size()-1)
			{
				vect.set(i, vect.get(i+1));
			}
		}
		vect.remove(vect.size()-1);
	}
//  !!Lasa manala tsirairay
	public void DeleteToTab(Relation relation,String[] condition) throws IOException
	{
		Vector<Integer> indice = new Vector<Integer>();
		int col = getIndexOfColumn(relation, condition[0]);
		for(int i=0;i<relation.getDonne().size();i++)
		{
			int br = 0;
			if(!indice.contains(i))
			{
				for(int j=0;j<relation.getDonne().get(i).size();j++)
				{
					if(j==col)
					{
						if(relation.getDonne().get(i).get(j).toString().compareTo(condition[1])==0)
						{
							indice.add(i);
						}
					}
				}
			}
			
		}
		for(int i=0;i<indice.size();i++)
		{
			relation.getDonne().removeElementAt(indice.get(i)-i);
		}
		System.out.println("Nombre de ligne supprimï¿½: "+indice.size());
		this.WriteToFile();
	}
	public static boolean isAttribut(String att,Relation r)
	{
		boolean res = false;
 		for(int i=0;i<r.getColonne().size();i++)
 		{
 			if(r.getColonne().get(i).compareTo(att)==0)
 			{
 				res=true;
 				break;
 			}
 		}
 		return res;
	}
	public void UpdateInTable(Relation r,String[] changement,String[]condition) throws IOException
	{
		int col = getIndexOfColumn(r,changement[0]);
		int colCon = getIndexOfColumn(r, condition[0]);
		int count=0;
		for(int i=0;i<r.getDonne().size();i++)
		{
			for(int j=0;j<r.getDonne().get(i).size();j++)
			{
				if(j==colCon)
				{
					if(condition[1].compareTo(r.getDonne().get(i).get(j).toString())==0)
					{
						r.getDonne().get(i).set(col, changement[1]);
						count++;
					}
				}
			}
		}
		System.out.println("Ligne modifiï¿½: "+count);
		this.WriteToFile();
	}
	public void update(String[] req,int index) throws IOException
	{
		if(ifTableExist(req[index]))
		{
			Relation r = this.getRelationByName(req[index]);
			if(canContinue(req, index+1))
			{
				index++;
				if(req[index].compareTo("SET")==0)
				{
					if(canContinue(req, index+1))
					{
						index++;
						String[] changement = splitCondition(req[index]);
						if(isAttribut(changement[0], r))
						{
							if(canContinue(req, index+1))
							{
								index++;
								if(req[index].compareTo("WHERE")==0)
								{
									if(canContinue(req, index+1))
									{
										index++;
										String[] condition = splitCondition(req[index]);
										if(isAttribut(condition[0], r))
										{
											this.UpdateInTable(r, changement, condition);
										}
									}
									
								}
								else {
									System.out.println("Syntax "+req[index]+" non reconnue");
								}
							}
								
						}
					}
				}
				else {
					System.out.println("Syntax "+req[index]+" non reconnue");
				}
			}
		}
		else {
			System.out.println("Table "+req[index]+" non reconnue");
		}
	}
	//CREER TABLE
	public void CreateTable(String[] req,int index) throws IOException
	{
		
		if(ifTableExist(req[index]))
		{
			System.out.println("La table "+req[index]+" existe dï¿½ja");
		}
		else {
			Relation relation =new Relation();
			relation.setNom(req[index]);
			Vector<Object> obj = new Vector<Object>();
			Vector<Vector<Object>> vect = new Vector<Vector<Object>>();
			relation.setDonne(vect);
			if(canContinue(req, index+1))
			{
				index++;
				Vector<String> col = this.splitArgument(req[index]);
				relation.setColonne(col);
				this.getTabRelations().add(relation);
				this.WriteToFile();
			}
		}
	}
	//CREATE DATABASE
	public void CreateDatabase(String[] req,int index) throws IOException
	{
		
		Database d = new Database(req[index]);
		
	}
	//DROP TABLE
	public void DropRelation(String[] req,int index) throws IOException
	{
		if(ifTableExist(req[index]))
		{
			Relation r = getRelationByName(req[index]);
			int res=0;
			for(int i =0;i<this.getTabRelations().size();i++)
			{
				if(this.getTabRelations().get(i).getNom().compareTo(r.getNom())==0)
				{
					res = i;
				}
			}
			System.out.println("La table "+req[index]+" a ete supprimï¿½");
			this.getTabRelations().removeElementAt(res);
			this.WriteToFile();
		}
		else {
			System.out.println("La table "+req[index]+" n'existe pas");
		}
	}
	//SHOW TABLE
	public void ShowTable()
	{
		System.out.println("eo a");
		
		for(int i=0;i<this.getTabRelations().size();i++)
		{
			System.out.println(this.getTabRelations().get(i).getNom());
			System.out.println("--------");
		}
	}
	public void ShowDatabase()
	{
		
		for(int i=0;i<Grammaire.getAllDB().size();i++)
		{
			System.out.println(Grammaire.getAllDB().get(i));
			System.out.println("--------");
		}
		System.out.println(" ( "+Grammaire.getAllDB().size()+" ) lignes selectionnÃ©s");
	}
	//DESC
	public void Desc(String[] req,int index)
	{
		if(ifTableExist(req[index]))
		{
			Relation relation = getRelationByName(req[index]);
			for(int i=0;i<relation.getColonne().size();i++)
			{
				System.out.print(relation.getColonne().get(i)+"  |  ");
				
			}
		}
		System.out.println();
	}
	//Order by
	public void Order(String[] req,int index,Relation r,Vector<String> attr)
	{
		if(isAttribut(req[index], r))
		{
			String att = req[index];
			if(canContinue(req, index+1))
			{
				index++;
				String action = req[index];
				Vector<Vector<Object>> temporaire = r.getDonne();
				Vector<Object> temp = null;
				int col = getIndexOfColumn(r,att);
				int b = 0;
				for(int i=0;i<temporaire.size();i++)
				{
					for(int j=i+1;j<temporaire.size();j++)
					{
						if(action.compareTo("ASC")==0)
						{
							if(Integer.parseInt(temporaire.get(i).get(col).toString()) > Integer.parseInt(temporaire.get(j).get(col).toString()))
							{
								temp=temporaire.get(i);
								temporaire.set(i, temporaire.get(j));
								temporaire.set(j, temp);
							}
						}else if (action.compareTo("DESC")==0) {
							temp=temporaire.get(i);
							temporaire.set(i, temporaire.get(j));
							temporaire.set(j, temp);
						}else {
							System.out.println(req[index]+" non reconnue");
							b=1;
							break;
						}
						
					}
					if(b==1)
					{
						break;
					}
				}
				Relation rtemp = new Relation();
				rtemp.setColonne(r.getColonne());
				rtemp.setDonne(temporaire);
				if(attr!=null)
				{
					rtemp = rtemp.Projection(attr);
				}
				
				rtemp.DisplayRelation();
			}
			else {
				System.out.println("Non terminï¿½");
			}
		}
	}
}
