package com.yyw.yhyc.order.utils;

import com.yyw.yhyc.order.dto.UserDto;

public class ContextHolder {
	 private static ThreadLocal<UserDto> threadLocalUserDto = new ThreadLocal<UserDto>();  
	 
	 public static UserDto getUserDtoInfo(){
		 
		  return threadLocalUserDto.get();
	 }
	 
	 public static void setUserDto(UserDto userDto){
		 threadLocalUserDto.set(userDto);
	 }

}
