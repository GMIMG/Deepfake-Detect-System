package com.gmimg.multicampus.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmimg.multicampus.springboot.mapper.IMemMapper;
import com.gmimg.multicampus.springboot.member.Member;

@Service
public class MemService implements IMemService {

	@Autowired
	IMemMapper mapper;
	
	//IMemService 인터페이스를 상속받아 IMemMapper.insertMem 으로 받아온 데이터 전달 
	@Override
	public void insertMem(Member member) throws Exception {
		mapper.insertMem(member);
	}
	//IMemService 인터페이스를 상속받아 IMemMapper.findMem 으로 받아온 데이터 전달 
	@Override
	public Member findMem(Member member) throws Exception {
		return mapper.findMem(member);
	}
	//IMemService 인터페이스를 상속받아 IMemMapper.idCheckMem 으로 받아온 데이터 전달 
	@Override
	public Member idCheckMem(Member member) throws Exception {
		return mapper.idCheckMem(member);
	}

}
