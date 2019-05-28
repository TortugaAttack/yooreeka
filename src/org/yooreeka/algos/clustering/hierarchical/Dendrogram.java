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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import org.yooreeka.algos.clustering.model.Cluster;
import org.yooreeka.config.YooreekaConfigurator;

public class Dendrogram {

	private static final Logger LOG = Logger.getLogger(Dendrogram.class.getName());

	/*
	 * Clusters by level.
	 */
	private Map<Integer, ClusterSet> entryMap;
	private Map<Integer, String> levelLabels;
	private Integer nextLevel;
	private String levelLabelName;

	public Dendrogram(String levelLabelName) {
				
		entryMap = new LinkedHashMap<Integer, ClusterSet>();
		levelLabels = new LinkedHashMap<Integer, String>();
		nextLevel = 1;
		this.levelLabelName = levelLabelName;
	}

	public int addLevel(String label, Cluster cluster) {
		List<Cluster> values = new ArrayList<Cluster>();
		values.add(cluster);
		return addLevel(label, values);
	}

	/**
	 * Creates a new dendrogram level using copies of provided clusters.
	 */
	public int addLevel(String label, Collection<Cluster> clusters) {

		ClusterSet clusterSet = new ClusterSet();

		for (Cluster c : clusters) {
			// copy cluster before adding - over time cluster elements may
			// change
			// but for dendrogram we want to keep current state.
			clusterSet.add(c.copy());
		}

		int level = nextLevel;

		entryMap.put(level, clusterSet);
		levelLabels.put(level, label);

		nextLevel++;
		return level;
	}

	public List<Integer> getAllLevels() {
		return new ArrayList<Integer>(entryMap.keySet());
	}

	public List<Cluster> getClustersForLevel(int level) {
		ClusterSet cs = entryMap.get(level);
		return cs.getAllClusters();
	}

	public String getLabelForLevel(int level) {
		return levelLabels.get(level);
	}

	public int getTopLevel() {
		return nextLevel - 1;
	}

	public String[] print(int level) {
		String label = levelLabels.get(level);
		ClusterSet clusters = entryMap.get(entryMap.size());
		int n;		
		for(n=entryMap.size(); clusters.size() < level ; n--) { 
			clusters = entryMap.get(n);		
		}
		System.out.println(n);
		if(clusters.size() == level) {
			System.out.println("Required Cluster sizes is done");
		}
		else if(n == 0) {
			System.out.println("\nYou can have: " +entryMap.get(n+1).size()+ " clusters\n");
		}
		else if(n > 0 && n < entryMap.keySet().size() ) {
			System.out.println("\nYou can either have: " +entryMap.get(n).size()+
						" clusters or: " +entryMap.get(n+1).size() +" clusters\n");
			System.out.println("Press 1 for first option and 2 for second option");
			Scanner in = new Scanner(System.in);
			int opt = in.nextInt();
			if(opt == 1) 
				clusters = entryMap.get(n);
			else if(opt == 2) 
				clusters = entryMap.get(n+1);
		}
		else if(n == entryMap.keySet().size() ) {
			System.out.println("\nYou can have: " +entryMap.keySet().size()+ " clusters\n");		
		}
		String s = null;
		String[] queries = new String[clusters.size()];
		int i = 0;
		for (Cluster c : clusters.getAllClusters()) {	
			queries[i] = c.getElementsAsString();
			i++;
		}		
		return queries;
	}

	public void printAll() {
		for (Map.Entry<Integer, ClusterSet> e : entryMap.entrySet()) {
			Integer level = e.getKey();
			print(level);
		}
	}

	/**
	 * Replaces clusters in the specified level. If level doesn't exist it will
	 * be created.
	 * 
	 * @param level
	 *            dendrogram level.
	 * @param label
	 *            level description.
	 * @param clusters
	 *            clusters for the level.
	 * @return
	 */
	public void setLevel(int level, String label, Collection<Cluster> clusters) {

		ClusterSet clusterSet = new ClusterSet();

		for (Cluster c : clusters) {
			clusterSet.add(c.copy());
		}


		entryMap.put(level, clusterSet);
		levelLabels.put(level, label);

		if (level >= nextLevel) {
			nextLevel = level + 1;
		}
	}

}