package pl.lome.graph.grenchmark.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.script.ScriptContext;

import pl.lome.graph.grenchmark.step.Step;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.gremlin.GremlinScriptEngine;

/**
 * @author Blazej Zak (http://twitter.com/blazejzak)
 */
public abstract class Grenchmark {
	protected Graph graph;
	private ArrayList<Step> steps;
	private HashMap<Step, String> stepNames;
	private HashMap<Step, Long> results;

	public Grenchmark() {
		this.steps = new ArrayList<Step>();
		this.results = new HashMap<Step, Long>();
		this.stepNames = new HashMap<Step, String>();
	}

	protected abstract Graph initGraph();

	protected abstract void cleanupGraph();

	public void run() throws IOException {
		System.gc();

		this.graph = initGraph();

		GremlinScriptEngine engine = new GremlinScriptEngine();
		engine.getBindings(ScriptContext.ENGINE_SCOPE).put("$_g", graph);

		try {
			for (Step step : steps) {
				long start = System.currentTimeMillis();
				step.execute(engine, graph);
				results.put(step, System.currentTimeMillis() - start);
			}

		} catch (Exception e) {
			String message = GremlinScriptEngine.exceptionInPrintableFormat(e);
			engine.getContext().getErrorWriter().write(message + "\n");
			engine.getContext().getErrorWriter().flush();
		} finally {
			cleanupGraph();
		}

	}

	public void AddStep(Step step, String name) {
		this.steps.add(step);
		this.stepNames.put(step, name);
	}

	public void printHeader() {
		for (Step step : steps) {
			System.out.print(stepNames.get(step) + "\t");
		}
		System.out.println("Total");
	}

	public void printResults() {
		long sum = 0;
		for (Step step : steps) {
			System.out.print(results.get(step) + "\t");
			sum += results.get(step);
		}
		System.out.println(sum + "\t");
	}
}
