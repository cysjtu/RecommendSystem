package com.cy.backend.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cy.backend.vo.Item;
import com.cy.backend.vo.Recommendation;

@Service
public class RecommendService {

	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private ItemService itemService;
	
	private Map<Integer,String> srcTypeMap=new HashMap<Integer, String>();
	{
		srcTypeMap.put(1, "hybrid");
		
	}

	private List<Item> filterActedItems(Integer userId,List<Item> items){
		
		return items;
	}
	
	public Recommendation getRecommendation(Integer sourceTypeId,Integer userId,Integer numberOfResults){
		
		String sql="select * from recommend where user_id=? and source=? order by score desc limit 0,? ;";
		
		
		
		List<Map<String,Object>> temp=jdbc.queryForList(sql, userId,srcTypeMap.get(sourceTypeId),numberOfResults);
		
		List<Item> items=new ArrayList<Item>();
		
		for(Map<String,Object> m:temp){
			Item item=itemService.getItem((String)(m.get("item_id")));
			
			item.setExplain((String)(m.get("explain")));
			
			
			if(null!=item)
				items.add(item);
		}
		
		
		Recommendation rec=new Recommendation();
		rec.setRecommendedItems(items);
		rec.setUserId(userId+"");
		
		return rec;
		
		
	}
	
	public static void main(String [] args){
		ObjectMapper map=new ObjectMapper();
		
		Map<String,Object> m=new HashMap<String, Object>();
		
		m.put("1", "1");
		m.put("2", "2");
		m.put("3", "3");
		m.put("4", "4");
		m.put("5", "5");
		
		Item item=new Item();
		
		
		
		List<Item> lst=new ArrayList<Item>();
		
		lst.add(item);
		lst.add(item);
		
		m.put("213123123", lst);
		
		Recommendation rec=new Recommendation();
		rec.setRecommendedItems(lst);
		rec.setUserId("21");
		
		try {
			String res=map.writeValueAsString(m);
			System.err.println(res);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
}
