package edu.virginia.cs.Framework.Types;

/**
 * @author tang
 * AbstractQuery is a pair of action and object 
 */
public class AbstractQuery {
	
	public enum Action {
	    INSERT, SELECT, UPDATE 
	}
	
	private Action action = null;
	private ObjectOfDM oodm = null;
	
	public AbstractQuery(){}
	
	public AbstractQuery(Action a, ObjectOfDM oodm){
		this.action = a;
		this.oodm = oodm;
	}
	
	public Action getAction(){
		return this.action;
	}
	
	public void setAction(Action a){
		this.action = a;
	}

	public ObjectOfDM getOodm() {
		return oodm;
	}

	public void setOodm(ObjectOfDM oodm) {
		this.oodm = oodm;
	}
}
