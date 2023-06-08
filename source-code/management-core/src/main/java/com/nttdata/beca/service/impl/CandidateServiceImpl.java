package com.nttdata.beca.service.impl;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.dto.*;
import com.nttdata.beca.entity.Candidate;
import com.nttdata.beca.entity.Interview;
import com.nttdata.beca.entity.Session;
import com.nttdata.beca.repository.CandidateRepository;
import com.nttdata.beca.repository.InterviewRepository;
import com.nttdata.beca.repository.SessionRepository;
import com.nttdata.beca.service.CandidateService;
import com.nttdata.beca.transformer.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CandidateServiceImpl extends GenericServiceImpl<Candidate, CandidateDTO, Long> implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private InterviewRepository interviewRepository;




    private static final Transformer<Candidate, CandidateDTO> candidateTransformer = new CandidateTransformer();

    private static final Transformer<Session, SessionDTO> sessionTransformer = new SessionTransformer();

    private static final Transformer<Interview, InterviewDTO> interviewtransformer = new InterviewTransformer();
    public CandidateServiceImpl() {
        super(candidateTransformer);
    }

    @Override
    public List<CandidateDTO> findByEmail(String email) {
        return candidateTransformer.toDTOList(candidateRepository.findByEmail(email));
    }

    @Override
    public List<CandidateDTO> findByPreselectionStatus() {
        return candidateTransformer.toDTOList(candidateRepository.findByPreselectionStatus());
    }
    @Override
    public List<CandidateDTO> findAcceptedInternshipCandidate() {
        return candidateTransformer.toDTOList(candidateRepository.findAcceptedInternshipCandidate());
    }
    @Override
    public List<CandidateDTO> findBySession(Long sessionId) {
        return candidateTransformer.toDTOList(candidateRepository.findBySession(sessionId));
    }

    @Override
    public List<String> findAllUniversities() {
        return candidateRepository.findAllUniversities();
    }

    @Override
    public List<String> findAllCities() {
        return candidateRepository.findAllCities();
    }

    @Override
    public Long countAdmittedPreviousSession() {
        return candidateRepository.countAdmittedPreviousSession();
    }

    @Override
    public Long countAdmittedPreviousInternshipSession() {
        return candidateRepository.countAdmittedPreviousInternshipSession();
    }

    @Override
    public List<CandidateDTO> getCurrentSessionCandidates() {
        return candidateTransformer.toDTOList(candidateRepository.getCurrentSessionCandidates());
    }
    @Override
    public List<CandidateDTO> getCurrentSessionInternshipCandidates() {
        return candidateTransformer.toDTOList(candidateRepository.getCurrentSessionInternshipCandidates());
    }
    @Override
    public List<CandidateDTO> getFinalList(int admittedNumber) {
        return candidateTransformer.toDTOList(candidateRepository.getFinalList(admittedNumber));
    }

    @Override
    public List<CandidateDTO> getAdmitted() {
        return candidateTransformer.toDTOList(candidateRepository.getAdmitted());
    }

    @Override
    public CandidateDTO addCandidate(CandidateDTO candidateDTO) throws Exception {
        SessionDTO currentSession = sessionTransformer.toDTO(sessionRepository.findBySessionStatus("current"));
        List<CandidateDTO> candidates = findByEmail(candidateDTO.getEmail())
                .stream().filter(dto -> dto.getSession().equals(currentSession)).toList();
        if (candidates.size() > 0 && candidates.size() != candidates.stream().filter(CandidateDTO::isDeleted).toList().size()) {
            throw new Exception("Candidate with this email already exists !!");
        } else {
            candidateDTO.setSession(currentSession);
            candidateDTO.setType("recruitment");
            return super.save(candidateDTO);
        }
    }

    @Override
    public CandidateDTO updateCandidate(CandidateDTO candidateDTO) {
        CandidateDTO dto = findById(candidateDTO.getCandidateId());
        dto.setFirstName(candidateDTO.getFirstName());
        dto.setLastName(candidateDTO.getLastName());
        dto.setEmail(candidateDTO.getEmail());
        dto.setPhoneNumber(candidateDTO.getPhoneNumber());
        dto.setCity(candidateDTO.getCity());
        dto.setAnapec(candidateDTO.getAnapec());
        dto.setGender(candidateDTO.getGender());
        dto.setDiplome(candidateDTO.getDiplome());
        dto.setUniversity(candidateDTO.getUniversity());
        dto.setCVname(candidateDTO.getCVname());
        return super.save(dto);
    }
    public CandidateDTO moveWaitingListToSession(CandidateDTO candidateDTO,Long sessionId){
        CandidateDTO dto = findById(candidateDTO.getCandidateId());
        SessionDTO session = sessionTransformer.toDTO(sessionRepository.findBySessionId(sessionId));
        if(session.getType().equals("recruitment")){
            dto.setPreselectionStatus(Boolean.TRUE);
            dto.setSession(session);
            dto.setScore(null);
            dto.setInterview(null);
            dto.setTimeOfInterview(null);
            dto.setInterview(null);
            return super.save(dto);
        }else {
            return candidateDTO;
        }
    }
    @Override
    public Long countPreselectedStatus() {
        return candidateRepository.preselectedCountCurrent();
    }

    @Override
    public List<CandidateDTO> getWaiting(int admittedNumber, int waitingNumber) {
        return candidateTransformer.toDTOList(candidateRepository.getWaiting(admittedNumber, waitingNumber));
    }

    @Override
    public Long countInSession(Long sessionId) {
        return candidateRepository.countInSession(sessionId);
    }

    @Override
    public Long preselectedCount() {
        return candidateRepository.preselectedCount();
    }

    @Override
    public Long admittedCount() {
        return candidateRepository.admittedCount();
    }

    @Override
    public Long countEmail(String email) {
        return candidateRepository.countEmail(email);
    }

    @Override
    public int setAdmitted(int nbAdmitted) {
        if (candidateRepository.setAdmitted(nbAdmitted) > 0)
            return Math.toIntExact(sessionRepository.getLatestSessionId());
        else
            return 0;
    }

    @Override
    public MessageResponse delete(Long candidateId) {
        CandidateDTO candidateDTO = findById(candidateId);
        candidateDTO.setDeleted(true);
        super.save(candidateDTO);
        return new MessageResponse("Candidate has been deleted successfully");
    }

    @Override
    public MessageResponse setPreselectionStatus(Long candidateId, Boolean preselectionStatus) {
        CandidateDTO candidate = findById(candidateId);
        candidate.setPreselectionStatus(preselectionStatus);
        super.save(candidate);
        return new MessageResponse(preselectionStatus ? "Candidate accepted" : "Candidate rejected");
    }

    @Override
    public CandidateDTO saveScore(Long candidateId, ScoreDTO scoreDTO) {
        CandidateDTO candidateDTO = findById(candidateId);
        scoreDTO.setIsAccepted(scoreDTO.getTechnicalTestScore() >= sessionRepository.getEliminatingMark());
        candidateDTO.setScore(scoreDTO);
        return super.save(candidateDTO);
    }

    public void divideCandidates(List<InterviewDTO> DTOS) {
        int numberOfInterviews= DTOS.size();
        String initStartTime = sessionRepository.getstartTimeOfInterview();
        String endTime = sessionRepository.getendTimeOfInterview();
        String startTime = initStartTime;
        int duration = sessionRepository.gettestDuration();
        LocalTime startTimeOfInterview = LocalTime.parse(startTime);
        LocalTime endTimeOfInterview = LocalTime.parse(endTime);
        int dayInterviewNb = sessionRepository.getDayInterviewNumber();
        Duration durationTotal = Duration.between(startTimeOfInterview, endTimeOfInterview);
        long Minutes = durationTotal.toMinutes();
        Long newDayInterviewNb = Minutes/duration;
        int candidatesWithNullTime =0;
        List<LocalDate> existingDates = new ArrayList<>();
        for(InterviewDTO interviewDTO :DTOS){
            Date date = interviewDTO.getDate();
            Instant instant = date.toInstant();
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            existingDates.add(localDate);
        }
        if(newDayInterviewNb<dayInterviewNb){
           dayInterviewNb= Math.toIntExact(newDayInterviewNb);
        }
        List<CandidateDTO> selectedcandidates = findByPreselectionStatus();
        assignInterviews(numberOfInterviews, DTOS, dayInterviewNb, startTimeOfInterview, endTimeOfInterview, duration, candidatesWithNullTime,selectedcandidates);

    }
    private void assignInterviews(int numberOfInterviews, List<InterviewDTO> DTOS, int dayInterviewNb, LocalTime startTimeOfInterview, LocalTime endTimeOfInterview, int duration, int candidatesWithNullTime,  List<CandidateDTO> selectedcandidates) {
        int extraCandidates = selectedcandidates.size() % numberOfInterviews;
        List<Integer> candidatesPerDay = new ArrayList<>();
        int remainingCandidates = selectedcandidates.size();
        for (int day = 1; day <= numberOfInterviews; day++) {
            int candidatesForThisDay = remainingCandidates / (numberOfInterviews - day +1);
            candidatesPerDay.add(candidatesForThisDay);
            remainingCandidates -= candidatesForThisDay;
        }
        int candidateIndex = 0;
        for (int day = 1; day <= numberOfInterviews; day++) {
            List<CandidateDTO> candidatesForThisDay = new ArrayList<>();
            InterviewDTO interviewDTO1 = DTOS.get(day - 1);
            for (int i = 0; i < candidatesPerDay.get(day - 1); i++) {
                candidatesForThisDay.add(selectedcandidates.get(candidateIndex++));
                if (extraCandidates > 0) {
                    extraCandidates--;
                }
            }
            int candidatesAssigned = 0;
            LocalTime currentTime = startTimeOfInterview;
            while (candidatesAssigned < candidatesPerDay.get(day - 1)) {
                int totalSlots = candidatesPerDay.get(day - 1) - candidatesAssigned;
                int slotsPerInterviewer = totalSlots / dayInterviewNb;
                int extraSlots = totalSlots % dayInterviewNb;
                List<Integer> slotsPerInterviewerList = new ArrayList<>();
                for (int i = 0; i < dayInterviewNb; i++) {
                    int slots = slotsPerInterviewer + (i < extraSlots ? 1 : 0);
                    slotsPerInterviewerList.add(slots);
                }
                for (int i = 0; i < dayInterviewNb; i++) {
                    int candidatesForThisTime =Math.min(slotsPerInterviewerList.get(i), candidatesPerDay.get(day - 1) - candidatesAssigned);
                    System.out.println("candidatesForThisTime "+candidatesForThisTime);
                    int j;
                    for (j = 0; j < candidatesForThisTime; j++) {
                        candidatesForThisDay.get(candidatesAssigned + j).setTimeOfInterview(currentTime.toString());
                    }
                    slotsPerInterviewerList.set(i, slotsPerInterviewerList.get(i) - candidatesForThisTime);
                    candidatesAssigned += candidatesForThisTime;
                    currentTime = currentTime.plusMinutes(duration);
                }
            }
            for (CandidateDTO candidate : candidatesForThisDay) {
                String timeString = candidate.getTimeOfInterview();
                LocalTime localTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
                int hours = duration / 60;
                int minutes = duration % 60;
                LocalTime localTime2 = LocalTime.of(hours, minutes);
                LocalTime sum = localTime.plusHours(localTime2.getHour())
                        .plusMinutes(localTime2.getMinute())
                        .plusSeconds(localTime2.getSecond());
                if (!localTime.isBefore(endTimeOfInterview) || sum.isAfter(endTimeOfInterview) ) {
                    candidate.setInterview(null);
                    candidate.setTimeOfInterview(null);
                    candidatesWithNullTime++;
                    super.save(candidate);
                } else {
                    candidate.setInterview(interviewDTO1);
                    super.save(candidate);
                }
            }
        }
    }
    @Override
    public int SetIdInterview(CandidateDTO candidate) {
        return candidateRepository.SetIdInterview(Math.toIntExact(candidate.getCandidateId()), Math.toIntExact(candidate.getInterview().getInterviewId()));
   }
    @Override

    public int SetTimeInterview(int candidateId, String timeOfInterview) {
        return candidateRepository.SetTimeInterview(candidateId,timeOfInterview);
    }
    @Override
    public List<CandidateDTO> findByInterview(Long idInterview) {
        Interview interview = interviewRepository.findById(idInterview).get();
        return candidateTransformer.toDTOList(candidateRepository.findByInterview(interview));
    }
    @Override
    public CandidateDTO addInternshipCandidate(CandidateDTO candidateDTO) throws Exception {
        SessionDTO currentInternshipSession = sessionTransformer.toDTO(sessionRepository.findCurrentInternshipSession());
        List<CandidateDTO> candidates = findByEmail(candidateDTO.getEmail())
                .stream().filter(dto -> dto.getSession().equals(currentInternshipSession)).toList();
        if (candidates.size() > 0 && candidates.size() != candidates.stream().filter(CandidateDTO::isDeleted).toList().size()) {
            throw new Exception("Candidate with this email already exists !!");
        } else {
            candidateDTO.setSession(currentInternshipSession);
            candidateDTO.setType("internship");
            return super.save(candidateDTO);
        }
    }
}





