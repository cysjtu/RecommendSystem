package librec.ext;

import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import librec.data.DenseMatrix;
import librec.data.DenseVector;
import librec.data.SparseMatrix;
import librec.data.SparseVector;
import librec.intf.Recommender;

public class CachedSlopOne extends Recommender {

	
	LoadingCache<String, double [] > devCache;
	
	
	
	public CachedSlopOne(SparseMatrix trainMatrix, SparseMatrix testMatrix, int fold) {
		super(trainMatrix, testMatrix, fold);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected void initModel() throws Exception {
		
		
		devCache=CacheBuilder.newBuilder().maximumSize(600000)//.expireAfterAccess(30, TimeUnit.SECONDS)
				.build(new CacheLoader<String, double []>(){

					@Override
					public double [] load(String key) throws Exception {
						// TODO Auto-generated method stub
						String [] items=key.split("_");
						Integer i=Integer.valueOf(items[0]);
						Integer j=Integer.valueOf(items[1]);
						
						SparseVector vi=trainMatrix.column(i);
						SparseVector vj=trainMatrix.column(j);
						
						double dev=0.0,card=0.0;
						
						for(int k1:vi.getIndex()){
							if(vj.contains(k1)){
								dev+=(vi.get(k1)-vj.get(k1));
								card+=1;
								
							}
						}
						
						if(card>0) dev/=card;
						
						
						return new double[]{card,dev};

					}
					
				});
		
		
	}

	
	@Override
	protected void buildModel() throws Exception {

	
	}

	@Override
	protected double predict(int u, int j) {
		SparseVector uv=new SparseVector(numItems);
		
		try{
			uv = trainMatrix.row(u, j);
		}catch(Exception e){
			
		}
		
		double preds = 0, cards = 0;
		for (int i : uv.getIndex()) {
			double[] dev=new double[2];
			try {
				dev = devCache.get(j+"_"+i);
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dev[0] > 0) {
				preds += (dev[1] + uv.get(i)) * dev[0];
				cards += dev[0];
			}
		}

		return cards > 0 ? preds / cards : globalMean;
	}

	
	
}
