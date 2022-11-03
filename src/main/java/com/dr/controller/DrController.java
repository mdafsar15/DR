package com.dr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dr.model.DrModel;
import com.dr.service.DrService;

@RestController
@RequestMapping("/dr")
public class DrController {

	@Autowired
	DrService drService;

	@PostMapping("/insert")
	public boolean drInserBirth(@RequestBody DrModel drModel) throws Exception {

		drService.DrInsertBirth(drModel);
		return true;
	}

	@GetMapping("/drUpdate")
	public Object drUpdate() throws Exception {
		return drService.DrUpdatePendingToSubmmitInBlockchain();
	}

	@GetMapping("/all")
	public List<Object> all() throws Exception {
		return drService.insertDrDbToBlockchain();
	}

}
