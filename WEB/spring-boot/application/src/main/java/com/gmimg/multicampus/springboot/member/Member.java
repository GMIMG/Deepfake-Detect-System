package com.gmimg.multicampus.springboot.member;

public class Member {
	private String memId;
	private String memPw;
	private String memVideo;
	
	public String getMemVideo() {
		return memVideo;
	}
	public void setMemVideo(String memVideo) {
		this.memVideo = memVideo;
	}
	public String getMemId() {
		return memId;
	}
	public void setMemId(String memId) {
		this.memId = memId;
	}
	public String getMemPw() {
		return memPw;
	}
	public void setMemPw(String memPw) {
		this.memPw = memPw;
	}
	
}
