package com.gmimg.multicampus.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gmimg.multicampus.springboot.member.Member;

@Mapper
public interface IMemMapper {
	//resource/Mappers/memberMapper.xml 내의 insertMem 쿼리 실행
	public void insertMem(Member member) throws Exception;
	//resource/Mappers/memberMapper.xml 내의 findMem 쿼리 실행
	public Member findMem(Member member) throws Exception;
	//resource/Mappers/memberMapper.xml 내의 idCheckMem 쿼리 실행
	public Member idCheckMem(Member member) throws Exception;

}
