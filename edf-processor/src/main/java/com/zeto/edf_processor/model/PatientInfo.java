package com.zeto.edf_processor.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*
* Value object for representing the Patient Details from the EDF SubjectID field.
* EDF Standard for SubjectID: PatientCode + Sex + Birthdate + Name
* SubjectID example: DO0815199 F 06-MAY-2024 Jane_Doe
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PatientInfo {

    /** Raw SubjectID from EDF header */
    private final String subjectId;

    /** Extracted patient name */
    private final String patientName;

    /**
     * Factory method to create a PatientInfo object from a subject ID.
     *
     * @param subjectId EDF SubjectID
     * @return PatientInfo object
     */
    public static PatientInfo from(String subjectId) {
        if (subjectId == null || subjectId.isBlank()) {
            return new PatientInfo(null, "Not Avaliable");
        }
        String extractedName = extractPatientName(subjectId);
        return new PatientInfo(subjectId, extractedName);
    }

    /**
     * Extracts the patient's name from the EDF SubjectID string.
     * EDF SubjectID is a free text, patient name is the 4th token of SubjectID by standard
     * Patient Name is the format of FirstName_OptionalFamilyName1_OptionalName2_...,
     * Optionally with prefix contains '.', eg.: Dr., Mrs. etc.
     *
     * @param subjectId EDF SubjectID
     * @return formatted patient name or "Not Available"
     */
    private static String extractPatientName(String subjectId) {
        String[] parts = subjectId.trim().split("\\s+");
        if (parts.length >= 4 && parts[3].matches("^[A-Za-z.]+(?:_[A-Za-z.]+)+$")) {
            return parts[3].trim().replaceAll("_", " " );
        }
        return "Not Avaliable";
    }
}
