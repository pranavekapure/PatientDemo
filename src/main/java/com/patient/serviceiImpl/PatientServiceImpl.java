package com.patient.serviceiImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.patient.exception.APIException;
import com.patient.pojo.Criteria;
import com.patient.pojo.Patient;
import com.patient.service.PatientService;

@Service
public class PatientServiceImpl {

	@Autowired
	PatientService patientService;

	public List<Patient> getPatientInfo() throws APIException {

		List<Patient> patientList = patientService.findAll(Sort.by(Sort.Direction.ASC, "patientId"));

		if (patientList.isEmpty()) {
			throw new APIException(HttpStatus.BAD_REQUEST, "No Patient Available in the DB",
					"No Patient Available in the DB");
		} else {
			return patientList;
		}
	}

	public Patient saveOrUpdatePatientInfo(Patient patient) throws APIException {
		try {
			Patient savedPatient = patientService.save(patient);
			return savedPatient;
		} catch (Exception e) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Patient caould not be saved", e.getMessage());
		}

	}

	public Patient getPatient(Integer id) throws APIException {
		try {
			Patient getPatient = patientService.getOne(id);
			return getPatient;
		} catch (Exception e) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Patient not found", e.getMessage());
		}

	}

	public String deletePatient(Integer id) throws APIException {
		try {
			if (patientService.existsById(id)) {
				patientService.deleteById(id);
				;
				return "Patient has been deleted";
			}
		} catch (Exception e) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Patient not deleted", e.getMessage());
		}
		return "patient with id : " + id + "not found";
	}

	/*-------------------*/
	public Set<Patient> getFilteredPatients(Criteria filterCriteria) {
		List<Patient> patientList = patientService.findAll(Sort.by(Sort.Direction.ASC, "patientId"));
		Set<Patient> filtreedPatientSet = new HashSet<Patient>();
		patientList.stream().forEach(patient -> {
			if (filterCriteria.getListDob()!=null)
			{	filterCriteria.getListDob().stream().filter(dob -> dob.equals(patient.getDob())).forEach(dob -> {
					filtreedPatientSet.add(patient);
				});}
			if (filterCriteria.getListFirstName()!=null)
				{filterCriteria.getListFirstName().stream().filter(firstName -> firstName.equals(patient.getFirstName()))
						.forEach(firstName -> {
							filtreedPatientSet.add(patient);
						});}

			if (filterCriteria.getListLastName()!=null) {
				filterCriteria.getListLastName().stream().filter(lastName -> lastName.equals(patient.getLastName()))
						.forEach(lastName -> {
							filtreedPatientSet.add(patient);
						});}
			if (filterCriteria.getListTreatmentLocation()!=null) {
				filterCriteria.getListTreatmentLocation().stream()
						.filter(treatLocation -> treatLocation.equals(patient.getTreatmentLocation()))
						.forEach(treatLocation -> {
							filtreedPatientSet.add(patient);
						});}
		});
		return filtreedPatientSet;
	}

}
