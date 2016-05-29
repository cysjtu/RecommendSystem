package com.cy.backend.vo;

import java.util.List;

public class Recommendation {

	private String userId;
	private Item item;
    private List<Item> recommendedItems;
    
    
    public Recommendation(){
    	
    }
    
    
	public Recommendation(String userId, Item item, List<Item> recommendedItems) {
		super();
		this.userId = userId;
		this.item = item;
		this.recommendedItems = recommendedItems;
	}
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public List<Item> getRecommendedItems() {
		return recommendedItems;
	}
	public void setRecommendedItems(List<Item> recommendedItems) {
		this.recommendedItems = recommendedItems;
	}
    
    
}
