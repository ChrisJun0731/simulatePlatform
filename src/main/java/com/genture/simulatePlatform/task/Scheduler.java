package com.genture.simulatePlatform.task;

import com.genture.simulatePlatform.dao.RoadInfoDao;
import com.genture.simulatePlatform.model.RoadCondition;
import com.genture.simulatePlatform.model.RoadInfo;
import com.genture.simulatePlatform.service.DataProcess;
import com.genture.simulatePlatform.service.RandomData;
import com.mongodb.Block;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.security.cert.Certificate;
import java.util.List;

/**
 * Created by Administrator on 2018/1/12.
 */
@Component
public class Scheduler {
	@Autowired
	private RoadInfoDao roadInfoDao;

	private int congestion_distance;
	private int amble_distance;
	private int rest_distance;
	private int unimpeded_distance;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Scheduled(cron="0 0/5 * * * ?")
	public void updateRoadCond(){
		final DataProcess dataProcess = new DataProcess();
		//更新所有的路况
		List<RoadInfo> roadInfoList = mongoTemplate.findAll(RoadInfo.class);
		if(roadInfoList!=null){
			for (RoadInfo info : roadInfoList) {
				JSONObject data;
				JSONObject json;
				String cityId = info.getCityId();
				String roadId = info.getRoadId();
				int distance = info.getRoadDistance();
				RoadCondition condition=roadInfoDao.fetchValidRoadCondition(cityId,roadId);
				if(condition!=null){
					//更换数据，
					data=new JSONObject();
					try{
						data.put("distance",distance);
						data.put("unimpeded",condition.getUnimpededDistance());
						data.put("amble",condition.getAmbleDistance());
						data.put("congestion",condition.getCongestionDistance());

						RandomData randomData = new RandomData();
						json=randomData.updateData(data);
						congestion_distance= json.getInt("congestion");
						amble_distance=json.getInt("amble");
						unimpeded_distance=json.getInt("unimpeded");
					}catch(JSONException e){
						System.out.println("数据生成json格式错误："+e.getMessage());
					}catch(Exception e1){
						System.out.println("数据生成错误："+e1.getMessage());
					}
					RoadCondition conditionInfo=new RoadCondition(unimpeded_distance,amble_distance,congestion_distance,true);
					roadInfoDao.addRoadCondition(cityId,roadId,conditionInfo);
				}else{
					dataProcess.initData(cityId,roadId,distance);
				}
			}
		}else{
			System.out.println("无数据！请添加数据");
		}
	}
}
