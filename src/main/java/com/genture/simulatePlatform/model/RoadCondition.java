package com.genture.simulatePlatform.model;

/**
 * Created by Administrator on 2018/1/5.
 */
public class RoadCondition {
	private int unimpededDistance;
	private int ambleDistance;
	private int congestionDistance;
	private boolean status = false;

	public RoadCondition(){

	}

	public RoadCondition(int unimpededDistance, int ambleDistance, int congestionDistance, boolean status){
		this.ambleDistance = ambleDistance;
		this.congestionDistance = congestionDistance;
		this.unimpededDistance = unimpededDistance;
		this.status = status;
	}

	public int getUnimpededDistance() {
		return unimpededDistance;
	}

	public void setUnimpededDistance(int unimpededDistance) {
		this.unimpededDistance = unimpededDistance;
	}

	public int getAmbleDistance() {
		return ambleDistance;
	}

	public void setAmbleDistance(int ambleDistance) {
		this.ambleDistance = ambleDistance;
	}

	public int getCongestionDistance() {
		return congestionDistance;
	}

	public void setCongestionDistance(int congestionDistance) {
		this.congestionDistance = congestionDistance;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
