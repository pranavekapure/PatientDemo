package com.patient.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.patient.pojo.Patient;
import com.patient.pojo.PatientDto;
import com.patient.serviceImpl.PatientServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PatientControllerTest {

	@InjectMocks
	private PatientController patientController;
	
	@Mock
	private PatientServiceImpl patientServiceImpl;
	
	@Test
	public void getPatientInfoTest_shouldReturnListOfPatients() {
		//given
		List<PatientDto> listPatient = new ArrayList<PatientDto>();
		PatientDto patient = new PatientDto();
		patient.setPatientId(1);
		listPatient.add(patient);
		//When
		Mockito.doReturn(listPatient).when(patientServiceImpl).getPatientInfo();
		List<PatientDto> actualPatientList= patientController.getPatientInfo().getBody();
		//then
		assertEquals(listPatient.get(0).getPatientId(), actualPatientList.get(0).getPatientId());
	}
	

	@Test
	public void saveOrUpdatePatientInfo_shouldReurnSavedPatient() {
		//given
		PatientDto patient = new PatientDto();
		patient.setPatientId(1);
		//when
		Mockito.doReturn(patient).when(patientServiceImpl).saveOrUpdatePatientInfo(Mockito.anyObject());
		//then
		PatientDto actualSavedPatient=patientController.saveOrUpdatePatientInfo(patient).getBody();
		assertEquals(1, actualSavedPatient.getPatientId());
	}
}
