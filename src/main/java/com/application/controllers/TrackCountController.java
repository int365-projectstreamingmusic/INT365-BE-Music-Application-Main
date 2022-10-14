package com.application.controllers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.copmskeys.TrackCountCompKey;
import com.application.entities.models.TrackCountModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.TracksRepository;
import com.application.repositories.TrackCountStatisticRepository;

@Service
public class TrackCountController {

	@Autowired
	private TracksRepository tracksRepository;
	@Autowired
	private TrackCountStatisticRepository trackCountRepository;

	private static final long TIME_DIF_DAY = 86400000;
	private static final long TIME_DIF = 25200000;

	// GetViewCountInPassDays
	public int getViewCountInPassDays(int trackId, int numberOfDay) {
		String timesTampTo = getTimestampToday().toString();
		String timesTampFrom = getTimeStampFromMilisecond(getTimestampToday().getTime() - (numberOfDay * TIME_DIF_DAY))
				.toString();
		return trackCountRepository.getAllViewCountFromTrackIdBetween(trackId, timesTampFrom, timesTampTo);
	}

	// GetViewCount
	public int getViewCount(int trackId) {
		if (!tracksRepository.existsById(trackId)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] The track with this ID does not exist.");
		} else {
			return trackCountRepository.getAllViewCountFromTrackId(trackId);
		}

	}

	// getAllFavouriteCount
	public int getAllFavouriteCount(int trackId) {
		if (!tracksRepository.existsById(trackId)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] The track with this ID does not exist.");
		} else {
			return trackCountRepository.getAllViewCountFromTrackId(trackId);
		}
	}

	// --------------------------------
	// Adding and deleting view count or favorite count
	// --------------------------------

	// NO API
	// increateViewCount
	public void increateViewCount(int trackId) {
		TrackCountCompKey id = new TrackCountCompKey(trackId, getTimestampToday().toString());
		TrackCountModel trackCount = trackCountRepository.findById(id).orElse(null);
		if (trackCount == null) {
			trackCount = createNewTrackCountRecordForToday(trackId);
		}
		trackCountRepository.updateViewCount(trackCount.getViewCount() + 1, id);
	}

	// NO API
	// IncreaseFavouriteCount
	public void increaseFavouriteCount(int trackId) {
		TrackCountCompKey id = new TrackCountCompKey(trackId, getTimestampToday().toString());
		TrackCountModel trackCount = trackCountRepository.findById(id).orElse(null);
		if (trackCount == null) {
			trackCount = createNewTrackCountRecordForToday(trackId);
		}
		trackCountRepository.updateFavoriteCount(trackCount.getFavoriteCount() + 1, id);
	}

	// NO API
	// DecreaseFavouriteCount
	public void decreaseFavouriteCount(int trackId) {
		TrackCountCompKey id = new TrackCountCompKey(trackId, getTimestampToday().toString());
		TrackCountModel trackCount = trackCountRepository.findById(id).orElse(null);
		if (trackCount == null) {
			createNewTrackCountRecordForToday(trackId);
		}
		trackCountRepository.updateFavoriteCount(trackCount.getFavoriteCount() - 1, id);
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
	// CreateNewTrackCountRecordForToday
	private TrackCountModel createNewTrackCountRecordForToday(int trackId) {
		long currentTimeMili = Calendar.getInstance().getTimeInMillis();
		long currentTimeRecord = currentTimeMili - (currentTimeMili % TIME_DIF_DAY) - TIME_DIF;
		Timestamp timeStamp = new Timestamp(currentTimeRecord);
		TrackCountModel newTrackCount = new TrackCountModel();

		System.out.println(timeStamp.toString());

		newTrackCount.setViewCount(0);
		newTrackCount.setFavoriteCount(0);
		newTrackCount.setId(new TrackCountCompKey(trackId, timeStamp.toString()));
		trackCountRepository.save(newTrackCount);
		return newTrackCount;
	}

	// ---------------
	// !! TESTING FUNCTION !!
	// ---------------

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
				newTrackCount.setFavoriteCount(randomizedNumberOfFavorite);
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

}
