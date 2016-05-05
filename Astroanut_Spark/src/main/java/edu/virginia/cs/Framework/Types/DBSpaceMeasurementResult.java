package edu.virginia.cs.Framework.Types;

import java.io.Serializable;

public class DBSpaceMeasurementResult implements Serializable{
	private double dbSpace = -1.0;

	public DBSpaceMeasurementResult(double sc){
		this.dbSpace = sc;
	}
	
	public double getDbSpace() {
		return dbSpace;
	}

	public void setDbSpace(double dbSpace) {
		this.dbSpace = dbSpace;
	}
}
