package com.nttdata.beca.service;


public interface GenericService<DTO, Key> {

    long count();

    void delete(DTO dto);

    void deleteAll(Iterable<DTO> dtoList);

    void deleteAll();

    void deleteById(Key id);

    boolean existsById(Key id);

    Iterable<DTO> findAll();

    DTO findById(Key id);
    //DTO findByIds(Key id);

    Iterable<DTO> findAllById(Iterable<Key> id);
    /*
      Optional<DTO> findAllById(Key id);
     */

    DTO save(DTO dto);

    Iterable<DTO> saveAll(Iterable<DTO> dtoList);
}
