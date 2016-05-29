package com.cy.backend.controler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cy.backend.service.ActionService;
import com.cy.backend.service.RecommendService;
import com.cy.backend.vo.Item;
import com.cy.backend.vo.Recommendation;

@Controller
public class Common {
	
	private static final Logger log=LoggerFactory.getLogger(Common.class);
	
	@Autowired
	private ActionService actionService;
	
	@Autowired
	private RecommendService recommendService;
	
	
	

	@RequestMapping(value="/rate")
	@ResponseBody
	public String rate(HttpServletRequest req,HttpServletResponse resp,
			@RequestParam("userId") String userId,@RequestParam("itemid") String itemId,
			@RequestParam("ratingvalue") String ratingValue,
			@RequestParam("itemdescription") String itemDescription, @RequestParam("itemurl") String itemUrl,
			@RequestParam("itemimageurl") String itemImageUrl, @RequestParam("actiontime") String actionTime){
		
		//Item item=new Item(itemId, itemDescription, itemUrl, itemImageUrl);
		
		
		//actionService.saveAction("rate", Integer.valueOf(userId),  Integer.valueOf(itemId), Double.valueOf(ratingValue), item);
		return "{ \"ret\":\"ok\" }";
		
	}
	
	@RequestMapping(value="/buy")
	@ResponseBody
	public String buy(HttpServletRequest req,HttpServletResponse resp,
			@RequestParam("userId") String userId,@RequestParam("itemid") String itemId,
			@RequestParam("itemdescription") String itemDescription, @RequestParam("itemurl") String itemUrl,
			@RequestParam("itemimageurl") String itemImageUrl, @RequestParam("actiontime") String actionTime){
		//Item item=new Item(itemId, itemDescription, itemUrl, itemImageUrl);
		
		
		//actionService.saveAction("buy", Integer.valueOf(userId),  Integer.valueOf(itemId), 0.0, item);
		
		return "{ \"ret\":\"ok\" }";
	}

	
	@RequestMapping(value="/view")
	@ResponseBody
	public String view(HttpServletRequest req,HttpServletResponse resp,
			@RequestParam("userId") String userId,@RequestParam("itemid") String itemId,
			@RequestParam("itemdescription") String itemDescription, @RequestParam("itemurl") String itemUrl,
			@RequestParam("itemimageurl") String itemImageUrl, @RequestParam("actiontime") String actionTime){
		
		
		//Item item=new Item(itemId, itemDescription, itemUrl, itemImageUrl);
		
		
		//actionService.saveAction("view", Integer.valueOf(userId),  Integer.valueOf(itemId), 0.0, item);
		
		
		return "{ \"ret\":\"ok\" }";
	}

	
	//recommendation
	
	@RequestMapping(value="/recommend")
	@ResponseBody
	public String recommend(HttpServletRequest req,HttpServletResponse resp,
			@RequestParam("userid") String userId,
			@RequestParam("numberOfResults") Integer numberOfResults,
			@RequestParam("sourceTypeId") String sourceTypeId){
		
		Recommendation recom=recommendService.getRecommendation(Integer.valueOf(sourceTypeId), Integer.valueOf(userId), numberOfResults);
		
		
		ObjectMapper map=new ObjectMapper();
		
		String ret="{ \"ret\":\"fail\" }";
		
		try {
			ret=map.writeValueAsString(recom);
			log.info(ret);
			
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
		
		
		return ret;
	}
	
	
	@RequestMapping(value="/index")
	public ModelAndView index(HttpServletRequest req,HttpServletResponse resp){
		ModelAndView mv=new ModelAndView("index");
		
		
		
		return mv;
		
	}

	
}
