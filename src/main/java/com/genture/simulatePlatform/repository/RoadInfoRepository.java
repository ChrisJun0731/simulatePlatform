package com.genture.simulatePlatform.repository;

import com.genture.simulatePlatform.model.RoadInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Administrator on 2018/1/5.
 */
public interface RoadInfoRepository extends MongoRepository<RoadInfo, Long> {
	/**
	 * 根据城市和道路编码查询道路信息
	 * @param cityId 城市id
	 * @param roadId 道路id
	 * @return
	 */
	public RoadInfo findByCityIdAndRoadId(String cityId, String roadId);

	/**
	 * 根据城市id、起始和终点坐标查询道路信息
	 * @param cityId 城市编号
	 * @param startPos 起始坐标
	 * @param endPos 终点坐标
	 * @return
	 */
	public RoadInfo findByCityIdAndStartPosAndEndPos(String cityId, String startPos, String endPos);
}
