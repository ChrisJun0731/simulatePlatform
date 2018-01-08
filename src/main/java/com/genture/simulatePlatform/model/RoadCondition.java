package com.genture.simulatePlatform.model;

/**
 * Created by Administrator on 2018/1/5.
 */
public class RoadCondition {
	private long unimpededDistance;
	private long ambleDistance;
	private long congestionDistance;
	private boolean status = false;

	public RoadCondition(){

	}

	public RoadCondition(long unimpededDistance, long ambleDistance, long congestionDistance, boolean status){
		this.ambleDistance = ambleDistance;
		this.congestionDistance = congestionDistance;
		this.unimpededDistance = unimpededDistance;
		this.status = status;
	}

	public long getUnimpededDistance() {
		return unimpededDistance;
	}

	public void setUnimpededDistance(long unimpededDistance) {
		this.unimpededDistance = unimpededDistance;
	}

	public long getAmbleDistance() {
		return ambleDistance;
	}

	public void setAmbleDistance(long ambleDistance) {
		this.ambleDistance = ambleDistance;
	}

	public long getCongestionDistance() {
		return congestionDistance;
	}

	public void setCongestionDistance(long congestionDistance) {
		this.congestionDistance = congestionDistance;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
