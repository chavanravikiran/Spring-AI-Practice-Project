package com.example.jobagent.model;

import java.util.List;
import java.util.Map;

public record CandidateProfile(
        // Personal
        String firstName,
        String lastName,
        String name,
        String email,
        String phone,
        String dateOfBirth,
        String gender,

        // Professional
        String headline,
        String targetRole,
        String summary,

        // Address
        String location,
        String address,
        String city,
        String state,
        String country,
        String zipCode,

        // Social
        String linkedin,
        String github,
        String portfolio,
        String website,

        // Work
        String currentCompany,
        String currentDesignation,
        Integer totalExperience,
        Integer relevantExperience,
        String noticePeriod,
        String currentCTC,
        String expectedCTC,
        String currentSalary,
        String expectedSalary,

        // Authorization
        String workAuthorization,
        String visaStatus,
        Boolean requireSponsorship,

        // Education
        String degree,
        String college,
        String graduationYear,
        String cgpa,
        String education,

        // Resume
        String resumePath,

        // Languages
        String languages,

        // Certifications
        String certifications,

        // Driving
        String drivingLicense,

        // Skills
        List<String> skills,
        Map<String,Integer> skillExperience,

        List<String> experience,
        
        String coverLetter,

        String whyJoin,

        String whyHire,

        String strength,

        String weakness,

        String achievement,

        String leadership,

        String teamWork,

        String motivation,

        String careerGoal,
        
     // Project
        String recentProject,
        String currentProject,
        String responsibilities,
        String teamSize,
        String projectDuration,

        // Employment
        String employmentType,
        String department,
        String industry,

        // Salary
        String currentFixedSalary,
        String currentVariableSalary,
        String currentMonthlySalary,

        // Education
        String percentage10,
        String percentage12,
        String graduationPercentage,
        String postGraduationPercentage,

        // Availability
        String lastWorkingDay,
        String expectedJoiningDate,

        // Misc
        String reasonForLeaving,
        String conflictResolution,
        String biggestChallenge,
        String technologiesUsed
		
		) {
}