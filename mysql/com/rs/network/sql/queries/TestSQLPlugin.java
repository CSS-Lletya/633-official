package com.rs.network.sql.queries;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.rs.network.sql.GameDatabase;
import com.rs.network.sql.model.DatabaseModel;
import com.rs.network.sql.pool.DatabaseConnection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

/**
 * This is just a Dummy class for SQL plugin.
 * You should now get the idea of how the system works.
 * 
 * https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html
 * https://www.w3schools.com/sql/
 * @author Dennis
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TestSQLPlugin extends DatabaseModel {

	@Override
	public String getSelectStatement() {
		return "select * from log where context=?";
	}
	
	@Override
	public String getUpdateStatement() {
		return "Update test set username='zedz' where username='zed'";
	}
	
	@Override
	public String getInsertStatement() {
		return "insert into log (context) values (?)";
	}
	
    @Override
    @SneakyThrows(SQLException.class)
    public void execute() {
        DatabaseConnection databaseConnection = GameDatabase.getGameServer().getConnection(getClass().getSimpleName());
        if (databaseConnection == null) {
            GameDatabase.getGameServer().reportUnavailableConnection();
            return;
        }
        prepareStatement(databaseConnection, StatementType.SELECT);
        selectStatement.setString(1, "?");//example: lets try and print out as much ? as we have stored in db.
        selectStatement.execute(getSelectStatement());
        ResultSet set =  selectStatement.getResultSet().getStatement().getResultSet();
        while (set.next()) {
        	System.out.println(set.getString(2));
        }
        prepareStatement(databaseConnection, StatementType.UPDATE);
        updateStatement.execute(getUpdateStatement());
        
        prepareStatement(databaseConnection, StatementType.INSERT);
        insertStatement.setString(1, "Some randy shit to input");
        insertStatement.executeUpdate();
        close();
    }
}