package edu.virginia.cs.Framework.Types;

import java.io.Serializable;

public class DBConcreteMeasurementFunctionSet implements Serializable{
	private DBConcreteSpaceMeasurementFunction csmf;
	private DBConcreteTimeMeasurementFunction ctmf;
	private DBImplementation impl;
	
	public DBConcreteMeasurementFunctionSet(DBConcreteTimeMeasurementFunction ctf, 
			DBConcreteSpaceMeasurementFunction stf){
		this.ctmf = ctf;
		this.csmf = stf;
	}
	

	public DBConcreteSpaceMeasurementFunction getCsmf() {
		return csmf;
	}


	public void setCsmf(DBConcreteSpaceMeasurementFunction csmf) {
		this.csmf = csmf;
	}


	public DBConcreteTimeMeasurementFunction getCtmf() {
		return ctmf;
	}


	public void setCtmf(DBConcreteTimeMeasurementFunction ctmf) {
		this.ctmf = ctmf;
	}


	public DBImplementation getImpl() {
		return impl;
	}
	public void setImpl(DBImplementation impl) {
		this.impl = impl;
	}
}
