package com.dr.service;

import java.util.List;

import com.dr.model.DrModel;

public interface DrService {

	public boolean DrInsertBirth(DrModel drModel) throws Exception;

	public Object DrUpdatePendingToSubmmitInBlockchain() throws Exception;

	public List<Object> insertDrDbToBlockchain() throws Exception;

}
