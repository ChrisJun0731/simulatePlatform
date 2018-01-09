package com.genture.simulatePlatform.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.genture.simulatePlatform.model.*;
import com.genture.simulatePlatform.dao.*;
import com.genture.simulatePlatform.repository.*;
import com.genture.simulatePlatform.service.RandomData;
/**
 * Created by Administrator on 2018/1/5.
 */
@RestController
public class DataController {
    @Autowired
    private MongoTemplate mongoTemplate;
	@Autowired
	private RoadInfoDao roadInfoDao=null;

	@Autowired
	private RoadInfoRepository roadInfoRepository;

	private final int UNIMPEDED_SPEED=85;
	private final int AMBLE_SPEED=55;
	private final int CONGESTION_SPEED=25;

	public DataController(){

		roadInfoDao = new RoadInfoDao();
	}
	@RequestMapping("/gate")
	public String response(HttpServletRequest req, HttpServletResponse resp) throws JSONException {
        //数据会初始化、插入数据
      /* List<RoadCondition> roadCondition = new ArrayList<RoadCondition>();
        RoadInfo road = new RoadInfo(
                "0010",
                "100010",
                "132.45",
                "189.23",
                roadCondition,
                1400
        );
        mongoTemplate.insert(road);
        System.out.println(road);*/

	    String sid = req.getParameter("sid");
		String reqDate = req.getParameter("reqData");
		JSONObject jsonObject =new JSONObject(reqDate);
		//result用于存储返回结果
		String result = "";
		//
		int road_distance=0,
				unimpeded_distance=0,
				amble_distance=0,
				congestion_distance=0,
				rest_distance=0;
		int duration=0;
		if(sid.equals("30012")){
			//获取到请求地址中的city和roadId
			String cityId = jsonObject.getString("city");
			String roadId = jsonObject.getString("roadId");
			//获取道路状态
			result+="{sid:"+sid;
			//获取道路信息
			RoadInfo roadInfo=roadInfoRepository.findByCityIdAndRoadId(cityId,roadId);
			RoadCondition condition=roadInfoDao.fetchValidRoadCondition(cityId,roadId);
			if(roadInfo!=null){
				road_distance=roadInfo.getRoadDistance();
				if(condition!=null) {
					//更换数据，
					JSONObject data=new JSONObject();
					data.put("distance",road_distance);
					data.put("unimpeded",condition.getUnimpededDistance());
					data.put("amble",condition.getAmbleDistance());
					data.put("congestion",condition.getCongestionDistance());

					RandomData randomData = new RandomData();
					JSONObject json=randomData.updateData(data);

					unimpeded_distance = json.getInt("unimpeded");
					amble_distance = json.getInt("amble");
					congestion_distance = json.getInt("congestion");

					//将随机产生的数值插入数据库
					RoadCondition conditionInfo=new RoadCondition(unimpeded_distance,amble_distance,congestion_distance,true);
					roadInfoDao.addRoadCondition(cityId,roadId,conditionInfo);
				}else{
					Random random = new Random();
					congestion_distance=random.nextInt(road_distance+1);
					rest_distance=road_distance-congestion_distance;
					amble_distance=random.nextInt(rest_distance+1);
					unimpeded_distance=rest_distance-amble_distance;
					RoadCondition conditionInfo=new RoadCondition(unimpeded_distance,amble_distance,congestion_distance,true);
					roadInfoDao.addRoadCondition(cityId,roadId,conditionInfo);
				}
				result+=",status:{code:0,msg:success},data:[{status:1,length:"+unimpeded_distance+"},{status:2,length:"+amble_distance+
						"},{status:3,length:"+congestion_distance+"}]}";
			}else{
				System.out.println("该城市编号还未添加，请管理员添加！");
				result+=",status:{code:1,msg:miss},";
			}
		}else if(sid.equals("30011")){
			//获取到请求地址中的city和startPos ,endPos
			String cityId = jsonObject.getString("city");
			String startPos = jsonObject.getString("startpos");
			String endPos = jsonObject.getString("endpos");
			//获取道路长度及旅行时间
			result+="{sid:"+sid;

			RoadInfo roadInfo=roadInfoRepository.findByCityIdAndStartPosAndEndPos(cityId,startPos,endPos);
			//String roadId = roadInfo.getRoadId();
			//RoadCondition condition=roadInfoDao.fetchValidRoadCondition(cityId,roadId);
			if(roadInfo!=null) {
                String roadId = roadInfo.getRoadId();
                RoadCondition condition=roadInfoDao.fetchValidRoadCondition(cityId,roadId);
				road_distance = roadInfo.getRoadDistance();
				if (condition != null) {
					//更换数据，
					JSONObject data = new JSONObject();
					data.put("distance", road_distance);
					data.put("unimpeded", unimpeded_distance);
					data.put("amble", amble_distance);
					data.put("congestion", congestion_distance);

					RandomData randomData = new RandomData();
					JSONObject json = randomData.updateData(data);

					unimpeded_distance = json.getInt("unimpeded");
					amble_distance = json.getInt("amble");
					congestion_distance = json.getInt("congestion");
					//计算通行时间
					duration=time_consumming(unimpeded_distance,amble_distance,congestion_distance);
					//将随机产生的数值插入数据库
					RoadCondition conditionInfo=new RoadCondition(unimpeded_distance,amble_distance,congestion_distance,true);
					roadInfoDao.addRoadCondition(cityId,roadId,conditionInfo);
				} else {
					Random random = new Random();
					congestion_distance = random.nextInt(road_distance + 1);
					rest_distance = road_distance - congestion_distance;
					amble_distance = random.nextInt(rest_distance + 1);
					unimpeded_distance = rest_distance - amble_distance;
					//计算通行时间
					duration=time_consumming(unimpeded_distance,amble_distance,congestion_distance);

					RoadCondition conditionInfo=new RoadCondition(unimpeded_distance,amble_distance,congestion_distance,true);
					roadInfoDao.addRoadCondition(cityId,roadId,conditionInfo);
				}
				result+=",status:{code:0,msg:success},paths:[{distance:"+road_distance+",duration:"+duration+"}]}";
			}else{
				System.out.println("该城市编号还未添加，请管理员添加！");
				result+=",status:{code:1,msg:miss},";
			}
		}else{
			result+="无此值";
		}
		return result;
	}
	public int time_consumming(int unimpeded,int amble,int congestion){
		int duration=0;
		//速度km/h,换成m/s,方便操作
		BigDecimal bd_unimpeded=new BigDecimal((float)UNIMPEDED_SPEED*1000/(60*60));
		Double unimpeded_speed=bd_unimpeded.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		BigDecimal bd_amble=new BigDecimal((float)AMBLE_SPEED*1000/(60*60));
		Double amble_speed=bd_amble.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		BigDecimal bd_congestion=new BigDecimal((float)CONGESTION_SPEED*1000/(60*60));
		Double congestion_speed=bd_congestion.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

		//计算时间
		duration=(int)((unimpeded/unimpeded_speed+amble/amble_speed+congestion/congestion_speed)*1000);
		return duration;
	}
}
