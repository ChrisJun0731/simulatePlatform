package com.genture.simulatePlatform.service;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Random;

public class RandomData {
    public RandomData(){

    }
    public JSONObject updateData(JSONObject data) throws JSONException{
        JSONObject json=new JSONObject();
        int unimpeded_distance=0,
                amble_distance=0,
                congestion_distance=0,
                rest_distance=0;
        double upper_limit;
        //取拥堵随机值
        upper_limit = getUpper(data.getInt("distance"),data.getInt("congestion"));
        congestion_distance = getValue(data.getInt("congestion"),upper_limit,data.getInt("distance"));
        rest_distance = data.getInt("distance")-congestion_distance;
        // 取缓行随机值
        upper_limit = getUpper(rest_distance,data.getInt("amble"));
        amble_distance = getValue(data.getInt("amble"),upper_limit,rest_distance);
        rest_distance = rest_distance-amble_distance;
        //畅行随机值
        unimpeded_distance=rest_distance;

        json.put("unimpeded",unimpeded_distance);
        json.put("amble",amble_distance);
        json.put("congestion",congestion_distance);
        return json;
    }
    public double getUpper(int distance,int condition){
        double upper_limit=0.0,upper=0.0;
        if (condition==0) {
            upper=-1;
        }else{
            upper_limit=(float)(distance-condition)/condition;
            BigDecimal bd = new BigDecimal(upper_limit);
            upper = bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return upper;
    }
    public int getValue(int original,double upper_limit,int distance){
        int value=0;
        double upper;
        Random random=new Random();
        if(upper_limit==-1){
            value=random.nextInt(distance+1);
        }else{
            int rand_1 = random.nextInt((int)(upper_limit+1)*100+1);
            BigDecimal bd = new BigDecimal((float)rand_1/100);
            double rand_2 = bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            BigDecimal bd_1 = new BigDecimal(rand_2-1);
            upper = bd_1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            value =(int)(upper*original+original);
        }
        return value;
    }
}