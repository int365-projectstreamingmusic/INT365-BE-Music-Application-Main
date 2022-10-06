package com.application.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.TracksModel;
import com.application.entities.models.TrackCountModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.TracksRepository;
import com.application.repositories.TrackCountRepository;

@Service
public class TrackCountController {

	@Autowired
	private TracksRepository tracksRepository;
	@Autowired
	private TrackCountRepository viewCountRepository;

	private static final int TIME_DIF_DAY = 86400000;
	private static final int TIME_DIF_WEEK = 86400000 * 7;

	// !! TESTING FUNCTION !!
	// AddCuctomVIewCount
	public Map<String, Object> addCustomViewCount(int trackId, int numberOfWeek) {
		TracksModel targetTrack = tracksRepository.findById(trackId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] Track ID " + trackId + " does not exist."));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
		try {
			Calendar calendarToday = Calendar.getInstance();

			Date anotherToday = new Date();
			Date today = sdf.parse(calendarToday.get(Calendar.YEAR) + "/" + calendarToday.get(Calendar.MONTH) + "/"
					+ calendarToday.get(Calendar.DAY_OF_MONTH));
			Date day1 = sdf.parse("2022/10/05");
			Date day2 = sdf.parse("2022/10/06");
			Date day3 = sdf.parse("2022/10/07");

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("AnotherToday", anotherToday.toString());
			resultMap.put("todayST", today.toString());
			resultMap.put("today", today.getTime());
			resultMap.put("day1", day1.getTime());
			resultMap.put("day2", day2.getTime());
			resultMap.put("day3", day3.getTime());
			// resultMap.put("today", formatted.);
			resultMap.put("timeDif1", day2.getTime() - day1.getTime());
			resultMap.put("timeDif2", day3.getTime() - day2.getTime());

			return resultMap;

		} catch (ParseException e) {
			throw new ExceptionFoundation(EXCEPTION_CODES.DEAD, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ DEAD ] Error in function : addCustomViewCount " + e.getLocalizedMessage());
		}

	}

	// AddViewToTrack
	public void addViewToTrack(int trackId) {

	}

	// CreateNewViewCountRecordForThisWeek
	public void CreateNewViewCountRecordForThisWeek(int trackId) {

	}

	// GetViewCountInPassWeeks
	public void GetViewCountInPassWeeks(int trackId) {

	}

	// GetViewCountInAllWeek
	public void getViewCountInAllWeek(int trackId) {

	}

	// IncreaseFavouriteCount
	public void increaseFavouriteCount(int trackId) {

	}

	// DecreaseFavouriteCount
	public void decreaseFavouriteCount(int trackId) {

	}

	// getAllFavouriteCount
	public void getAllFavouriteCount(int trackId) {

	}
}
