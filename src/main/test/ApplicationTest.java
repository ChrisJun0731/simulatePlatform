import com.genture.simulatePlatform.SimulatePlatformApplication;
import com.genture.simulatePlatform.dao.RoadInfoDao;
import com.genture.simulatePlatform.repository.RoadInfoRepository;
import com.genture.simulatePlatform.model.RoadCondition;
import com.genture.simulatePlatform.model.RoadInfo;
import com.mongodb.CommandResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SimulatePlatformApplication.class)
public class ApplicationTest {
	@Autowired
	private RoadInfoRepository roadInfoRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private RoadInfoDao roadInfoDao;

	@Before
	public void setUp(){

	}

	@Test
	public void save(){
		RoadInfo roadInfo = new RoadInfo();
		roadInfo.setCityId("1101");
		roadInfo.setRoadId("36");
		roadInfo.setStartPos("111,222");
		roadInfo.setEndPos("333,444");
		roadInfo.setRoadDistance(1500);
		List roadConditions = new ArrayList();
//		roadConditions.add(new RoadCondition(600, 200, 700, true));
		roadInfo.setRoadConditions(roadConditions);
		roadInfoRepository.insert(roadInfo);

	}

	@Test
	public void find(){
		RoadInfo roadInfo = roadInfoRepository.findByCityIdAndRoadId("1101", "33");

	}

	@Test
	public void query(){
		Query query =  Query.query(Criteria.where("roadId").is("33"));
		long count = mongoTemplate.count(query, "roadInfo");
		System.out.println(count);
	}

	@Test
	public void update(){
//		RoadCondition roadCondition = new RoadCondition(100, 500, 900, false);
//		Query query = Query.query(Criteria.where("roadId").is("36"));
		Query query = Query.query(Criteria.where("roadId").is("36").and("roadConditions.status").is(true));
//		Update update = new Update().push("roadConditions", roadCondition);
		Update update = new Update().set("roadConditions.$.status", false);
		mongoTemplate.updateFirst(query, update, "roadInfo");
	}

	@Test
	public void addRoadConditionTest(){
		RoadCondition roadCondition = new RoadCondition(500, 500, 500, true);
		roadInfoDao.addRoadCondition("1101", "36", roadCondition);
	}

	@Test
	public void fetchValidRoadConditionTest(){
		Query query = new Query().addCriteria(Criteria.where("roadId").is("36"));
		RoadInfo roadInfo = mongoTemplate.findOne(query, RoadInfo.class);
		List<RoadCondition> roadConditions = roadInfo.getRoadCondition();
		RoadCondition roadCondition = null;
		for(int i=0; i<roadConditions.size(); i++){
			if(roadConditions.get(i).isStatus() == true){
				roadCondition = roadConditions.get(i);
			}
		}
		System.out.println(roadCondition.isStatus());
	}

}
