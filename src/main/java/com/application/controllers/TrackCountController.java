package com.application.controllers;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.TracksModel;
import com.application.entities.copmskeys.TrackCountCompKey;
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
	private TrackCountRepository trackCountRepository;

	private static final long TIME_DIF_DAY = 86400000;
	private static final long TIME_DIF_WEEK = 86400000 * 7;
	private static final long TIME_DIF = 25200000;

	// !! TESTING FUNCTION !!
	// AddCuctomVIewCount
	public List<TrackCountModel> addCustomViewCount(int trackId, int numberOfWeek) {
		tracksRepository.findById(trackId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] Track ID " + trackId + " does not exist."));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Calendar calendarToday = Calendar.getInstance();
			System.out.println(calendarToday.getTime());

			Date today = sdf.parse(calendarToday.get(Calendar.YEAR) + "/" + (calendarToday.get(Calendar.MONTH) + 1)
					+ "/" + calendarToday.get(Calendar.DAY_OF_MONTH));

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("Today", calendarToday.toString());

			List<TrackCountModel> trackCountList = new ArrayList<>();

			for (int i = 0; i <= numberOfWeek; i++) {
				Random random = new Random();
				int randomizedNumberOfview = (int) random.nextInt(9950) + 10;
				int randomizedNumberOfFavorite = (int) random.nextInt(150) + 10;

				long currentMili = today.getTime() + (TIME_DIF_DAY * -i);
				Date currentDate = new Date(currentMili);
				resultMap.put("day" + i, currentDate.toString());
				Timestamp timeStamp = new Timestamp(currentMili);
				TrackCountModel newTrackCount = new TrackCountModel();
				newTrackCount.setFavouriteCount(randomizedNumberOfFavorite);
				newTrackCount.setViewCount(randomizedNumberOfview);
				newTrackCount.setId(new TrackCountCompKey(trackId, timeStamp.toString()));
				trackCountRepository.save(newTrackCount);
			}
			return trackCountList;
		} catch (ParseException e) {
			throw new ExceptionFoundation(EXCEPTION_CODES.DEAD, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ DEAD ] Error in function : addCustomViewCount " + e.getLocalizedMessage());
		}
	}

	// NO API
	// AddViewCountToTrack
	public void addViewCountToTrack(int trackId) {
		TrackCountCompKey id = new TrackCountCompKey(trackId, getTimestampToday().toString());
		if (trackCountRepository.existsById(id)) {
			TrackCountModel trackCount = trackCountRepository.findById(id)
					.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
							HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] No record found for this Id."));
			trackCountRepository.updateViewCount(trackCount.getViewCount() + 1, id);
		} else {
			createNewTrackCountRecordForThisWeek(trackId);
		}
	}

	// GetViewCountInPassWeeks
	public void GetViewCountInPassWeeks(int trackId, int numberOfDay) {
		long milisecondToday = getTimestampToday().getTime();
		long milisecondFrom = 0;
	}

	// GetViewCountInAllWeek
	public int getViewCountInAllWeek(int trackId) {
		if (!tracksRepository.existsById(trackId)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] The track with this ID does not exist.");
		} else {
			return trackCountRepository.getAllViewCountFromTrackId(trackId);
		}

	}

	// NO API
	// IncreaseFavouriteCount
	public void increaseFavouriteCount(int trackId) {

	}

	// NO API
	// DecreaseFavouriteCount
	public void decreaseFavouriteCount(int trackId) {

	}

	// getAllFavouriteCount
	public void getAllFavouriteCount(int trackId) {

	}

	// ---------------
	// PRIVATE or AUTOMATION
	// ---------------

	// PRIVATE
	// GetTimestampToday
	private Timestamp getTimestampToday() {
		long currentTimeMili = Calendar.getInstance().getTimeInMillis();
		long currentTimeRecord = currentTimeMili - (currentTimeMili % TIME_DIF_DAY) - TIME_DIF;
		Timestamp timeStamp = new Timestamp(currentTimeRecord);
		return timeStamp;
	}

	// PRIVATE
	// GetTimeStampFromMilisecond
	private Timestamp getTimeStampFromMilisecond(long miliSecond) {
		Timestamp timestamp = new Timestamp(miliSecond);
		return timestamp;
	}

	// PRIVATE
	// CreateNewTrackCountRecordForThisWeek
	private TrackCountModel createNewTrackCountRecordForThisWeek(int trackId) {
		long currentTimeMili = Calendar.getInstance().getTimeInMillis();
		long currentTimeRecord = currentTimeMili - (currentTimeMili % TIME_DIF_DAY) - TIME_DIF;
		Timestamp timeStamp = new Timestamp(currentTimeRecord);
		TrackCountModel newTrackCount = new TrackCountModel();

		System.out.println(timeStamp.toString());

		newTrackCount.setViewCount(0);
		newTrackCount.setFavouriteCount(0);
		newTrackCount.setId(new TrackCountCompKey(trackId, timeStamp.toString()));
		trackCountRepository.save(newTrackCount);
		return newTrackCount;
	}

}
