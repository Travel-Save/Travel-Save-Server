package com.travel.board.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.travel.board.model.BoardThumbnailDto;
import org.apache.ibatis.annotations.Mapper;

import com.travel.board.model.BoardDto;
import com.travel.board.model.BoardFavoriteDto;
import com.travel.board.model.FileInfoDto;

@Mapper
public interface BoardMapper {

	void writeArticle(BoardDto boardDto) throws SQLException;

	void registerFile(BoardDto boardDto) throws Exception;

	List<BoardDto> listArticle(Map<String, Object> param) throws SQLException;

	int getTotalArticleCount(Map<String, Object> param) throws SQLException;

	BoardDto getArticle(int articleNo) throws SQLException;

	void updateHit(int articleNo) throws SQLException;

	void modifyArticle(BoardDto boardDto) throws SQLException;

	void deleteFile(int articleNo) throws Exception;

	void deleteArticle(int articleNo) throws SQLException;

	List<FileInfoDto> getFiles(int articleNo) throws SQLException;

	void uploadFile(List<FileInfoDto> files) throws SQLException;
	
	void insertFavorite(Map<String, Object> param) throws SQLException;
	
	BoardFavoriteDto getFavorite(Map<String, Object> param) throws SQLException;
	
	void deleteFavorite(Map<String, Object> param) throws SQLException;

	List<BoardDto> listLikeArticle(Map<String, Object> param) throws SQLException;
	
	BoardThumbnailDto getThumbnail(Map<String, Object> param) throws SQLException;
}
