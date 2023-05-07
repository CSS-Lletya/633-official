package com.rs.network.sql.model;

import java.sql.SQLException;

public interface Node {
	
	public void query() throws SQLException;
	
	public void close();
}