package com.travel.board.model.service;

import java.util.List;
import java.util.Map;

import com.travel.board.model.BoardDto;
import com.travel.board.model.BoardFavoriteDto;
import com.travel.board.model.BoardListDto;
import com.travel.board.model.BoardThumbnailDto;
import com.travel.board.model.FileInfoDto;

public interface BoardService {

	void writeArticle(BoardDto boardDto) throws Exception;
	BoardListDto listArticle(Map<String, String> map) throws Exception;
//	PageNavigation makePageNavigation(Map<String, String> map) throws Exception;
	BoardDto getArticle(int articleNo) throws Exception;
	void updateHit(int articleNo) throws Exception;
	
	void modifyArticle(BoardDto boardDto) throws Exception;
//	
	void deleteArticle(int articleNo) throws Exception;
	void uploadFile(List<FileInfoDto> files) throws Exception;
	List<FileInfoDto> getFiles(int articleNo) throws Exception;
	
	void insertFavorite(Map<String, Object> param) throws Exception;
	BoardFavoriteDto getFavorite(Map<String, Object> param) throws Exception;
	void deleteFavorite(Map<String, Object> param) throws Exception;
	BoardListDto listLikeArticle(Map<String, String> map) throws Exception;
	
	BoardThumbnailDto getThumbnail(Map<String, Object> param) throws Exception;
}
