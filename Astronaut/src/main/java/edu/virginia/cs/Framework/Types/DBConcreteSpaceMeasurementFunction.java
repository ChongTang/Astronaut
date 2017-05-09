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
		double spaceConsumption = super.mfByDB.checkSpace();
		// drop database after test
		super.mfByDB.dropDB();
		DBSpaceMeasurementResult dbSMR = new DBSpaceMeasurementResult(spaceConsumption);
		return dbSMR;
	}
}
