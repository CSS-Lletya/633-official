package com.rs.network.sql;

import java.util.Iterator;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Utilities for Database handling.
 * 
 * @author Pb600
 * @author Dennis - improvements
 * @version 1.0 17/03/13
 */
public class DatabaseUtils {

	/***
	 * Generate a MySQL insert script based on table name and its columns names.
	 * 
	 * @param tableName: Name of table.
	 * @param tableColums: Array of columns names.
	 * @return: An insert statement script.
	 */
	public static String queryOf(String tableName, String... tableColums) {
		StringBuilder sb = new StringBuilder("INSERT INTO `" + tableName + "`");
		sb.append("(");
		for (String column : tableColums) {
			sb.append('`').append(column).append("`, ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(")");
		sb.append("VALUES");
		sb.append("(");
		for (int i = 0; i < tableColums.length; i++) {
			sb.append("?");
			if (i + 1 < tableColums.length)
				sb.append(", ");
		}
		sb.append(");");
		return sb.toString();
	}
	
	/***
	 * Generate a MySQL insert script based on table name and its columns names.
	 * 
	 * @param tableName: Name of table.
	 * @param tableColums: Array of columns names.
	 * @return: An insert statement script.
	 */
	public static String selectAll(String tableName) {
		return "SELECT * FROM " + tableName;
	}


	public static String getDataStructure(Iterator<?> iterator, boolean updateType) {
		StringBuilder sb = new StringBuilder();
		while (iterator.hasNext()) {
			Object object = iterator.next();
			sb.append('`').append(object.toString()).append(updateType ? "` = ?, " : "`, ");
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}

	public static String getUpdateStrucutre(String[] keys) {
		StringBuilder sb = new StringBuilder();
		for (String string : keys) {
			if (string != null) {
				sb.append('`').append(string).append("` = ?, ");
			}
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}

	public static String getSelectStructure(ObjectArrayList<String> keys) {
		StringBuilder sb = new StringBuilder();
		for (String string : keys) {
			if (string != null) {
				sb.append('`').append(string).append("`, ");
			}
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}

	public static String getSelectStructure(String[] keys) {
		StringBuilder sb = new StringBuilder();
		for (String string : keys) {
			if (string != null) {
				sb.append('`').append(string).append("`, ");
			}
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}
}