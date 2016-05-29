package com.cy.lenskit.lenscy.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.grouplens.lenskit.vectors.ImmutableSparseVector;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.la4j.Vector;
import org.la4j.iterator.VectorIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RatingDao {

	@Autowired
	private IdMapingDao idmaping;
	
	@Autowired
	private JdbcTemplate jdbc;
	
	public static Logger log=LoggerFactory.getLogger(RatingDao.class);
	
	
	public void processRawActions(){
		String sql="select * from `BX-Book-Ratings` ;";
		//String sql2="insert into action_processed(user_id,item_id,rate) values(?,?,?) ;";
		
		List<Map<String,Object>> res=jdbc.queryForList(sql);
		
		StringBuilder sb=new StringBuilder();
		sb.append("insert into action_processed(user_id,item_id,rate) values ");
		
		for(Map<String,Object> m:res){
			Integer User_ID=(Integer) m.get("User-ID");
			String ISBN=(String) m.get("ISBN");
			Integer rate=(Integer) m.get("Book-Rating");
			
			Integer i_ISBN=idmaping.String2IdCatched(ISBN);
			
			if(null!=i_ISBN && null!=User_ID && null!=rate){
				
				sb.append("(").append(User_ID).append(",").append(i_ISBN).append(",")
				.append(Double.valueOf(rate)).append("),");
				
			}
			else{
				log.error(User_ID+" "+ISBN+" "+rate+" "+i_ISBN);
				
			}
			
		}
		
		
		try{
			sb.setCharAt(sb.length()-1, ';');
			
			jdbc.update(sb.toString());
			
			}catch(Exception e){
				log.error(e.getMessage());
				
			}
		
		
	}
	
	
	public void db2file(String path) throws IOException{
		
		String sql="select * from `BX-Book-Ratings` order by `User-ID`,`ISBN`;";
		//String sql2="insert into action_processed(user_id,item_id,rate) values(?,?,?) ;";
		
		List<Map<String,Object>> res=jdbc.queryForList(sql);
		
		StringBuilder explicit=new StringBuilder();
		StringBuilder implicit=new StringBuilder();
		
		
		
		Map<Integer,Integer> usermap=new HashMap<>(200000);
		Map<String,Integer> itemmap=new HashMap<>(200000);
		
		for(Map<String,Object> m:res){
			Integer User_ID=(Integer) m.get("User-ID");
			String ISBN=(String) m.get("ISBN");
			Integer rate=(Integer) m.get("Book-Rating");
			Integer uid,itemId;
			
			if(usermap.containsKey(User_ID)){
				uid=usermap.get(User_ID);
			}
			else{
				uid=usermap.size();
				usermap.put(User_ID, uid);
			}
			
			if(itemmap.containsKey(ISBN)){
				itemId=itemmap.get(ISBN);
			}
			else{
				itemId=itemmap.size();
				itemmap.put(ISBN, itemId);
			}

			if(0==rate){
				implicit.append(uid).append(" ").append(itemId).append(" ").append(rate).append("\n");
			}
			else{
				explicit.append(uid).append(" ").append(itemId).append(" ").append(rate).append("\n");
			}
			
			
			
		}
		
		
		File file_expli=new File(path+"_explicit.txt");
		File file_implicit=new File(path+"_implicit.txt");
		
		
		file_expli.delete();
		file_implicit.delete();
		
		
		
		try {
			file_expli.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(file_expli));
			out.write(explicit.toString());
			out.close();
			
			file_implicit.createNewFile();
			BufferedWriter out2 = new BufferedWriter(new FileWriter(file_implicit));
			out2.write(implicit.toString());
			out2.close();
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File file_mapper_user=new File(path+"_idmap_user.txt");
		File file_mapper_item=new File(path+"_idmap_item.txt");
		file_mapper_user.delete();
		file_mapper_item.delete();
		
		StringBuilder idmap_user=new StringBuilder();
		StringBuilder idmap_item=new StringBuilder();
		//map user
		file_mapper_user.createNewFile();
		BufferedWriter out_user = new BufferedWriter(new FileWriter(file_mapper_user));
		BufferedWriter out_item = new BufferedWriter(new FileWriter(file_mapper_item));
		
		idmap_user.append("raw_user,inner_user\n");
		
		for(Entry<Integer, Integer> en:usermap.entrySet()){
			idmap_user.append(en.getKey()+","+en.getValue()+"\n");
		}
		
		out_user.write(idmap_user.toString());
		out_user.close();
		
		
		idmap_item.append("raw_item,inner_itemr\n");
		
		for(Entry<String, Integer> en:itemmap.entrySet()){
			idmap_item.append(en.getKey()+","+en.getValue()+"\n");
		}
		
		out_item.write(idmap_item.toString());
		out_item.close();
		
	}
	
	
	
	
	/////////
	
	public Map<Long, SparseVector> getUserMatrix(String table,String userCol,String itemCol,String rateCol){
		
		List<Map<String,Object>> ratings=getTestSet(table, userCol, itemCol, rateCol);
		
		Map<Long,SparseVector> matrix=new LinkedHashMap<Long,SparseVector>(20000,0.75f,false);
		
		long userId=0,itemId=0;
		double rate=0;
		
		Map<Long,Map<Long,Double>> temp_matrix=new HashMap<Long,Map<Long,Double>>(20000,0.75f);
		
		
		for(Map<String,Object> r:ratings){
			
			
			userId =(long) ((Integer) r.get("user_id")).intValue();
			
			itemId =(long)((Integer) r.get("item_id")).intValue();
			
			rate =((Double) r.get("rate")).floatValue();
			
			if(rate==0.0) rate=1.0;
			
			if(!temp_matrix.containsKey(userId)){
				Map<Long,Double> temp=new HashMap<>();
				temp_matrix.put(userId, temp);
			}
			
			temp_matrix.get(userId).put(itemId, rate);
			

		}
		
		for(Entry<Long,Map<Long,Double>> e:temp_matrix.entrySet()){
			userId=e.getKey();
			Map<Long,Double> item2score=e.getValue();
			
			ImmutableSparseVector msv = ImmutableSparseVector.create(item2score);
			
			matrix.put(userId, msv);
			
			
		}
		
		
		return matrix;
		
		
		
	}
	
	public Map<Long, SparseVector> change2ItemMatrix(Map<Long, SparseVector> userScores){
		
		Map<Long,Map<Long,Double>> temp_matrix=new HashMap<Long,Map<Long,Double>>(userScores.size(),0.75f);
		
		Map<Long,SparseVector> matrix=new LinkedHashMap<Long,SparseVector>(userScores.size(),0.75f,false);
		
		
		long userId,ItemId;
		double score;
		
		for(Entry<Long, SparseVector> e:userScores.entrySet()){
			userId=e.getKey();
			SparseVector vec=e.getValue();
			
			for(VectorEntry ve:vec){
				ItemId=ve.getKey();
				score=ve.getValue();
				
				if(!temp_matrix.containsKey(ItemId)){
					Map<Long,Double> temp=new HashMap<>();
					temp_matrix.put(ItemId, temp);
				}
				
				temp_matrix.get(ItemId).put(userId, score);

				
			}
			
		}
		
		
		for(Entry<Long,Map<Long,Double>> e:temp_matrix.entrySet()){
			ItemId=e.getKey();
			Map<Long,Double> user2score=e.getValue();
			
			ImmutableSparseVector msv = ImmutableSparseVector.create(user2score);
			
			matrix.put(ItemId, msv);
			
		}

		
		return matrix;
		
	}
	
	
	public void genTestSet(String table,String user,String item,String rate){
		
		
		StringBuilder sb=new StringBuilder();
		sb.append("select count(*) from ").append("`").append(table).append("` ;");
		String sql=sb.toString();
		
		int cnt=jdbc.queryForInt(sql);
		int test_cnt=cnt/10;
		
		Set<Integer> ids=new TreeSet<Integer>();
		List<Integer> ls=new ArrayList<Integer>();
		
		while(ids.size()<test_cnt){
			int tmp=(int)(Math.random()*cnt);
			tmp=tmp>=0?tmp:0;
			if(!ids.contains(tmp)){
				ids.add(tmp);
				ls.add(tmp);
			}
		}
		
		ls.sort(null);

		//String sql2="select id,user,item,rating from ml_1m where id in ( ";
		
		//String sql4="delete from ml_1m where id in ( ";
		
		sb=new StringBuilder();
		sb.append("select id,").append(user).append(',').append(item).append(',').append(rate)
		.append(" from ").append('`').append(table).append('`').append(" where id in ( ");
		
		StringBuilder sb2=new StringBuilder();
		sb2.append("delete from ").append('`').append(table).append('`')
		.append(" where id in ( ");
		
		for(Integer i: ls){
			sb.append(i).append(",");
			sb2.append(i).append(",");
		}
		
		sb.setCharAt(sb.length()-1, ')');
		sb2.setCharAt(sb2.length()-1, ')');
		
		
		
		List<Map<String, Object> > res=jdbc.queryForList(sb.toString());
		
		//String sql3="insert into ml_1m_test(id,user,item,rating) values ";
		sb=new StringBuilder();
		sb.append("insert into ").append('`').append(table+"_test").append('`')
		.append("(id,").append(user+",").append(item+",").append(rate+" )").append(" values ");
		
		
		for(Map<String, Object> its :res){
			sb.append("(")
			.append(its.get("id")).append(",")
			.append(its.get(user)).append(",")
			.append(its.get(item)).append(",")
			.append(its.get(rate)).append(")").append(",");
			
		}
		
		sb.setCharAt(sb.length()-1, ';');
		
		
		jdbc.update(sb.toString());
		
		//删除被选取的数据
		
		jdbc.update(sb2.toString());
		
		
	}
	
	
	public List<Map<String,Object>> getTestSet(String table,String user,String item,String rate){
		StringBuilder sb=new StringBuilder();
		sb.append("select id,").append(user).append(',').append(item).append(',').append(rate)
		.append(" from ").append('`').append(table).append("` where rate !=0;");
		
		return jdbc.queryForList(sb.toString());
		
	}
	
	
	
}
