import com.genture.simulatePlatform.SimulatePlatformApplication;
import com.genture.simulatePlatform.dao.RoadInfoRepository;
import com.genture.simulatePlatform.model.RoadCondition;
import com.genture.simulatePlatform.model.RoadInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Administrator on 2018/1/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SimulatePlatformApplication.class)
public class ApplicationTest {
	@Autowired
	private RoadInfoRepository roadInfoRepository;

	@Before
	public void setUp(){

	}

	@Test
	public void save(){
		RoadInfo roadInfo = new RoadInfo();
		roadInfo.setCityId("1101");
		roadInfo.setRoadId("33");
		roadInfo.setStartPos("111,222");
		roadInfo.setEndPos("333,444");
		roadInfo.setRoadDistance(1500);
		roadInfo.setRoadCondition(new RoadCondition(600, 200, 700));

		roadInfoRepository.insert(roadInfo);

	}

	@Test
	public void find(){
		RoadInfo roadInfo = roadInfoRepository.findByCityIdAndRoadId("1101", "33");

	}
}
