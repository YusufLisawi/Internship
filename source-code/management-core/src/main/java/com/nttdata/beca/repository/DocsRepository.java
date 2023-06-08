package com.nttdata.beca.repository;

import com.nttdata.beca.entity.Docs;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface DocsRepository extends CrudRepository<Docs, Long> {

    Docs findDocsById(Long id);

    List<Docs> findByInternship_InternshipId(Long internshipId);

    void deleteAllByInternship_InternshipId(Long internshipId);

}
