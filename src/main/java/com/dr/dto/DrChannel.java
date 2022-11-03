package com.dr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrChannel {

	private String name;
	private String smartContract;
	private String clientUser;
	private String contractMethod;
	private String connectionProfile;
	private String walletPath;

}
