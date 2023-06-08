package com.nttdata.beca.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {

    private String filename;
    private String url;

}
