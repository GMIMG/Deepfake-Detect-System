package com.gmimg.multicampus.springboot.dao;

import com.gmimg.multicampus.springboot.member.Member;

public interface IMemDao {
	
	int memberInsert(Member member);
	Member memberSelect(Member member);
}
