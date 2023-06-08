package com.nttdata.beca.service.impl;

import com.nttdata.beca.controller.FileController;
import com.nttdata.beca.dto.File;
import com.nttdata.beca.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {

    private final Path root = Paths.get("uploads");

    {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            // Folder already exist
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Files.copy(file.getInputStream(), this.root.resolve(fileName));
        return fileName;
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }


    public String findById(Long internshipId) {
        String file="";
        List<File> fileInfos = this.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();
            return new File(filename, url);
        }).collect(Collectors.toList());
        for(int i=0; i < fileInfos.size();i++){
            String[] parts = fileInfos.get(i).getFilename().split("[_\\.]");
            if(parts.length >= 3 && parts[2].equals(String.valueOf(internshipId))){
               file=fileInfos.get(i).getFilename();
               break;
            }
        }
        return file;
    }
}
