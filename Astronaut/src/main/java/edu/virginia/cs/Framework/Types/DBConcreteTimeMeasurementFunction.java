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
		System.out.println("Run functions in DBConcreteTimeMeasurementFunction");
		// run this step 3 times
		double insertTime = 0.0;
		double selectTime = 0.0;
		for(int i=0; i<3; i++) {
			super.mfByDB.dropDB();
			super.mfByDB.createDB();
			super.mfByDB.createTables();
			insertTime += super.mfByDB.runInsert();
			selectTime += super.mfByDB.runSelect();
		}
		insertTime /= 3;
		selectTime /= 3;
		DBTimeMeasurementResult dbTMR = new DBTimeMeasurementResult(insertTime, selectTime);
		return dbTMR;
	}
}
