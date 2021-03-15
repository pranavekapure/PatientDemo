package com.patient.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patient.pojo.Medicine;
import com.patient.pojo.Patient;
import com.patient.pojo.PatientDto;
import com.patient.service.PatientService;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class PatientServiceImplTest {

	@Mock
	private PatientService patientService;

	@Mock
	private ModelMapper mapper;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private PatientServiceImpl patientServiceImpl;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		// Do any additional configuration here
		return builder.build();
	}

	@Test
	public void getPatientInfo_shouldReturnListOfPatients() throws Exception, JsonProcessingException {
		// given
		String restTemplateURL = "http://localhost:9090/medicine/getList";
		List<Patient> listPatient = new ArrayList<Patient>();
		Patient patient = new Patient();
		patient.setPatientId(1);
		listPatient.add(patient);
		List<Medicine> medicineList = new ArrayList<Medicine>();
		Medicine meds = new Medicine();
		meds.setPatientId(1);
		meds.setMedicineId(1);
		medicineList.add(meds);
		PatientDto patientDto = new PatientDto();
		patientDto.setPatientId(1);
		// when

		Mockito.doReturn(listPatient).when(patientService).findAll(Sort.by(Sort.Direction.ASC, "patientId"));
		Mockito.doReturn(patientDto).when(mapper).map(patient, PatientDto.class);
		Mockito.when(restTemplate.getForObject(restTemplateURL, String.class)).thenReturn(restTemplateURL);
		Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(medicineList);
		//when
		List<PatientDto> actualListOfPatients = patientServiceImpl.getPatientInfo();
		// then
		Assert.assertEquals(listPatient.get(0).getPatientId(), actualListOfPatients.get(0).getPatientId());

	}
	
	

}
