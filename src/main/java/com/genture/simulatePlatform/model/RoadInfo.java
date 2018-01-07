package com.genture.simulatePlatform.model;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */
public class RoadInfo {

	private String cityId;

	private String roadId;

	private String startPos;

	private String endPos;

	private List<RoadCondition> roadConditions;

	private long roadDistance;

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getRoadId() {
		return roadId;
	}

	public void setRoadId(String roadId) {
		this.roadId = roadId;
	}

	public String getStartPos() {
		return startPos;
	}

	public void setStartPos(String startPos) {
		this.startPos = startPos;
	}

	public String getEndPos() {
		return endPos;
	}

	public void setEndPos(String endPos) {
		this.endPos = endPos;
	}

	public List<RoadCondition> getRoadCondition() {
		return roadConditions;
	}

	public void setRoadConditions(List<RoadCondition> roadConditions) {
		this.roadConditions = roadConditions;
	}

	public long getRoadDistance() {
		return roadDistance;
	}

	public void setRoadDistance(long roadDistance) {
		this.roadDistance = roadDistance;
	}

}
