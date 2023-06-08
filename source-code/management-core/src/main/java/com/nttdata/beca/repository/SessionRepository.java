package com.nttdata.beca.repository;

import com.nttdata.beca.entity.Session;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
    @Query(value = "SELECT * from session where type='recruitment' and session_status=?1",nativeQuery = true)
    Session findBySessionStatus(String sessionStatus);
    Session findBySessionName(String sessionName);
    Session findBySessionNameAndType(String sessionName,String type);
    Session findBySessionId(Long sessionId);

    @Query(value = "select admitted_number from session WHERE type='recruitment' and session_status LIKE 'current'", nativeQuery = true)
    int getAdmittedNumber();

    @Query(value = "select eliminating_mark from session WHERE type='recruitment' and session_status LIKE 'current'", nativeQuery = true)
    int getEliminatingMark();

    @Query(value = "select start_time_of_interview from session WHERE type='recruitment' and session_status LIKE 'current'", nativeQuery = true)
    String getstartTimeOfInterview();

    @Query(value = "select end_time_of_interview from session WHERE type='recruitment' and session_status LIKE 'current'", nativeQuery = true)
    String getendTimeOfInterview();

    @Query(value = "select day_interview_number from session WHERE type='recruitment' and session_status LIKE 'current'", nativeQuery = true)
    int getDayInterviewNumber();

    @Query(value = "select test_duration from session WHERE type='recruitment' and session_status LIKE 'current'", nativeQuery = true)
    int gettestDuration();

    @Query(value = "select english_level_require from session WHERE type='recruitment' and session_status LIKE 'current'", nativeQuery = true)
    String getEliminatingEnglishLevel();

    @Query(value = "SELECT DISTINCT technology from session", nativeQuery = true)
    List<String> findAllTechnologies();

    @Query(value = "SELECT session_id FROM Session s WHERE s.session_status='current' and s.type='recruitment'", nativeQuery = true)
    Long getLatestSessionId();

    @Modifying
    @Transactional
    @Query(value ="UPDATE session SET session_status='closed' WHERE type='recruitment' and session_status= 'previous'", nativeQuery = true)
    int toClosed();

    @Modifying
    @Transactional
    @Query(value ="UPDATE session SET session_status='previous' WHERE type='recruitment' and session_status= 'current'", nativeQuery = true)
    int toPrevious();

    @Query(value = "SELECT * from session where type='internship' and session_status='current'",nativeQuery = true)
    Session findCurrentInternshipSession();

    @Query(value = "SELECT * from session where type='internship' and session_status=?1",nativeQuery = true)
    Session findInternshipSessionStatus(String sessionStatus);

    @Query(value = "SELECT * from session where type='internship'",nativeQuery = true)
    List<Session> findInternshipSessionAll();

    @Query(value = "SELECT * from session where type='recruitment'",nativeQuery = true)
    List<Session> findAll();

    @Modifying
    @Transactional
    @Query(value ="UPDATE session SET session_status='previous' WHERE type='internship' and session_status= 'current'", nativeQuery = true)
    int toPreviousInternship();

    @Modifying
    @Transactional()
    @Query(value ="UPDATE session SET session_status='closed' WHERE type='internship' and session_status= 'previous'", nativeQuery = true)
    int toClosedInternship();

    @Query(value = "SELECT session_id FROM Session s WHERE s.session_status='current' and s.type='internship' ", nativeQuery = true)
    Long getLatestInternshipSessionId();

}
