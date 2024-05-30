package com.travel.board.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "BoardDto : 게시글정보", description = "게시글의 상세 정보를 나타낸다.")
public class BoardDto {
	
	@Schema(description = "게시글 아이디")
	private int id; 
	@Schema(description = "작성자 아이디")
	private int userId; 
	@Schema(description = "작성자 이름")
	private String userName; 
	@Schema(description = "게시글 유형 (C: 일반, N: 공지)")
	private String type; 
	@Schema(description = "게시글 제목")
	private String title; 
	@Schema(description = "게시글 내용")
	private String content; 
	@Schema(description = "추가내용")
	private String content2; 
	@Schema(description = "썸네일")
	private String thumbnail;
	@Schema(description = "게시글 조회수")
	private int viewCount; 
	@Schema(description = "게시글 좋아요수")
	private int likeCount; 
	@Schema(description = "등록일시")
	private String regDate; 
	@Schema(description = "수정일시")
	private String modDate; 
	@Schema(description = "상태 (1: 정상)")
	private int status; 

}
