package edu.virginia.cs.Framework.Types;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.virginia.cs.AppConfig;
import edu.virginia.cs.Evaluator.ScriptRunner;
import edu.virginia.cs.Framework.Types.DBFormalAbstractMeasurementFunction.MeasurementType;
import edu.virginia.cs.Synthesizer.CodeNamePair;

public class DBConcreteMeasurementFunction implements Serializable{
	private MeasurementType mType = null;
	private ArrayList<ConcreteLoad> loads;
	private DBImplementation impl;
	private Boolean isDebugOn = AppConfig.getDebug();
	private HashMap<String, HashMap<String, ArrayList<CodeNamePair>>> instances;

	
	String mysqlUser = AppConfig.getMySQLUser();
	String mysqlPassword = AppConfig.getMysqlPassword();
	private String mysqlCMD = "mysql --user='" + mysqlUser + "' --password='" + mysqlPassword+"'";

	public DBConcreteMeasurementFunction(MeasurementType m) {
		this.setmType(m);
	}
	
	public HashMap<String, HashMap<String, ArrayList<CodeNamePair>>> getInstances() {
		return instances;
	}
	
	public void setInstances(HashMap<String, HashMap<String, ArrayList<CodeNamePair>>> ins) {
		this.instances = ins;
	}

	public DBImplementation getImpl() {
		return impl;
	}

	public void setImpl(DBImplementation impl) {
		this.impl = impl;
	}

	public MeasurementType getmType() {
		return mType;
	}

	public void setmType(MeasurementType mType) {
		this.mType = mType;
	}

	public ArrayList<ConcreteLoad> getLoads() {
		return loads;
	}

	public void setLoads(ArrayList<ConcreteLoad> loads) {
		this.loads = loads;
	}

	protected double checkSpace() {
		String implPath = this.impl.getImPath();
		String dbName = implPath.substring(
				implPath.lastIndexOf(File.separator) + 1,
				implPath.lastIndexOf("."));
		String[] command = new String[] {
				"bash",
				"-c",
				this.mysqlCMD
						+ " -Bse\"select table_schema, sum((data_length+index_length)/1024) AS KB from information_schema.tables group by 1;\"" };
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] splited = line.split("\\s+");
				if (splited.length == 2) {
					if (splited[0].equalsIgnoreCase(dbName)) { // find right
																// data base
						return Double.valueOf(splited[1]);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1.0;
	}

	protected void dropDB() {
		/**
		 * get the name from implPath create drop database script :
		 * "drop database implName;" create cmd to drop the database execute the
		 * cmd
		 */
		System.out.println("dropDB function in DBConcreteTimeMeasurementFunction");
		String implPath = this.impl.getImPath();
		String dbName = implPath.substring(
				implPath.lastIndexOf(File.separator) + 1,
				implPath.lastIndexOf("."));
//		String dropDatabase = "drop database " + dbName + ";";
//		String scriptFileName = implPath.substring(0,
//				implPath.lastIndexOf(File.separator))
//				+ "dropDatabase.sql";
//
//		System.out.println("The scriptFileName is: "+scriptFileName);
//		try {
//			PrintWriter pw = new PrintWriter(new File(scriptFileName));
//			String outToFile = this.mysqlCMD + " -Bse " + "\"" + dropDatabase
//					+ "\"";
//			System.out.println("Prepare to write script into file: "+outToFile);
//			pw.println(outToFile);
//			pw.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		String[] command = new String[] {
				"bash",
				"-c",
				this.mysqlCMD
						+ " -Bse\"drop database "+ dbName+";\"" };

		try {
			System.out.println("Prepare to execute command");
			Process p = Runtime.getRuntime().exec(command);
			System.out.println("Wait for command to finish");
			p.waitFor();
			System.out.println("Command finished");
			if (p.exitValue() != 0) {
				if (isDebugOn) {
					System.out.println("Drop DB Failure...");
					System.out.println("DropDB: exit value = " + p.exitValue());
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void createDB() {
		System.out.println("createDB function in DBConcreteTimeMeasurementFunction");
		/**
		 * get the name from implPath create create database script :
		 * "create database implName;" create cmd to create the database execute
		 * the cmd
		 */

		String implPath = this.impl.getImPath();
		String dbName = implPath.substring(
				implPath.lastIndexOf(File.separator) + 1,
				implPath.lastIndexOf("."));
		String createDatabase = "create database " + dbName + ";";
		String scriptFileName = implPath.substring(0,
				implPath.lastIndexOf(File.separator))
				+ "createDatabase.sql";
		try {
			PrintWriter pw = new PrintWriter(new File(scriptFileName));
			String outToFile = this.mysqlCMD + " -Bse " + "\"" + createDatabase
					+ "\"";
			pw.println(outToFile);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String[] command = new String[] {
				"bash",
				"-c",
				this.mysqlCMD
						+ " -Bse\"create database "+ dbName+";\"" };
		try {
			Process p = Runtime.getRuntime().exec("bash " + scriptFileName);
			p.waitFor();
			if (p.exitValue() != 0) {
				if (isDebugOn) {
					System.out.println("Created DB Failure...");
					System.out.println("Create DB: exit value = "
							+ p.exitValue());
				}
			}
			// delete the script
			new File(scriptFileName).delete();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void createTables() {
		System.out.println("createTables function in DBConcreteTimeMeasurementFunction");
		/**
		 * get the name from implPath create create tables script call bash to
		 * execute that script
		 */
		String implPath = this.impl.getImPath();
		// String dbName =
		// implPath.substring(implPath.lastIndexOf(File.separator)+1,
		// implPath.lastIndexOf("."));
		String scriptFileName = implPath.substring(0,
				implPath.lastIndexOf(File.separator))
				+ "createTables.sql";

		try {
			PrintWriter pw = new PrintWriter(new File(scriptFileName));
			String outToFile = this.mysqlCMD + " < " + implPath;
			pw.println(outToFile);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			Process p = Runtime.getRuntime().exec("bash " + scriptFileName);
			p.waitFor();
			if (p.exitValue() != 0) {
				if (isDebugOn) {
					System.out.println("Create schema Failure...");
					System.out.println("Create Schema: exit value = "
							+ p.exitValue());
				}
			}
			// delete the script
			new File(scriptFileName).delete();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected double runInsert() {
		System.out.println("runInsert function in DBConcreteTimeMeasurementFunction");
		// iterate all concrete load
		// record start time
		// run scripts
		// record end time
		// return end-start
		long insertInterval = -1;
		for (ConcreteLoad cl : this.loads) {
			String insertPath = cl.getInsertPath();

			if (insertPath.length() == 0) {
				continue;
			}
			String imPath = this.impl.getImPath();
			String dbName = imPath.substring(
					imPath.lastIndexOf(File.separator) + 1,
					imPath.lastIndexOf("."));
//			String[] cmd = new String[] { "mysql", dbName, "-u" + mysqlUser,
//					"-p" + mysqlPassword, "-e", "source " + insertPath };
			long startTime = System.currentTimeMillis();

			// Connection conn=getConnection();//some method to get a Connection
			if (isDebugOn) {
				System.out.println("Insert start----" + dbName);
			}
			Connection conn;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost/"
						+ dbName + "?user=" + mysqlUser + "&password="
						+ mysqlPassword);
				ScriptRunner runner = new ScriptRunner(conn, false, false);
				InputStreamReader reader = new InputStreamReader(
						new FileInputStream(insertPath));
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
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long endTime = System.currentTimeMillis();
			insertInterval = endTime - startTime;
			if (isDebugOn) {
				System.out.println("Spend time: "
						+ TimeUnit.MILLISECONDS.toSeconds(insertInterval));
				System.out.println("Insert finished----" + dbName);
			}
		}
		return insertInterval;
	}

	protected double runSelect() {
		System.out.println("runSelect function in DBConcreteTimeMeasurementFunction");
		long selectInterval = -1;
		for (ConcreteLoad cl : this.loads) {
			String selectPath = cl.getSelectPath();
			if (selectPath.length() == 0) {
				continue;
			}

			String imPath = this.impl.getImPath();
			String dbName = imPath.substring(
					imPath.lastIndexOf(File.separator) + 1,
					imPath.lastIndexOf("."));

			long startTime = System.currentTimeMillis();
			Connection conn;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost/"
						+ dbName + "?user=" + mysqlUser + "&password="
						+ mysqlPassword);
				ScriptRunner runner = new ScriptRunner(conn, false, false);
				InputStreamReader reader = new InputStreamReader(
						new FileInputStream(selectPath));
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
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			try {
//				if (isDebugOn) {
//					System.out.println("Select start----" + dbName);
//				}
//				proc = Runtime.getRuntime().exec(cmd);
//				BufferedReader stdInput = new BufferedReader(
//						new InputStreamReader(proc.getInputStream()));
//
//				BufferedReader stdError = new BufferedReader(
//						new InputStreamReader(proc.getErrorStream()));
//				String s = null;
//				while ((s = stdInput.readLine()) != null) {
//					// System.out.println(s);
//				}
//
//				// read any errors from the attempted command
//				// System.out.println("Here is the standard error of the command (if any):\n");
//				while ((s = stdError.readLine()) != null) {
//					// System.out.println(s);
//				}
//				proc.waitFor();
//				if (proc.exitValue() != 0) {
//					if (isDebugOn) {
//						System.err.println("Select: exit value = "
//								+ proc.exitValue() + "----" + dbName);
//					}
//				}
//
//			} catch (IOException ex) {
//				Logger.getLogger(DBConcreteMeasurementFunction.class.getName())
//						.log(Level.SEVERE, null, ex);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			long endTime = System.currentTimeMillis();
			selectInterval = endTime - startTime;
			if (isDebugOn) {
				System.out.println("Spend time: "
						+ TimeUnit.MILLISECONDS.toSeconds(selectInterval));
				System.out.println("Select finished----" + dbName);
			}
		}
		return selectInterval;
	}
}
