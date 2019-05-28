/*
 *   ________________________________________________________________________________________
 *   
 *   Y O O R E E K A
 *   A library for data mining, machine learning, soft computing, and mathematical analysis
 *   ________________________________________________________________________________________ 
 *    
 *   The Yooreeka project started with the code of the book "Algorithms of the Intelligent Web " 
 *   (Manning 2009). Although the term "Web" prevailed in the title, in essence, the algorithms 
 *   are valuable in any software application.
 *  
 *   Copyright (c) 2007-2009 Haralambos Marmanis & Dmitry Babenko
 *   Copyright (c) 2009-${year} Marmanis Group LLC and individual contributors as indicated by the @author tags.  
 * 
 *   Certain library functions depend on other Open Source software libraries, which are covered 
 *   by different license agreements. See the NOTICE file distributed with this work for additional 
 *   information regarding copyright ownership and licensing.
 * 
 *   Marmanis Group LLC licenses this file to You under the Apache License, Version 2.0 (the "License"); 
 *   you may not use this file except in compliance with the License.  
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under 
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 *   either express or implied. See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */
package org.yooreeka.algos.clustering.hierarchical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;




//import org.aksw.simba.benchmark.startup.QuerySelecter;
import org.yooreeka.algos.clustering.model.Cluster;
import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.config.YooreekaConfigurator;

/** A hierarchical agglomerative clustering algorithm based on single link */
public class SingleLinkAlgorithm {

	private static final Logger LOG = Logger.getLogger(SingleLinkAlgorithm.class.getName());
	public static void main(String[] args) {

		//	Define data
		DataPoint[] elements = new DataPoint[6];
		elements[0] = new DataPoint("o1", new double[] {});
		elements[1] = new DataPoint("o2", new double[] {});
		elements[2] = new DataPoint("o3", new double[] {});
		elements[3] = new DataPoint("o4", new double[] {});
		elements[4] = new DataPoint("o5", new double[] {});
		elements[5] = new DataPoint("o6", new double[] {});
//	    double[][] a= new double[][] { 
//	      { 0, 1, 9, 7, 11, 14 },
//	      { 1, 0, 4, 3, 8, 10 }, 
//	      { 9, 4, 0, 9, 2, 8 },
//	      { 7, 3, 9, 0, 6, 13 }, 
//	      { 11, 8, 2, 6, 0, 10 },
//	      { 14, 10, 8, 13, 10, 0 }};
	      
	  	Map<Integer, double[]> distance = new HashMap<Integer, double[]>();
	  	double[] value = new double[] { 0, 1, 9, 7, 11, 14 };
	  	double[] value1 = new double[] {0, 4, 3, 8, 10};
	  	double[] value2 = new double[] {0, 9, 2, 8};
	  	double[] value3 = new double[] {0, 6, 13 };
	  	double[] value4 = new double[] {0, 10};
	  	double[] value5 = new double[] {0};
	  	distance.put(0, value);
	  	distance.put(1, value1);
	  	distance.put(2, value2);
	  	distance.put(3, value3);
	  	distance.put(4, value4);
	  	distance.put(5, value5);
	  	
		SingleLinkAlgorithm ca = new SingleLinkAlgorithm(elements, distance);
		
		Dendrogram dnd = ca.cluster();
		
		// dnd.printAll();
		String[] queries = dnd.print(3); // the parameter contains number of clusters to be made.	
		
		ca.getQueriesFromClusters(elements, queries);		
		
	}
	
	private void getQueriesFromClusters(DataPoint[] elements, String[] queries) {
		Map<String, Double[]> finalVectors = new HashMap<>();
		Double[] getAvg = null;
//		System.out.println(queries[2]);
//		System.out.println(elements[5].getLabel());
//		if(queries[1] == elements[5].getLabel())
//			System.out.println("same");
//		System.out.println(queries[2].split(","));
//		char a;
			
	}
	private DataPoint[] elements;

	private double[][] a;
	private Map<Integer, double[]> distanceMatrix = new HashMap<Integer, double[]>();
	
	

	// Hierarchical Agglomerative Algorithm
	public SingleLinkAlgorithm(DataPoint[] elements, double[][] adjacencyMatrix) {

		LOG.setLevel(YooreekaConfigurator.getLevel(SingleLinkAlgorithm.class.getName()));

		this.elements = elements;
		this.a = adjacencyMatrix;
	}

	public SingleLinkAlgorithm(DataPoint[] dataToBeClustered, Map<Integer, double[]> distanceMatrix2) {
		this.elements = dataToBeClustered;
		this.distanceMatrix = distanceMatrix2;
	}

	// Implements Single Link Technique
	private List<Cluster> buildClusters(double distanceThreshold) {
		boolean[] usedElementFlags = new boolean[elements.length];
		List<Cluster> clusters = new ArrayList<Cluster>();
		System.out.println();
		for (int i = 0, n = a.length; i < n; i++) {
			//System.out.println(elements.length);
			List<DataPoint> clusterPoints = new ArrayList<DataPoint>();
			for (int j = i, k = a.length; j < k; j++) 
			{
				System.out.print(a[i][j]+" ");
				if (a[i][j] <= distanceThreshold && usedElementFlags[j] == false)
				{
					clusterPoints.add(elements[j]);
					usedElementFlags[j] = true;
				}
			}
			System.out.println();
			if (clusterPoints.size() > 0) {
				Cluster c = new Cluster(clusterPoints);
				clusters.add(c);
				System.out.println("cluster added "+ c.getElementsAsString());
			}
		}
		return clusters;
	}
	private List<Cluster> buildSingleLinkClusters(double distanceThreshold) {
		boolean[] usedElementFlags = new boolean[elements.length];
		List<Cluster> clusters = new ArrayList<Cluster>();
		//System.out.println();
		for (int i = 0; i <distanceMatrix.size(); i++) {
			//System.out.println(elements.length);
			List<DataPoint> clusterPoints = new ArrayList<DataPoint>();
			double[] distanceVector = distanceMatrix.get(i);
			for (int j = 0; j< distanceVector.length; j++) 
			{
				//System.out.print(distanceVector[j]+" ");
				if (distanceVector[j] <= distanceThreshold && usedElementFlags[i+j] == false)
				{
					clusterPoints.add(elements[i+j]);
					usedElementFlags[i+j] = true;
				}
			}
		//	System.out.println();
			if (clusterPoints.size() > 0) {
				Cluster c = new Cluster(clusterPoints);
				clusters.add(c);
			//	System.out.println("cluster added "+ c.getElementsAsString());
			}
		}
		return clusters;
	}

	public Dendrogram cluster() {
		Dendrogram dnd = new Dendrogram("Distance");
		double d = 0;
		// initially load all elements as individual clusters
		List<Cluster> initialClusters = new ArrayList<Cluster>();
		for (DataPoint e : elements) {
			Cluster c = new Cluster(e);
			initialClusters.add(c);
		}

		dnd.addLevel(String.valueOf(d), initialClusters);

		d = Double.MIN_VALUE;

		int k = initialClusters.size();

		while (k > 1) {
			int oldK = k;
		//	List<Cluster> clusters = buildClusters(d);
			List<Cluster> clusters = buildSingleLinkClusters(d);
			//System.out.println("Total Clusters = " + clusters.size() + " at distance = " + d);
			k = clusters.size();
			if (oldK != k) {
				dnd.addLevel(String.valueOf(d), clusters);
			}

			d = (d + 1)/2;
		}
		return dnd;
	}

	
}
