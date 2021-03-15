package com.patient.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.patient.pojo.Patient;
import com.patient.pojo.PatientDto;
import com.patient.serviceImpl.PatientServiceImpl;

@RestController
@RequestMapping("/patient")
public class PatientController {

	@Autowired
	private PatientServiceImpl patientServiceImpl;

	@RequestMapping(method = RequestMethod.GET, path = "/getPatientList")
	public ResponseEntity<List<PatientDto>> getPatientInfo() {

		return new ResponseEntity<List<PatientDto>>(patientServiceImpl.getPatientInfo(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/savePatient")
	public ResponseEntity<PatientDto> saveOrUpdatePatientInfo(@RequestBody PatientDto patient) {

		return new ResponseEntity<PatientDto>(patientServiceImpl.saveOrUpdatePatientInfo(patient), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/updatePatient")
	public ResponseEntity<PatientDto> updatePatientInfo(@RequestBody PatientDto patient) {

		return new ResponseEntity<PatientDto>(patientServiceImpl.saveOrUpdatePatientInfo(patient), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/getPatient/{id}")
	public ResponseEntity<Patient> getPatient(@PathVariable Integer id) {

		return new ResponseEntity<>(patientServiceImpl.getPatient(id), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/deletePatient/{id}")
	public ResponseEntity<String> deletePatientInfo(Integer id) {

		return new ResponseEntity<>(patientServiceImpl.deletePatient(id), HttpStatus.OK);
	}

}
