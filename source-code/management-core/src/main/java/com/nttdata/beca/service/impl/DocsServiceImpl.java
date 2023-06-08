package com.nttdata.beca.service.impl;

import com.nttdata.beca.dto.DocsDTO;
import com.nttdata.beca.entity.Docs;
import com.nttdata.beca.repository.DocsRepository;
import com.nttdata.beca.service.DocsService;
import com.nttdata.beca.transformer.DocsTransformer;
import com.nttdata.beca.transformer.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocsServiceImpl extends GenericServiceImpl<Docs, DocsDTO, Long>
        implements DocsService {

    public static Transformer<Docs, DocsDTO> docsTransformer = new DocsTransformer();

    @Autowired
    private DocsRepository docsRepository;

    public DocsServiceImpl() {
        super(docsTransformer);
    }

    @Override
    public DocsDTO saveDocs(DocsDTO docsDTO) {
        return super.save(docsDTO);
    }

    @Override
    public List<DocsDTO> findAll() {
        return docsTransformer.toDTOList(docsRepository.findAll());
    }

    @Override
    public List<DocsDTO> findByInternshipId(Long candidateId) {
        return docsTransformer.toDTOList(docsRepository.findByInternship_InternshipId(candidateId));
    }

    @Override
    public void deleteDocsById(Long id) {
        docsRepository.deleteById(id);
    }

    @Override
    public void deleteAllDocsByInternshipId(Long candidateId) {
        docsRepository.deleteAllByInternship_InternshipId(candidateId);
    }

    @Override
    public DocsDTO updateDocs(DocsDTO doc) {

        DocsDTO docsDTO = findById(doc.getDocsId());
        docsDTO.setType(doc.getType());
        docsDTO.setPath(doc.getPath());
        docsDTO.setInternship(doc.getInternship());
        DocsDTO dtoSaved = save(docsDTO);

        return dtoSaved;
    }

    @Override
    public DocsDTO findDocsById(Long id) {
        return docsTransformer.toDTO(docsRepository.findDocsById(id));
    }

}
