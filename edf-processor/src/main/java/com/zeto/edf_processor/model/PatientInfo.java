package com.zeto.edf_processor.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*
* Value object for representing the Patient Details from EDF Header SubjectID
* EDF Standard for SubjectID: PatientCode + Sex + Birthdate + Name
* SubjectID example: DO0815199 F 06-MAY-2024 Jane_Doe
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PatientInfo {
    private final String subjectId;
    private final String patientName;

    public static PatientInfo from(String subjectId) {
        if (subjectId == null || subjectId.isBlank()) {
            return new PatientInfo(null, "Not Avaliable");
        }
        String extractedName = extractPatientName(subjectId);
        return new PatientInfo(subjectId, extractedName);
    }

    /* EDF SubjectID is a free text, but patient name is the 4th token of SubjectID by standard
     * Patient Name is the format of FirstName_OptionalFamilyName1_OptionalName2_...,
     * Optionally with prefix contains '.', eg.: Dr., Mrs. etc.
     */
    private static String extractPatientName(String subjectId) {
        String[] parts = subjectId.trim().split("\\s+");
        if (parts.length >= 4 && parts[3].matches("^[A-Za-z.]+(?:_[A-Za-z.]+)+$")) {
            return parts[3].trim().replaceAll("_", " " );
        }
        return "Not Avaliable";
    }
}
