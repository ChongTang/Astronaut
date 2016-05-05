package edu.virginia.cs.Framework.Types;

import java.util.ArrayList;
import edu.virginia.cs.Framework.Types.DBFormalAbstractMeasurementFunction.MeasurementType;

public class DBConcreteTimeMeasurementFunction extends DBConcreteMeasurementFunction  {
	public DBConcreteTimeMeasurementFunction() {
		super (MeasurementType.TIME); 
	}
	
	public DBConcreteTimeMeasurementFunction(ArrayList<ConcreteLoad> loads) { 
		super (MeasurementType.TIME); 
		super.setLoads(loads);
	}
	
	public DBTimeMeasurementResult run(){
		System.out.println("Run function in DBConcreteTimeMeasurementFunction");
		dropDB();
		createDB();
		createTables();
		double insertTime = runInsert();
		double selTime = runSelect();
		DBTimeMeasurementResult dbTMR = new DBTimeMeasurementResult(insertTime, selTime);
		return dbTMR;
	}
}
