package edu.virginia.cs.Framework.Types;

import java.util.ArrayList;

import edu.virginia.cs.Framework.Types.DBFormalAbstractMeasurementFunction.MeasurementType;

public class DBFormalConcreteSpaceMeasurementFunction extends DBFormalConcreteMeasurementFunction{
	public DBFormalConcreteSpaceMeasurementFunction(ConcreteLoad load) { 
		super (MeasurementType.SPACE); 
		ArrayList<ConcreteLoad> l = new ArrayList();
		l.add(load);
		super.setLoads(l);
	}
}
