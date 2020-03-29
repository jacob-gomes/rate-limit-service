package com.blue.optima.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blue.optima.assignment.controller.model.RecordDTO;
import com.blue.optima.assignment.service.ApiService;

@Component
@RestController
@RequestMapping("records")
public class ApiController {
		
	private ApiService apiService;	
	
	@Autowired
	public ApiController(ApiService apiService) {
		super();
		this.apiService = apiService;
	}

	@GetMapping("")
	public ResponseEntity<RecordDTO> getRecord(@RequestParam("id") String id) {
		
		RecordDTO recordDTO = apiService.getRecord(id);
		
		return new ResponseEntity<>(recordDTO,HttpStatus.OK);		
	}
	
	@PostMapping("")
	public ResponseEntity<String> createRecord(@RequestBody RecordDTO recordDTORequestBody) {
		
		String id = apiService.createRecord(recordDTORequestBody);
		
		return new ResponseEntity<>(id,HttpStatus.CREATED);		
	}
	
	@DeleteMapping("")
	public ResponseEntity<?> deleteRecord(@RequestParam("id") String id) {		
		apiService.deleteRecord(id);	
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("")
	public ResponseEntity<?> updateRecord(@RequestParam("id") String id,
			@RequestBody RecordDTO recordDTORequestBody) {		
		apiService.updateRecord(id,recordDTORequestBody);	
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
