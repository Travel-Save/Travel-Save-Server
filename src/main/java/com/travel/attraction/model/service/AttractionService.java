package com.travel.attraction.model.service;

import java.util.List;
import java.util.Map;

import com.travel.attraction.model.AttractionDto;
import com.travel.attraction.model.AttractionFavoriteDto;
import com.travel.attraction.model.AttractionListDto;

public interface AttractionService {

	void insertFavorite(Map<String, Object> param) throws Exception;
	AttractionFavoriteDto getFavorite(Map<String, Object> param) throws Exception;
	void deleteFavorite(Map<String, Object> param) throws Exception;
	AttractionListDto listLikeAttraction(Map<String, String> param) throws Exception;
}
