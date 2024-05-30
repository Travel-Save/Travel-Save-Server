package com.travel.user.model.mapper;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.travel.user.model.UserDto;

@Mapper
public interface UserMapper {

	UserDto login(UserDto userDto) throws SQLException;
	UserDto userInfo(String loginId) throws SQLException;
	void saveRefreshToken(Map<String, String> map) throws SQLException;
	Object getRefreshToken(String loginId) throws SQLException;
	void deleteRefreshToken(Map<String, String> map) throws SQLException;
	int join(UserDto userDto) throws SQLException;
	int update(UserDto userDto) throws SQLException;
	int findPw(UserDto userDto) throws SQLException;
	int delete(String loginId) throws SQLException;
}
