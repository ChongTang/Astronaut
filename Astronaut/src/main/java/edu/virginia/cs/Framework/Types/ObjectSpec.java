package edu.virginia.cs.Framework.Types;

import java.util.ArrayList;
import java.util.HashMap;

import edu.virginia.cs.Synthesizer.Sig;

public class ObjectSpec {
	private String specPath = "";
	
	private ArrayList<String> ids= new ArrayList<String>();
	private ArrayList<String>  associations = null;
	private HashMap<String, String> typeList = null;
	private ArrayList<Sig>  sigs = null;
	
	public ArrayList<String> getIds() {
		return ids;
	}

	public void setIds(ArrayList<String> ids) {
		this.ids = ids;
	}

	public ArrayList<String> getAssociations() {
		return associations;
	}

	public void setAssociations(ArrayList<String> associations) {
		this.associations = associations;
	}

	public HashMap<String, String> getTypeList() {
		return typeList;
	}

	public void setTypeList(HashMap<String, String> typeList) {
		this.typeList = typeList;
	}

	public ArrayList<Sig> getSigs() {
		return sigs;
	}

	public void setSigs(ArrayList<Sig> sigs) {
		this.sigs = sigs;
	}

	public String getSpecPath(){
		return this.specPath;
	}
	
	public void setSpecPath(String path){
		this.specPath = path;
	}
}
