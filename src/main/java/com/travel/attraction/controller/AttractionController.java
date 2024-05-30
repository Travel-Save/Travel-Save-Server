package com.travel.attraction.controller;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travel.attraction.model.AttractionDto;
import com.travel.attraction.model.AttractionFavoriteDto;
import com.travel.attraction.model.AttractionListDto;
import com.travel.attraction.model.service.AttractionService;
import com.travel.board.model.BoardFavoriteDto;
import com.travel.board.model.BoardListDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/attraction")
@Tag(name = "관광지 컨트롤러", description = "관광지 정보를 처리하는 클래스.")
@Slf4j
public class AttractionController {
	
	private AttractionService attractionService;

	public AttractionController(AttractionService attractionService) {
		super();
		this.attractionService = attractionService;
	}
	
	@Operation(summary = "좋아요 목록 가져오기", description = "좋아요한 게시물을 가져온다.")
	@GetMapping("/favorite/list")
	private ResponseEntity<?> listLikeArticle(@RequestParam @Parameter Map<String, String> map) {
		log.info("listLikeAttraction param - {}", map);
		try {
			AttractionListDto attractionListDto = attractionService.listLikeAttraction(map);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(attractionListDto);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@Operation(summary = "좋아요 추가하기", description = "게시물을 좋아요 정보에 추가한다.")
	@PostMapping("/favorite")
	public ResponseEntity<?> insertFavorite(
			@RequestBody Map<String, Object> favoriteInfo) {
		log.info("insertFavorite favoriteInfo - {}", favoriteInfo);
		try {
			attractionService.insertFavorite(favoriteInfo);
			return new ResponseEntity<Integer>(HttpStatus.CREATED);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@GetMapping("/favorite")
	public ResponseEntity<?> getFavorite(@RequestParam Map<String, Object> favoriteInfo) {
	    log.info("getFavorite favoriteInfo - {}", favoriteInfo);
	    try {
	        AttractionFavoriteDto attractionFavoriteDto = attractionService.getFavorite(favoriteInfo);
	        log.info("getFavorite query - {}", attractionFavoriteDto);
	        return new ResponseEntity<AttractionFavoriteDto>(attractionFavoriteDto, HttpStatus.OK);
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
			attractionService.deleteFavorite(favoriteInfo);
			return new ResponseEntity<Integer>(HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
