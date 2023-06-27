package com.rs.net.wordlist;

import lombok.Data;

@Data
public class WorldEntry {

	public final String activity;
	public final String ip;
	public final int countryId;
	public final String countryName;
	public final boolean members;
}