package edu.virginia.cs.Framework.Types;

import edu.virginia.cs.Framework.Types.AbstractQuery.Action;

public class ConcreteQuery {
	private Action action = null;
	private SpecializedQuery sq = null;

	public ConcreteQuery() {
	}

	public ConcreteQuery(Action a, SpecializedQuery sq) {
		this.action = a;
		this.sq = sq;
	}

	public Action getAction() {
		return this.action;
	}

	public void setAction(Action a) {
		this.action = a;
	}

	public SpecializedQuery getSq() {
		return sq;
	}

	public void setSq(SpecializedQuery sq) {
		this.sq = sq;
	}
}
