package ru.gzpn.spc.csl.model.utils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.utils.ProjectEntityGraph.Rib.LinkedFields;

/**
 * The graph of the project entities. It holds the relationships between entities (nodes)
 * Contains useful methods to get path through the intermediate entities etc.
 */
public class ProjectEntityGraph {
	// the graph
	private static final int [][] G = new int [][] {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // the first column and row aren't being used
												    {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 }, // 1 HProject				//Крупный проект
												  	{0, 2, 0, 2, 2, 2, 5, 5, 4, 14,4, 2, 4, 4, 2, 4, 5 }, // 2 CProject				//Капитальный проект
												  	{0, 2, 3, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 }, // 3 Phase					//Фаза
												  	{0, 2, 4, 2, 0, 2, 2, 2, 4, 2, 8, 2, 8, 8, 2, 4, 2 }, // 4 Stage					//Стадия
												  	{0, 2, 5, 2, 2, 0, 5, 5, 6, 7, 7, 2, 7, 7, 2, 2, 5 }, // 5 PlanObject				//Объекты генплана
												  	{0, 5, 5, 5, 5, 6, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 }, // 6 Mark    				// Марка
												  	{0, 5, 5, 5, 7, 6, 5, 0, 7, 14,7, 8, 8, 8, 7, 8, 7 }, // 7 Work					//Работа
												  	{0, 4, 4, 4, 7, 6, 4, 8, 0, 7, 8, 8, 8, 8, 7, 8, 10}, // 8 LocalEstimate			//Локальная смета 
													{0,14,14,14,14,14,14,14,14, 0,14,14,14,14, 9,14, 14}, // 9 Contract 				//Договор
												  	{0, 8, 8, 8, 8,16,16,10,10, 7, 0, 8, 8, 8, 7, 8, 7 }, // 10 Document				//Документ
												  	{0, 2,11, 2, 2, 2, 2, 8,11, 2, 8, 0, 8, 8, 2,11, 2 }, // 11 EstimateCalculation	//Сметный расчет
													{0, 8, 8, 8, 8, 8, 8, 8,12, 8, 8, 8, 0, 8, 8,12, 8 }, // 12 EstimateCost			//Сметная стоимость
												  	{0, 8, 8, 8, 8, 8, 8, 8,13, 8, 8, 8, 8, 0, 8,13, 8 }, // 13 EstimateHead			//Главы
												  	{0, 2, 14,2, 2, 2, 2,14, 7,14, 7, 2, 7, 7, 0, 2, 7 }, // 14 Milestone				//Этап
												  	{0, 4, 4, 4,15, 4, 4, 8,15, 4, 8,15,15,15, 4 ,0 ,8 }, // 15 ObjectEstimate			//Объектная смета
												  	{0 ,5 ,5 ,5 ,5 ,16,5,16, 7, 7, 7, 5, 7, 7, 7, 7, 0 }  // 16 WorkSet 				//Комплекты
													};

	private static final EnumMap<Entities, Integer> mapNodes = new EnumMap<>(Entities.class);
	private static final Map<Rib, LinkedFields> mapRibs = new HashMap<>();
	
	static {
		mapNodes.put(Entities.HPROJECT, 1);
		mapNodes.put(Entities.CPROJECT, 2);
		mapNodes.put(Entities.PHASE, 3);
		mapNodes.put(Entities.STAGE, 4);
		mapNodes.put(Entities.PLANOBJECT, 5);
		mapNodes.put(Entities.MARK, 6);
		mapNodes.put(Entities.WORK, 7);
		mapNodes.put(Entities.LOCALESTIMATE, 8);
		mapNodes.put(Entities.CONTRACT, 9);
		mapNodes.put(Entities.DOCUMENT, 10);
		mapNodes.put(Entities.ESTIMATECALCULATION, 11);
		mapNodes.put(Entities.ESTIMATECOST, 12);
		mapNodes.put(Entities.ESTIMATEHEAD, 13);
		mapNodes.put(Entities.MILESTONE, 14);
		mapNodes.put(Entities.OBJECTESTIMATE, 15);
		mapNodes.put(Entities.WORKSET, 16);
		
		mapRibs.put(new Rib(Entities.HPROJECT, Entities.CPROJECT), new LinkedFields("id", "hproject"));
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.HPROJECT), new LinkedFields("hproject", "id"));
		
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.PHASE), new LinkedFields("phase", "id"));
		mapRibs.put(new Rib(Entities.PHASE, Entities.CPROJECT), new LinkedFields("id", "phase"));
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.STAGE), new LinkedFields("stage", "id"));
		mapRibs.put(new Rib(Entities.STAGE, Entities.CPROJECT), new LinkedFields("id", "stage"));
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.PLANOBJECT), new LinkedFields("id", "cproject"));
		mapRibs.put(new Rib(Entities.PLANOBJECT, Entities.CPROJECT), new LinkedFields("cproject", "id"));
		//new Ribs 16.12.2018 by Fisenko KI
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.ESTIMATECALCULATION), new LinkedFields("estimateCalculations", "project"));
		mapRibs.put(new Rib(Entities.ESTIMATECALCULATION, Entities.CPROJECT), new LinkedFields("project", "estimateCalculations"));
		mapRibs.put(new Rib(Entities.CPROJECT, Entities.MILESTONE), new LinkedFields("milestone", "project"));
		mapRibs.put(new Rib(Entities.MILESTONE, Entities.CPROJECT), new LinkedFields("project", "milestone"));
		
		mapRibs.put(new Rib(Entities.STAGE, Entities.LOCALESTIMATE), new LinkedFields("id", "stage"));
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.STAGE), new LinkedFields("stage", "id"));
		mapRibs.put(new Rib(Entities.STAGE, Entities.OBJECTESTIMATE), new LinkedFields("objectEstimates", "stage"));
		mapRibs.put(new Rib(Entities.OBJECTESTIMATE, Entities.STAGE), new LinkedFields("stage", "objectEstimates"));
		
		mapRibs.put(new Rib(Entities.PLANOBJECT, Entities.WORKSET), new LinkedFields("id", "planObject"));
		mapRibs.put(new Rib(Entities.WORKSET, Entities.PLANOBJECT), new LinkedFields("planObject", "id"));
		mapRibs.put(new Rib(Entities.PLANOBJECT, Entities.WORK), new LinkedFields("id", "planObj"));
		mapRibs.put(new Rib(Entities.WORK, Entities.PLANOBJECT), new LinkedFields("planObj", "id"));
		
		mapRibs.put(new Rib(Entities.WORK, Entities.DOCUMENT), new LinkedFields("documents", "work"));
		mapRibs.put(new Rib(Entities.DOCUMENT, Entities.WORK), new LinkedFields("work", "documents"));
		mapRibs.put(new Rib(Entities.WORK, Entities.LOCALESTIMATE), new LinkedFields("localEstimate", "id"));
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.WORK), new LinkedFields("id", "localEstimate"));
		mapRibs.put(new Rib(Entities.WORK, Entities.MILESTONE), new LinkedFields("milestone", "works"));
		mapRibs.put(new Rib(Entities.MILESTONE, Entities.WORK), new LinkedFields("works", "milestone"));
		
		
		mapRibs.put(new Rib(Entities.WORK, Entities.WORKSET), new LinkedFields("workSet", "id"));
		mapRibs.put(new Rib(Entities.WORKSET, Entities.WORK), new LinkedFields("id", "workSet"));
		
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.DOCUMENT), new LinkedFields("document", "localEstimates"));
		mapRibs.put(new Rib(Entities.DOCUMENT, Entities.LOCALESTIMATE), new LinkedFields("localEstimates", "document"));
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.ESTIMATECALCULATION), new LinkedFields("estimateCalculation", "localEstimates"));
		mapRibs.put(new Rib(Entities.ESTIMATECALCULATION, Entities.LOCALESTIMATE), new LinkedFields("localEstimates", "estimateCalculation"));
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.ESTIMATECOST), new LinkedFields("estimateCosts", "localEstimate"));
		mapRibs.put(new Rib(Entities.ESTIMATECOST, Entities.LOCALESTIMATE), new LinkedFields("localEstimate", "estimateCosts"));
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.ESTIMATEHEAD), new LinkedFields("estimateHead", "localEstimates"));
		mapRibs.put(new Rib(Entities.ESTIMATEHEAD, Entities.LOCALESTIMATE), new LinkedFields("localEstimates", "estimateHead"));
		mapRibs.put(new Rib(Entities.LOCALESTIMATE, Entities.OBJECTESTIMATE), new LinkedFields("objectEstimate", "localEstimates"));
		mapRibs.put(new Rib(Entities.OBJECTESTIMATE, Entities.LOCALESTIMATE), new LinkedFields("localEstimates", "objectEstimate"));
		
		mapRibs.put(new Rib(Entities.CONTRACT, Entities.MILESTONE), new LinkedFields("milestones", "contract"));
		mapRibs.put(new Rib(Entities.MILESTONE, Entities.CONTRACT), new LinkedFields("contract", "milestones"));
		
		mapRibs.put(new Rib(Entities.ESTIMATECALCULATION, Entities.OBJECTESTIMATE), new LinkedFields("objectEstimates", "estimateCalculation"));
		mapRibs.put(new Rib(Entities.OBJECTESTIMATE, Entities.ESTIMATECALCULATION), new LinkedFields("estimateCalculation", "objectEstimates"));
		
		mapRibs.put(new Rib(Entities.ESTIMATECOST, Entities.OBJECTESTIMATE), new LinkedFields("objectEstimate", "estimateCosts"));
		mapRibs.put(new Rib(Entities.OBJECTESTIMATE, Entities.ESTIMATECOST), new LinkedFields("estimateCosts", "objectEstimate"));
		
		mapRibs.put(new Rib(Entities.ESTIMATEHEAD, Entities.OBJECTESTIMATE), new LinkedFields("objectEstimates", "estimateHead"));
		mapRibs.put(new Rib(Entities.OBJECTESTIMATE, Entities.ESTIMATEHEAD), new LinkedFields("estimateHead", "objectEstimates"));
		
		mapRibs.put(new Rib(Entities.PLANOBJECT, Entities.MARK), new LinkedFields("mark", "id"));
		mapRibs.put(new Rib(Entities.MARK, Entities.PLANOBJECT), new LinkedFields("id", "mark"));
		
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

