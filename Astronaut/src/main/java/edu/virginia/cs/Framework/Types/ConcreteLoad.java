package edu.virginia.cs.Framework.Types;

import java.util.ArrayList;

public class ConcreteLoad {
	private String insertPath = "";
	private String selectPath = "";
	
	private ArrayList<ConcreteQuery> querySet = null;

	public ConcreteLoad() {
		this.querySet = new ArrayList<ConcreteQuery>();
	}

	public ConcreteLoad(ArrayList<ConcreteQuery> querySet) {
		this.querySet = querySet;
	}

	public ArrayList<ConcreteQuery> getQuerySet() {
		return this.querySet;
	}

	public void setQuerySet(ArrayList<ConcreteQuery> querySet) {
		this.querySet = querySet;
	}

	public String getInsertPath() {
		return insertPath;
	}

	public void setInsertPath(String insertPath) {
		this.insertPath = insertPath;
	}

	public String getSelectPath() {
		return selectPath;
	}

	public void setSelectPath(String selectPath) {
		this.selectPath = selectPath;
	}

}
