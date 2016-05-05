package edu.virginia.cs.Framework.Types;

import java.util.ArrayList;

import edu.virginia.cs.Framework.Types.DBFormalAbstractMeasurementFunction.MeasurementType;

public class DBFormalConcreteTimeMeasurementFunction extends DBFormalConcreteMeasurementFunction {
	public DBFormalConcreteTimeMeasurementFunction(ConcreteLoad ins_load, ConcreteLoad sel_load) { 
		super (MeasurementType.TIME); 
		ArrayList<ConcreteLoad> l =  new ArrayList();
		l.add(ins_load);
		l.add(sel_load);
		super.setLoads(l);
	}
}
