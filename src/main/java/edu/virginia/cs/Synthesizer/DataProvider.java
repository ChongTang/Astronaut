package edu.virginia.cs.Synthesizer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA. User: ct4ew Date: 7/23/13 Time: 3:53 PM To change
 * this template use File | Settings | File Templates.
 */
public class DataProvider implements Serializable{
	// pairs is used to store all code and real name
	// for example, a table pair will be <Table$0, Customer>
	// a field pair will be <field$1, customerID>
	private ArrayList<CodeNamePair> pairs;
	private HashMap<String, ArrayList<CodeNamePair>> tableItems;
	private ArrayList<CodeNamePair> types;
	private ArrayList<CodeNamePair> parents;
	private HashMap<String, ArrayList<String>> allStmts = new HashMap<String, ArrayList<String>>();
	private ArrayList<String> orders = new ArrayList<String>();
	private ArrayList<Sig> sigs;

	public DataProvider(ArrayList<Sig> sigs) {
		pairs = new ArrayList<CodeNamePair>();
		tableItems = new HashMap<String, ArrayList<CodeNamePair>>();
		types = new ArrayList<CodeNamePair>();
		parents = new ArrayList<CodeNamePair>();
		// assert TableName.size() == TableItem.size();
		this.sigs = sigs;
	}

	public DataProvider() {
		pairs = new ArrayList<CodeNamePair>();
		tableItems = new HashMap<String, ArrayList<CodeNamePair>>();
		types = new ArrayList<CodeNamePair>();
		parents = new ArrayList<CodeNamePair>();
		// assert TableName.size() == TableItem.size();
	}

	public HashMap<String, ArrayList<CodeNamePair>> getTables() {
		return this.tableItems;
	}

	public ArrayList<CodeNamePair> getParents() {
		return this.parents;
	}

	public ArrayList<String> getAttrByTableName(String table) {
		ArrayList<String> attrs = new ArrayList<String>();
		Iterator<Map.Entry<String, ArrayList<CodeNamePair>>> it = this.tableItems
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ArrayList<CodeNamePair>> entry = it
					.next();
			String tableName = entry.getKey();
			if (tableName.equalsIgnoreCase(table)) {
				for (CodeNamePair pair : entry.getValue()) {
					if (pair.getFirst().equalsIgnoreCase("attr")) {
						attrs.add(pair.getSecond());
					}
				}
			}
		}

		return attrs;
	}

	public Boolean isClassAssociate(String className) {
		Iterator<Map.Entry<String, ArrayList<CodeNamePair>>> it = this.tableItems
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ArrayList<CodeNamePair>> entry = it
					.next();
			String tableName = entry.getKey();
			if (tableName.equalsIgnoreCase(className)) {
				for (CodeNamePair pair : entry.getValue()) {
					if (pair.getFirst().equalsIgnoreCase("src")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public ArrayList<CodeNamePair> getTypes() {
		ArrayList<CodeNamePair> list = new ArrayList<CodeNamePair>();
		for (CodeNamePair pair : this.types) {
			CodeNamePair tmp = new CodeNamePair(pair.getFirst()
					.toString(), pair.getSecond().toString());
			list.add(tmp);
		}
		return list;
	}

	public ArrayList<CodeNamePair> getReverseIds() {
		ArrayList<CodeNamePair> list = new ArrayList<CodeNamePair>();

		Iterator<Map.Entry<String, ArrayList<CodeNamePair>>> tableItemIt = this.tableItems
				.entrySet().iterator();
		while (tableItemIt.hasNext()) {
			Map.Entry<String, ArrayList<CodeNamePair>> entry = tableItemIt.next();
			String tableName = entry.getKey();
			ArrayList<CodeNamePair> pair = entry.getValue();
			for (CodeNamePair p : pair) {
				if (p.getFirst().equalsIgnoreCase("id")) {
					list.add(new CodeNamePair(p.getSecond(), tableName));
				}
			}
		}
		return list;
	}

	public String addPair(String code, String name) {
		// check if same first existed
		for (int i = 0; i < this.pairs.size(); i++) {
			if (this.pairs.get(i).getFirst().toString().equalsIgnoreCase(code)) {
				if (code.startsWith("Table")) {
					return this.pairs.get(i).getSecond().toString();
				}
			}
		}
		CodeNamePair myPair = new CodeNamePair(code, name);
		this.pairs.add(myPair);
		return "true";
	}

	public void addItem(String table, String key, String value) {
		if (this.tableItems.containsKey(table)) {
			CodeNamePair tmp = new CodeNamePair(key, value);
			this.tableItems.get(table).add(tmp);
		} else {
			ArrayList<CodeNamePair> tmpArray = new ArrayList();
			CodeNamePair tmpPair = new CodeNamePair(key, value);
			tmpArray.add(tmpPair);
			this.tableItems.put(table, tmpArray);
		}
	}

	public boolean addType(String filed, String type) {
		CodeNamePair newPair = new CodeNamePair(filed, type);
		if (this.types.contains(newPair)) {
			return false;
		} else {
			this.types.add(newPair);
		}
		return true;
	}

	public boolean addParent(String child, String parent) {
		CodeNamePair newPair = new CodeNamePair(child, parent);
		if (this.parents.contains(newPair)) {
			return false;
		} else {
			this.parents.add(newPair);
		}
		return true;
	}

	public String getSecondByFirst(String first) {
		String second = null;
		for (int i = 0; i < this.pairs.size(); i++) {
			if (this.pairs.get(i).getFirst().equals(first)) {
				second = this.pairs.get(i).getSecond().toString();
			}
		}
		return second;
	}

	/**
	 * replace all items with $ symbol and add them to the table, then delete
	 * all table items with $ symbol
	 */
	public void refineTable(ArrayList<Sig> sigs) {
		HashMap<String, ArrayList<CodeNamePair>> tmpTableItems = new HashMap();
		Set<Map.Entry<String, ArrayList<CodeNamePair>>> tableItemSet = this.tableItems
				.entrySet();
		// replace all items with $ symbol
		for (Map.Entry<String, ArrayList<CodeNamePair>> entry : tableItemSet) {
			String tableName = entry.getKey();
			ArrayList<CodeNamePair> tableContents = entry.getValue();

			if (tableName.contains("$")) {
				tableName = getSecondByFirst(tableName);
				if (!tmpTableItems.containsKey(tableName)) {
					tmpTableItems.put(tableName,
							new ArrayList<CodeNamePair>());
				}

				int arraySize = tableContents.size();
				for (int i = 0; i < arraySize; i++) {
					String firstField = tableContents.get(i).getFirst()
							.toString();
					if (firstField.contains("$")) {
						firstField = this.getSecondByFirst(firstField);
					}
					String secondField = tableContents.get(i).getSecond()
							.toString();
					if (secondField.contains("$")) {
						secondField = this.getSecondByFirst(secondField);
					}
					// add new items into tmpTableItems
					CodeNamePair tmpPair = new CodeNamePair(firstField,
							secondField);
					tmpTableItems.get(tableName).add(tmpPair);
				}
			} else {
				if (tmpTableItems.containsKey(tableName)) {
					// add all items in this.tableItems in to tmpTableItems
					for (int i = 0; i < tableContents.size(); i++) {
						tmpTableItems.get(tableName).add(tableContents.get(i));
					}
				} else {
					tmpTableItems.put(tableName, tableContents);
				}
			}
		}

		// // handle parent here
		// for (int i = 0; i < this.parents.size(); i++) {
		// String tableName = this.parents.get(i).getFirst().toString();
		// if (tableName.contains("$")) {
		// String tmpTableName = getPairSecond(tableName);
		// this.parents.get(i).setFirst(tmpTableName);
		// }
		// }
		// handle type here
		for (int i = 0; i < this.types.size(); i++) {
			String typeName = this.types.get(i).getFirst().toString();
			if (typeName.contains("$")) {
				this.types.get(i).setFirst(typeName.split("\\$")[0]);
			}
		}

		// remove pairs from parents which the child is an independent table
		for (int i = 0; i < this.pairs.size(); i++) {
			if (this.pairs.get(i).getFirst().toString().startsWith("Table")) {
				int j = 0;
				for (j = 0; j < this.parents.size(); j++) {
					if (this.pairs
							.get(i)
							.getSecond()
							.toString()
							.equalsIgnoreCase(
									this.parents.get(j).getFirst().toString())) {
						break;
					}
				}
				if (j < this.parents.size()) {
					this.parents.remove(j);
				}
			}
		}

		for (int i = 0; i < this.parents.size(); i++) {
			// change DType$0 in parent to DType
			ArrayList<CodeNamePair> tmpList1 = tmpTableItems
					.get(this.parents.get(i).getSecond().toString());
			if (tmpList1 == null) {
				continue;
			}
			for (int j = 0; j < tmpList1.size(); j++) {
				String tmp = tmpList1.get(j).getSecond().toString();
				if (tmp.contains("$")) {
					String tmp1 = tmp.split("\\$")[0];
					tmpTableItems
							.get(this.parents.get(i).getSecond().toString())
							.get(j).setSecond(tmp1);
					// tmpList1.get(i).setSecond(tmp1);
				}
			}

			ArrayList<CodeNamePair> tmpList = tmpTableItems
					.get(this.parents.get(i).getFirst());
			for (int j = 0; j < tmpList.size(); j++) {
				if (!hasItemInArray(
						tmpTableItems.get(this.parents.get(i).getSecond()),
						tmpList.get(j))) {

					tmpTableItems.get(this.parents.get(i).getSecond()).add(
							tmpList.get(j));
				}
			}
			tmpTableItems.remove(this.parents.get(i).getFirst());
		}

		this.tableItems.clear();
		this.tableItems = tmpTableItems;
	}

	public boolean hasItemInArray(ArrayList<CodeNamePair> list,
			CodeNamePair pair) {
		boolean has = false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getFirst().toString()
					.equalsIgnoreCase(pair.getFirst().toString())
					&& list.get(i).getSecond().toString()
							.equalsIgnoreCase(pair.getSecond().toString())) {
				return true;
			}
		}
		return has;
	}

	public CodeNamePair hasSrcDst(String tableName) {
		CodeNamePair srcdst = new CodeNamePair("", "");
		ArrayList<CodeNamePair> items = this.tableItems.get(tableName);
		for (CodeNamePair pair : items) {
			String tmp = pair.getFirst().toString();
			if (tmp.equalsIgnoreCase("src")) {
				srcdst.setFirst(pair.getSecond().toString());
			}
			if (tmp.equalsIgnoreCase("dst")) {
				srcdst.setSecond(pair.getSecond().toString());
			}
			if (srcdst.getFirst().length() > 0
					&& srcdst.getSecond().length() > 0) {
				return srcdst;
			}
		}
		return srcdst;
	}

	public CodeNamePair getMultiplicity(String tableName) {
		CodeNamePair srcdst_mul = new CodeNamePair("", "");
		ArrayList<CodeNamePair> items = this.tableItems.get(tableName);
		for (CodeNamePair pair : items) {
			String tmp = pair.getFirst().toString();
			if (tmp.equalsIgnoreCase("srcMul")) {
				srcdst_mul.setFirst(pair.getSecond().toString());
			}
			if (tmp.equalsIgnoreCase("dstMul")) {
				srcdst_mul.setSecond(pair.getSecond().toString());
			}
			if (srcdst_mul.getFirst().length() > 0
					&& srcdst_mul.getSecond().length() > 0) {
				return srcdst_mul;
			}
		}
		return srcdst_mul;
	}

	public ArrayList<String> getPrimaryKey(String tableName) {
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<CodeNamePair> items = this.tableItems.get(tableName);
		for (CodeNamePair pair : items) {
			String tmp = pair.getFirst().toString();
			if (tmp.equalsIgnoreCase("primaryKey")) {
				keys.add(pair.getSecond().toString());
			}
		}
		return keys;
	}

	public boolean isForeignKey(String table, String field) {
		boolean isForeignKey = false;
		ArrayList<CodeNamePair> table1 = this.tableItems.get(table);

		for (int i = 0; i < table1.size(); i++) {
			if (table1.get(i).getFirst().toString()
					.equalsIgnoreCase("foreignKey")) {
				if (table1.get(i).getSecond().toString()
						.equalsIgnoreCase(field)) {
					return true;
				}
			}
		}
		return isForeignKey;
	}

	public String getParent(String tableName) {
		for (CodeNamePair pair : this.parents) {
			if (pair.getFirst().toString().equalsIgnoreCase(tableName)) {
				return pair.getSecond().toString();
			}
		}
		return null;
	}

	/**
	 * Find the table name that ID is its primaryKey and ID is not its
	 * foreignKeyStr
	 *
	 * @param ID
	 *            : input id
	 * @return the table name
	 */
	public String tableNameByID(String ID) {

		String tableName = "NULL";
		Set<Map.Entry<String, ArrayList<CodeNamePair>>> tableItemSet = this.tableItems
				.entrySet();
		for (Map.Entry<String, ArrayList<CodeNamePair>> entry : tableItemSet) {

			ArrayList<CodeNamePair> tmpArray = entry.getValue();
			int arraySize = tmpArray.size();
			for (int i = 0; i < arraySize; i++) {
				// if
				// (tmpArray.get(i).getFirst().toString().equalsIgnoreCase("attr"))
				// {
				if (tmpArray.get(i).getFirst().toString()
						.equalsIgnoreCase("ID")) {
					if (tmpArray.get(i).getSecond().toString()
							.equalsIgnoreCase(ID)) {
						// tableName = entry.getKey();
						// System.out.println("Find Table: "+ tableName);
						// check the ID is also in attr
						for (int j = 0; j < arraySize; j++) {
							if (tmpArray.get(j).getFirst()
									.equalsIgnoreCase("attr")) {
								if (tmpArray.get(j).getSecond()
										.equalsIgnoreCase(ID)) {
									return entry.getKey();
								}
							}
						}
					}
				}
			}
		}

		return tableName;
	}

	public String hasParent(String child) {
		String parent = null;
		for (int i = 0; i < this.parents.size(); i++) {
			System.out.println(this.pairs.get(i).getFirst().toString() + "   "
					+ child);
			if (this.pairs.get(i).getFirst().toString().equals(child)) {
				parent = this.parents.get(i).getSecond().toString();
			}
		}
		return parent;
	}

	public int hasMultipleItem(String tableName, String item) {
		int key = 0;
		ArrayList<CodeNamePair> table1 = this.tableItems.get(tableName);

		for (int i = 0; i < table1.size(); i++) {
			if (table1.get(i).getFirst().toString().equalsIgnoreCase(item)) {
				key++;
			}
		}

		return key;
	}

	public boolean isID(String field, String table) {
		boolean isID = false;
		ArrayList<CodeNamePair> table1 = this.tableItems.get(table);

		for (int i = 0; i < table1.size(); i++) {
			// if (table1.get(i).getFirst().toString().equalsIgnoreCase("Id")) {
			if (table1.get(i).getFirst().toString()
					.equalsIgnoreCase("primaryKey")) {
				if (table1.get(i).getSecond().toString()
						.equalsIgnoreCase(field)) {
					isID = true;
					break;
				}
			}
		}
		return isID;
	}

	public String getTypesByName(String name) {
		String second = null;
		int size = this.types.size();
		for (int i = 0; i < size; i++) {
			if (this.types.get(i).getFirst().toString().equalsIgnoreCase(name)) {
				second = this.types.get(i).getSecond().toString();
			}
		}
		return second;
	}

	public boolean isParent(String code) {
		for (int i = 0; i < this.parents.size(); i++) {
			if (this.parents.get(i).getSecond().toString()
					.equalsIgnoreCase(code)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasPairCode(String code) {
		int i = 0;
		for (i = 0; i < this.pairs.size(); i++) {
			CodeNamePair tmp = this.pairs.get(i);
			if (tmp.getFirst().toString().equalsIgnoreCase(code)) {
				return true;
			}
		}
		return false;
	}

	public void removePairByCode(String code) {
		int i = 0;
		for (i = 0; i < this.pairs.size(); i++) {
			CodeNamePair tmp = this.pairs.get(i);
			if (tmp.getFirst().toString().equalsIgnoreCase(code)) {
				break;
			}
		}
		if (i < this.pairs.size()) {
			this.pairs.remove(i);
		}

	}

	public void outputData(String output) {
		System.out.println("====================" + output
				+ "=============================");

		Set<Map.Entry<String, ArrayList<CodeNamePair>>> tableItemSet = this.tableItems
				.entrySet();
		for (Map.Entry<String, ArrayList<CodeNamePair>> entry : tableItemSet) {
			System.out.println(entry.getKey() + ":");
			ArrayList<CodeNamePair> tmpArray = entry.getValue();
			int arraySize = tmpArray.size();
			for (int i = 0; i < arraySize; i++) {
				System.out.printf("    %s", tmpArray.get(i).getFirst());
				System.out.println("    " + tmpArray.get(i).getSecond());
			}
		}
		System.out.println("=================================================");

		int listSize = this.types.size();
		for (int j = 0; j < listSize; j++) {
			System.out.println(this.types.get(j).getFirst() + ": \t"
					+ this.types.get(j).getSecond());
		}
		System.out
				.println("=================================================\n\n");
	}

	public boolean writeIntoFile(String filename) throws IOException {
		ArrayList<String> errorTable = new ArrayList(); // for debug
		File sqlFile;
		sqlFile = new File(filename);
		FileOutputStream oFile;
		PrintStream pPRINT = null;
		if (!sqlFile.exists()) {
			sqlFile.createNewFile();
		}
		oFile = new FileOutputStream(sqlFile, false);
		pPRINT = new PrintStream(oFile);
		pPRINT.println("-- CREATE DATABASE FOR " + filename + "\n");
		String dbName = sqlFile.getName();
		dbName = dbName.substring(0, dbName.length() - 4);
		pPRINT.println("USE " + dbName + ";");

		String tableName = null;

		int PKNum = 0;
		int FKNum = 0;
		ArrayList<String> foreignKeyList = new ArrayList();
		Set<Map.Entry<String, ArrayList<CodeNamePair>>> entrySet = this.tableItems
				.entrySet();
		// iterate all tables
		for (Map.Entry<String, ArrayList<CodeNamePair>> entry : entrySet) {
			tableName = entry.getKey();
			ArrayList<String> primaryKeys = getPrimaryKey(tableName);
			if (primaryKeys.size() == 0) {
				continue;
			}
			// get the number of primary keys and foreign keys
			PKNum = hasMultipleItem(tableName, "primaryKey");
			FKNum = hasMultipleItem(tableName, "foreignKey");

			pPRINT.println("--");
			pPRINT.println("-- Table structure for table " + tableName);
			pPRINT.println("--" + "\n");

			// pPRINT.println("CREATE TABLE `"+filename+"`.`"+tableName +"` (");
			pPRINT.println("CREATE TABLE `" + tableName + "` (");
			ArrayList<CodeNamePair> tableItems = entry.getValue();
			int arraySize = tableItems.size();
			boolean firstPK = true;

			// primaryKeyStr will write to file at the end of every create table
			// block
			String primaryKeyStr = new String("PRIMARY KEY (`");
			// foreignKeyStr will write to file at the end of file
			String foreignKeyStr = new String("ALTER TABLE `" + tableName
					+ "`\n");
			int lastFKCounter = 0;
			// iterate all items of every table
			for (int i = 0; i < arraySize; i++) {
				// the itemName is the name of this item, like customerID,
				// orderID
				String itemName = tableItems.get(i).getSecond().toString();
				// if(itemName.contains("$")){
				// itemName = itemName.split("$")[0];
				// }
				// itemType is the type of the item, for example, the type of
				// customerID is Ineteger
				String itemType = getTypesByName(itemName);
				if (itemType == null) {
					itemType = "NULL";
					errorTable.add(tableName + itemName + itemType);
				}
				if (itemType.equalsIgnoreCase("Integer")) {
					itemType = "int";
				} else if (itemType.equalsIgnoreCase("Real")) {
					itemType = "decimal(20,5)";
				} else if (itemType.equalsIgnoreCase("string")) {
					itemType = "varchar(64)";
				} else if (itemType.equalsIgnoreCase("class")) {
					itemType = "longblob";
				} else if (itemType.equalsIgnoreCase("DType")) {
					itemType = "varchar(64)";
				} else if (itemType.equalsIgnoreCase("Bool")) {
					itemType = "boolean"; // boolean is tinyint in mysql
				} else if (itemType.equalsIgnoreCase("Longblob")) {
					itemType = "Longblob";
				} else if (itemType.equalsIgnoreCase("Time")) {
					itemType = "TIMESTAMP";
				}

				String caseName = tableItems.get(i).getFirst().toString();
				if (caseName.equalsIgnoreCase("fields")) {
					String postfix = isID(itemName, tableName) ? " NOT NULL, \n"
							: ",\n";
					pPRINT.print("`" + itemName + "` " + itemType + postfix);
				} else if (caseName.equalsIgnoreCase("primaryKey")) {
					if (PKNum > 1) {
						if (firstPK) {
							primaryKeyStr = primaryKeyStr + itemName + "`";
							firstPK = false;
						} else {
							primaryKeyStr = primaryKeyStr + ",`" + itemName
									+ "`)";
						}
					} else {
						primaryKeyStr = "PRIMARY KEY (`" + itemName + "`)";
					}
				} else if (caseName.equalsIgnoreCase("foreignKey")) {
					pPRINT.println("KEY `FK_" + tableName + "_" + itemName
							+ "_idx` (`" + itemName + "`),");

					// add constrains for ths table
					// find the primary tablename
					String pkTable = tableNameByID(itemName);
					if (1 == FKNum) {
						foreignKeyStr = foreignKeyStr + "  ADD CONSTRAINT `FK_"
								+ tableName + "_" + itemName + "` "
								+ "FOREIGN KEY (`" + itemName
								+ "`) REFERENCES `" + pkTable + "` (`"
								+ itemName + "`) "
								+ "ON DELETE CASCADE ON UPDATE CASCADE;\n";
					} else if (FKNum > 1) {
						lastFKCounter++;
						if (lastFKCounter < FKNum) {
							foreignKeyStr = foreignKeyStr
									+ "  ADD CONSTRAINT `FK_" + tableName + "_"
									+ itemName + "` " + "FOREIGN KEY (`"
									+ itemName + "`) REFERENCES `" + pkTable
									+ "` (`" + itemName + "`) "
									+ "ON DELETE CASCADE ON UPDATE CASCADE,\n";
						} else if (lastFKCounter >= FKNum) {
							foreignKeyStr = foreignKeyStr
									+ "  ADD CONSTRAINT `FK_" + tableName + "_"
									+ itemName + "` " + "FOREIGN KEY (`"
									+ itemName + "`) REFERENCES `" + pkTable
									+ "` (`" + itemName + "`) "
									+ "ON DELETE CASCADE ON UPDATE CASCADE;\n";
						}
					}
				}
			}
			pPRINT.println(primaryKeyStr);
			pPRINT.println(");" + "\n");
			if (FKNum > 0) {
				foreignKeyList.add(foreignKeyStr);
			}
		}

		// ====================
		// output foreignKeyStr here
		// ====================
		for (int i = 0; i < foreignKeyList.size(); i++) {
			pPRINT.println(foreignKeyList.get(i));
		}

		pPRINT.close();
		return true;
	}

	public void insertGen(String path) throws IOException {
		String pattern = Pattern.quote(System.getProperty("file.separator"));
		String[] paths = path.split(pattern);
		String tmpFileName = paths[paths.length - 1];
		if (tmpFileName.contains("Customer")) {
			this.orders.add("Customer");
			this.orders.add("PreferredCustomer");
			this.orders.add("Order");
			this.orders.add("CustomerOrderAssociation");
		} else if (tmpFileName.contains("CSOS")) {
			// ======================== CSOS
			this.orders.add("Channel");
			this.orders.add("Principal");
			this.orders.add("Role");
			this.orders.add("ProcessStateMachine");
			this.orders.add("ProcessStateMachineState");
			this.orders.add("ProcessStateMachineAction");
			this.orders.add("ProcessStateMachineEvent");
			this.orders.add("ProcessStateMachineTransition");
			this.orders.add("ProcessStateMachineExecution");
			this.orders.add("EmailChannel");
			this.orders.add("SMSChannel");
			this.orders.add("ProcessQueryResponse");
			this.orders.add("ProcessQueryResponseAction");
			this.orders.add("ProcessQueryResponseExecution");
			this.orders.add("PrincipalProxy");
			this.orders.add("PrincipalRole");
			this.orders.add("MachineStates");
			this.orders.add("TerminalStates");
			this.orders.add("StateMachineEvents");
			this.orders.add("StateMachineTransitions");
		} else if (tmpFileName.contains("ecommerce")) {
			// ================================== ecommerce
			this.orders.add("Customer");
			this.orders.add("Order");
			this.orders.add("ShippingCart");
			this.orders.add("Item");
			this.orders.add("Category");
			this.orders.add("Catalog");
			this.orders.add("Product");
			this.orders.add("CartItem");
			this.orders.add("OrderItem");
			this.orders.add("PhysicalProduct");
			this.orders.add("ElectronicProduct");
			this.orders.add("Service");
			this.orders.add("Media");
			this.orders.add("Documents");
			this.orders.add("CustomerOrderAssociation");
			this.orders.add("CustomerShippingCartAssociation");
			this.orders.add("ShippingCartItemAssociation");
			this.orders.add("OrderItemAssociation");
			this.orders.add("ProductCategoryAssociation");
			this.orders.add("ProductCatalogAssociation");
			this.orders.add("ProductItemAssociation");
			this.orders.add("ProductAssetAssociation");
		} else if (tmpFileName.contains("decider")) {
			// //////======================decider
			this.orders.add("User");
			this.orders.add("NameSpace");
			this.orders.add("Variable");
			this.orders.add("Relationship");
			this.orders.add("Role");
			this.orders.add("Cluster");
			this.orders.add("DecisionSpace");
			this.orders.add("roleBindings");
			this.orders.add("Participants");
			this.orders.add("DSN");
			this.orders.add("NameSpaceOwnerAssociation");
			this.orders.add("varInAssociation");
			this.orders.add("varOutAssociation");
			this.orders.add("clusterVariableAssociation");
			this.orders.add("userDecisionSpaceAssociation");
			this.orders.add("descisionSpaceRoleBindingsAssociation");
			this.orders.add("descisionSpaceParticipantsAssociation");
			this.orders.add("descisionSpaceVariablesAssociation");
			this.orders.add("descisionSpaceRoleAssociation");
			this.orders.add("descisionSpaceUserAssociation");
			this.orders.add("DSNUserAssociation");
			this.orders.add("DSNNamespaceAssociation");
			this.orders.add("DSNDecisionSpaceAssociation");
		}

		long start = System.currentTimeMillis();
		String modulePath = path.split("\\.")[0]; // delete file extension
		// String tmp[] = modulePath.split("\\\\");
		String tmp[] = modulePath.split(pattern);
		String moduleName = tmp[tmp.length - 1];
		int path_len = tmp.length;
		String tmp_path = "";
		for (int i = 0; i < path_len - 1; i++) {
			// tmp_path += tmp[i] + "\\";
			tmp_path += tmp[i] + File.separator;
		}
		// tmp_path += "Inserts\\" + moduleName;
		tmp_path += "Inserts" + File.separator + moduleName;
		modulePath = tmp_path;
		String fileName = modulePath + "_insert_r.sql";
		int num_tables = this.tableItems.size();
		Integer IDs[] = new Integer[num_tables];
		File moduleFile = new File(fileName);
		FileOutputStream oFile;
		PrintStream pFile;
		if (!moduleFile.exists()) {
			moduleFile.createNewFile();
		}

		ArrayList<String> forShow = new ArrayList<String>();
		oFile = new FileOutputStream(moduleFile, false);
		pFile = new PrintStream(oFile);
		int rows = 100000;
		// initialize primaryKey array
		for (int i = 0; i < num_tables; i++) {
			IDs[i] = 0;
		}
		int which_table = 0;
		Set<Map.Entry<String, ArrayList<CodeNamePair>>> entrySet = this.tableItems
				.entrySet();
		Random random_gen = new Random(System.currentTimeMillis());

		for (Map.Entry<String, ArrayList<CodeNamePair>> entry : entrySet) {
			String tableName = entry.getKey();
			ArrayList<CodeNamePair> items = entry.getValue();
			ArrayList<String> pKeys = getPrimaryKey(tableName);
			if (pKeys.size() == 0) {
				continue;
			}
			int rows_for_one_table = 0;
			while (rows_for_one_table++ < rows) {
				String columnNames = "";
				String values = "";
				for (CodeNamePair pair : items) {
					String first = pair.getFirst().toString();
					String second = pair.getSecond().toString();
					if (!first.equalsIgnoreCase("fields")) {
						continue;
					}
					columnNames += second + ",";
					if (isID(second, tableName)) {
						IDs[which_table]++;
						values += IDs[which_table] + ",";
						continue;
					}

					String itemType = getTypesByName(second);
					// Chong: needs to revise
					if (itemType.equalsIgnoreCase("Integer")) {
						itemType = "Int";
						int tmp_int = random_gen.nextInt(60000);
						if (isForeignKey(tableName, second)) {
							tmp_int = tmp_int % rows;
						}
						values += String.valueOf(tmp_int) + ",";
					} else if (itemType.equalsIgnoreCase("Real")) {
						// itemType = "decimal(5,0)";
						itemType = "Int";
						float tmp_float = random_gen.nextFloat();
						values += String.valueOf(tmp_float) + ",";
					} else if (itemType.equalsIgnoreCase("string")) {
						itemType = "string";
						String randomString = UUID.randomUUID().toString();
						values += randomString + ",";
					} else if (itemType.equalsIgnoreCase("class")) {
						itemType = "longblob";
					} else if (itemType.equalsIgnoreCase("DType")) {
						// itemType = "varchar(31)";
						itemType = "string";
						String randomString = tableName;
						values += randomString + ",";
					} else if (itemType.equalsIgnoreCase("Bool")) {
						itemType = "boolean"; // boolean is tinuint in mysql
						String randomString = "TRUE";
						values += randomString + ",";
					} else if (itemType.equalsIgnoreCase("Longblob")) {
						itemType = "Longblob"; // boolean is tinuint in mysql
					} else if (itemType.equalsIgnoreCase("Time")) {
						itemType = "TIMESTAMP"; // boolean is tinuint in mysql
					}
				}
				columnNames = columnNames
						.substring(0, columnNames.length() - 1);
				values = values.substring(0, values.length() - 1);
				String statement = "INSERT INTO `" + tableName + "` ("
						+ columnNames + ") VALUES (" + values + ");";
				if (!this.allStmts.containsKey(tableName)) {
					this.allStmts.put(tableName, new ArrayList<String>());
				}
				this.allStmts.get(tableName).add(statement);
				// pFile.println(statement);
			}
			which_table++;
		}
		for (String s : this.orders) {
			if (!this.allStmts.containsKey(s)) {
				continue;
			}
			ArrayList<String> stmts = this.allStmts.get(s);
			for (String stmt : stmts) {
				pFile.println(stmt);
			}
		}
		long end = System.currentTimeMillis();
		long elapsed = end - start;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		pFile.println(sdf.format(new Date(elapsed)));
		oFile.close();
		pFile.close();
	}
}
