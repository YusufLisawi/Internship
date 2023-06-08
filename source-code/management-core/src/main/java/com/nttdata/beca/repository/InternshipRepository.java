package com.nttdata.beca.repository;

import com.nttdata.beca.dto.InternshipDTO;
import com.nttdata.beca.entity.Candidate;
import com.nttdata.beca.entity.Internship;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InternshipRepository extends CrudRepository<Internship,Long > {

    Internship findById(long id);

    @Query(value = "SELECT * FROM internship i inner join session s on i.session_id = s.session_id inner join candidate c on i.candidate_id = c.candidate_id where i.deleted = false AND c.deleted = false AND s.type = 'internship' AND s.session_status = 'current' ORDER BY i.subject ", nativeQuery = true)
    List<Internship> getCurrentSessionInternships();

    @Query(value = "SELECT * FROM internship i inner join session s on i.session_id = s.session_id inner join candidate c on i.candidate_id = c.candidate_id where i.deleted = false AND c.deleted = false AND s.session_id=?1 ", nativeQuery = true)
    List<Internship> getInternshipsBySessionId(Long sessionId);
}
