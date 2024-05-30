package com.travel.user.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.travel.board.model.FileInfoDto;
import com.travel.user.model.UserDto;
import com.travel.user.model.service.UserService;
import com.travel.util.JWTUtil;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Tag(name = "회원 인증 컨트롤러", description = "로그인 로그아웃, 토큰처리등 회원의 인증관련 처리하는 클래스.")
@Slf4j
public class UserController {
	
	private final UserService userService;
	private final JWTUtil jwtUtil;
	
	@Value("${file.upload-dir}")
    private String uploadDir;

	public UserController(UserService userService, JWTUtil jwtUtil) {
		super();
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	@Operation(summary = "로그인", description = "아이디와 비밀번호를 이용하여 로그인 처리.")
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(
			@RequestBody @Parameter(description = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) UserDto userDto) {
		log.debug("login user : {}", userDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			UserDto loginUser = userService.login(userDto);
			if(loginUser != null) {
				String accessToken = jwtUtil.createAccessToken(loginUser.getLoginId());
				String refreshToken = jwtUtil.createRefreshToken(loginUser.getLoginId());
				log.debug("access token : {}", accessToken);
				log.debug("refresh token : {}", refreshToken);
				
//				발급받은 refresh token 을 DB에 저장.
				userService.saveRefreshToken(loginUser.getLoginId(), refreshToken);
				
//				JSON 으로 token 전달.
				resultMap.put("access-token", accessToken);
				resultMap.put("refresh-token", refreshToken);
				
				status = HttpStatus.CREATED;
			} else {
				resultMap.put("message", "아이디 또는 패스워드를 확인해 주세요.");
				status = HttpStatus.UNAUTHORIZED;
			} 
			
		} catch (Exception e) {
			log.debug("로그인 에러 발생 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@Operation(summary = "회원가입", description = "회원정보를 전달받아 회원가입.")
	@PostMapping("/join")
	public ResponseEntity<Map<String, Object>> join(
			@RequestBody @Parameter(description = "회원가입 시 필요한 회원정보(로그인아이디, 비밀번호, 이름, 전화번호).", required = true) UserDto userDto) {
		log.debug("join user : {}", userDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			int successCnt = userService.join(userDto);
			
			if (successCnt > 0) {
				resultMap.put("message", "회원가입 성공");
				status = HttpStatus.OK;
			} else {
				resultMap.put("message", "아이디 또는 패스워드를 확인해 주세요.");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			} 
			
		} catch (Exception e) {
			log.debug("회원가입 실패 : {}", e);
			resultMap.put("message", "회원가입 실패");
			status = HttpStatus.BAD_REQUEST;
		}
		log.debug("resultmap :  {} ", resultMap);

		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@Operation(summary = "회원수정", description = "변경된 회원정보를 전달받아 회원정보 수정.")
	@PutMapping
	public ResponseEntity<Map<String, Object>> update(
			@ModelAttribute  @Parameter(description = "변경된 회원정보(로그인아이디 제외, 비밀번호, 이름, 전화번호).", required = true) UserDto userDto, 
			@RequestParam(value = "profileImg", required = false) MultipartFile profileImg) {
		log.debug("update user : {}", userDto);
		log.debug("update user profile : {}", profileImg);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		try {
			if (profileImg != null && !profileImg.isEmpty()) {
				String realPath = uploadDir;
				String saveFolder = realPath + File.separator + "profile";
				log.debug("저장 폴더 : {}", saveFolder);
				File folder = new File(saveFolder);
				if (!folder.exists())
					folder.mkdirs();
				
				String originalFileName = profileImg.getOriginalFilename();
				if (!originalFileName.isEmpty()) {
					String saveFileName = UUID.randomUUID().toString()
							+ originalFileName.substring(originalFileName.lastIndexOf('.'));
					log.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", profileImg.getOriginalFilename(), saveFileName);
					profileImg.transferTo(new File(folder, saveFileName));
					userDto.setProfile("profile" + File.separator + saveFileName);
				}
			}
			try {
				int successCnt = userService.update(userDto);
				
				if (successCnt > 0) {
					resultMap.put("message", "회원정보 수정 성공");
					status = HttpStatus.OK;
				} else {
					resultMap.put("message", "회원정보 수정 실패");
					status = HttpStatus.BAD_REQUEST;
				} 
				
			} catch (Exception e) {
				log.debug("회원정보 수정 실패 : {}", e);
				resultMap.put("message", "회원정보 수정 실패");
				status = HttpStatus.BAD_REQUEST;
			}
		} catch (Exception e) {
			log.debug("회원정보 프로필 등록 실패 : {}", e);
			resultMap.put("message", "회원 프로필 사진 등록 실패");
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@Operation(summary = "비밀번호 찾기", description = "로그인 아이디와 전화번호를 받아 비밀번호 갱신")
	@PostMapping("/find/password")
	public ResponseEntity<Map<String, Object>> findPw(
			@RequestBody @Parameter(description = "회원정보(로그인아이디, 전화번호).", required = true) UserDto userDto) {
		log.debug("findPw user : {}", userDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			int successCnt = userService.findPw(userDto);
			
			if (successCnt > 0) {
				resultMap.put("message", "비밀번호 갱신 성공");
				
				UserDto updateUserDto = userService.userInfo(userDto.getLoginId()); 
				if (updateUserDto != null) {
					resultMap.put("password", updateUserDto.getPassword());
					log.debug("findPw user update password : {}", userDto.getPassword());
					status = HttpStatus.OK;
				} else {
					resultMap.put("message", "비밀번호 갱신 실패");
					status = HttpStatus.INTERNAL_SERVER_ERROR;
				}
			} else {
				resultMap.put("message", "비밀번호 갱신 실패");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			} 
			
		} catch (Exception e) {
			log.debug("비밀번호 갱신 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
//	
//	@Operation(summary = "회원정보 얻기", description = "로그인한 회원 정보를 반환한다.")
//	@GetMapping("/{loginId}")
//	public ResponseEntity<Map<String, Object>> getUserInfo(
//			@PathVariable("loginId") @Parameter(description = "로그인한 회원의 아이디.", required = true) String loginId,
//			HttpServletRequest request) {
//		log.debug("delete User : {} ", loginId);
//		Map<String, Object> resultMap = new HashMap<>();
//		HttpStatus status = HttpStatus.ACCEPTED;
//		if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
//			log.info("사용 가능한 토큰!!!");
//			try {
////				로그인 사용자 정보.
//				UserDto userDto = userService.userInfo(loginId);
//				resultMap.put("userInfo", userDto);
//				status = HttpStatus.OK;
//			} catch (Exception e) {
//				log.error("정보조회 실패 : {}", e);
//				resultMap.put("message", e.getMessage());
//				status = HttpStatus.INTERNAL_SERVER_ERROR;
//			}
//		} else {
//			log.error("사용 불가능 토큰!!!");
//			status = HttpStatus.UNAUTHORIZED;
//		}
//		return new ResponseEntity<Map<String, Object>>(resultMap, status);
//	}
	
	@Operation(summary = "회원인증", description = "회원 정보를 담은 Token 을 반환한다.")
	@GetMapping("/info/{loginId}")
	public ResponseEntity<Map<String, Object>> getInfo(
			@PathVariable("loginId") @Parameter(description = "인증할 회원의 아이디.", required = true) String loginId,
			HttpServletRequest request) {
//		logger.debug("userId : {} ", userId);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
			log.info("사용 가능한 토큰!!!");
			try {
//				로그인 사용자 정보.
				UserDto userDto = userService.userInfo(loginId);
				resultMap.put("userInfo", userDto);
				status = HttpStatus.OK;
			} catch (Exception e) {
				log.error("정보조회 실패 : {}", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			log.error("사용 불가능 토큰!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@Operation(summary = "회원탈퇴", description = "회원을 탈퇴한다.")
	@DeleteMapping("/{loginId}")
	public ResponseEntity<Map<String, Object>> delete(
			@PathVariable("loginId") @Parameter(description = "삭제할 회원의 아이디.", required = true) String loginId,
			HttpServletRequest request) {
		log.debug("delete User : {} ", loginId);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			int successCnt = userService.delete(loginId);
			
			if (successCnt > 0) {
				resultMap.put("message", "회원탈퇴 성공");
				status = HttpStatus.OK;
			} else {
				resultMap.put("message", "회원탈퇴 실패");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			} 
			
		} catch (Exception e) {
			log.debug("회원탈퇴 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}


	@Operation(summary = "로그아웃", description = "회원 정보를 담은 Token 을 제거한다.")
	@GetMapping("/logout/{loginId}")
	public ResponseEntity<?> removeToken(@PathVariable ("loginId") @Parameter(description = "로그아웃 할 회원의 아이디.", required = true) String loginId) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			userService.deleRefreshToken(loginId);
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.error("로그아웃 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@Operation(summary = "Access Token 재발급", description = "만료된 access token 을 재발급 받는다.")
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody UserDto userDto, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refreshToken");
		log.debug("token : {}, memberDto : {}", token, userDto);
		if (jwtUtil.checkToken(token)) {
			if (token.equals(userService.getRefreshToken(userDto.getLoginId()))) {
				String accessToken = jwtUtil.createAccessToken(userDto.getLoginId());
				log.debug("token : {}", accessToken);
				log.debug("정상적으로 access token 재발급!!!");
				resultMap.put("access-token", accessToken);
				status = HttpStatus.CREATED;
			}
		} else {
			log.debug("refresh token 도 사용 불가!!!!!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
