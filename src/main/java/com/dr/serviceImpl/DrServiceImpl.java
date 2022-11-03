package com.dr.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dr.dto.Constants;
import com.dr.dto.DrChannel;
import com.dr.dto.DrDto;
import com.dr.model.DrBlkTransLog;
import com.dr.model.DrModel;
import com.dr.repository.DrRepository;
import com.dr.service.DrService;
import com.dr.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DrServiceImpl implements DrService {

	private final Logger logger = LoggerFactory.getLogger(DrServiceImpl.class);

	@Autowired
	DrRepository drRepo;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public boolean DrInsertBirth(DrModel drModel) throws Exception {

		String jsonPayload = JsonUtil.getJsonString(drModel);

		String status = drModel.getStatus();

		DrBlkTransLog blk = new DrBlkTransLog();

		if (Constants.RECORD_STATUS_PENDING.equalsIgnoreCase(status)) {
			blk.setStatus(Constants.RECORD_STATUS_PENDING);
			blk.setData(jsonPayload);
			blk.setBlcFunction(Constants.REGISTER_BIRTH_BLC_FUNC);
//			blk.setChainCode(blockchainContractName);
			blk.setRecordType(Constants.RECORD_TYPE_BIRTH);
			blk.setBndId(drModel.getBirthId().toString());

			drRepo.save(blk);
		}

		return true;
	}

	@Override
	public Object DrUpdatePendingToSubmmitInBlockchain() throws Exception {

		String data;
		ObjectMapper objectMapper = new ObjectMapper();

		DrBlkTransLog dr = drRepo.findSingleRecord(Constants.RECORD_STATUS_PENDING);

		DrDto drto = new DrDto();

		DrChannel drchannel = new DrChannel();

		if (dr != null) {

			data = dr.getData().toString().replace(":\"\"", ": null");

			Object ob = objectMapper.readValue(data, Map.class);

			drto.setDataRecord(ob);

			drchannel.setName(Constants.BLC_CHANNEL);
			drchannel.setSmartContract(Constants.BLC_SMART_CONTRACT);
			drchannel.setClientUser(Constants.BLC_CLIENT_USER);
			drchannel.setContractMethod(dr.getBlcFunction().toString());
			drchannel.setConnectionProfile(Constants.BLC_CONNECTION_PROFILE);
			drchannel.setWalletPath(Constants.BLC_WALLET_PATH);

			drto.setChannel(drchannel);

			drto.setBlockChain(Constants.BLC_HYPERLEDGERFABRIC);

			ResponseEntity<Object> obj = callBlcForReplication(drto.getDataRecord());

			if (obj != null) {

				drRepo.updateSingleRecord(Constants.RECORD_SUBMITTED, LocalDateTime.now(), dr.getLogId());
			}

		}

		return drto;
	}

	private ResponseEntity<Object> callBlcForReplication(Object object) {
		logger.info("INSIDE BLC FUNCTION REQUEST ==" + object);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> httpEntity = new HttpEntity<>(object, headers);
		ResponseEntity<Object> response = restTemplate().exchange(Constants.REGISTER_BLOCKCHAIN_DR_URL, HttpMethod.POST,
				httpEntity, Object.class);
		logger.info("Remote Transactions: " + response.getStatusCode() + " Body: " + response.getBody());

		return response;
	}

	@Override
	public List<Object> insertDrDbToBlockchain() throws Exception {

		List<DrBlkTransLog> all = drRepo.findByStatus(Constants.RECORD_STATUS_PENDING);

		List<Object> single = new ArrayList<Object>();

		for (DrBlkTransLog dr : all) {

			logger.info("recieve data " + dr);

			single.add(dr);
		}

		return single;
	}

}
