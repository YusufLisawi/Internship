package com.nttdata.beca.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileService {

    Stream<Path> loadAll();
    Resource load(String filename);
    String save(MultipartFile file) throws IOException;
    void deleteAll();
    String findById(Long internshipId);

}
