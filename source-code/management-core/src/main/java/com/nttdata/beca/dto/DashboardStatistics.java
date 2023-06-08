package com.nttdata.beca.dto;

import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class DashboardStatistics {

    private Long sessionId;
    private int candidateNumber;
    private int preselectedCandidateNumber;
    private int admittedCandidateNumber;
    private int maleCandidateNumber;
    private int femaleCandidateNumber;
    private Map<String, Integer> candidatesByCities = new HashMap<>();
    private Map<String, Integer> candidatesByUniversities = new HashMap<>();
    private Map<String, Integer> candidatesByDiplome = new HashMap<>();

    public DashboardStatistics(List<CandidateDTO> candidates) {
        if (candidates == null || candidates.size() == 0)
            return;//Add some error handling here
        this.sessionId = candidates.get(0).getSession().getSessionId();
        this.candidateNumber = candidates.size();
        this.preselectedCandidateNumber = 0;
        this.admittedCandidateNumber = 0;
        this.maleCandidateNumber = 0;
        this.femaleCandidateNumber = 0;
    }

    public DashboardStatistics(List<CandidateDTO> candidates, List<InternshipDTO> internships,Long sessionId) {
        this.sessionId=sessionId;
        if (candidates == null || candidates.size() == 0)
            return;//Add some error handling here
        this.sessionId = candidates.get(0).getSession().getSessionId();
        this.candidateNumber = candidates.size();
        List<CandidateDTO> preselectedCandidates = candidates.stream().filter(candidate -> candidate.getPreselectionStatus() != null && candidate.getPreselectionStatus() != false )
                .collect(Collectors.toList());
        List<CandidateDTO> admittedCandidates;
        if (internships != null) {
           admittedCandidates = candidates.stream()
                    .filter(candidate -> internships.stream()
                            .anyMatch(internship -> internship.getCandidate().getCandidateId() == candidate.getCandidateId()))
                    .collect(Collectors.toList());
        }
        else {
            admittedCandidates = candidates.stream()
                    .filter(candidate -> !candidate.getType().equals("internship"))
                    .filter(candidate -> candidate.getScore() != null && candidate.getScore().getIsAccepted())
                    .collect(Collectors.toList());
        }
        this.preselectedCandidateNumber = preselectedCandidates.size();
        this.admittedCandidateNumber = admittedCandidates.size();
        this.maleCandidateNumber = admittedCandidates.stream().filter(candidate -> candidate.getGender().equals("male")).collect(Collectors.toList()).size();
        this.femaleCandidateNumber = admittedCandidates.stream().filter(candidate -> candidate.getGender().equals("female")).collect(Collectors.toList()).size();
        this.candidatesByCities = admittedCandidates.stream()
                .collect(Collectors.groupingBy(CandidateDTO::getCity, Collectors.counting()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().intValue()));


        this.candidatesByUniversities = admittedCandidates.stream()
                .collect(Collectors.groupingBy(CandidateDTO::getUniversity, Collectors.counting()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().intValue()));

        this.candidatesByDiplome = admittedCandidates.stream()
                .collect(Collectors.groupingBy(CandidateDTO::getDiplome, Collectors.counting()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().intValue()));


    }
}
