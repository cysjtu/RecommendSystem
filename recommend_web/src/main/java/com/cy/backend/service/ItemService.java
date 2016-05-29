package com.cy.backend.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cy.backend.vo.Item;

@Service
public class ItemService {

	@Autowired
	private JdbcTemplate jdbc;
	
	private SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	

	
	public  void saveItem(Item item){
		
		String sql1="select count(*) from `BX-Books` where ISBN=?  ;";
		
		String sql2="insert into `BX-Books`(ISBN,`Book-Title`,`Book-Author`,`Year-Of-Publication`,`Publisher`,`Image-URL-S`,`Image-URL-M`,`Image-URL-L`) values(?,?,?,?,?,?,?,?) ;";
		
		String sql3="update `BX-Books` set `Book-Title`=?,`Book-Author`=?,`Year-Of-Publication`=?,`Publisher`=?,`Image-URL-S`=?,`Image-URL-M`=?,`Image-URL-L`=?  where ISBN=? ;";
		
		
		
		int cnt=jdbc.queryForInt(sql1);
		
		String date=sdf1.format(new Date());
		
		if(cnt==0){
			
			
			jdbc.update(sql2, item.getItemid(),item.getBook_Title(),item.getBook_Author(),item.getYear_Of_Publication(),item.getPublisher(),item.getImage_URL_S(),item.getImage_URL_M(),item.getImage_URL_L());
			
			
		}
		else{
			jdbc.update(sql3, item.getBook_Title(),item.getBook_Author(),item.getYear_Of_Publication(),item.getPublisher(),item.getImage_URL_S(),item.getImage_URL_M(),item.getImage_URL_L(),item.getItemid());
			
			
		}
		
	}

	
	public Item getItem(String ItemId){
		String sql1="select * from `BX-Books` where ISBN=? ;";
		
		List<Map<String,Object>> temp=jdbc.queryForList(sql1, ItemId);
		
		if(null!=temp && temp.size()>0){
			Map<String,Object> m=temp.get(0);
			
			Item item=new Item();
			item.setItemid(ItemId+"");
			item.setBook_Title((String)m.get("Book-Title"));
			item.setBook_Author((String)m.get("Book-Author"));
			item.setYear_Of_Publication((Long)m.get("Year-Of-Publication")+"");
			item.setPublisher((String)m.get("Publisher"));
			item.setImage_URL_S((String)m.get("Image-URL-S"));
			item.setImage_URL_M((String)m.get("Image-URL-M"));
			item.setImage_URL_L((String)m.get("Image-URL-L"));
			
			
			return item;
		}
		
		return null;
		
	}
}
