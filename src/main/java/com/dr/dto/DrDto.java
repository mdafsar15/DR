package com.dr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrDto {

	private Object dataRecord;

	private DrChannel channel;

	private String blockChain;
}
