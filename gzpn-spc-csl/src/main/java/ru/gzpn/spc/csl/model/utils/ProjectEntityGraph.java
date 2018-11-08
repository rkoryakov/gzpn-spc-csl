package ru.gzpn.spc.csl.model.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.gzpn.spc.csl.model.utils.ProjectEntityGraph.Rib.LinkedFields;

/**
 * The graph of the project entities. It holds the relationships between entities (nodes)
 * Contains useful methods to get path through the intermediate entities etc.
 */
public class ProjectEntityGraph {
	// the graph
	private static final int [][] G = new int [][] {{0,0,0,0,0,0,0}, // the first column and row aren't being used
													{0,0,1,2,2,2,2}, // 1 HProject
													{0,2,0,2,2,2,5}, // 2 CProject
													{0,2,3,0,2,2,2}, // 3 Phase
													{0,2,4,2,0,2,2}, // 4 Stage
													{0,2,5,2,2,0,5}, // 5 PlanObject
													{0,5,5,5,5,6,0}  // 6 Work
								   					};

	private static final Map<Entities, Integer> mapNodes = new LinkedHashMap<>();
	private static final Map<Rib, LinkedFields> mapRibs = new HashMap<>();
	
	static {
		mapNodes.put(Entities.HPROJECT, 1);
		mapNodes.put(Entities.CPROJECT, 2);
		mapNodes.put(Entities.PHASE, 3);
		mapNodes.put(Entities.STAGE, 4);
		mapNodes.put(Entities.PLAN_OBJECT, 5);
		mapNodes.put(Entities.WORK, 6);
		
		mapRibs.put(new Rib(Entities.HPROJECT, Entities.CPROJECT), new LinkedFields("id", "hp_id"));
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.HPROJECT), new LinkedFields("hp_id", "id"));
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.PHASE), new LinkedFields("phase_id", "id"));
		mapRibs.put(new Rib(Entities.PHASE, Entities.CPROJECT), new LinkedFields("id", "phase_id"));
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.STAGE), new LinkedFields("staget_id", "id"));
		mapRibs.put(new Rib(Entities.STAGE, Entities.CPROJECT), new LinkedFields("id", "staget_id"));
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.PLAN_OBJECT), new LinkedFields("id", "cp_id"));
		mapRibs.put(new Rib(Entities.PLAN_OBJECT, Entities.CPROJECT), new LinkedFields("cp_id", "id"));
		mapRibs.put(new Rib(Entities.PLAN_OBJECT, Entities.WORK), new LinkedFields("id", "plan_obj_id"));
		mapRibs.put(new Rib(Entities.WORK, Entities.PLAN_OBJECT), new LinkedFields("plan_obj_id", "id"));
	}
	
	private ProjectEntityGraph() {
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
				result.add(Entities.values()[iFrom - 1]);
				iFrom = G[iFrom][iTo];
			}
			result.add(Entities.values()[iFrom - 1]);
			result.add(nTo);
		} else {
			result.add(nFrom);
		}
		
		return result;
	}
	
	public static final class Rib {
		private Entities leftEntity;
		private Entities rightEntity;
		
		public Rib(Entities leftEntity, Entities rightEntity) {
			this.leftEntity = leftEntity;
			this.rightEntity = rightEntity;
		}

		@Override
		public int hashCode() {
			final int prime = 7;
			int result = (leftEntity == null) ? 0 : leftEntity.hashCode();
			result = prime * result + ((rightEntity == null) ? 0 : rightEntity.hashCode());
			
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Rib other = (Rib) obj;
			if (leftEntity != other.leftEntity)
				return false;
			if (rightEntity != other.rightEntity)
				return false;
			
			return true;
		}

		public Entities getLeftEntity() {
			return leftEntity;
		}

		public void setLeftEntity(Entities leftEntity) {
			this.leftEntity = leftEntity;
		}

		public Entities getRightEntity() {
			return rightEntity;
		}

		public void setRightEntity(Entities rightEntity) {
			this.rightEntity = rightEntity;
		}


		public static final class LinkedFields {
			private String leftEntityField;
			private String rightEntityField;
			
			public LinkedFields(String leftEntityField, String rightEntityField) {
				this.leftEntityField = leftEntityField;
				this.rightEntityField = rightEntityField;
			}
			
			@Override
			public int hashCode() {
				final int prime = 10;
				int result = ((leftEntityField == null) ? 0 : leftEntityField.hashCode());
				result = prime * result + ((rightEntityField == null) ? 0 : rightEntityField.hashCode());
				
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				LinkedFields other = (LinkedFields) obj;
				if (leftEntityField == null) {
					if (other.leftEntityField != null)
						return false;
				} else if (!leftEntityField.equals(other.leftEntityField)) {
					return false;
				}
				if (rightEntityField == null) {
					if (other.rightEntityField != null)
						return false;
				} else if (!rightEntityField.equals(other.rightEntityField)) {
					return false;
				}
				
				return true;
			}

			public String getLeftEntityField() {
				return leftEntityField;
			}
			public void setLeftEntityField(String leftEntityField) {
				this.leftEntityField = leftEntityField;
			}
			public String getRightEntityField() {
				return rightEntityField;
			}
			public void setRightEntityField(String rightEntityField) {
				this.rightEntityField = rightEntityField;
			}
		}
	}
}

