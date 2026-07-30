package dev.xkmc.gensokyolegacy.util;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matcher {

	public record Req(int count, boolean[] criteria) {
	}

	/**
	 * Returns assignment[itemIndex][requirementIndex], or null if no valid
	 * arrangement exists.
	 * <p>
	 * assignment[i][j] is the number of units from item i assigned to
	 * requirement j.
	 */
	public static int @Nullable [][] solve(int[] items, Req[] reqs) {
		int nItem = items.length;
		int nReq = reqs.length;

		int sumItem = 0;
		int sumReq = 0;

		for (int count : items)
			sumItem += count;
		for (Req requirement : reqs)
			sumReq += requirement.count();

		if (sumItem < sumReq) return null;

		for (Req req : reqs) {
			int valid = 0;
			for (int i = 0; i < nItem; i++)
				if (req.criteria()[i]) valid += items[i];
			if (valid < req.count()) return null;
		}

		int source = 0;
		int itemStart = 1;
		int reqStart = itemStart + nItem;
		int sink = reqStart + nReq;

		Dinic dinic = new Dinic(sink + 1);
		for (int i = 0; i < nItem; i++) {
			dinic.addEdge(source, itemStart + i, items[i]);
		}
		Dinic.Edge[][] edges = new Dinic.Edge[nItem][nReq];
		for (int i = 0; i < nItem; i++) {
			for (int j = 0; j < nReq; j++) {
				if (reqs[j].criteria()[i]) {
					edges[i][j] = dinic.addEdge(itemStart + i, reqStart + j, items[i]);
				}
			}
		}
		for (int j = 0; j < nReq; j++) {
			dinic.addEdge(reqStart + j, sink, reqs[j].count());
		}
		long maxFlow = dinic.maxFlow(source, sink);
		if (maxFlow != sumReq) return null;
		int[][] assignment = new int[nItem][nReq];
		for (int i = 0; i < nItem; i++) {
			for (int j = 0; j < nReq; j++) {
				Dinic.Edge edge = edges[i][j];
				if (edge != null) {
					assignment[i][j] = edge.flow;
				}
			}
		}
		return assignment;
	}

	private static class Dinic {

		private static class Edge {

			int to;
			int reverseIndex;
			int capacity;
			int flow;

			Edge(int to, int reverseIndex, int capacity) {
				this.to = to;
				this.reverseIndex = reverseIndex;
				this.capacity = capacity;
			}

			int remainingCapacity() {
				return capacity - flow;
			}
		}

		private final List<Edge>[] graph;
		private final int[] level;
		private final int[] nextEdge;

		@SuppressWarnings("unchecked")
		Dinic(int nodeCount) {
			graph = new ArrayList[nodeCount];
			for (int i = 0; i < nodeCount; i++) {
				graph[i] = new ArrayList<>();
			}
			level = new int[nodeCount];
			nextEdge = new int[nodeCount];
		}

		/**
		 * Adds a directed edge and returns the forward edge.
		 */
		Edge addEdge(int from, int to, int capacity) {
			Edge forward = new Edge(to, graph[to].size(), capacity);
			Edge backward = new Edge(from, graph[from].size(), 0);
			graph[from].add(forward);
			graph[to].add(backward);
			return forward;
		}

		long maxFlow(int source, int sink) {
			long totalFlow = 0;

			while (buildLevelGraph(source, sink)) {
				Arrays.fill(nextEdge, 0);
				int pushed;
				while ((pushed = sendFlow(source, sink, Integer.MAX_VALUE)) > 0) {
					totalFlow += pushed;
				}
			}
			return totalFlow;
		}

		private boolean buildLevelGraph(int source, int sink) {
			Arrays.fill(level, -1);
			ArrayDeque<Integer> queue = new ArrayDeque<>();
			level[source] = 0;
			queue.add(source);
			while (!queue.isEmpty()) {
				int current = queue.remove();
				for (Edge edge : graph[current]) {
					if (edge.remainingCapacity() > 0 && level[edge.to] == -1) {
						level[edge.to] = level[current] + 1;
						queue.add(edge.to);
					}
				}
			}
			return level[sink] != -1;
		}

		private int sendFlow(int current, int sink, int availableFlow) {
			if (current == sink) {
				return availableFlow;
			}

			List<Edge> edges = graph[current];
			while (nextEdge[current] < edges.size()) {
				Edge edge = edges.get(nextEdge[current]);
				if (edge.remainingCapacity() > 0 && level[edge.to] == level[current] + 1) {
					int pushed = sendFlow(edge.to, sink, Math.min(availableFlow, edge.remainingCapacity()));
					if (pushed > 0) {
						edge.flow += pushed;
						Edge reverse = graph[edge.to].get(edge.reverseIndex);
						reverse.flow -= pushed;
						return pushed;
					}
				}
				nextEdge[current]++;
			}
			return 0;
		}
	}

}