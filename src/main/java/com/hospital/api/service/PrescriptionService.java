package com.hospital.api.service;

import com.hospital.api.entity.Prescription;
import com.hospital.api.exception.notfound.PrescriptionNotFoundException;
import com.hospital.api.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(String.valueOf(id)));
    }

    public Prescription createPrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    public Prescription updatePrescription(Long id, Prescription prescriptionDetails) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(String.valueOf(id)));

        prescription.setMedication(prescriptionDetails.getMedication());
        prescription.setInstructions(prescriptionDetails.getInstructions());
        prescription.setAppointment(prescriptionDetails.getAppointment());

        return prescriptionRepository.save(prescription);
    }

    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(String.valueOf(id)));

        prescriptionRepository.delete(prescription);
    }
}
