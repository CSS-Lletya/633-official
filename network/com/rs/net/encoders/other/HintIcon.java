package com.rs.net.encoders.other;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class HintIcon {

	private int coordX;
	private int coordY;
	private int plane;
	private int distanceFromFloor;
	private int targetType;
	private int targetIndex;
	private int arrowType;
	private int modelId;
	private int index;

	public HintIcon() {
		this.setIndex(7);
	}

	public HintIcon(int targetType, int modelId, int index) {
		this.setTargetType(targetType);
		this.setModelId(modelId);
		this.setIndex(index);
	}

	public HintIcon(int targetIndex, int targetType, int arrowType, int modelId, int index) {
		this.setTargetType(targetType);
		this.setTargetIndex(targetIndex);
		this.setArrowType(arrowType);
		this.setModelId(modelId);
		this.setIndex(index);
	}

	public HintIcon(int coordX, int coordY, int height, int distanceFromFloor, int targetType, int arrowType,
			int modelId, int index) {
		this.setCoordX(coordX);
		this.setCoordY(coordY);
		this.setPlane(height);
		this.setDistanceFromFloor(distanceFromFloor);
		this.setTargetType(targetType);
		this.setArrowType(arrowType);
		this.setModelId(modelId);
		this.setIndex(index);
	}
}