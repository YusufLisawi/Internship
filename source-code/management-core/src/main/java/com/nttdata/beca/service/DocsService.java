package com.nttdata.beca.service;

import com.nttdata.beca.dto.DocsDTO;

import java.util.List;

public interface DocsService extends GenericService<DocsDTO, Long> {

    List<DocsDTO> findAll();

    List<DocsDTO> findByInternshipId(Long candidateId);

    void deleteDocsById(Long id);

    DocsDTO findDocsById(Long id);

    DocsDTO saveDocs(DocsDTO docsDTO);

    void deleteAllDocsByInternshipId(Long candidateId);

    DocsDTO updateDocs(DocsDTO docDTO);

}
