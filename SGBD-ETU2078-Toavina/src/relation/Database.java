package relation;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class Database {
	String dbName;
	Vector<Relation> tabRelations;
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public Vector<Relation> getTabRelations() {
		return tabRelations;
	}
	public void setTabRelations(Vector<Relation> tabRelations) {
		this.tabRelations = tabRelations;
	}
	public Database(String name)
	{
		this.setDbName(name);
		if(Grammaire.DBexist(name)) {
			this.initFromFile();
		}
		else {
			this.setTabRelations(new Vector<Relation>());
			try {
				this.WriteToFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void initFromFile()
	{
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(this.getDbName()+".txt"));
			Vector<Relation> tabTemp = (Vector<Relation>)in.readObject();
			this.setTabRelations(tabTemp);	
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void WriteToFile() throws IOException
	{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.getDbName()+".txt",false));
		out.writeObject(this.getTabRelations());
		out.close();
	}
}
