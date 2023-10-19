package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.SensorDAO;
import com.example.demo.dao.dataReciever;
import com.example.demo.model.data;
import com.example.demo.model.historydata;

@CrossOrigin
@RestController
public class sensorControl {
	private SensorDAO dao = new SensorDAO();
	private dataReciever reciever= new dataReciever();
	@GetMapping("/datas")
	public List<data> getDatas(){
		List<data> datas = dao.selectAllRecentData();
		return  datas;
		
	}
	
	@GetMapping("sensor/datas")
	public List<data> getAllDatas(){
		List<data> datas = dao.selectAllSensorData();
		return  datas;
		
	}
	
	@GetMapping("/history")
	public List<historydata> getHistoryData(){
		List<historydata> historyDatas = dao.selectAllHistoryData();
		return  historyDatas;
		
	}
	
	@GetMapping("/onChange/{device}")
	public boolean changeLight(@PathVariable String device){
		boolean change = dao.publishMessage(Integer.parseInt(device));
		return  change;
		
	}
}
