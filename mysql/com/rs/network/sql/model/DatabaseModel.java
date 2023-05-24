package com.rs.network.sql.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

import com.rs.network.sql.pool.DatabaseConnection;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;

import lombok.Data;
import lombok.SneakyThrows;

/**
 * Server database model structure
 * to handle DAO Operation requests.
 * 
 * @author Pb600
 * @version 2.0 05/11/13
 */
@Data
public abstract class DatabaseModel implements Node {

	protected PreparedStatement insertStatement;
	protected PreparedStatement deleteStatement;
	protected PreparedStatement updateStatement;
	protected PreparedStatement selectStatement;

	protected DatabaseConnection databaseConnection;
	private ResultSet resultSet;

	/**
	 * Constructor
	 */
	protected DatabaseModel() {
		setDefault();
	}

	private void setDefault() {
		insertStatement = null;
		deleteStatement = null;
		updateStatement = null;
		selectStatement = null;
		databaseConnection = null;
	}

	/**
	 * implement AutoCloseable interface, for cleaning statement on destruct object
	 */
	@Override
	public void close() {
		if (databaseConnection != null) {
			databaseConnection.close(selectStatement);
			databaseConnection.close(deleteStatement);
			databaseConnection.close(insertStatement);
			databaseConnection.close(updateStatement);
			databaseConnection.close(resultSet);
			databaseConnection.release();
		}
		setDefault();
		LogUtility.log(LogType.SQL, "All SQL statements purged and database connection has now been closed.");
	}

	public enum StatementType {
		SELECT, INSERT, DELETE, UPDATE;
	}

	protected void prepareStatement(DatabaseConnection databaseConnection, StatementType statementType) {
		setDatabaseConnection(databaseConnection);
		if (statementType == StatementType.INSERT) {
			if (getInsertStatement() != null)
				insertStatement = databaseConnection.prepareStatement(getInsertStatement());
		} else if (statementType == StatementType.DELETE) {
			if (getDeleteStatement() != null)
				deleteStatement = databaseConnection.prepareStatement(getDeleteStatement());
		} else if (statementType == StatementType.SELECT) {
			if (getSelectStatement() != null)
				selectStatement = databaseConnection.prepareStatement(getSelectStatement());
		} else if (statementType == StatementType.UPDATE) {
			if (getUpdateStatement() != null)
				updateStatement = databaseConnection.prepareStatement(getUpdateStatement());
		}
	}

	public String getInsertStatement() {
		return null;
	}

	public String getDeleteStatement() {
		return null;
	}

	public String getUpdateStatement() {
		return null;
	}

	public String getSelectStatement() {
		return null;
	}
	
	@SneakyThrows(SQLException.class)
	public void setFunction(Consumer<ResultSet> doSet) {
			while(resultSet.next()) {
				
				doSet.accept(resultSet);
			}

	}
}