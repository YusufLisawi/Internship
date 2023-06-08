package com.nttdata.beca.repository;



import com.nttdata.beca.dto.CandidateDTO;
import com.nttdata.beca.entity.Candidate;
import com.nttdata.beca.entity.Interview;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface InterviewRepository extends CrudRepository<Interview, Long> {




    Iterable<Interview> findByDate(Date date);

    List<Interview> findBySession_SessionStatus(String sessionStatus);

    Interview findInterviewByInterviewId(Long id);
    @Query(value = "SELECT * FROM interview i inner join session s on i.session_id = s.session_id where deleted = false AND  s.session_status = 'current' ORDER BY i.date", nativeQuery = true)
    List<Interview> findInterviewByCurrentSession();

}
