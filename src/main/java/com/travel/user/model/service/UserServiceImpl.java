package com.travel.user.model.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.travel.user.model.UserDto;
import com.travel.user.model.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {
	
	private UserMapper userMapper;

	public UserServiceImpl(UserMapper userMapper) {
		super();
		this.userMapper = userMapper;
	}

	@Override
	public UserDto login(UserDto userDto) throws Exception {
		return userMapper.login(userDto);
	}
	
	@Override
	public UserDto userInfo(String loginId) throws Exception {
		return userMapper.userInfo(loginId);
	}

	@Override
	public void saveRefreshToken(String loginId, String refreshToken) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginId", loginId);
		map.put("token", refreshToken);
		userMapper.saveRefreshToken(map);
	}

	@Override
	public Object getRefreshToken(String loginId) throws Exception {
		return userMapper.getRefreshToken(loginId);
	}

	@Override
	public void deleRefreshToken(String loginId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginId", loginId);
		map.put("token", null);
		userMapper.deleteRefreshToken(map);
	}
	
	@Override
	public int join(UserDto userDto) throws Exception {
		return userMapper.join(userDto);
	}
	
	@Override
	public int update(UserDto userDto) throws Exception {
		return userMapper.update(userDto);
	}
	
	@Override
	public int findPw(UserDto userDto) throws Exception {
		int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();
	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	                                   .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	                                   .limit(targetStringLength)
	                                   .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	                                   .toString();
	    System.out.println(generatedString);
	    userDto.setPassword(generatedString);
		return userMapper.findPw(userDto);
	}
	@Override
	public int delete(String loginId) throws Exception {
		return userMapper.delete(loginId);
	}
}
