package pl.lome.graph.grenchmark.impl;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;

/**
 * @author Blazej Zak (http://twitter.com/blazejzak)
 */
public class TinkerBench extends Grenchmark {

	@Override
	protected Graph initGraph() {
		return new TinkerGraph();
	}

	@Override
	protected void cleanupGraph() {

	}

}
