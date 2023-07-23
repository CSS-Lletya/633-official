package com.rs.net.host;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An enumeration of possible host list types.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
@AllArgsConstructor
public enum HostListType {
	BANNED_IP(0, "banned_ips"),
	MUTED_IP(1, "muted_ips"),
	STARTER_RECEIVED(2, "started_ips");
	
	/**
	 * The index in the list array.
	 */
	@Getter
	private final int index;
	
	/**
	 * The file name.
	 */
	@Getter
	private final String file;
}