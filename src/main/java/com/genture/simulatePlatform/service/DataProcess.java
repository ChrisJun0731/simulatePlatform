package com.genture.simulatePlatform.service;


import com.genture.simulatePlatform.dao.RoadInfoDao;
import com.genture.simulatePlatform.model.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.Random;

import java.util.List;

public class DataProcess {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private RoadInfoDao roadInfoDao;

	private int congestion_distance;
	private int amble_distance;
	private int rest_distance;
	private int unimpeded_distance;
	public DataProcess(){
		roadInfoDao = new RoadInfoDao();
	}
	public JSONObject initData(String cityId, String roadId, int road_distance){
		JSONObject jsonObject=new JSONObject();
		Random random = new Random();
		congestion_distance=random.nextInt(road_distance+1);
		rest_distance=road_distance-congestion_distance;
		amble_distance=random.nextInt(rest_distance+1);
		unimpeded_distance=rest_distance-amble_distance;
		try{
			//返回的数据给接口
			jsonObject.put("congestion",congestion_distance);
			jsonObject.put("amble",amble_distance);
			jsonObject.put("unimpeded",unimpeded_distance);
		}catch(JSONException e){
			System.out.println("初始化json格式错误："+e.getMessage());
		}catch(Exception e1){
			System.out.println("初始化数据错误："+e1.getMessage());
		}
		//执行插入语句
		RoadCondition conditionInfo=new RoadCondition(unimpeded_distance,amble_distance,congestion_distance,true);
		roadInfoDao.insertRoadCondition(cityId,roadId,conditionInfo);
		return jsonObject;
	}
}
