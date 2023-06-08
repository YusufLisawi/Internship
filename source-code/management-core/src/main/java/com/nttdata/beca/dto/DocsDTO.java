package com.nttdata.beca.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class DocsDTO {

    private Long docsId;
    private String type;
    private String path;
    private InternshipDTO internship;
}

