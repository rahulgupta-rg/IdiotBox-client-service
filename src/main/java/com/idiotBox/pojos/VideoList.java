package com.idiotBox.pojos;

import java.util.LinkedList;
import java.util.List;

public class VideoList {
	List<Video> ls;

	public VideoList() {
		ls = new LinkedList<>();
	}
	
	public VideoList(List<Video> ls) {
		super();
		this.ls = ls;
	}

	public List<Video> getLs() {
		return ls;
	}

	public void setLs(List<Video> ls) {
		this.ls = ls;
	}
}