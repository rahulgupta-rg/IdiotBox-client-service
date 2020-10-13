package com.idiotBox.pojos;

import java.util.LinkedList;
import java.util.List;

public class CommentList {
	List<Comment> ls;

	public CommentList() {
		ls = new LinkedList<>();
	}
	
	public CommentList(List<Comment> ls) {
		super();
		this.ls = ls;
	}
	
	public List<Comment> getLs() {
		return ls;
	}

	public void setLs(List<Comment> ls) {
		this.ls = ls;
	}

	@Override
	public String toString() {
		return "CommentList [ls=" + ls + "]";
	}

}
