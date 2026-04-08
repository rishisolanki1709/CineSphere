package com.cinesphere.main.dto;

public class StatusCountDTO {
	private String status;

	public StatusCountDTO(String status, Long count) {
		super();
		this.status = status;
		this.count = count;
	}

	private Long count;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}