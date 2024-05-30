package com.travel.attraction.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.travel.attraction.model.AttractionDto;
import com.travel.attraction.model.AttractionFavoriteDto;
import com.travel.attraction.model.AttractionListDto;
import com.travel.attraction.model.mapper.AttractionMapper;
import com.travel.board.model.BoardListDto;

@Service
public class AttractionServiceImpl implements AttractionService {
	
	private AttractionMapper attractionMapper;

	public AttractionServiceImpl(AttractionMapper attractionMapper) {
		super();
		this.attractionMapper = attractionMapper;
	}

	@Override
	public void insertFavorite(Map<String, Object> param) throws Exception {
		AttractionDto select = attractionMapper.selectOne(param);
		if (select == null) {
			attractionMapper.insertAttraction(param);
		}
		attractionMapper.insertFavorite(param);
	}
	
	@Override
	public AttractionFavoriteDto getFavorite(Map<String, Object> param) throws Exception {
		return attractionMapper.getFavorite(param);
	}
	
	@Override
	public void deleteFavorite(Map<String, Object> param) throws Exception {
		attractionMapper.deleteFavorite(param);		
	}
	
	@Override
	public AttractionListDto listLikeAttraction(Map<String, String> map) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("word", map.get("word") == null ? "" : map.get("word"));
		int currentPage = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
		int sizePerPage = Integer.parseInt(map.get("spp") == null ? "20" : map.get("spp"));
		int start = currentPage * sizePerPage - sizePerPage;
		param.put("userId", map.get("userId"));
		param.put("sort", map.get("sort"));
		param.put("start", start);
		param.put("listsize", sizePerPage);

		String key = map.get("key");
		param.put("key", key == null ? "" : key);
		if ("u.login_id".equals(key))
			param.put("key", key == null ? "" : "u.login_id");
		
		List<AttractionDto> list = attractionMapper.listLikeAttraction(param);
		System.out.println(list);
		if ("u.login_id".equals(key))
			param.put("key", key == null ? "" : "u.login_id");
		int totalArticleCount = attractionMapper.getTotalAttractionCount(param);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;
		
		AttractionListDto attractionListDto = new AttractionListDto();
		attractionListDto.setAttractions(list);
		attractionListDto.setCurrentPage(currentPage);
		attractionListDto.setTotalPageCount(totalPageCount);
		return attractionListDto;
	}

}
