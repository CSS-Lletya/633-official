package com.alex.io;

public abstract class Stream {

	protected int offset;
	protected int length;
	protected byte[] buffer;
	protected int bitPosition;

	public int getLength() {
		return length;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public int getOffset() {
		return offset;
	}

	public void decodeXTEA(int keys[]) {
		decodeXTEA(keys, 5, length);
	}

	public void decodeXTEA(int keys[], int start, int end) {
		int k = 0;
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] == 0) {
				k++;
			}
		}
		if (k == keys.length) {
			return;
		}
		int i_54_ = offset;
		offset = start;
		int i_55_ = (-start + end) / 8;
		for (int i_56_ = 0; (i_55_ ^ 0xffffffff) < (i_56_ ^ 0xffffffff); i_56_++) {
			int i_57_ = readInt();
			int i_58_ = readInt();
			int i_59_ = -957401312;
			int i_60_ = -1640531527;
			int i_61_ = 32;
			while ((i_61_-- ^ 0xffffffff) < -1) {
				i_58_ -= ((i_57_ >>> 5 ^ i_57_ << 4) - -i_57_ ^ keys[~0x439ffffc
						& i_59_ >>> 11]
						+ i_59_);
				i_59_ -= i_60_;
				i_57_ -= ((i_58_ >>> 5 ^ i_58_ << 4) - -i_58_ ^ i_59_
						+ keys[i_59_ & 0x3]);
			}
			offset -= 8;
			writeInt(i_57_);
			writeInt(i_58_);
		}
		offset = i_54_;
	}

	public final void encodeXTEA(int keys[], int start, int end) {
		int o = offset;
		int j = (end - start) / 8;
		offset = start;
		for (int k = 0; k < j; k++) {
			int l = readInt();
			int i1 = readInt();
			int sum = 0;
			int delta = 0x9e3779b9;
			for (int l1 = 32; l1-- > 0;) {
				l += sum + keys[3 & sum] ^ i1 + (i1 >>> 5 ^ i1 << 4);
				sum += delta;
				i1 += l + (l >>> 5 ^ l << 4) ^ keys[(0x1eec & sum) >>> 11]
						+ sum;
			}

			offset -= 8;
			writeInt(l);
			writeInt(i1);
		}
		offset = o;
	}

	private final int readInt() {
		offset += 4;
		return ((this.buffer[offset - 1] & 0xff)
				+ (~0xffffff & this.buffer[offset - 4] << 24) - (-(0xff0000 & this.buffer[offset - 3] << 16) + -((this.buffer[offset - 2] & 0xff) << 8)));
	}

	public void writeInt(int value) {
		buffer[offset++] = (byte) (value >> 24);
		buffer[offset++] = (byte) (value >> 16);
		buffer[offset++] = (byte) (value >> 8);
		buffer[offset++] = (byte) value;
	}

	public final void getBytes(byte data[], int off, int len) {
		for (int k = off; k < len + off; k++) {
			data[k] = buffer[offset++];
		}
	}

}
