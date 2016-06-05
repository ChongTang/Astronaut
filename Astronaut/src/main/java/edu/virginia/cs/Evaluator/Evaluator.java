package edu.virginia.cs.Evaluator;

import edu.virginia.cs.Framework.Types.*;

import java.util.ArrayList;


/**
 * Created by tang on 11/24/14.
 */
public class Evaluator {

    public static ArrayList<Thread> threadList = new ArrayList<Thread>();

    /**
     * initialize thread list
     */
    public Evaluator(){

    }

    public static DBMeasurementResult evaluate(DBImplementation impl, DBConcreteMeasurementFunctionSet dbMfs) {
        /**
         * get a idle node, connect to it with JDBC, execute whatever I need with JDBC
         */
        Node node = WorkNodes.getAnIdleNode();
        // create a new thread to do job
        Thread thread = new Thread(new Worker(node, impl, dbMfs));
        threadList.add(thread);
        // the thread will write the result in to file, return -1 instead
        thread.run();

        // construct DBMeasurementResult
        DBTimeMeasurementResult tmr = new DBTimeMeasurementResult(-1, -1);
        DBSpaceMeasurementResult smr = new DBSpaceMeasurementResult(-1);
        DBMeasurementResult mr = new DBMeasurementResult(tmr, smr);
        return mr;
    }
}
