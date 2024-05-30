package com.travel.attraction.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.travel.attraction.model.AttractionDto;
import com.travel.attraction.model.AttractionFavoriteDto;
import com.travel.board.model.BoardDto;
import com.travel.board.model.BoardFavoriteDto;

@Mapper
public interface AttractionMapper {

	void insertFavorite(Map<String, Object> param) throws SQLException;
	AttractionFavoriteDto getFavorite(Map<String, Object> param) throws SQLException;
	void deleteFavorite(Map<String, Object> param) throws SQLException;
	List<AttractionDto> listLikeAttraction(Map<String, Object> param) throws SQLException;
	int getTotalAttractionCount(Map<String, Object> param) throws SQLException;
	void insertAttraction(Map<String, Object> param) throws SQLException;
	AttractionDto selectOne(Map<String, Object> param) throws SQLException;
}
