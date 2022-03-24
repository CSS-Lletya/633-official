package com.rs.net.host;

/**
 * An enumeration of possible host list types.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum HostListType {
	BANNED_IP(0, "banned_ips"),
	MUTED_IP(1, "muted_ips"),
	STARTER_RECEIVED(2, "started_ips");
	
	/**
	 * The index in the list array.
	 */
	private final int index;
	
	/**
	 * The file name.
	 */
	private final String file;
	
	HostListType(int index, String file) {
		this.index = index;
		this.file = file;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getFile() {
		return file;
	}
}
