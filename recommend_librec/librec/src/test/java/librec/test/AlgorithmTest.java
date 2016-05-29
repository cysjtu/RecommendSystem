package librec.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.google.common.collect.TreeBasedTable;

import librec.baseline.UserAverage;
import librec.data.DataDAO;
import librec.data.DataSplitter;
import librec.data.MatrixEntry;
import librec.data.SparseMatrix;
import librec.ext.External;
import librec.intf.Recommender;
import librec.intf.Recommender.Measure;
import librec.main.LibRec;
import librec.rating.BiasedMF;
import librec.rating.SvdItemKNN;
import librec.rating.SvdUserKNN;
import librec.rating.TopNItemKNN;
import librec.util.FileIO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AlgorithmTest {
	public String configDirPath="/Users/nali/GitHub/librec/librec/demo/config/";
	
	@Test
	public void topItemNKNN(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "topNItemKNN.conf");
		try {
			librec.execute(new String[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void CachedItemNKNN(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "CachedItemKNN.conf");
		try {
			librec.execute(new String[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	@Test
	public void topUserNKNN(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "topNUserKNN.conf");
		try {
			librec.execute(new String[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void CachedUserNKNN(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "CachedUserKNN.conf");
		try {
			librec.execute(new String[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	@Test
	public void Random(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "random.conf");
		librec.algorithm=Recommender.algoName="Random";
		try {
			librec.execute(new String[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Test
	public void GlobalMean(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "GlobalAvg.conf");
		librec.algorithm=Recommender.algoName="GlobalAvg";
		try {
			librec.execute(new String[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void UserMean(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "UserAvg.conf");
		librec.algorithm=Recommender.algoName="UserAvg";
		try {
			librec.execute(new String[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void ItemMean(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "ItemAvg.conf");
		librec.algorithm=Recommender.algoName="ItemAvg";
		try {
			librec.execute(new String[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	@Test
	public void CachedSlopOne(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "CachedSlopeOne.conf");
		librec.algorithm=Recommender.algoName="CachedSlopeOne";
		try {
			librec.execute(new String[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void CachedSlopOneFlip(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "CachedSlopeOneFlip.conf");
		
		librec.algorithm=Recommender.algoName="CachedSlopOneFlip";
		try {
			librec.execute(new String[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	@Test
	public void MostPop(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "MostPop.conf");
		
		librec.algorithm=Recommender.algoName="MostPop";
		try {
			librec.execute(new String[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	@Test
	public void propTest() throws FileNotFoundException, IOException{
		String path=configDirPath + "BiasedMF.conf";
		
		Properties p=new Properties();
		p.load(new FileInputStream(path));
		
		System.err.println(p.getProperty("reg.lambda"));
		
		p.setProperty("reg.lambda", "1.9 -u 0.2 -i 0.15 -b 0.15 -s 0.001");
		
		p.store(new FileOutputStream(path), "");
		
		
		
	}
	
	@Test
	public void SVD() throws FileNotFoundException, IOException{
		
		double reg,regu,regi,regb;
		int fac;
		String path=configDirPath + "SVD++.conf";
		
		String best_set="";
		double best_rmse=10000;
		
		for(fac=5;fac<55;fac+=10)
			for(reg=0.001;reg<1.0;reg+=0.05){
			
			Properties p=new Properties();
			p.load(new FileInputStream(path));
			String regular=String.format("%.5f -u %.5f -i %.5f -b %.5f ", reg,reg,reg,reg);
			p.setProperty("num.factors",fac+"");
			
			
			String factor=p.getProperty("num.factors");
			
			//System.err.println(para);
			p.setProperty("reg.lambda", regular);
			
			p.store(new FileOutputStream(path), "");
			
			//==============================
			LibRec librec = new LibRec();
			librec.setConfigFiles(configDirPath + "SVD++.conf");
			librec.algorithm=Recommender.algoName="SVD++";
			try {
				librec.execute(new String[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//====================================
			
			
			
			if(librec.rec.measures.get(Measure.RMSE)<best_rmse){
				best_rmse=librec.rec.measures.get(Measure.RMSE);
				best_set="reg.lambda="+regular+"\n num.factors="+factor;
				
				System.err.println("============best_set="+best_set);
				System.err.println("============best_rmse="+best_rmse);
				
				
			}
			
			
		}
		
		System.err.println("============best_set="+best_set);
		System.err.println("============best_rmse="+best_rmse);
		
		
		
	}
	
	@Test
	public void SVDPlusPlus(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "SVD++.conf");
		librec.algorithm=Recommender.algoName="SVD++";
		try {
			librec.execute(new String[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Test
	public void SVDPlusPlus_flip(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "SVD++_flip.conf");
		
		librec.algorithm=Recommender.algoName="SVD++_flip";
		
		try {
			librec.execute(new String[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	@Test
	public void genRankTestSet() throws Exception{
		double threahold=6.0;
		
		for(int i=1;i<=5;++i){
			String raw_file="/Users/nali/Eclipse/lenscy/bx_rating_explicit_test"+i+".txt";
			String dst_file="/Users/nali/Eclipse/lenscy/bx_rating_explicit_test_rank"+i+"_"+threahold+".txt";
			List<String> testset=new ArrayList<String>();
			DataDAO DAO =new DataDAO(raw_file);
			Table<Integer, Integer, Double> tb=DAO.readDataTable(raw_file, new int[]{0,1,2});
			for(Cell<Integer, Integer, Double> ce:tb.cellSet()){
				int r=ce.getRowKey();
				int c=ce.getColumnKey();
				double rate=ce.getValue();
				if(rate>threahold){
					testset.add(r+" "+c+" "+rate);
				}
			}
			
			FileIO.writeList(dst_file, testset);
			
			
		}
	}

	
	@Test
	public void DataSplit(){
		LibRec librec = new LibRec();
		try {
			librec.preset(configDirPath + "UserAvg.conf");
			librec.readData();
			DataSplitter ds = new DataSplitter(librec.rateMatrix, 5);
			
			for(int i=1;i<=5;++i){
				
				SparseMatrix [] data=ds.getKthFold(i);
				SparseMatrix trainMatrix = data[0], testMatrix = data[1];
				DataDAO DAO=librec.rateDao;
				String trainfile="/Users/nali/Eclipse/lenscy/bx_rating_explicit_train"+i+".txt",
						testfile="/Users/nali/Eclipse/lenscy/bx_rating_explicit_test"+i+".txt";
				
				List<String> sbtrain=new ArrayList<String>(),sbtest=new ArrayList<String>();
				
				for (MatrixEntry me : trainMatrix){
					
					
					double rate = me.get();

					String u = DAO.getUserId(me.row());
					String j =DAO.getItemId(me.column());
					
					sbtrain.add(u+" "+j+" "+rate);//append(u).append(" ").append(j).append(" ").append(rate).append("\n");
					
					
				}
				
				FileIO.writeList(trainfile, sbtrain, false);
				
				
				for (MatrixEntry me : testMatrix){
					
					
					double rate = me.get();

					String u = DAO.getUserId(me.row());
					String j =DAO.getItemId(me.column());
					
					sbtest.add(u+" "+j+" "+rate);//append(u).append(" ").append(j).append(" ").append(rate).append("\n");
					
					
				}
				
				FileIO.writeList(testfile, sbtest, false);

			}

			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void flipData() throws Exception{
		String toFile_pred_SVDppFlip="/Users/nali/GitHub/librec/librec/demo/Results/SVD++_flip-rating-predictions.txt",
				toFile_pred_SVDpp="/Users/nali/GitHub/librec/librec/demo/Results/SVD++-rating-predictions.txt";
		
		Table<Integer, Integer, Double> pred_SVDpp,pred_SVDppFlip;
		
		
		
		
		DataDAO rateDao = new DataDAO(toFile_pred_SVDppFlip);
		rateDao.setHeadline(true);
		pred_SVDppFlip=rateDao.readDataTable(toFile_pred_SVDppFlip,new int[]{0,1,3});
		List<String> res=new ArrayList<>();
		res.add("# userId itemId rating prediction");
		
		
		BufferedReader br = FileIO.getReader(toFile_pred_SVDpp);
		
		
		String line = null;
		
		
		int line_cnt=0;
		line = br.readLine();
		
		while ((line = br.readLine()) != null) {
			
			String[] data = line.trim().split("[ \t,]+");

			Integer user = Integer.valueOf(data[0]);
			Integer item =Integer.valueOf(data[1]);
			Double rate = Double.valueOf(data[2]);
			Double pred=pred_SVDppFlip.get(user, item);
			res.add(user+" "+item+" "+rate+" "+pred);
			
		}

		FileIO.writeList(toFile_pred_SVDppFlip, res);
		
		
	}
	
	
	@Test
	public void Hybrid(){
		LibRec librec = new LibRec();
		//librec.setConfigFiles(configDirPath + "UserAvg.conf");
		try {
			librec.preset(configDirPath + "UserAvg.conf");
			librec.readData();
			librec.algorithm=Recommender.algoName="Hybrid";
			
			String toFile_pred_UserAverage="/Users/nali/GitHub/librec/librec/demo/Results/UserAvg-rating-predictions.txt",
			toFile_pred_SVD="/Users/nali/GitHub/librec/librec/demo/Results/SVD-rating-predictions.txt",
			toFile_pred_SVDpp="/Users/nali/GitHub/librec/librec/demo/Results/SVD++-rating-predictions.txt",
			toFile_pred_SVDppFlip="/Users/nali/GitHub/librec/librec/demo/Results/SVD++_flip-rating-predictions.txt";
			
				
				//=============hybrid
				SparseMatrix rate=null;
				SparseMatrix pred=null;
				
				
				
				DataDAO rateDao = new DataDAO(toFile_pred_UserAverage);
				rateDao.setHeadline(true);
				rate=rateDao.readData(new int[]{0,1,2}, -1)[0];
				
				
				SparseMatrix pred_UserAverage,pred_SVD,pred_SVDpp,pred_SVDppFlip;
				//======
				rateDao = new DataDAO(toFile_pred_UserAverage);
				rateDao.setHeadline(true);
				pred_UserAverage=rateDao.readData(new int[]{0,1,3}, -1)[0];
				
				
				
				rateDao = new DataDAO(toFile_pred_SVD);
				rateDao.setHeadline(true);
				pred_SVD=rateDao.readData(new int[]{0,1,3}, -1)[0];
				
				rateDao = new DataDAO(toFile_pred_SVDppFlip);
				rateDao.setHeadline(true);
				pred_SVDppFlip=rateDao.readData(new int[]{0,1,3}, -1)[0];
				
				rateDao = new DataDAO(toFile_pred_SVDpp);
				rateDao.setHeadline(true);
				pred_SVDpp=rateDao.readData(new int[]{0,1,3}, -1)[0];
				
				
				String setting="";
				double minimal=1000;
				
				//===================
				double w_pred_UserAverage_t=2.5,//1.0/ 1.692648,
						w_pred_SVD_t=1,//1.0/ 1.708389,
						w_pred_SVDpp_t=3.5,//1.0/1.633017,
						w_pred_SVDppFlip_t=3;//1.0/ 1.637965;
				
				double b_w_pred_UserAverage_t=2.5,//1.0/ 1.692648,
						b_w_pred_SVD_t=1,//1.0/ 1.708389,
						b_w_pred_SVDpp_t=3.5,//1.0/1.633017,
						b_w_pred_SVDppFlip_t=3;//1.0/ 1.637965;
				
				
				pred=new SparseMatrix(rate);
				
				
				int cnttt=1;
				for(w_pred_UserAverage_t=0.1;w_pred_UserAverage_t<10.0;w_pred_UserAverage_t+=0.5){
					for(w_pred_SVD_t=0.1;w_pred_SVD_t<10.0-w_pred_UserAverage_t;w_pred_SVD_t+=0.5){
						for(w_pred_SVDpp_t=0.1;w_pred_SVDpp_t<10.0-w_pred_SVD_t-w_pred_UserAverage_t;w_pred_SVDpp_t+=0.5){
							
							
								w_pred_SVDppFlip_t=10-w_pred_SVDpp_t-w_pred_SVD_t-w_pred_UserAverage_t;
								
							//System.out.println(cnttt++);
							
							double w_sum=w_pred_UserAverage_t+w_pred_SVD_t+w_pred_SVDpp_t+w_pred_SVDppFlip_t;
							
							double w_pred_UserAverage=w_pred_UserAverage_t/w_sum;
							double w_pred_SVD=w_pred_SVD_t/w_sum;
							double w_pred_SVDpp=w_pred_SVDpp_t/w_sum;
							double w_pred_SVDppFlip=w_pred_SVDppFlip_t/w_sum;
							
							//System.err.println(w_pred_UserAverage+"  "+w_pred_SVD+"  "+w_pred_SVDpp+"  "+w_pred_SVDppFlip);
							
							
							double cnt=0.0,mse=0.0;
							
							
							
							
							
							for(MatrixEntry en:rate){
								
								double hybr=0.0;
								int r=en.row();
								int c=en.column();
								
								
							
								
								
								hybr=    pred_UserAverage.get(r, c)*w_pred_UserAverage
										+pred_SVD.get(r, c)*w_pred_SVD
										+pred_SVDpp.get(r, c)*w_pred_SVDpp
										+pred_SVDppFlip.get(r, c)*w_pred_SVDppFlip;
								
								
								
								pred.set(r, c, hybr);
								
								cnt++;
								mse+=Math.pow(rate.get(r, c)-hybr, 2);
								
								
							}
							
							if(Math.sqrt(mse/cnt)<minimal){
								System.err.println("predict========finish rmse="+Math.sqrt(mse/cnt));
								minimal=Math.sqrt(mse/cnt);
								setting=w_pred_UserAverage+"  "+w_pred_SVD+"  "+w_pred_SVDpp+"  "+w_pred_SVDppFlip;
								b_w_pred_UserAverage_t=w_pred_UserAverage;//1.0/ 1.692648,
										b_w_pred_SVD_t=w_pred_SVD;//1.0/ 1.708389,
										b_w_pred_SVDpp_t=w_pred_SVDpp;//1.0/1.633017,
										b_w_pred_SVDppFlip_t=w_pred_SVDppFlip;//1.0/ 1.637965;
								
							}
							

							
							
							
						}
					}
				}
				
				
				
				
				
				
				System.err.println("predict========finish rmse="+minimal);
				System.err.println("predict========finish setting="+setting);
				
				
		//////////============
				
				double w_sum=b_w_pred_UserAverage_t+b_w_pred_SVD_t+b_w_pred_SVDpp_t+b_w_pred_SVDppFlip_t;
				
				double w_pred_UserAverage=b_w_pred_UserAverage_t/w_sum;
				double w_pred_SVD=b_w_pred_SVD_t/w_sum;
				double w_pred_SVDpp=b_w_pred_SVDpp_t/w_sum;
				double w_pred_SVDppFlip=b_w_pred_SVDppFlip_t/w_sum;
				
				System.err.println(w_pred_UserAverage+"  "+w_pred_SVD+"  "+w_pred_SVDpp+"  "+w_pred_SVDppFlip);
				
				
				double cnt=0.0,mse=0.0;
				
				pred=new SparseMatrix(rate);
				
				
				
				for(MatrixEntry en:rate){
					
					double hybr=0.0;
					int r=en.row();
					int c=en.column();
					
					
				
					
					
					hybr=    pred_UserAverage.get(r, c)*w_pred_UserAverage
							+pred_SVD.get(r, c)*w_pred_SVD
							+pred_SVDpp.get(r, c)*w_pred_SVDpp
							+pred_SVDppFlip.get(r, c)*w_pred_SVDppFlip;
					
					
					
					pred.set(r, c, hybr);
					
					cnt++;
					mse+=Math.pow(rate.get(r, c)-hybr, 2);
					
					
				}

				
				//////////============
				Recommender algo =new External(pred, rate, -1);
				algo.execute();
				librec.printEvalInfo(algo, algo.measures);
				
				
				
				///////////////
				//int test_rows=pred.rowKeySet().size(),test_cols=pred.columnKeySet().size();

				
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	@Test
	public void svdItemKNN(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "svdItemKNN.conf");
		try {
			librec.preset(configDirPath + "svdItemKNN.conf");
			librec.readData();
			//==================
			int kFold =5;
			boolean isParallelFold =false;
			
			DataSplitter ds = new DataSplitter(librec.rateMatrix, kFold);

			Thread[] ts = new Thread[kFold];
			Recommender[] algos = new Recommender[kFold];

			for (int i = 0; i < kFold; i++) {
				SparseMatrix [] data=ds.getKthFold(i + 1);
				
				SparseMatrix trainMatrix = data[0], testMatrix = data[1];
				
				
				Recommender algo =new SvdItemKNN(trainMatrix, testMatrix, i) ;//librec.getRecommender(ds.getKthFold(i + 1), i + 1);

				algos[i] = algo;
				ts[i] = new Thread(algo);
				ts[i].start();

				if (!isParallelFold)
					ts[i].join();
			}

			if (isParallelFold)
				for (Thread t : ts)
					t.join();

			// average performance of k-fold
			Map<Measure, Double> avgMeasure = new HashMap<>();
			for (Recommender algo : algos) {
				for (Entry<Measure, Double> en : algo.measures.entrySet()) {
					Measure m = en.getKey();
					double val = avgMeasure.containsKey(m) ? avgMeasure.get(m) : 0.0;
					avgMeasure.put(m, val + en.getValue() / kFold);
				}
			}

			librec.printEvalInfo(algos[0], avgMeasure);
			
			
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	@Test
	public void svdUserKNN(){
		LibRec librec = new LibRec();
		librec.setConfigFiles(configDirPath + "svdUserKNN.conf");
		try {
			librec.preset(configDirPath + "svdUserKNN.conf");
			librec.readData();
			//==================
			int kFold =5;
			boolean isParallelFold =false;
			
			DataSplitter ds = new DataSplitter(librec.rateMatrix, kFold);

			Thread[] ts = new Thread[kFold];
			Recommender[] algos = new Recommender[kFold];

			for (int i = 0; i < kFold; i++) {
				SparseMatrix [] data=ds.getKthFold(i + 1);
				
				SparseMatrix trainMatrix = data[0], testMatrix = data[1];
				
				
				Recommender algo =new SvdUserKNN(trainMatrix, testMatrix, i) ;//librec.getRecommender(ds.getKthFold(i + 1), i + 1);

				algos[i] = algo;
				ts[i] = new Thread(algo);
				ts[i].start();

				if (!isParallelFold)
					ts[i].join();
			}

			if (isParallelFold)
				for (Thread t : ts)
					t.join();

			// average performance of k-fold
			Map<Measure, Double> avgMeasure = new HashMap<>();
			for (Recommender algo : algos) {
				for (Entry<Measure, Double> en : algo.measures.entrySet()) {
					Measure m = en.getKey();
					double val = avgMeasure.containsKey(m) ? avgMeasure.get(m) : 0.0;
					avgMeasure.put(m, val + en.getValue() / kFold);
				}
			}

			librec.printEvalInfo(algos[0], avgMeasure);
			
			
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

	
}
