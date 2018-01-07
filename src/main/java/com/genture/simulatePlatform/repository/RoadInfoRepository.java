package com.genture.simulatePlatform.repository;

import com.genture.simulatePlatform.model.RoadInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Administrator on 2018/1/5.
 */
public interface RoadInfoRepository extends MongoRepository<RoadInfo, Long> {
	public RoadInfo findByCityIdAndRoadId(String cityId, String roadId);
}
