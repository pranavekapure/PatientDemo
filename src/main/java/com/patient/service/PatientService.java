package com.patient.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patient.pojo.Patient;

@Repository
public interface PatientService extends JpaRepository<Patient, Integer> {

}
