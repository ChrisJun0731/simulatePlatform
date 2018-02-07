package com.genture.simulatePlatform.dao;

import com.genture.simulatePlatform.model.RoadCondition;
import com.genture.simulatePlatform.model.RoadInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2018/1/6.
 */
@Component
public class RoadInfoDao {

    @Autowired
    private MongoTemplate mongoTemplate;

	/**
	 * 添加有效的路况信息
	 * @param cityId 城市id
	 * @param roadId 道路id
	 * @param roadCondition 路况
	 */
    public void addRoadCondition(String cityId, String roadId, RoadCondition roadCondition){
    	//先将之前有效的路况信息置为无效
        Query validRoadConditionQuery = Query.query(Criteria.where("cityId").is(cityId)
				.and("roadId").is(roadId).and("roadConditions.status").is(true));
        Update invalidRoadConditionUpdate = new Update().set("roadConditions.$.status", false);
        mongoTemplate.updateFirst(validRoadConditionQuery, invalidRoadConditionUpdate,
				"roadInfo");

        //添加新的路况信息并置为有效
	    Query roadQuery = Query.query(Criteria.where("cityId").is(cityId).and("roadId").is(roadId));
	    Update addRoadConditionUpdate = new Update().push("roadConditions", roadCondition);
	    mongoTemplate.updateFirst(roadQuery, addRoadConditionUpdate, "roadInfo");
    }
	/**
	 * 初始化有效的路况信息
	 * @param cityId 城市id
	 * @param roadId 道路id
	 * @param roadCondition 路况
	 */
	public void insertRoadCondition(String cityId, String roadId, RoadCondition roadCondition){
		//添加新的路况信息并置为有效
		Query roadQuery = Query.query(Criteria.where("cityId").is(cityId).and("roadId").is(roadId));
		Update addRoadConditionUpdate = new Update().push("roadConditions", roadCondition);
		System.out.println(cityId+"-"+roadId+"-"+roadCondition.getAmbleDistance()+"/"+roadCondition.getCongestionDistance()+"/"+roadCondition.getUnimpededDistance());
		mongoTemplate.updateFirst(roadQuery, addRoadConditionUpdate, "roadInfo");
	}

	/**
	 * 获取有效的路况信息
	 * @param cityId 城市id
	 * @param roadId 道路id
	 * @return
	 */
    public RoadCondition fetchValidRoadCondition(String cityId, String roadId){
		Query query = Query.query(Criteria.where("cityId").is(cityId).and("roadId").is(roadId));
		RoadInfo roadInfo = mongoTemplate.findOne(query, RoadInfo.class);
		RoadCondition roadCondition = null;
		if(roadInfo != null){
			List<RoadCondition> roadConditionList = roadInfo.getRoadCondition();
			for(int i=0; i<roadConditionList.size(); i++){
				if(roadConditionList.get(i).isStatus() == true){
					roadCondition = roadConditionList.get(i);
				}
			}
		}
		return roadCondition;
	}
}
