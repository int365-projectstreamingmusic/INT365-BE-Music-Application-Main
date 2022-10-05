package com.application.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
			Date day1 = sdf.parse("2022/10/05");
			Date day2 = sdf.parse("2022/10/06");
			Date day3 = sdf.parse("2022/10/07");
			
		/*	DateFormat df = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
			Date today = Calendar.getInstance().getTime();
			Date todayDate = sdf.parse("" + Calendar.getInstance().getTime().getYear() + "/" + today.get);*/
	
			
			
			
			
		/*	Date currentDate = Calendar.getInstance().getTime();
			System.out.println("" + "");
			
			DateFormat df = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
			String formatted = df.format(currentDate);*/

			//System.out.println("|"+currentDate.toString().substring(0, 10)+" 00:00:00");
			//Date today = sdf.parse(currentDate.toString().substring(0, 10) + " 00:00:00" );
			

			/*for (int i = 1; i <= numberOfWeek; i++) {
				Date 
				
				TrackCountModel newViewCount = new TrackCountModel();

			}*/

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("day1", day1.getTime());
			resultMap.put("day2", day2.getTime());
			resultMap.put("day3", day3.getTime());
			//resultMap.put("today", formatted.);
			resultMap.put("timeDif1", day2.getTime() - day1.getTime());
			resultMap.put("timeDif2", day3.getTime() - day2.getTime());

			return resultMap;

		} catch (ParseException e) {
			throw new ExceptionFoundation(EXCEPTION_CODES.DEAD, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ DEAD ] Error in function : addCustomViewCount");
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
