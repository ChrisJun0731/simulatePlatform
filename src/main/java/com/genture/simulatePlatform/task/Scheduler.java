package com.genture.simulatePlatform.task;

import com.genture.simulatePlatform.model.RoadInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/1/12.
 */
@Component
public class Scheduler {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Scheduled(cron="0 0/1 * * * ?")
	public void updateRoadCond(){
		//更新所有的路况

	}
}
