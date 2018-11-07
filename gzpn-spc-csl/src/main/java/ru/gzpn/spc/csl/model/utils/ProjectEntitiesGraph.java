package ru.gzpn.spc.csl.model.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The graph of the project entities. It holds the relationships between entities (nodes)
 * Contains useful methods to get path through the intermediate entities etc.
 */
public class ProjectEntitiesGraph {
	
	private static final int [][] G = new int [][] {{0,0,0,0,0,0,0}, // the first column and row aren't being used
													{0,0,1,2,2,2,2}, // 1 HProject
													{0,2,0,2,2,2,5}, // 2 CProject
													{0,2,3,0,2,2,2}, // 3 Phase
													{0,2,4,2,0,2,2}, // 4 Stage
													{0,2,5,2,2,0,5}, // 5 PlanObject
													{0,5,5,5,5,6,0}  // 6 Work
								   					};
								   							   
	private static final Map<Entities, Integer> mapNodes = new LinkedHashMap<>();
	private static final Entities [] arrayNodes = new Entities[] {Entities.HPROJECT, Entities.CPROJECT, Entities.PHASE, Entities.STAGE, Entities.PLAN_OBJECT, Entities.WORK};
	
	static {
		mapNodes.put(Entities.HPROJECT, 1);
		mapNodes.put(Entities.CPROJECT, 2);
		mapNodes.put(Entities.PHASE, 3);
		mapNodes.put(Entities.STAGE, 4);
		mapNodes.put(Entities.PLAN_OBJECT, 5);
		mapNodes.put(Entities.WORK, 6);
	}
	
	/**
	 * Build entity path between two nodes(entities). It needs when 
	 * there is no straight relationships between two entities. And 
	 * we build path based on intermediate entities.   
	 *  
	 * @param nodeFrom
	 * @param nodeTo
	 * @return
	 */
	public static List<Entities> getPathBetweenNodes(String nodeFrom, String nodeTo) {
		List<Entities> result = new ArrayList<>();
		Entities nFrom = Entities.valueOf(nodeFrom.toUpperCase());
		Entities nTo = Entities.valueOf(nodeTo.toUpperCase());

		if (nFrom != nTo) {
			int iFrom = mapNodes.get(nFrom);
			int iTo = mapNodes.get(nTo);

			while (G[iFrom][iTo] != iFrom) {
				result.add(arrayNodes[iFrom - 1]);
				iFrom = G[iFrom][iTo];
			}
			result.add(arrayNodes[iFrom - 1]);
			result.add(nTo);
		} else {
			result.add(nFrom);
		}
		
		return result;
	}
}
