package com.application.knitting.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Photo {
    private String name;
    private String url;
}
