package edu.virginia.cs.Framework.Types;

import java.util.ArrayList;
import edu.virginia.cs.Framework.Types.DBFormalAbstractMeasurementFunction.MeasurementType;

public class DBConcreteSpaceMeasurementFunction extends DBConcreteMeasurementFunction {
	public DBConcreteSpaceMeasurementFunction() { 
		super (MeasurementType.SPACE); 
	}
	
	public DBConcreteSpaceMeasurementFunction(ArrayList<ConcreteLoad> loads) { 
		super (MeasurementType.SPACE); 
		super.setLoads(loads);
	}
	
	public DBSpaceMeasurementResult run(){
		// run insert only, and check the space consumption
//		dropDB();
//		createDB();
//		createTables();
//		runInsert();
		double spaceConsumption = checkSpace();
		// drop database after test
//		Chong:
		dropDB();
		DBSpaceMeasurementResult dbSMR = new DBSpaceMeasurementResult(spaceConsumption);
		return dbSMR;
	}
}
