package com.rs.net.encoders;

import com.rs.net.Session;

import lombok.Data;

@Data
public abstract class Encoder {

	protected final Session session;
}