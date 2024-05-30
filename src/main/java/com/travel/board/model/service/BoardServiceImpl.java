package com.travel.board.model.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.travel.board.model.BoardThumbnailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.board.model.BoardDto;
import com.travel.board.model.BoardFavoriteDto;
import com.travel.board.model.BoardListDto;
import com.travel.board.model.FileInfoDto;
import com.travel.board.model.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

	private BoardMapper boardMapper;

	@Autowired
	public BoardServiceImpl(BoardMapper boardMapper) {
		super();
		this.boardMapper = boardMapper;
	}

	@Override
	@Transactional
	public void writeArticle(BoardDto boardDto) throws Exception {
		boardMapper.writeArticle(boardDto);
//		List<FileInfoDto> fileInfos = boardDto.getFileInfos();
//		if (fileInfos != null && !fileInfos.isEmpty()) {
//			boardMapper.registerFile(boardDto);
//		}
	}

	@Override
	public BoardListDto listArticle(Map<String, String> map) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("word", map.get("word") == null ? "" : map.get("word"));
		int currentPage = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
		int sizePerPage = Integer.parseInt(map.get("spp") == null ? "20" : map.get("spp"));
		int start = currentPage * sizePerPage - sizePerPage;
		param.put("type", map.get("type"));
		param.put("start", start);
		param.put("listsize", sizePerPage);

		String key = map.get("key");
		param.put("key", key == null ? "" : key);
		if ("u.login_id".equals(key))
			param.put("key", key == null ? "" : "u.login_id");
		List<BoardDto> list = boardMapper.listArticle(param);

		if ("u.login_id".equals(key))
			param.put("key", key == null ? "" : "u.login_id");
		int totalArticleCount = boardMapper.getTotalArticleCount(param);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		BoardListDto boardListDto = new BoardListDto();
		boardListDto.setArticles(list);
		boardListDto.setCurrentPage(currentPage);
		boardListDto.setTotalPageCount(totalPageCount);

		return boardListDto;
	}


	@Override
	public BoardDto getArticle(int articleNo) throws Exception {
		return boardMapper.getArticle(articleNo);
	}

	@Override
	public void updateHit(int articleNo) throws Exception {
		boardMapper.updateHit(articleNo);
	}

	@Override
	public void modifyArticle(BoardDto boardDto) throws Exception {
		// TODO : BoardDaoImpl의 modifyArticle 호출
		boardMapper.modifyArticle(boardDto);
	}

//	@Override
//	@Transactional
//	public void deleteArticle(int articleNo, String path) throws Exception {
//		// TODO : BoardDaoImpl의 deleteArticle 호출
//		List<FileInfoDto> fileList = boardMapper.fileInfoList(articleNo);
//		boardMapper.deleteFile(articleNo);
//		boardMapper.deleteArticle(articleNo);
//		for(FileInfoDto fileInfoDto : fileList) {
//			File file = new File(path + File.separator + fileInfoDto.getSaveFolder() + File.separator + fileInfoDto.getSaveFile());
//			file.delete();
//		}
//	}

	@Override
	public void deleteArticle(int articleNo) throws Exception {
		// TODO : BoardDaoImpl의 deleteArticle 호출
		boardMapper.deleteArticle(articleNo);
	}

	@Override
	public void uploadFile(List<FileInfoDto> files) throws Exception {
		boardMapper.uploadFile(files);		
	}
	
	@Override
	public List<FileInfoDto> getFiles(int articleNo) throws Exception {
		return boardMapper.getFiles(articleNo);
	}
	
	@Override
	public void insertFavorite(Map<String, Object> param) throws Exception {
		boardMapper.insertFavorite(param);
	}
	
	@Override
	public BoardFavoriteDto getFavorite(Map<String, Object> param) throws Exception {
		return boardMapper.getFavorite(param);
	}
	
	@Override
	public void deleteFavorite(Map<String, Object> param) throws Exception {
		boardMapper.deleteFavorite(param);
	}
	
	@Override
	public BoardListDto listLikeArticle(Map<String, String> map) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("word", map.get("word") == null ? "" : map.get("word"));
		int currentPage = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
		int sizePerPage = Integer.parseInt(map.get("spp") == null ? "20" : map.get("spp"));
		int start = currentPage * sizePerPage - sizePerPage;
		param.put("type", map.get("type"));
		param.put("start", start);
		param.put("listsize", sizePerPage);
		param.put("userId", map.get("userId"));

		String key = map.get("key");
		param.put("key", key == null ? "" : key);
		if ("u.login_id".equals(key))
			param.put("key", key == null ? "" : "u.login_id");
		List<BoardDto> list = boardMapper.listLikeArticle(param);

		if ("u.login_id".equals(key))
			param.put("key", key == null ? "" : "u.login_id");
		int totalArticleCount = boardMapper.getTotalArticleCount(param);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		BoardListDto boardListDto = new BoardListDto();
		boardListDto.setArticles(list);
		boardListDto.setCurrentPage(currentPage);
		boardListDto.setTotalPageCount(totalPageCount);
		
		return boardListDto;
	}

	@Override
	public BoardThumbnailDto getThumbnail(Map<String, Object> param) throws Exception {
		BoardThumbnailDto thumbnailDto = boardMapper.getThumbnail(param);
		if (thumbnailDto == null) return null;
		return BoardThumbnailDto.builder()
				.saveFile(thumbnailDto.getSaveFile())
				.saveFolder(thumbnailDto.getSaveFolder())
				.build();
	}
}
