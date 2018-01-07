package com.genture.simulatePlatform.dao;

import com.genture.simulatePlatform.model.RoadCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by Administrator on 2018/1/6.
 */
public class RoadInfoDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void addRoadCondition(String cityId, String roadId, RoadCondition roadCondition){
    	//先将之前有效的路况信息置为无效
        Query validRoadConditionQuery = Query.query(Criteria.where("cityId").is(cityId).and("roadId").is(roadId).and("roadCondtions.status").is(true));
        Update invalidRoadConditionUpdate = new Update().set("roadConditions.$.status", false);
        mongoTemplate.updateFirst(validRoadConditionQuery, invalidRoadConditionUpdate, "roadInfo");

        //添加新的路况信息并置为有效
	    Query roadQuery = Query.query(Criteria.where("city").is(cityId).and("roadId").is(roadId));
	    Update addRoadConditionUpdate = new Update().push("roadConditions", roadCondition);
	    mongoTemplate.updateFirst(roadQuery, addRoadConditionUpdate, "roadInfo");
    }
}
