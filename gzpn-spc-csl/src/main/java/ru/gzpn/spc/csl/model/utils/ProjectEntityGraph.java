package ru.gzpn.spc.csl.model.utils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ru.gzpn.spc.csl.model.utils.ProjectEntityGraph.Rib.LinkedFields;

/**
 * The graph of the project entities. It holds the relationships between entities (nodes)
 * Contains useful methods to get path through the intermediate entities etc.
 */
public class ProjectEntityGraph {
	// the graph
	private static final int [][] G = new int [][] {{0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 }, // the first column and row aren't being used
												    {0 ,0 ,1 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 }, // 1 HProject				//Крупный проект
												  	{0 ,2 ,0 ,2 ,2 ,2 ,5 ,4 ,13,4 ,2 ,4 ,4 ,2 ,4 ,5 }, // 2 CProject				//Капитальный проект
												  	{0 ,2 ,3 ,0 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 }, // 3 Phase					//Фаза
												  	{0 ,2 ,4 ,2 ,0 ,2 ,2 ,4 ,2 ,7 ,2 ,7 ,7 ,2 ,4 ,2 }, // 4 Stage					//Стадия
												  	{0 ,2 ,5 ,2 ,2 ,0 ,5 ,6 ,6 ,6 ,2 ,6 ,6 ,2 ,2 ,5 }, // 5 PlanObject				//Объекты генплана
												  	{0 ,5 ,5 ,5 ,7 ,6 ,0 ,6 ,13,6 ,7 ,7 ,7 ,6 ,7 ,6 }, // 6 Work					//Работа
												  	{0 ,4 ,4 ,4 ,7 ,6 ,7 ,0 ,6 ,7 ,7 ,7 ,7 ,6 ,7 ,6 }, // 7 LocalEstimate			//Локальная смета 
													{0 ,13,13,13,13,13,13,13,0 ,13,13,13,13,8 ,13,13}, // 8 Contract 				//Договор
												  	{0 ,7 ,7 ,7 ,7 ,6 ,9 ,9 ,6 ,0 ,7 ,7 ,7 ,6 ,7 ,6 }, // 9 Document				//Документ
												  	{0 ,2 ,10,2 ,2 ,2 ,7 ,10,2 ,7 ,0 ,7 ,7 ,2 ,10,2 }, // 10 EstimateCalculation	//Сметный расчет
													{0 ,7 ,7 ,7 ,7 ,7 ,7 ,11,7 ,7 ,7 ,0 ,7 ,7 ,11,7 }, // 11 EstimateCost			//Сметная стоимость
												  	{0 ,7 ,7 ,7 ,7 ,7 ,7 ,12,7 ,7 ,7 ,7 ,0 ,7 ,12,7 }, // 12 EstimateHead			//Главы
												  	{0 ,2 ,13,2 ,2 ,2 ,13,6 ,13,6 ,2 ,6 ,6 ,0 ,2 ,6 }, // 13 Milestone				//Этап
												  	{0 ,4 ,4 ,4 ,14,4 ,7 ,14,4 ,7 ,14,14,14,4 ,0 ,7 }, // 14 ObjectEstimate			//Объектная смета
												  	{0 ,5 ,5 ,5 ,5 ,15,15,6 ,6 ,6 ,5 ,6 ,6 ,6 ,6 ,0 }  // 15 WorkSet 				//Комплекты
													};

	private static final EnumMap<Entities, Integer> mapNodes = new EnumMap<>(Entities.class);
	private static final Map<Rib, LinkedFields> mapRibs = new HashMap<>();
	
	static {
		mapNodes.put(Entities.HPROJECT, 1);
		mapNodes.put(Entities.CPROJECT, 2);
		mapNodes.put(Entities.PHASE, 3);
		mapNodes.put(Entities.STAGE, 4);
		mapNodes.put(Entities.PLANOBJECT, 5);
		mapNodes.put(Entities.WORK, 6);
		mapNodes.put(Entities.LOCALESTIMATE, 7);
		mapNodes.put(Entities.CONTRACT, 8);
		mapNodes.put(Entities.DOCUMENT, 9);
		mapNodes.put(Entities.ESTIMATE_CALCULATION, 10);
		mapNodes.put(Entities.ESTIMATE_COST, 11);
		mapNodes.put(Entities.ESTIMATE_HEAD, 12);
		mapNodes.put(Entities.MILESTONE, 13);
		mapNodes.put(Entities.OBJECT_ESTIMATE, 14);
		mapNodes.put(Entities.WORK_SET, 15);
		
		mapRibs.put(new Rib(Entities.HPROJECT, Entities.CPROJECT), new LinkedFields("id", "hproject"));
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.HPROJECT), new LinkedFields("hproject", "id"));
		
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.PHASE), new LinkedFields("phase", "id"));
		mapRibs.put(new Rib(Entities.PHASE, Entities.CPROJECT), new LinkedFields("id", "phase"));
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.STAGE), new LinkedFields("stage", "id"));
		mapRibs.put(new Rib(Entities.STAGE, Entities.CPROJECT), new LinkedFields("id", "stage"));
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.PLANOBJECT), new LinkedFields("id", "cproject"));
		mapRibs.put(new Rib(Entities.PLANOBJECT, Entities.CPROJECT), new LinkedFields("cproject", "id"));
		//new Ribs 16.12.2018 by Fisenko KI
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.ESTIMATE_CALCULATION), new LinkedFields("estimateCalculations", "project"));
		mapRibs.put(new Rib(Entities.ESTIMATE_CALCULATION, Entities.CPROJECT), new LinkedFields("project", "estimateCalculations"));
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.MILESTONE), new LinkedFields("milestone", "project"));
		mapRibs.put(new Rib(Entities.MILESTONE, Entities.CPROJECT), new LinkedFields("project", "milestone"));
		
		mapRibs.put(new Rib(Entities.STAGE, Entities.LOCALESTIMATE), new LinkedFields("id", "stage"));
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.STAGE), new LinkedFields("stage", "id"));
		mapRibs.put(new Rib(Entities.STAGE, Entities.OBJECT_ESTIMATE), new LinkedFields("objectEstimates", "stage"));
		mapRibs.put(new Rib(Entities.OBJECT_ESTIMATE, Entities.STAGE), new LinkedFields("stage", "objectEstimates"));
		
		mapRibs.put(new Rib(Entities.PLANOBJECT, Entities.WORK_SET), new LinkedFields("workset", "planObject"));
		mapRibs.put(new Rib(Entities.WORK_SET, Entities.PLANOBJECT), new LinkedFields("planObject", "workset"));
		mapRibs.put(new Rib(Entities.PLANOBJECT, Entities.WORK), new LinkedFields("id", "planObj"));
		mapRibs.put(new Rib(Entities.WORK, Entities.PLANOBJECT), new LinkedFields("planObj", "id"));
		
		mapRibs.put(new Rib(Entities.WORK, Entities.DOCUMENT), new LinkedFields("documents", "work"));
		mapRibs.put(new Rib(Entities.DOCUMENT, Entities.WORK), new LinkedFields("work", "documents"));
		mapRibs.put(new Rib(Entities.WORK, Entities.LOCALESTIMATE), new LinkedFields("localEstimate", "id"));
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.WORK), new LinkedFields("id", "localEstimate"));
		mapRibs.put(new Rib(Entities.WORK, Entities.MILESTONE), new LinkedFields("milestone", "works"));
		mapRibs.put(new Rib(Entities.MILESTONE, Entities.WORK), new LinkedFields("works", "milestone"));
		
		
		mapRibs.put(new Rib(Entities.WORK, Entities.WORK_SET), new LinkedFields("workSet", "id"));
		mapRibs.put(new Rib(Entities.WORK_SET, Entities.WORK), new LinkedFields("id", "workSet"));
		
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.DOCUMENT), new LinkedFields("document", "localEstimates"));
		mapRibs.put(new Rib(Entities.DOCUMENT, Entities.LOCALESTIMATE), new LinkedFields("localEstimates", "document"));
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.ESTIMATE_CALCULATION), new LinkedFields("estimateCalculation", "localEstimates"));
		mapRibs.put(new Rib(Entities.ESTIMATE_CALCULATION, Entities.LOCALESTIMATE), new LinkedFields("localEstimates", "estimateCalculation"));
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.ESTIMATE_COST), new LinkedFields("estimateCosts", "localEstimate"));
		mapRibs.put(new Rib(Entities.ESTIMATE_COST, Entities.LOCALESTIMATE), new LinkedFields("localEstimate", "estimateCosts"));
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.ESTIMATE_HEAD), new LinkedFields("estimateHead", "localEstimates"));
		mapRibs.put(new Rib(Entities.ESTIMATE_HEAD, Entities.LOCALESTIMATE), new LinkedFields("localEstimates", "estimateHead"));
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.OBJECT_ESTIMATE), new LinkedFields("objectEstimate", "localEstimates"));
		mapRibs.put(new Rib(Entities.OBJECT_ESTIMATE, Entities.LOCALESTIMATE), new LinkedFields("localEstimates", "objectEstimate"));
		
		mapRibs.put(new Rib(Entities.CONTRACT, Entities.MILESTONE), new LinkedFields("milestones", "contract"));
		mapRibs.put(new Rib(Entities.MILESTONE, Entities.CONTRACT), new LinkedFields("contract", "milestones"));
		
		mapRibs.put(new Rib(Entities.ESTIMATE_CALCULATION, Entities.OBJECT_ESTIMATE), new LinkedFields("objectEstimates", "estimateCalculation"));
		mapRibs.put(new Rib(Entities.OBJECT_ESTIMATE, Entities.ESTIMATE_CALCULATION), new LinkedFields("estimateCalculation", "objectEstimates"));
		
		mapRibs.put(new Rib(Entities.ESTIMATE_COST, Entities.OBJECT_ESTIMATE), new LinkedFields("objectEstimate", "estimateCosts"));
		mapRibs.put(new Rib(Entities.OBJECT_ESTIMATE, Entities.ESTIMATE_COST), new LinkedFields("estimateCosts", "objectEstimate"));
		
		mapRibs.put(new Rib(Entities.ESTIMATE_HEAD, Entities.OBJECT_ESTIMATE), new LinkedFields("objectEstimates", "estimateHead"));
		mapRibs.put(new Rib(Entities.OBJECT_ESTIMATE, Entities.ESTIMATE_HEAD), new LinkedFields("estimateHead", "objectEstimates"));
		
		// hierarchical entities
		mapRibs.put(new Rib(Entities.PLANOBJECT, Entities.PLANOBJECT), new LinkedFields("id", "parent"));
		mapRibs.put(new Rib(Entities.PHASE, Entities.PHASE), new LinkedFields("id", "parent"));
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
	
	/**
	 * Get relations (the foreign key fields) between two nodes (entities) 
	 * If left_node = right_node (i.e. relation between the same node) we
	 * say that left_node can relates to the right_node by the ID field.
	 * @param nodeFrom
	 * @param nodeTo
	 * @return
	 */
	public static Optional<LinkedFields> getLinkedFields(String nodeFrom, String nodeTo) {
		Entities left = Entities.valueOf(nodeFrom.toUpperCase());
		Entities right = Entities.valueOf(nodeTo.toUpperCase());
		return Optional.ofNullable(
				mapRibs.computeIfAbsent(new Rib(left, right), e -> {
					LinkedFields result = null;
					if (left == right) {
						result = new LinkedFields("id", "id");
					}
					return result;
				}));
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
			private String linkField;
			
			
			public LinkedFields(String leftEntityField, String rightEntityField) {
				this.leftEntityField = leftEntityField;
				this.rightEntityField = rightEntityField;
			}
			
			public LinkedFields(String leftEntityField, String rightEntityField, String linkField) {
				this.setLeftEntityField(leftEntityField);
				this.setRightEntityField(rightEntityField);
				this.setLinkField(linkField);
			}
			
			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + ((leftEntityField == null) ? 0 : leftEntityField.hashCode());
				result = prime * result + ((linkField == null) ? 0 : linkField.hashCode());
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
				if (linkField == null) {
					if (other.linkField != null)
						return false;
				} else if (!linkField.equals(other.linkField)) {
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
			
			public String getLinkField() {
				return linkField;
			}

			public void setLinkField(String linkField) {
				this.linkField = linkField;
			}
			
			@Override
			public String toString() {
				return "LinkedFields [leftEntityField=" + leftEntityField + ", rightEntityField=" + rightEntityField
						+ "]";
			}
		}
	}
}

