package com.travel.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "MemberDto : 회원정보", description = "회원의 상세 정보를 나타낸다.")
public class UserDto {

	@Schema(description = "아이디")
	private int id;
	@Schema(description = "로그인아이디")
	private String loginId;
	@Schema(description = "비밀번호")
	private String password;
	@Schema(description = "이름")
	private String name;
	@Schema(description = "회원유형")
	private String type;
	@Schema(description = "전화번호")
	private String phone;
	@Schema(description = "이메일")
	private String email;
	@Schema(description = "우편번호")
	private String zipcode;
	@Schema(description = "주소")
	private String addr;
	@Schema(description = "상세주소")
	private String addr2;
	@Schema(description = "개인정보동의여부")
	private String agreeYn;
	@Schema(description = "등록일시")
	private String regDate;
	@Schema(description = "수정일시")
	private String modDate;
	@Schema(description = "상태")
	private int status;
	@Schema(description = "프로필사진")
	private String profile;
	
	@Schema(description = "refreshToken")
	private String refreshToken;
	
}
