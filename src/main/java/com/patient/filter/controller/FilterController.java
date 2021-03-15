package com.patient.filter.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.patient.pojo.Criteria;
import com.patient.pojo.Patient;
import com.patient.pojo.PatientDto;
import com.patient.serviceImpl.PatientServiceImpl;

@RestController
@RequestMapping("/filter")
public class FilterController {

	@Autowired
	PatientServiceImpl patientServiceImpl;
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Set<PatientDto>> getFilteredPatients(@RequestBody Criteria filterCriteria){
		
		return new ResponseEntity<Set<PatientDto>>(patientServiceImpl.getFilteredPatients(filterCriteria), HttpStatus.OK);
	}

}
