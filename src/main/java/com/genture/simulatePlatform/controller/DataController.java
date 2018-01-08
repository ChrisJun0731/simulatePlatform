package com.genture.simulatePlatform.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/1/5.
 */
@RestController
public class DataController {
	@RequestMapping("/gate")
	public String response(HttpServletRequest req, HttpServletResponse resp){
		String sid = req.getParameter("sid");
		String reqData = req.getParameter("reqData");
		System.out.println(reqData);
		//result用于存储返回结果
		String result = reqData;
		if(sid.equals("30012")){
			//获取道路状态
		}
		if(sid.equals("30011")){
			//获取道路长度及旅行时间

		}
		return result;
	}
}
