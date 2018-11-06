package ru.gzpn.spc.csl.model.utils;

public class EntityGraph {
	
	int [][] matrix = new int [][] {{0,0,0,0,0,0}, // row isn't being used
									{0,1,2,2,2,2}, // 1 HProject
									{2,0,2,2,2,5}, // 2 CProject
									{2,3,0,2,2,2}, // 3 Phase
									{2,4,2,0,2,2}, // 4 Stage
									{2,5,2,2,0,5}, // 5 PlanObject
									{5,5,5,5,6,0}  // 6 Work
								   };
	
}
