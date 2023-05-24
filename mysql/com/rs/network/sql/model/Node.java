package com.rs.network.sql.model;

import java.sql.SQLException;

public interface Node {
	
	public void execute() throws SQLException;
	
	public void close();
}