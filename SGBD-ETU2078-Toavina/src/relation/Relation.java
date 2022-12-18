package relation;

import java.io.Serializable;

import java.security.DrbgParameters.NextBytes;
import java.util.Vector;


public class Relation implements Serializable{
	String nom;
	Vector<String> colonne;
	Vector<Vector<Object>> donne;
	
	// Getters and Setters
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Vector<String> getColonne() {
		return colonne;
	}
	public void setColonne(Vector<String> colonne) {
		this.colonne = colonne;
	}
	public Vector<Vector<Object>> getDonne() {
		return donne;
	}
	public void setDonne(Vector<Vector<Object>> donne) {
		this.donne = donne;
	}
	//Constructor
	public Relation() {
		
	}
	// Methods
	
	public Relation Selection(String attCondition,Object valCondition)
	{
		Relation res = new Relation();
		res.setNom(this.getNom());
		res.setColonne(this.getColonne());
		Vector<Vector<Object>> ligne = new Vector<Vector<Object>>();
		for(int i=0;i<this.getDonne().size();i++)
		{
			Vector<Object> temp = new Vector<Object>();
			for(int j=0;j<this.getDonne().get(i).size();j++)
			{
				for(int k=0;k<this.getColonne().size();k++)
				{
					if(this.getColonne().get(k).compareTo(attCondition)==0)
					{
						if((valCondition.toString()).compareTo(this.getDonne().get(i).get(j).toString())==0)
						{ 
							ligne.add(this.getDonne().get(i));
						}
					}
				}		
			}	
		}
		res.setDonne(ligne);
		return res;
	}
	public Relation Projection(Vector<String> attribut)
	{
		Relation res = new Relation();
		res.setNom(this.getNom());
		res.setColonne(attribut);
		Vector<Vector<Object>> ligne = new Vector<Vector<Object>>();
		for(int i=0;i<this.getDonne().size();i++)
		{
			Vector<Object> temp = new Vector<Object>();
			for(int j=0;j<this.getDonne().get(i).size();j++)
			{
				for(int k=0;k<res.getColonne().size();k++)
				{
					if(this.getColonne().get(j).compareTo(res.getColonne().get(k))==0)
					{
						temp.add(this.getDonne().get(i).get(j));
					}
				}
			}
			ligne.add(temp);
		}
		res.setDonne(ligne);
		return res;
	}
	public Relation Union(Relation r)
	{
		Relation res = new Relation();
		res.setNom(this.getNom()+r.getNom());
		res.setColonne(this.getColonne());
		Vector<Vector<Object>> ligne = new Vector<Vector<Object>>();
		ligne = this.getDonne();
		for(int i=0;i<r.getDonne().size();i++)
		{
			ligne.add(r.getDonne().get(i));
		}
		res.setDonne(ligne);
		return res;
	}
	
	public Relation Intersection(Relation r)
	{
		Relation res = new Relation();
		res.setNom(nom);
		res.setColonne(this.getColonne());
		Vector<Vector<Object>> ligne = new Vector<Vector<Object>>(); 
		for(int i=0;i<this.getDonne().size();i++)
		{
			int count = 0;
			for(int j=0;j<r.getDonne().size();j++)
			{
				for(int k=0;k<this.getDonne().get(i).size();k++)
				{
					for(int l=0;l<r.getDonne().get(j).size();l++)
					{
						if(this.getDonne().get(i).get(k).toString().compareTo(r.getDonne().get(j).get(l).toString())==0)
						{
							count++;
						}
					}
				}
			}
			if(count==this.getColonne().size())
			{
				ligne.add(this.getDonne().get(i));
			}
		}
//		for(int i=0;i<r.getDonne().size();i++)
//		{
//			for(int j=0;j<r.getDonne().get(i).size();j++)
//			{
//				
//			}
//		}
		res.setDonne(ligne);
		return res;
	}
	public int getIndexOfAtt(String att)
	{
		int res = 0;
		for(int i=0;i<this.getColonne().size();i++)
		{
			if(getColonne().get(i).compareTo(att)==0)
			{
				res = i;
				break;
			}
		}
		return res;
	}
	public Relation join(Relation r,String att1,String att2)
	{
		Relation res = new Relation();
		res.setNom(this.getNom()+r.getNom());
		Vector<String> col =concat(getColonne(), r.getColonne());
		res.setColonne(col);
		Vector<Vector<Object>> ligne = new Vector<Vector<Object>>();
		int col1 = this.getIndexOfAtt(att1);
		int col2 = r.getIndexOfAtt(att2);
		for(int i=0;i<this.getDonne().size();i++)
		{
			for(int j=0;j<r.getDonne().size();j++)
			{
				if(getDonne().get(i).get(col1).toString().compareTo(r.getDonne().get(j).get(col2).toString())==0)
				{
					Vector<Object> temp = concatObj(getDonne().get(i),r.getDonne().get(j));
					ligne.add(temp);
				}
			}
		}
		res.setDonne(ligne);
		return res;
	}
	public Relation Difference(Relation r)
	{
		Relation res = new Relation();
		res.setNom(this.getNom());
		res.setColonne(this.getColonne());
		Vector<Vector<Object>> ligne = new Vector<Vector<Object>>(); 
		for(int i=0;i<this.getDonne().size();i++)
		{
			int count = 0;
			for(int j=0;j<r.getDonne().size();j++)
			{
				for(int k=0;k<this.getDonne().get(i).size();k++)
				{
					for(int l=0;l<r.getDonne().get(j).size();l++)
					{
						if(this.getDonne().get(i).get(k).toString().compareTo(r.getDonne().get(j).get(l).toString())==0)
						{
							count++;
						}
					}
				}
			}
			System.out.println(count+"c");
			if(count!=this.getColonne().size())
			{
				ligne.add(this.getDonne().get(i));	
			}
		}
		res.setDonne(ligne);
		return res;
	}
	public Relation diff(Relation r)
	{
		Relation res = new Relation();
		res.setNom(this.getNom());
		res.setColonne(this.getColonne());
		Vector<Vector<Object>> ligne = new Vector<Vector<Object>>(); 
		for(int i=0;i<this.getDonne().size();i++)
		{
			
			
				if(!r.getDonne().contains(this.getDonne().get(i)))
				{
					ligne.add(this.getDonne().get(i));
						
				}
			
		}
		res.setDonne(ligne);
		return res;
	}
	public static boolean compareVector(Vector<Object> v1,Vector<Object> v2)
	{
		int count=0;
		for(int i=0;i<v1.size();i++)
		{
			if(v2.get(i).toString().compareTo(v1.get(i).toString())==0)
				{
					
					count++;
				}
		}
		
		if(count==v1.size())
		{
			return true;
		}else {
			return false;
		}	
	}
	//Division
	public Relation Division(Relation r)
	{
		Relation res = new Relation();
		res.setNom(nom);
		res.setColonne(this.getColonne());
		Vector<Vector<Object>> ligne = new Vector<Vector<Object>>(); 
		Relation temp = this.Intersection(r);
		for(int i=0;i<temp.getDonne().size();i++) 
		{
			for(int j=0;j<temp.getDonne().get(i).size();j++)
			{
				int count =0;
				for(int k=0;k<r.getDonne().size();k++)
				{
					for(int l=0;l<r.getDonne().get(k).size();l++)
					{
						if(temp.getDonne().get(i).get(j).toString().compareTo(r.getDonne().get(k).get(l).toString())==0)
						{
							count++;
						}
					}
				}
				if(count==r.getDonne().size())
				{
					ligne.add(temp.getDonne().get(i));
					continue;
				}
			}
			
		}
		res.setDonne(ligne);
		return res;
	}
	//public void concat 
	public Vector<String> concat(Vector<String> col1,Vector<String> col2)
	{
		Vector<String> res = col1;
		for(int i=0;i<col2.size();i++)
		{
			res.add(col2.get(i));
		}
		return res;
	}
	public Relation produit(Relation r)
	{
		Relation res = new Relation();
		res.setNom(getNom()+" et "+r.getNom());
		Vector<String> col =concat(getColonne(), r.getColonne());
		res.setColonne(col);
		Vector<Vector<Object>> ligne = new Vector<Vector<Object>>();
		for(int i=0;i<this.getDonne().size();i++)
		{
			for(int j=0;j<r.getDonne().size();j++)
			{	
				Vector<Object> temp = concatObj(getDonne().get(i),r.getDonne().get(j));
			
				ligne.add(temp);
			}
		}
		
		res.setDonne(ligne);
		
		return res;
	}
	public static Vector<Object> concatObj(Vector<Object> donne2, Vector<Object> donne3) {
		 Vector<Object> res = new Vector<Object>();
		 res.addAll(donne2);
		 res.addAll(donne3);
		 return res;
	}
	//colonne not in autre relation
	public Vector<String> DiffOfCol(Relation r)
	{
		Vector<String> res = new Vector<String>();
		for(int i=0;i<this.getColonne().size();i++)
		{
			if(!r.getColonne().contains(this.getColonne().get(i)))
			{
				res.add(this.getColonne().get(i));
			}
		}
		return res;
	}
	//division

	public Relation division(Relation r)
	{
		Vector<String> diffCol = this.DiffOfCol(r);
		Relation t1 = this.Projection(diffCol);		
		Relation pd = t1.produit(r);
		Relation df = pd.diff(this);
		diffCol = this.DiffOfCol(r);
		Relation t2 = df.Projection(diffCol);
		Relation res = t1.diff(t2);
		res.setColonne(diffCol);
		
		return res;
		
	}
	//Natural Join
	public Relation Jointure(Relation r)
	{
		Relation res = new Relation();  
		return new Relation();
	}
	//INSERT 
	
	public void DisplayRelation()
	{
		for(int i=0;i<this.getColonne().size();i++)
		{
			System.out.print(this.getColonne().get(i)+"    |    ");
		}
		//this.setDonne(Relation.manalaDoublon(donne));
		Relation.manalaDoublon(this.getDonne());
		System.out.println(" ");
		System.out.print("--------------------");
		System.out.println(" ");
		for(int i=0;i<this.getDonne().size();i++)
		{
			if(this.getDonne().get(i)!=null)
			{
				for(int j=0;j<this.getDonne().get(i).size();j++)
				{
					System.out.print(this.getDonne().get(i).get(j)+"    |    ");
				}
				System.out.println(" ");
				System.out.print("-----------------");
				System.out.println(" ");
			}
			
		}
		System.out.println("Ligne selectionné:"+this.getDonne().size());
	}
	
	public static Vector<Vector<Object>> manalaDoublon(Vector<Vector<Object>> tabDonne)
	{
		for(int i = tabDonne.size()-1;i>=0;i--){
			  for(int j = tabDonne.size()-1;j>i;j--){
				
				if(Relation.compareVector(tabDonne.get(i), tabDonne.get(j))) {
					
			        tabDonne.remove(j);
			    }
			}
		}
		return tabDonne;
	}
	
}
