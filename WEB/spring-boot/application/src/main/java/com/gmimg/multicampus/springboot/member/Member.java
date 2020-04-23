package com.gmimg.multicampus.springboot.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Member {
	private int memIdx;
	private String memId;
	private String memPw;
}
