package com.genture.simulatePlatform.controller;

import com.genture.simulatePlatform.service.DataProcess;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

import com.genture.simulatePlatform.model.*;
import com.genture.simulatePlatform.dao.*;
import com.genture.simulatePlatform.repository.*;
/**
 * Created by Administrator on 2018/1/5.
 */
@RestController
public class DataController {
	@Autowired
	private RoadInfoDao roadInfoDao;
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
       // System.out.println(road);
	    String sid = req.getParameter("sid");
		String reqDate = req.getParameter("reqData");
		JSONObject jsonObject =new JSONObject(reqDate);
		//result用于存储返回结果
		String result = "";
		//
		int road_distance,
				unimpeded_distance,
				amble_distance,
				congestion_distance;
		int duration=0;
		if(sid.equals("30012")){
			//获取到请求地址中的city和roadId
			String cityId = jsonObject.getString("city");
			String roadId = jsonObject.getString("roadId");
			System.out.println("30012请求："+cityId+"---"+roadId);
			//获取道路状态
			result+="{\"sid\":"+sid;
			//获取道路信息
			RoadInfo roadInfo=roadInfoRepository.findByCityIdAndRoadId(cityId,roadId);
			RoadCondition condition=roadInfoDao.fetchValidRoadCondition(cityId,roadId);
			if(roadInfo!=null){
				road_distance=roadInfo.getRoadDistance();
				if(condition!=null) {
					unimpeded_distance = condition.getUnimpededDistance();
					amble_distance = condition.getAmbleDistance();
					congestion_distance = condition.getCongestionDistance();
				}else{
					DataProcess dataProcess = new DataProcess();
					JSONObject jsonObject1 = dataProcess.initData(cityId,roadId,road_distance);
					congestion_distance=jsonObject1.getInt("congestion");
					amble_distance=jsonObject1.getInt("amble");
					unimpeded_distance=jsonObject1.getInt("unimpeded");
				}
				result+=",\"status\":{\"code\":0,\"msg\":\"success\"},\"data\":[{\"status\":1,\"length\":"+unimpeded_distance+"},{\"status\":2,\"length\":"+amble_distance+
						"},{\"status\":3,\"length\":"+congestion_distance+"}]}";
			}else{
				System.out.println("30012请求：该城市编号还未添加，请管理员添加！");
				result+=",\"status\":{\"code\":1,\"msg\":\"miss\"}}";
			}
		}else if(sid.equals("30011")){
			//获取到请求地址中的city和startPos ,endPos
			String cityId = jsonObject.getString("city");
			String startPos = jsonObject.getString("startpos");
			String endPos = jsonObject.getString("endpos");
			System.out.println("30011请求："+cityId+"---"+startPos+"---"+endPos);
			//获取道路长度及旅行时间
			result+="{\"sid\":"+sid;

			RoadInfo roadInfo=roadInfoRepository.findByCityIdAndStartPosAndEndPos(cityId,startPos,endPos);
			if(roadInfo!=null) {
                String roadId = roadInfo.getRoadId();
                RoadCondition condition=roadInfoDao.fetchValidRoadCondition(cityId,roadId);
				road_distance = roadInfo.getRoadDistance();
				if (condition != null) {
					unimpeded_distance = condition.getUnimpededDistance();
					amble_distance = condition.getAmbleDistance();
					congestion_distance = condition.getCongestionDistance();
					//计算通行时间
					duration=time_consumming(unimpeded_distance,amble_distance,congestion_distance);
				} else {
					DataProcess dataProcess = new DataProcess();
					JSONObject jsonObject1 = dataProcess.initData(cityId,roadId,road_distance);
					congestion_distance=jsonObject1.getInt("congestion");
					amble_distance=jsonObject1.getInt("amble");
					unimpeded_distance=jsonObject1.getInt("unimpeded");
					//计算通行时间
					duration=time_consumming(unimpeded_distance,amble_distance,congestion_distance);
				}
				result+=",\"status\":{\"code\":0,\"msg\":\"success\"},\"data\":{\"status\":\"1\",\"info\":\"OK\",\"infocode\":\"10000\",\"count\":\"1\",\"route\":{\"paths\":[{\"distance\":\""+road_distance+"\",\"duration\":\""+duration+"\"}]}}}";
			}else{
				System.out.println("30011请求：该城市编号还未添加，请管理员添加！");
				result+=",\"status\":{\"code\":1,\"msg\":\"miss\"}}";
			}
		}else{
			result+="{\"sid\":"+sid+",\"status\":{\"code\":1,\"msg\":\"miss\"}}";
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
		duration=(int)((unimpeded/unimpeded_speed+amble/amble_speed+congestion/congestion_speed));
		return duration;
	}
}
