package com.nttdata.beca.repository;


import java.util.List;

import com.nttdata.beca.entity.Candidate;

import com.nttdata.beca.entity.Interview;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CandidateRepository extends CrudRepository<Candidate, Long> {

    List<Candidate> findByEmail(String email);
    @Query(value = "SELECT * from candidate c inner join session s on c.session_id = s.session_id WHERE preselection_status = true AND deleted = false AND s.type = 'recruitment' AND s.session_status = 'current' ORDER BY c.first_name", nativeQuery = true)
    List<Candidate> findByPreselectionStatus();

    @Query(value = "SELECT * from candidate c inner join session s on c.session_id = s.session_id WHERE preselection_status = true AND deleted = false AND c.type = 'internship' AND s.session_status = 'current' ORDER BY c.first_name", nativeQuery = true)
    List<Candidate> findAcceptedInternshipCandidate();

    @Query(value = "SELECT DISTINCT university from candidate", nativeQuery = true)
    List<String> findAllUniversities();

    @Query(value = "SELECT DISTINCT diplome from candidate", nativeQuery = true)
    List<String> findAllDiplomas();

    @Query(value = "SELECT DISTINCT city from candidate", nativeQuery = true)
    List<String> findAllCities();

    @Query(value = "SELECT * FROM candidate WHERE deleted = false AND session_id=?1 ", nativeQuery = true)
    List<Candidate> findBySession(Long sessionId);

    @Query(value = "SELECT * FROM candidate c inner join session s on c.session_id = s.session_id where deleted = false AND s.type = 'recruitment' AND  s.session_status = 'current' ORDER BY c.first_name", nativeQuery = true)
    List<Candidate> getCurrentSessionCandidates();

    @Query(value = "SELECT * FROM candidate c inner join session s on c.session_id = s.session_id where deleted = false AND s.type = 'internship' AND  s.session_status = 'current' ORDER BY c.first_name", nativeQuery = true)
    List<Candidate> getCurrentSessionInternshipCandidates();

    @Query(value = "select * from candidate c INNER JOIN session s on c.session_id = s.session_id INNER JOIN score sr on c.score_id = sr.score_id WHERE s.session_status LIKE 'current' AND sr.is_accepted = true ORDER BY sr.final_score DESC LIMIT ?1", nativeQuery = true)
    List<Candidate> getFinalList(int admittedNumber);

    @Query(value = "select * from candidate c INNER JOIN session s on c.session_id = s.session_id INNER JOIN score sr on c.score_id = sr.score_id WHERE s.session_status LIKE 'current' ORDER BY sr.final_score DESC LIMIT ?1,?2", nativeQuery = true)
    List<Candidate> getWaiting(int admittedNumber, int waitingNumber);

    @Query(value = "SELECT * FROM candidate c INNER JOIN session s ON c.session_id=s.session_id WHERE c.final_admission_status='true' AND s.session_status='current'", nativeQuery = true)
    List<Candidate> getAdmitted();

    @Query(value = "SELECT COUNT(*) FROM candidate c INNER JOIN session s ON c.session_id=s.session_id WHERE s.session_status='previous'", nativeQuery = true)
    Long getTotalNumber();

    @Query(value = "SELECT COUNT(*) FROM candidate c WHERE c.session_id=?1", nativeQuery = true)
    Long countInSession(Long sessionId);

    @Query(value = "SELECT COUNT(*) FROM candidate c INNER JOIN session s ON c.session_id=s.session_id WHERE s.session_status='current' AND c.preselection_status=true AND s.type = 'recruitment'", nativeQuery = true)
    Long preselectedCountCurrent();

    @Query(value = "SELECT COUNT(*) FROM candidate c INNER JOIN session s ON c.session_id=s.session_id WHERE s.session_status='previous' AND c.preselection_status='true'", nativeQuery = true)
    Long preselectedCount();

    @Query(value = "SELECT COUNT(*) FROM candidate c INNER JOIN session s ON c.session_id=s.session_id WHERE s.session_status='previous' AND c.final_admission_status='true'", nativeQuery = true)
    Long admittedCount();

    @Query(value = "SELECT COUNT(*) from candidate WHERE email=?1", nativeQuery = true)
    Long countEmail(String email);

    @Modifying
    @Transactional
    @Query(value ="UPDATE candidate c INNER JOIN score s INNER JOIN session se ON c.candidate_id = s.candidate_id AND c.session_id=se.session_id SET c.final_admission_status = 'true' WHERE se.session_status='current' ORDER BY s.final_score DESC LIMIT ?1", nativeQuery = true)
    int setAdmitted(int nbAdmitted);
    @Modifying
    @Transactional
    @Query(value="UPDATE candidate SET interview_id =?2 WHERE deleted = false and candidate_id=?1" , nativeQuery = true)
    int SetIdInterview(int candidate_id, int interview_id);
    @Modifying
    @Transactional
    @Query(value="UPDATE candidate SET time_of_interview =?2 WHERE deleted = false and candidate_id=?1 " , nativeQuery = true)
    int SetTimeInterview(int candidate_id,String timeOfInterview);
    List<Candidate> findByInterview(Interview interview);


    @Query(value = "SELECT COUNT(*) FROM candidate c INNER JOIN session s ON c.session_id=s.session_id INNER JOIN internship i ON c.candidate_id=i.candidate_id WHERE i.deleted=false and c.deleted=false and s.session_status='previous' and s.type='internship'", nativeQuery = true)
    Long countAdmittedPreviousInternshipSession();

    @Query(value = "SELECT COUNT(*) FROM candidate c INNER JOIN session s ON c.session_id=s.session_id WHERE s.session_status='previous' and s.type='recruitment'", nativeQuery = true)
    Long countAdmittedPreviousSession();
}

