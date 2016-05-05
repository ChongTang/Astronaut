package edu.virginia.cs.Framework.Types;

import java.util.ArrayList;

public class ObjectSet {
	private ArrayList<ObjectOfDM> objSet = null;
	
	public ObjectSet(){
		this.objSet = new ArrayList<ObjectOfDM>();
	}
	
	public ObjectSet(ArrayList<ObjectOfDM> objSet){
		this.objSet = objSet;
	}
	
	public ArrayList<ObjectOfDM> getObjSet(){
		return this.objSet;
	}
	
	public void setObjSet(ArrayList<ObjectOfDM> objSet){
		this.objSet = objSet;
	}
}
