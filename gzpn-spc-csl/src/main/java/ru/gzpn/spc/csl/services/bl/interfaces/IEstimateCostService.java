package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.List;

import ru.gzpn.spc.csl.model.interfaces.IEstimateCost;

public interface IEstimateCostService extends IDataService<IEstimateCost, IEstimateCost> {
	/* get costs by LocalEstimate Id */
	public List<IEstimateCost> getEstimateCostsByLocal(Long id);
	/* create cost and link it to the LocalEstimateId */
	public IEstimateCost createEstimateCostByLocal(IEstimateCost ec, Long localEstimateId);
}
