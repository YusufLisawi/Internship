package com.nttdata.beca.service.impl;

import java.util.Optional;

import com.nttdata.beca.service.GenericService;
import com.nttdata.beca.transformer.Transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

public class GenericServiceImpl<Entity, DTO, Key> implements GenericService<DTO, Key> {

    @Autowired
    private CrudRepository<Entity, Key> crudRepository;

    private Transformer<Entity, DTO> trans;

    public GenericServiceImpl(Transformer<Entity, DTO> trans) {
        this.trans = trans;
    }

    @Override
    public long count() {
        return crudRepository.count();
    }

    @Override
    public void delete(DTO dto) {
        crudRepository.delete(trans.toEntity(dto));
    }

    @Override
    public void deleteAll(Iterable<DTO> dtoList) {
        crudRepository.deleteAll(trans.toEntityList(dtoList));
    }

    @Override
    public void deleteAll() {
        crudRepository.deleteAll();
    }

    @Override
    public void deleteById(Key id) {
        crudRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Key id) {
        return crudRepository.existsById(id);
    }

    @Override
    public Iterable<DTO> findAll() {
        return trans.toDTOList(crudRepository.findAll());
    }

    @Override
    public Iterable<DTO> findAllById(Iterable<Key> id) {
        return trans.toDTOList(crudRepository.findAllById(id));
    }

    @Override
    public DTO save(DTO dto) {
        return trans.toDTO(crudRepository.save(trans.toEntity(dto)));
    }

    @Override
    public Iterable<DTO> saveAll(Iterable<DTO> dtoList) {
        return trans.toDTOList(crudRepository.saveAll(trans.toEntityList(dtoList)));
    }

    @Override
    public DTO findById(Key id) {
        return trans.toDTO(crudRepository.findById(id).get());
    }

}
