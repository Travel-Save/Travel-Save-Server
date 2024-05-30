package com.travel.board.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class BoardThumbnailDto {
    private String saveFile;
    private String saveFolder;
}
