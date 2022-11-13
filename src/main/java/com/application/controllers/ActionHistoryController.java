package com.application.controllers;

import java.sql.Timestamp;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.ActionHistoryModel;
import com.application.entities.submittionforms.ActionForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.ActionHistoryRepository;
import com.application.repositories.ActionTypeRepository;

@Service
public class ActionHistoryController {

	@Autowired
	private ActionHistoryRepository actionHistoryRepository;
	@Autowired
	private ActionTypeRepository actionTypeRepository;

	private int defaultPageSize = 100;
	private int defauleMaxPageSize = 500;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// Browse Action History
	// NOTE : List by description.
	public Page<ActionHistoryModel> listHistory(int page, int pageSize, String searchContent) {
		Page<ActionHistoryModel> result;
		if (searchContent == "") {
			result = actionHistoryRepository.listHistory(getPageRequest(page, pageSize));
		} else {
			result = actionHistoryRepository.listHistoryByDescripton(getPageRequest(page, pageSize), searchContent);
		}
		if (result.getNumberOfElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] There are n record you are looking for.");
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// Browse Action History
	// NOTE : List by action type ID
	public Page<ActionHistoryModel> listHistoryByActionId(int page, int pageSize, int actionTypeId) {
		Page<ActionHistoryModel> result;
		if (actionTypeId != 0) {
			result = actionHistoryRepository.listHistory(getPageRequest(page, pageSize));
		} else {
			result = actionHistoryRepository.listHistoryByStatusId(getPageRequest(page, pageSize), actionTypeId);
		}
		if (result.getNumberOfElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] There are n record you are looking for.");
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// Browse Action History
	// NOTE : List by user ID
	public Page<ActionHistoryModel> listHistoryByUserId(int page, int pageSize, int userId) {
		Page<ActionHistoryModel> result;
		if (userId != 0) {
			result = actionHistoryRepository.listHistory(getPageRequest(page, pageSize));
		} else {
			result = actionHistoryRepository.listHistoryByUserId(getPageRequest(page, pageSize), userId);
		}
		if (result.getNumberOfElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] There are n record you are looking for.");
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// Save action history.
	public void addNewRecord(ActionForm form) {
		ActionHistoryModel record = new ActionHistoryModel();
		String currentdate = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();
		record.setAccount(form.getUser().getAccountId());
		record.setTarget(form.getTargetRef());

		record.setDescription(form.getDescription());
		record.setTimestamp(currentdate);
		record.setType(actionTypeRepository.findById(form.getActionTypeId()).orElseThrow(
				() -> new ExceptionFoundation(EXCEPTION_CODES.RECORD_INVALID_STATUS, HttpStatus.I_AM_A_TEAPOT,
						"[ RECORD_INVALID_STATUS ] Action type with this ID does not exist or invalid.")));
		actionHistoryRepository.save(record);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// Get pagerequest
	private Pageable getPageRequest(int page, int pageSize) {
		if (page < 0) {
			page = defaultPageSize;
		}
		if (pageSize > defauleMaxPageSize) {
			pageSize = defauleMaxPageSize;
		}
		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		return sendPageRequest;
	}

}
