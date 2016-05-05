package edu.virginia.cs.Evaluator;

import edu.virginia.cs.AppConfig;
import edu.virginia.cs.Framework.Types.DBConcreteMeasurementFunctionSet;
import edu.virginia.cs.Framework.Types.DBImplementation;


import java.io.*;
import java.sql.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tang on 11/24/14.
 */
public class Worker implements Runnable{
    Node node = null;
    DBImplementation impl = null;
    DBConcreteMeasurementFunctionSet dbMfs = null;
    private final Lock _mutex = new ReentrantLock(true);

    public Worker(Node n, DBImplementation impl, DBConcreteMeasurementFunctionSet dbMfs) {
        this.node = n;
        this.impl = impl;
        this.dbMfs = dbMfs;
    }
//
//    public Node getNode() {
//        return node;
//    }
//
//    public void setNode(Node node) {
//        this.node = node;
//    }

    public void run() {
        System.out.println("from worker: " + this.node.getStatus());
        // change node state to using
        this.node.setStatus(Node.NodeStatus.USING);
        /**
         * (1) create JDBC connection
         * (2) create database
         * (3) create schema
         * (4) run insert&select scripts
         * (5) get space
         * (6) drop database
         */

        // get JDBC connection
        Connection conn = getConn(node.getAddr());
        // create table with JDBC
        String dbName = this.impl.getImPath();
        // parse db name from insert path
        // Chong: check if there is a "_" in dbName
        dbName = dbName.substring(dbName.lastIndexOf(File.separator), dbName.indexOf("."));
        dropDB(conn, dbName);
        createDB(conn, dbName);
        // create tables
        runScript(conn, this.impl.getImPath());
        // run insert, and get time
        double insertTime = runScript(conn, this.dbMfs.getCtmf().getLoads().get(0).getInsertPath());
        // run select, and get time
        double selectTime = runScript(conn, this.dbMfs.getCtmf().getLoads().get(1).getSelectPath());
        // check space
        double space = checkSpace(conn, dbName);

        dropDB(conn, dbName);
        // write result into file
        _mutex.lock();
        String filePath = AppConfig.getResultFile();
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filePath, true));
            pw.println(dbName+":"+String.valueOf(insertTime)+","+String.valueOf(selectTime)+","+String.valueOf(space));
            pw.close();
        } catch (Exception e){
            // error
        }
        _mutex.unlock();
        this.node.setStatus(Node.NodeStatus.IDLE);
    }


    protected Connection getConn(String nodeAddr) {
        Connection conn = null;
        String dbAddr = nodeAddr;
        dbAddr = "jdbc:mysql://" + dbAddr+"/";
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            conn = DriverManager.getConnection(dbAddr, AppConfig.getMySQLUser(), AppConfig.getMysqlPassword());

        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return conn;
    }

    protected void createDB(Connection conn, String dbName){
        System.out.println("DB Name is: "+dbName);
        String sql = "CREATE DATABASE "+dbName;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    protected void createTables(Connection conn, String sqlSchema){
//        try {
//            ScriptRunner runner = new ScriptRunner(conn, false, false);
//            InputStreamReader reader = new InputStreamReader(
//                    new FileInputStream(sqlSchema));
//            runner.runScript(reader);
//            reader.close();
//            conn.close();
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    protected double runScript(Connection conn, String scriptPath) {
        double start = System.currentTimeMillis();

        try {
            ScriptRunner runner = new ScriptRunner(conn, false, false);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(scriptPath));
            runner.runScript(reader);
            reader.close();
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        double end = System.currentTimeMillis();
        return start-end;
    }


    protected double checkSpace(Connection conn, String db) {
        double space_consumption = 0.0;
        String sql = "select table_schema, sum((data_length+index_length)/1024) AS KB from information_schema.tables group by 1";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // iterate rs
            while(rs.next()){
                String dbName = rs.getString(0);
                if(dbName.equalsIgnoreCase(db)){
                    double space = rs.getDouble(1);
                    space_consumption = space;
                    break;
                }
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return space_consumption;
    }

    protected void dropDB(Connection conn, String dbName){
        System.out.println("DB Name is: "+dbName);
        String sql = "DROP DATABASE "+dbName;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
