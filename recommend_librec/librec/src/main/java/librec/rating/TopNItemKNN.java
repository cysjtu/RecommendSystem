package librec.rating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.html.parser.Entity;

import com.google.common.collect.Table;

import librec.data.DenseMatrix;
import librec.data.DenseVector;
import librec.data.SparseMatrix;
import librec.data.SparseVector;
import librec.data.SymmMatrix;
import librec.intf.Recommender;
import librec.util.Lists;
import librec.util.Stats;
import librec.util.Strings;

public class TopNItemKNN extends Recommender{

	//private SymmMatrix itemCorrs;
	private Table<Integer, Integer, Double> itemCorrs;
	
	private DenseVector itemMeans;

	
	
	public TopNItemKNN(SparseMatrix trainMatrix, SparseMatrix testMatrix, int fold) {
		super(trainMatrix, testMatrix, fold);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void initModel() throws Exception {
		itemCorrs = buildCorrsTopN(false,50);
		itemMeans = new DenseVector(numItems);
		for (int i = 0; i < numItems; i++) {
			SparseVector vs = trainMatrix.column(i);
			itemMeans.set(i, vs.getCount() > 0 ? vs.mean() : globalMean);
		}
	}

	@Override
	protected double predict(int u, int j) {

		// find a number of similar items
		Map<Integer, Double> nns = new HashMap<>();

		Map<Integer, Double> dvtmp = itemCorrs.row(j);
		
		SparseVector dv=new SparseVector(numItems);
		
		for(Entry<Integer, Double> en:dvtmp.entrySet()){
			dv.set(en.getKey(), en.getValue());
			
		}
		
		for (int i : dv.getIndex()) {
			double sim = dv.get(i);
			double rate = trainMatrix.get(u, i);

			if (isRankingPred && rate > 0)
				nns.put(i, sim);
			else if (sim > 0 && rate > 0)
				nns.put(i, sim);
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
