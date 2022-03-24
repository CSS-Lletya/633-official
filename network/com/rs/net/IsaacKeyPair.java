package com.rs.net;

import java.util.stream.IntStream;

import lombok.Getter;

/**
 * More information on Isaac can be found here:
 * https://www.rune-server.ee/runescape-development/rs-503-client-server/informative-threads/134817-why-rsa-isaac-should-not-removed.html
 * @author Dennis
 *
 */
public class IsaacKeyPair {

	@Getter
	private ISAACCipher inKey, outKey;

	public IsaacKeyPair(int[] seed) {
		inKey = new ISAACCipher(seed);
		IntStream.of(seed).forEach(key -> key += 50);
		outKey = new ISAACCipher(seed);
	}
}