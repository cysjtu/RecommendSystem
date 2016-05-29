package com.cy.backend.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cy.backend.vo.Item;

@Service
public class ActionService {

	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private ItemService itemService;
	
	
	private Map<String,Integer> actionTypeMap=new TreeMap<String,Integer>();
	
	{
		actionTypeMap.put("buy", 1);
		actionTypeMap.put("view", 2);
		actionTypeMap.put("rate", 3);
	}
	
	private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	
	
	public void saveAction(String actionType,Integer userId,Integer itemId,Double ratingValue,Item item){
		
		String sql1="insert into action(userId,itemId,actionTypeId,ratingValue,actionTime) values(?,?,?,?,?) ;";
		
		String actionTime=sdf1.format(new Date());
		
		jdbc.update(sql1, userId,itemId,actionTypeMap.get(actionType),ratingValue,actionTime);
		
		itemService.saveItem(item);
		
		
	}
	
}
