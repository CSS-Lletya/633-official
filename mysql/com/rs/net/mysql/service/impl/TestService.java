package com.rs.net.mysql.service.impl;

import com.rs.game.map.World;
import com.rs.net.mysql.service.MYSQLService;

import io.vavr.control.Try;

public class TestService implements MYSQLService {

	@Override
	public void execute() {
		Try.run(World.getConnectionPool()::nextFree).onSuccess(success -> {
			System.out.println("submiting");
			Try.of(() -> World.getConnectionPool().nextFree().createStatement().executeUpdate("INSERT INTO public.starter(username) values ('vavr');"));
		}).andFinally(() -> World.getConnectionPool().nextFree().returnConnection());
	}
}