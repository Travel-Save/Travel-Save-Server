package com.travel.attraction.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "AttractionFavoriteDto : 관광지 좋아요 정보", description = "관광지 좋아요 정보를 나타낸다.")
public class AttractionDto {
	@Schema(description = "회원아이디")
	private int userId;
	@Schema(description = "콘텐츠아이디")
	private int contentid;
	@Schema(description = "콘텐츠타입아이디")
	private String contenttypeid;
	@Schema(description = "관광지명")
	private String title;
	@Schema(description = "관광지대표이미지")
	private String firstimage;
	@Schema(description = "경도")
	private String mapx;
	@Schema(description = "위도")
	private String mapy;
	@Schema(description = "주소")
	private String addr1;
	@Schema(description = "생성일시")
	private String regDate;
	@Schema(description = "게시글 좋아요수")
	private int likeCount; 
	@Schema(description = "상태")
	private int status;
}
