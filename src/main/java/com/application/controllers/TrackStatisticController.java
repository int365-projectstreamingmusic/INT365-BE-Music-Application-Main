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
import com.application.entities.models.TrackStatisticModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.TracksRepository;
import com.application.repositories.TrackCountStatisticRepository;

@Service
public class TrackStatisticController {

	@Autowired
	private TracksRepository tracksRepository;
	@Autowired
	private TrackCountStatisticRepository trackCountRepository;

	public final long TIME_DIF_DAY = 86400000;
	public final long TIME_DIF = 25200000;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Get view count of a track in the pass X days.
	public int getViewCountInPassDays(int trackId, int numberOfDay) {
		String timesTampTo = getTimestampToday().toString();
		String timesTampFrom = getTimeStampFromMilisecond(getTimestampToday().getTime() - (numberOfDay * TIME_DIF_DAY))
				.toString();
		return trackCountRepository.getAllViewCountFromTrackIdBetween(trackId, timesTampFrom, timesTampTo);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Get total view count. ( Should use track repository, it is better. )
	public int getViewCount(int trackId) {
		if (!tracksRepository.existsById(trackId)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] The track with this ID does not exist.");
		} else {
			return trackCountRepository.getAllViewCountFromTrackId(trackId);
		}

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Get favorite count. ( Also should use track repository, it is better. )
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

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// NO API
	// Increast view count by 1
	public void increateViewCount(int trackId) {
		TrackCountCompKey id = new TrackCountCompKey(trackId, getTimestampToday().toString());
		TrackStatisticModel trackCount = trackCountRepository.findById(id).orElse(null);
		if (trackCount == null) {
			trackCount = createNewTrackCountRecordForToday(trackId);
		}
		trackCountRepository.updateViewCount(id);
		tracksRepository.increaseViewCount(trackId);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// NO API
	// Increast favorite count by 1
	public void increaseFavouriteCount(int trackId) {
		TrackCountCompKey id = new TrackCountCompKey(trackId, getTimestampToday().toString());
		TrackStatisticModel trackCount = trackCountRepository.findById(id).orElse(null);
		if (trackCount == null) {
			trackCount = createNewTrackCountRecordForToday(trackId);
		}
		trackCountRepository.updateFavoriteCount(id);
		tracksRepository.increaseFavoriteCount(trackId);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// NO API
	// Deduct favorite count by 1
	public void decreaseFavouriteCount(int trackId) {
		TrackCountCompKey id = new TrackCountCompKey(trackId, getTimestampToday().toString());
		TrackStatisticModel trackCount = trackCountRepository.findById(id).orElse(null);
		if (trackCount == null) {
			createNewTrackCountRecordForToday(trackId);
		}
		trackCountRepository.updateFavoriteCount(trackCount.getFavoriteCount() - 1, id);
		tracksRepository.decreaseFavoriteCount(trackId);
	}

	// ---------------
	// PRIVATE or AUTOMATION
	// ---------------

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PRIVATE
	// Get time stamp for today. Will set hour to zero.
	public Timestamp getTimestampToday() {
		long currentTimeMili = Calendar.getInstance().getTimeInMillis();
		long currentTimeRecord = currentTimeMili - (currentTimeMili % TIME_DIF_DAY) - TIME_DIF;
		Timestamp timeStamp = new Timestamp(currentTimeRecord);
		return timeStamp;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PRIVATE
	// GetTimeStampFromMilisecond
	public Timestamp getTimeStampFromMilisecond(long miliSecond) {
		Timestamp timestamp = new Timestamp(miliSecond);
		return timestamp;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PRIVATE
	// Create new record for statistic if none exist.
	private TrackStatisticModel createNewTrackCountRecordForToday(int trackId) {
		long currentTimeMili = Calendar.getInstance().getTimeInMillis();
		long currentTimeRecord = currentTimeMili - (currentTimeMili % TIME_DIF_DAY) - TIME_DIF;
		Timestamp timeStamp = new Timestamp(currentTimeRecord);
		TrackStatisticModel newTrackCount = new TrackStatisticModel();

		newTrackCount.setViewCount(0);
		newTrackCount.setFavoriteCount(0);
		newTrackCount.setId(new TrackCountCompKey(trackId, timeStamp.toString()));
		trackCountRepository.save(newTrackCount);
		return newTrackCount;
	}

	// ---------------
	// !! TESTING FUNCTION !!
	// ---------------

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Add custom view count. Only for testing.
	public List<TrackStatisticModel> addCustomViewCount(int trackId, int numberOfWeek) {
		tracksRepository.findById(trackId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] Track ID " + trackId + " does not exist."));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Calendar calendarToday = Calendar.getInstance();
			Date today = sdf.parse(calendarToday.get(Calendar.YEAR) + "/" + (calendarToday.get(Calendar.MONTH) + 1)
					+ "/" + calendarToday.get(Calendar.DAY_OF_MONTH));

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("Today", calendarToday.toString());

			List<TrackStatisticModel> trackCountList = new ArrayList<>();

			int totalCount = 0;
			for (int i = 0; i <= numberOfWeek; i++) {
				Random random = new Random();
				int randomizedNumberOfview = (int) random.nextInt(90) + 10;
				int randomizedNumberOfFavorite = (int) random.nextInt(10) + 10;

				long currentMili = today.getTime() + (TIME_DIF_DAY * -i);
				Date currentDate = new Date(currentMili);
				resultMap.put("day" + i, currentDate.toString());
				Timestamp timeStamp = new Timestamp(currentMili);
				TrackStatisticModel newTrackCount = new TrackStatisticModel();
				newTrackCount.setFavoriteCount(randomizedNumberOfFavorite);
				newTrackCount.setViewCount(randomizedNumberOfview);
				newTrackCount.setId(new TrackCountCompKey(trackId, timeStamp.toString()));
				trackCountRepository.save(newTrackCount);
				totalCount += randomizedNumberOfview;
			}
			tracksRepository.updateViewCount(totalCount, trackId);
			return trackCountList;
		} catch (ParseException e) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ DEAD ] Error in function : addCustomViewCount " + e.getLocalizedMessage());
		}
	}

}
