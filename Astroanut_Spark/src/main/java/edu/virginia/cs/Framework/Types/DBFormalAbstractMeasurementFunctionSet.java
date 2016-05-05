package edu.virginia.cs.Framework.Types;

import java.io.Serializable;

public class DBFormalAbstractMeasurementFunctionSet implements Serializable{
	private DBFormalAbstractMeasurementFunction tmf;
	private DBFormalAbstractMeasurementFunction smf;
	public DBFormalAbstractMeasurementFunctionSet(DBFormalAbstractMeasurementFunction tmf, DBFormalAbstractMeasurementFunction smf){
		this.setTmf(tmf);
		this.setSmf(smf);
	}
	public DBFormalAbstractMeasurementFunction getTmf() {
		return tmf;
	}
	public void setTmf(DBFormalAbstractMeasurementFunction tmf) {
		this.tmf = tmf;
	}
	public DBFormalAbstractMeasurementFunction getSmf() {
		return smf;
	}
	public void setSmf(DBFormalAbstractMeasurementFunction smf) {
		this.smf = smf;
	}
	
}
