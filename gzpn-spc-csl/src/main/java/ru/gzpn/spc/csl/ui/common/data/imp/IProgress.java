package ru.gzpn.spc.csl.ui.common.data.imp;

public interface IProgress {
	int getTotalAmount();
	int getProcessed();
	public void setOnProcess(Runnable progressListener);
}
