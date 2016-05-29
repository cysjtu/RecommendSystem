package librec.rating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import librec.data.DenseMatrix;
import librec.data.DenseVector;
import librec.data.SparseMatrix;
import librec.data.SparseVector;
import librec.intf.Recommender;
import librec.util.Lists;
import librec.util.Stats;
import librec.util.Strings;

public class SvdUserKNN extends Recommender{

	private DenseVector userMeans;

	private DenseMatrix userFeature;

	LoadingCache<String, Double> simcache;
	
	
	public SvdUserKNN(SparseMatrix trainMatrix, SparseMatrix testMatrix, int fold) {
		super(trainMatrix, testMatrix, fold);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initModel() throws Exception {
		BiasedMF biasedMF=new BiasedMF(this.trainMatrix, this.testMatrix, this.fold);
		biasedMF.initModel();
		biasedMF.buildModel();

		userFeature=biasedMF.P;
		
		
		userMeans = new DenseVector(numUsers);
		for (int u = 0; u < numUsers; u++) {
			SparseVector uv = trainMatrix.row(u);
			userMeans.set(u, uv.getCount() > 0 ? uv.mean() : globalMean);
		}
		
		
		simcache=CacheBuilder.newBuilder().maximumSize(600000)//.expireAfterAccess(30, TimeUnit.SECONDS)
				.build(new CacheLoader<String, Double>(){

					@Override
					public Double load(String key) throws Exception {
						// TODO Auto-generated method stub
						String [] items=key.split("_");
						Integer i=Integer.valueOf(items[0]);
						Integer j=Integer.valueOf(items[1]);
						
						DenseVector fi=userFeature.row(i);
						DenseVector fj=userFeature.row(j);
						
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

		// find a number of similar users
		Map<Integer, Double> nns = new HashMap<>();

		DenseVector fu=userFeature.row(u);
		
		
		for (int v=0; v<numUsers;++v ) {
			
			double sim=0.0;
			
			double rate = trainMatrix.get(v, j);

			if (isRankingPred && rate > 0)
			{
				try {
					sim=simcache.get(Math.min(u, v)+"_"+Math.max(u, v));
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nns.put(v, sim); // similarity could be negative for item ranking
			}
			else if (rate > 0){
				try {
					sim=simcache.get(Math.min(u, v)+"_"+Math.max(u, v));
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nns.put(v, sim);
			}
		}

		// topN similar users
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
			// for item ranking

			return Stats.sum(nns.values());
		} else {
			// for rating prediction

			double sum = 0, ws = 0;
			for (Entry<Integer, Double> en : nns.entrySet()) {
				int v = en.getKey();
				double sim = en.getValue();
				double rate = trainMatrix.get(v, j);

				sum += sim * (rate - userMeans.get(v));
				ws += Math.abs(sim);
			}

			return ws > 0 ? userMeans.get(u) + sum / ws : globalMean;
		}
	}

	@Override
	public String toString() {
		return Strings.toString(new Object[] { knn, similarityMeasure, similarityShrinkage });
	}
	
	
}
