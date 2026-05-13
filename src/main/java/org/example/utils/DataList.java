package org.example.utils;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataList<T>{
    private T data;
    private Long allElements;
    private int totalPages;
}
