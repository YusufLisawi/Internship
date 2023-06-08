package com.nttdata.beca.transformer;

import com.nttdata.beca.dto.CandidateDTO;
import com.nttdata.beca.dto.DocsDTO;
import com.nttdata.beca.dto.InternshipDTO;
import com.nttdata.beca.entity.Candidate;
import com.nttdata.beca.entity.Docs;
import com.nttdata.beca.entity.Internship;

public class DocsTransformer extends Transformer<Docs, DocsDTO> {

    Transformer<Internship, InternshipDTO> internshipTransformer = new InternshipTransformer();

    @Override
    public Docs toEntity(DocsDTO dto) {
        if (dto == null)
            return null;
        else {
            Docs entity = new Docs();
            entity.setId(dto.getDocsId());
            entity.setType(dto.getType());
            entity.setPath(dto.getPath());
            entity.setInternship(internshipTransformer.toEntity(dto.getInternship()));
            return entity;
        }
    }

    @Override
    public DocsDTO toDTO(Docs entity) {
        if (entity == null)
            return null;
        else {
            DocsDTO dto = new DocsDTO();
            dto.setDocsId(entity.getId());
            dto.setType(entity.getType());
            dto.setPath(entity.getPath());
            dto.setInternship(internshipTransformer.toDTO(entity.getInternship()));
            return dto;
        }
    }
}
