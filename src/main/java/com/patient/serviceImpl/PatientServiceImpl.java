package com.patient.serviceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patient.exception.APIException;
import com.patient.pojo.Criteria;
import com.patient.pojo.Medicine;
import com.patient.pojo.Patient;
import com.patient.pojo.PatientDto;
import com.patient.service.PatientService;

@Service
public class PatientServiceImpl {

	@Autowired
	private PatientService patientService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	private ModelMapper mapper = new ModelMapper();

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		// Do any additional configuration here
		return builder.build();
	}

	public List<PatientDto> getPatientInfo() throws APIException {
		List<Patient> listPatient = patientService.findAll(Sort.by(Sort.Direction.ASC, "patientId"));
		List<PatientDto> patientDtoList = new ArrayList<PatientDto>();
		listPatient.stream().forEach(patient -> {
			PatientDto patientDto = mapper.map(patient, PatientDto.class);
			patientDtoList.add(patientDto);
		});

		Map<Integer, List<Medicine>> medicineMap = getMapOfListOfMedicine();
		medicineMap.entrySet().stream().forEach(medicine -> {
			patientDtoList.stream().forEach(patientDto -> {
				if (patientDto.getPatientId().equals(medicine.getKey())) {
					patientDto.setMedicineList(medicine.getValue());
				}
			});
		});

		if (listPatient.isEmpty()) {
			throw new APIException(HttpStatus.BAD_REQUEST, "No Patient Available in the DB",
					"No Patient Available in the DB");
		} else {
			return patientDtoList;
		}
	}

	public PatientDto saveOrUpdatePatientInfo(PatientDto patient) throws APIException {
		try {
			Patient patientToBeSaved = mapper.map(patient, Patient.class);
			patientService.save(patientToBeSaved);
			String restTemplateURL = "http://localhost:8082/medicine/saveMedicine";
			List<Medicine> medicineList = new ArrayList<>();
			patient.getMedicineList().stream().forEach(medicine -> {
				medicine.setPatientId(patient.getPatientId());
				Medicine med = restTemplate.postForObject(restTemplateURL, medicine, Medicine.class);
				medicineList.add(med);
			});
			patient.setMedicineList(medicineList);
			return patient;
		} catch (Exception e) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Patient caould not be saved", e.getMessage());
		}

	}

	public Patient getPatient(Integer id) throws APIException {
		try {
			Patient getPatient = patientService.findById(id).get();
			/*
			 * PatientDto patientDto = mapper.map(getPatient, PatientDto.class);
			 * Map<Integer, List<Medicine>> medicineMap = getMapOfListOfMedicine();
			 * List<Medicine> listOfMedicines = medicineMap.get(patientDto.getPatientId());
			 * patientDto.setMedicineList(listOfMedicines);
			 */
			return getPatient;
		} catch (Exception e) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Patient not found", e.getMessage());
		}

	}

	public String deletePatient(Integer id) throws APIException {
		try {
			if (patientService.existsById(id)) {
				patientService.deleteById(id);

				return "Patient has been deleted";
			}
		} catch (Exception e) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Patient not deleted", e.getMessage());
		}
		return "patient with id : " + id + "not found";
	}

	private Map<Integer, List<Medicine>> getMapOfListOfMedicine() {
		String restTemplateURL = "http://localhost:8082/medicine/getList";
		String JSONPatientList = restTemplate.getForObject(restTemplateURL, String.class);
		List<Medicine> medicineList = new ArrayList<Medicine>();
		try {
			medicineList = objectMapper.readValue(JSONPatientList, new TypeReference<List<Medicine>>() {
			});

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return medicineList.stream().collect(Collectors.groupingBy(Medicine::getPatientId));
	}

	/*-------------------*/
	public Set<PatientDto> getFilteredPatients(Criteria filterCriteria) {
		List<PatientDto> patientList = getPatientInfo();
		Set<PatientDto> filtredPatientSet = new HashSet<PatientDto>();
		patientList.stream().forEach(patient -> {
			if (filterCriteria.getListDob() != null) {
				filterCriteria.getListDob().stream().filter(dob -> dob.equals(patient.getDob())).forEach(dob -> {
					filtredPatientSet.add(patient);
				});
			}
			if (filterCriteria.getListFirstName() != null) {
				filterCriteria.getListFirstName().stream().filter(firstName -> firstName.equals(patient.getFirstName()))
						.forEach(firstName -> {
							filtredPatientSet.add(patient);
						});
			}

			if (filterCriteria.getListLastName() != null) {
				filterCriteria.getListLastName().stream().filter(lastName -> lastName.equals(patient.getLastName()))
						.forEach(lastName -> {
							filtredPatientSet.add(patient);
						});
			}
			if (filterCriteria.getListTreatmentLocation() != null) {
				filterCriteria.getListTreatmentLocation().stream()
						.filter(treatLocation -> treatLocation.equals(patient.getTreatmentLocation()))
						.forEach(treatLocation -> {
							filtredPatientSet.add(patient);
						});
			}
		});
		return filtredPatientSet;
	}

}
