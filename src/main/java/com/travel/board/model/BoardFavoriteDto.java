package com.travel.board.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "BoardFavoriteDto : 게시글 좋아요 정보", description = "게시글의 좋아요 정보를 나타낸다.")
public class BoardFavoriteDto {
	
	@Schema(description = "게시글 좋아요여부")
	private int isLike; 
	@Schema(description = "게시글 좋아요 수")
	private int likeCount; 
}
