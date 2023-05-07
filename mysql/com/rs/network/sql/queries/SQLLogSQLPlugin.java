package com.rs.network.sql.queries;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.rs.network.sql.GameDatabase;
import com.rs.network.sql.model.DatabaseModel;
import com.rs.network.sql.pool.DatabaseConnection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

@Data
@EqualsAndHashCode(callSuper = false)
public class SQLLogSQLPlugin extends DatabaseModel {

	
	private final String logContext;
	
	@Override
	public String getQuery() {
		return "insert into log (context) values (?)";
	}
	
	@Override
	public String getSelectStatement() {
		return "select * from log where context=?";
	}
	
    @Override
    @SneakyThrows(SQLException.class)
    public void query() {
        DatabaseConnection databaseConnection = GameDatabase.getGameServer().getConnection(getClass().getCanonicalName());
        if (databaseConnection == null) {
            GameDatabase.getGameServer().reportUnavailableConnection(); 
            return;
        }
        prepareStatement(databaseConnection, StatementType.INSERT);
        insertStatement.setString(1, logContext);
        insertStatement.execute();
        
        prepareStatement(databaseConnection, StatementType.SELECT);
        selectStatement.setString(1, "?");
        selectStatement.execute();
        
        ResultSet set =  selectStatement.getResultSet().getStatement().getResultSet();
        while (set.next()) {
        	System.out.println(set.getString(2));
        }
    }
}