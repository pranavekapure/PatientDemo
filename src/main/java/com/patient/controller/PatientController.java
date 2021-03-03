package com.patient.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.patient.pojo.Patient;
import com.patient.serviceiImpl.PatientServiceImpl;

@RestController
@RequestMapping("/patient")
public class PatientController {

	@Autowired
	PatientServiceImpl patientServiceImpl;

	@RequestMapping(method = RequestMethod.GET, path = "/getPatientList")
	public ResponseEntity<List<Patient>> getPatientInfo() {

		return new ResponseEntity<List<Patient>>(patientServiceImpl.getPatientInfo(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/savePatient")
	public ResponseEntity<Patient> saveOrUpdatePatientInfo(@RequestBody Patient patient) {

		return new ResponseEntity<>(patientServiceImpl.saveOrUpdatePatientInfo(patient), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/updatePatient")
	public ResponseEntity<Patient> updatePatientInfo(@RequestBody Patient patient) {

		return new ResponseEntity<>(patientServiceImpl.saveOrUpdatePatientInfo(patient), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getPatient/{id}")
	public ResponseEntity<Patient> getPatient(Integer id) {

		return new ResponseEntity<>(patientServiceImpl.getPatient(id), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/deletePatient/{id}")
	public ResponseEntity<String> deletePatientInfo(Integer id) {

		return new ResponseEntity<>(patientServiceImpl.deletePatient(id), HttpStatus.OK);
	}
	
}
