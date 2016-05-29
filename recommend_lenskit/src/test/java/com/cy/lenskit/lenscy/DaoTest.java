package com.cy.lenskit.lenscy;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cy.lenskit.lenscy.dao.IdMapingDao;
import com.cy.lenskit.lenscy.dao.RatingDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:application-context.xml")
public class DaoTest {
	
	@Autowired
	RatingDao rating;
	
	@Autowired
	IdMapingDao idmap;
	
	

	@Test
	@Ignore
	public void test1(){
		//idmap.doMap("`BX-Books`", "ISBN");
		
		
		
		rating.processRawActions();
		
	}
	
	@Test
	public void db2file(){
		try {
			rating.db2file("bx_rating");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
