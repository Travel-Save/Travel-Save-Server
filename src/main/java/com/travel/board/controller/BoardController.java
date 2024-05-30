package com.travel.board.controller;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.travel.board.model.BoardThumbnailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.travel.board.model.BoardDto;
import com.travel.board.model.BoardFavoriteDto;
import com.travel.board.model.BoardListDto;
import com.travel.board.model.FileInfoDto;
import com.travel.board.model.service.BoardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "*" }, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.POST} , maxAge = 6000)
@RestController
@RequestMapping("/board")
@Tag(name = "게시판 컨트롤러", description = "게시판에 글을 등록, 수정, 삭제, 목록, 상세보기등 전반적인 처리를 하는 클래스.")
@Slf4j
public class BoardController {
	
	@Value("${file.upload-dir}")
    private String uploadDir;
    
	private BoardService boardService;

	public BoardController(BoardService boardService) {
		super();
		this.boardService = boardService;
	}

	@Operation(summary = "게시판 글작성", description = "새로운 게시글 정보를 입력한다.")
	@PostMapping
	public ResponseEntity<?> writeArticle(
			@RequestBody @Parameter(description = "작성글 정보.", required = true) BoardDto boardDto) {
		log.info("writeArticle boardDto - {}", boardDto);
		try {
			boardService.writeArticle(boardDto);
//			return ResponseEntity.ok().build();
			return new ResponseEntity<Integer>(boardDto.getId(), HttpStatus.CREATED);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@Operation(summary = "게시판 글목록", description = "모든 게시글의 정보를 반환한다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "게시글목록 OK!!"),
			@ApiResponse(responseCode = "404", description = "페이지없어!!"),
			@ApiResponse(responseCode = "500", description = "서버에러!!") })
	@GetMapping
	public ResponseEntity<?> listArticle(
			@RequestParam @Parameter(description = "게시글을 얻기위한 부가정보.", required = true) Map<String, String> map) {
		log.info("listArticle map - {}", map);
		try {
			BoardListDto boardListDto = boardService.listArticle(map);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(boardListDto);
		} catch (Exception e) {
			return exceptionHandling(e); 
		}
	}

	@Operation(summary = "게시판 글보기", description = "글번호에 해당하는 게시글의 정보를 반환한다.")
	@GetMapping("/{articleno}")
	public ResponseEntity<BoardDto> getArticle(
			@PathVariable("articleno") @Parameter(name = "articleno", description = "얻어올 글의 글번호.", required = true) int articleno)
			throws Exception {
		log.info("getArticle - 호출 : " + articleno);
		boardService.updateHit(articleno);
		return new ResponseEntity<BoardDto>(boardService.getArticle(articleno), HttpStatus.OK);
	}
	

	@Operation(summary = "수정 할 글 얻기", description = "글번호에 해당하는 게시글의 정보를 반환한다.")
	@GetMapping("/modify/{articleno}")
	public ResponseEntity<BoardDto> getModifyArticle(
			@PathVariable("articleno") @Parameter(name = "articleno", description = "얻어올 글의 글번호.", required = true) int articleno)
			throws Exception {
		log.info("getModifyArticle - 호출 : " + articleno);
		return new ResponseEntity<BoardDto>(boardService.getArticle(articleno), HttpStatus.OK);
	}

	@Operation(summary = "게시판 글수정", description = "수정할 게시글 정보를 입력한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.")
	@PutMapping
	public ResponseEntity<String> modifyArticle(
			@RequestBody @Parameter(description = "수정할 글정보.", required = true) BoardDto boardDto) throws Exception {
		log.info("modifyArticle - 호출 {}", boardDto);

		boardService.modifyArticle(boardDto);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "게시판 글삭제", description = "글번호에 해당하는 게시글의 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.")
	@DeleteMapping("/{articleno}")
	public ResponseEntity<String> deleteArticle(@PathVariable("articleno") @Parameter(name = "articleno", description = "살제할 글의 글번호.", required = true) int articleno) throws Exception {
		log.info("deleteArticle - 호출");
		boardService.deleteArticle(articleno);
		return ResponseEntity.ok().build();

	}
	
	@Operation(summary = "게시글 파일 업로드", description = "파일을 업로드한다.")
	@PostMapping("/upload")
	public ResponseEntity<?> uploadFiles(@RequestParam("id") String id, 
			@RequestParam("files") MultipartFile[] files) {
		log.info("MultipartFile.isEmpty : {}", files[0].isEmpty());
		try {
			if (!files[0].isEmpty()) {
				String realPath = uploadDir;
//				String realPath = servletContext.getRealPath("/resources/img");
				String today = new SimpleDateFormat("yyMMdd").format(new Date());
				String saveFolder = realPath + File.separator + today;
				log.debug("저장 폴더 : {}", saveFolder);
				File folder = new File(saveFolder);
				if (!folder.exists())
					folder.mkdirs();
				List<FileInfoDto> fileInfos = new ArrayList<FileInfoDto>();
				for (MultipartFile mfile : files) {
					FileInfoDto fileInfoDto = new FileInfoDto();
					String originalFileName = mfile.getOriginalFilename();
					if (!originalFileName.isEmpty()) {
						String saveFileName = UUID.randomUUID().toString()
								+ originalFileName.substring(originalFileName.lastIndexOf('.'));
						fileInfoDto.setBoardId(Integer.parseInt(id));
						fileInfoDto.setSaveFolder(today);
						fileInfoDto.setOriginalFile(originalFileName);
						fileInfoDto.setSaveFile(saveFileName);
						log.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", mfile.getOriginalFilename(), saveFileName);
						mfile.transferTo(new File(folder, saveFileName));
					}
					fileInfos.add(fileInfoDto);
				}
//				boardDto.setFileInfos(fileInfos);
				boardService.uploadFile(fileInfos);
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return exceptionHandling(e);
		}
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	

	@Operation(summary = "게시판 글 파일목록", description = "글번호에 해당하는 게시글 파일목록 정보를 반환한다.")
	@GetMapping("/{articleno}/files")
	public ResponseEntity<List<FileInfoDto>> getFiles(
			@PathVariable("articleno") @Parameter(name = "articleno", description = "얻어올 글의 글번호.", required = true) int articleno)
			throws Exception {
		log.info("getFiles - 호출 : " + articleno);
		
		return new ResponseEntity<List<FileInfoDto>>(boardService.getFiles(articleno), HttpStatus.OK);
	}
	
	@Operation(summary = "게시판 파일 다운로드", description = "요청한 파일을 다운로드한다.")
	@PostMapping("/download")
	public ModelAndView downloadFile(
			@RequestBody @Parameter(description = "다운로드할 파일 정보.", required = true) FileInfoDto fileInfoDto)
			throws Exception {
		log.info("downloadFile - 호출 : { }" + fileInfoDto);
		Map<String, Object> fileInfo = new HashMap<String, Object>();
		fileInfo.put("path", uploadDir);
		fileInfo.put("sfolder", fileInfoDto.getSaveFolder());
		fileInfo.put("ofile", fileInfoDto.getOriginalFile());
		fileInfo.put("sfile", fileInfoDto.getSaveFile());
		return new ModelAndView("fileDownLoadView", "downloadFile", fileInfo);
	}
	
	@Operation(summary = "좋아요 추가하기", description = "게시물을 좋아요 정보에 추가한다.")
	@PostMapping("/favorite")
	public ResponseEntity<?> insertFavorite(
			@RequestBody Map<String, Object> favoriteInfo) {
		log.info("insertFavorite favoriteInfo - {}", favoriteInfo);
		try {
			boardService.insertFavorite(favoriteInfo);
			return new ResponseEntity<Integer>(HttpStatus.CREATED);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@Operation(summary = "좋아요 정보 가져오기", description = "게시물의 좋아요 정보를 가져온다.")
	@GetMapping("/favorite")
	public ResponseEntity<?> getFavorite(
			@RequestParam @Parameter Map<String, Object> favoriteInfo) {
		log.info("getFavorite favoriteInfo - {}", favoriteInfo);
		try {
			
			log.info("getFavorite query - {}", boardService.getFavorite(favoriteInfo));
			return new ResponseEntity<BoardFavoriteDto>(boardService.getFavorite(favoriteInfo), HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@Operation(summary = "좋아요 추가하기", description = "게시물을 좋아요 정보에 추가한다.")
	@DeleteMapping("/favorite")
	public ResponseEntity<?> deleteFavorite(
			@RequestParam @Parameter Map<String, Object> favoriteInfo) {
		log.info("deleteFavorite favoriteInfo - {}", favoriteInfo);
		try {
			boardService.deleteFavorite(favoriteInfo);
			return new ResponseEntity<Integer>(HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	@Operation(summary = "썸네일 가져오기", description = "핫 플레이스 게시물의 저장된 썸네일을 가져온다.")
	@GetMapping("/thumbnail")
	private ResponseEntity<String> getThumbnail(@RequestParam @Parameter Map<String, Object> thumbnailInfo) {
		log.info("getThumbnail thumbnailInfo - {}", thumbnailInfo);
		try {
			String realPath = "";
			BoardThumbnailDto thumbnailDto = boardService.getThumbnail(thumbnailInfo);
			if (thumbnailDto != null) realPath = thumbnailDto.getSaveFolder() + "/" + thumbnailDto.getSaveFile();
			return new ResponseEntity<String>(realPath,HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@Operation(summary = "좋아요 목록 가져오기", description = "좋아요한 게시물을 가져온다.")
	@GetMapping("/favorite/list")
	private ResponseEntity<?> listLikeArticle(@RequestParam @Parameter Map<String, String> map) {
		log.info("listLikeArticle param - {}", map);
		try {
			BoardListDto boardListDto = boardService.listLikeArticle(map);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(boardListDto);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}


}