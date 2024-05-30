package com.travel.user.model.service;

import com.travel.user.model.UserDto;

public interface UserService {

	UserDto login(UserDto userDto) throws Exception;
	UserDto userInfo(String loginId) throws Exception;
	void saveRefreshToken(String loginId, String refreshToken) throws Exception;
	Object getRefreshToken(String loginId) throws Exception;
	void deleRefreshToken(String loginId) throws Exception;
	int join(UserDto userDto) throws Exception;
	int update(UserDto userDto) throws Exception;
	int findPw(UserDto userDto) throws Exception;
	int delete(String loginId) throws Exception;
}
