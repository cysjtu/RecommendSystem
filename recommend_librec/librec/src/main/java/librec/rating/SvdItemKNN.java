package librec.rating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import java.util.Map.Entry;

import librec.data.DenseMatrix;
import librec.data.DenseVector;
import librec.data.SparseMatrix;
import librec.data.SparseVector;
import librec.intf.Recommender;
import librec.intf.Recommender.Node;
import librec.intf.Recommender.NodeComparator;
import librec.util.Lists;
import librec.util.Logs;
import librec.util.Stats;
import librec.util.Strings;

public class SvdItemKNN extends Recommender{

	public SvdItemKNN(SparseMatrix trainMatrix, SparseMatrix testMatrix, int fold) {
		super(trainMatrix, testMatrix, fold);
		// TODO Auto-generated constructor stub
	}
	
	private DenseMatrix itemFeature;
	private DenseVector itemMeans;
	List<PriorityQueue<Node>> tempcorrs;
	
	LoadingCache<String, Double> simcache;
	
	//Table<Integer, Integer, Double> cacheSim = TreeBasedTable.create();
	
	
	@Override
	protected void initModel() throws Exception {
		
		BiasedMF biasedMF=new BiasedMF(this.trainMatrix, this.testMatrix, this.fold);
		biasedMF.initModel();
		biasedMF.buildModel();
		
		//==============
		itemFeature=biasedMF.Q;
		
		
		itemMeans = new DenseVector(numItems);
		for (int i = 0; i < numItems; i++) {
			SparseVector vs = trainMatrix.column(i);
			itemMeans.set(i, vs.getCount() > 0 ? vs.mean() : globalMean);
		}
		
		
		simcache=CacheBuilder.newBuilder().maximumSize(600000)//.expireAfterAccess(30, TimeUnit.SECONDS)
				.build(new CacheLoader<String, Double>(){

					@Override
					public Double load(String key) throws Exception {
						// TODO Auto-generated method stub
						String [] items=key.split("_");
						Integer i=Integer.valueOf(items[0]);
						Integer j=Integer.valueOf(items[1]);
						
						DenseVector fi=itemFeature.row(i);
						DenseVector fj=itemFeature.row(j);
						
						double inner=0.0,sum1=0.0,sum2=0.0;
						for (int k = 0; k < fi.size; k++){
							inner+=fj.get(k)*fi.get(k);
							
							sum1+=Math.pow(fj.get(k), 2);
							sum2+=Math.pow(fi.get(k), 2);
						}
						
						double sim=inner/(Math.sqrt(sum1)*Math.sqrt(sum2));

						return sim;
					}
					
				});
		
		
	}
	
	
	@Override
	protected double predict(int u, int j) {

		// find a number of similar items
		Map<Integer, Double> nns = new HashMap<>();

		
		DenseVector fj=itemFeature.row(j);
		
		
		
		for (int i=0;i<numItems; ++i) {
			if(j==i) continue;
			
			double sim =0.0;
			double rate = trainMatrix.get(u, i);

			if (isRankingPred && rate > 0){
				try {
					sim=simcache.get(Math.min(i, j)+"_"+Math.max(i, j));
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nns.put(i, sim);
			}
			else if (rate > 0){
				
				try {
					sim=simcache.get(Math.min(i, j)+"_"+Math.max(i, j));
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				nns.put(i, sim);
				
			}
		}

		// topN similar items
		if (knn > 0 && knn < nns.size()) {
			List<Map.Entry<Integer, Double>> sorted = Lists.sortMap(nns, true);
			List<Map.Entry<Integer, Double>> subset = sorted.subList(0, knn);
			nns.clear();
			for (Map.Entry<Integer, Double> kv : subset)
				nns.put(kv.getKey(), kv.getValue());
		}

		if (nns.size() == 0)
			return isRankingPred ? 0 : globalMean;

		if (isRankingPred) {
			// for recommendation task: item ranking

			return Stats.sum(nns.values());
		} else {
			// for recommendation task: rating prediction

			double sum = 0, ws = 0;
			for (Entry<Integer, Double> en : nns.entrySet()) {
				int i = en.getKey();
				double sim = en.getValue();
				double rate = trainMatrix.get(u, i);

				sum += sim * (rate - itemMeans.get(i));
				ws += Math.abs(sim);
			}

			return ws > 0 ? itemMeans.get(j) + sum / ws : globalMean;
		}
	}

	@Override
	public String toString() {
		return Strings.toString(new Object[] { knn, similarityMeasure, similarityShrinkage });
	}
	
	

}
