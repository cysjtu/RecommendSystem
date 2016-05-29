package com.cy.backend.vo;

public class Item {

	private String itemid;

	private String Book_Title,Book_Author,Year_Of_Publication,Publisher,Image_URL_S,Image_URL_M,Image_URL_L;
	
	
	private String explain;


	
	public Item(){
		
	}
	
	
	
	
	
	
	public Item(String itemid, String book_Title, String book_Author, String year_Of_Publication, String publisher,
			String image_URL_S, String image_URL_M, String image_URL_L) {
		super();
		this.itemid = itemid;
		Book_Title = book_Title;
		Book_Author = book_Author;
		Year_Of_Publication = year_Of_Publication;
		Publisher = publisher;
		Image_URL_S = image_URL_S;
		Image_URL_M = image_URL_M;
		Image_URL_L = image_URL_L;
	}

	

	public String getItemid() {
		return itemid;
	}


	public void setItemid(String itemid) {
		this.itemid = itemid;
	}


	public String getBook_Title() {
		return Book_Title;
	}


	public void setBook_Title(String book_Title) {
		Book_Title = book_Title;
	}


	public String getBook_Author() {
		return Book_Author;
	}


	public void setBook_Author(String book_Author) {
		Book_Author = book_Author;
	}


	public String getYear_Of_Publication() {
		return Year_Of_Publication;
	}


	public void setYear_Of_Publication(String year_Of_Publication) {
		Year_Of_Publication = year_Of_Publication;
	}


	public String getPublisher() {
		return Publisher;
	}


	public void setPublisher(String publisher) {
		Publisher = publisher;
	}


	public String getImage_URL_S() {
		return Image_URL_S;
	}


	public void setImage_URL_S(String image_URL_S) {
		Image_URL_S = image_URL_S;
	}


	public String getImage_URL_M() {
		return Image_URL_M;
	}


	public void setImage_URL_M(String image_URL_M) {
		Image_URL_M = image_URL_M;
	}


	public String getImage_URL_L() {
		return Image_URL_L;
	}


	public void setImage_URL_L(String image_URL_L) {
		Image_URL_L = image_URL_L;
	}


	public String getExplain() {
		return explain;
	}


	public void setExplain(String explain) {
		this.explain = explain;
	}
	
	
	
	
	
	
	
}
