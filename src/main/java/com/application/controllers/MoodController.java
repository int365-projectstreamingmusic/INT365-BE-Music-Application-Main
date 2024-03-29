package com.application.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.copmskeys.MoodPlaylistCompKey;
import com.application.entities.copmskeys.MoodTrackCompKey;
import com.application.entities.copmskeys.MoodUserCompKey;
import com.application.entities.models.MoodModel;
import com.application.entities.models.MoodPlaylistModel;
import com.application.entities.models.MoodTrackModel;
import com.application.entities.models.MoodUserModel;
import com.application.entities.models.UserAccountModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.MoodPlaylistRepository;
import com.application.repositories.MoodRepository;
import com.application.repositories.MoodTrackRepository;
import com.application.repositories.MoodUserRepository;

@Service
public class MoodController {

	@Autowired
	private MoodTrackRepository moodTrackRepository;
	@Autowired
	private MoodUserRepository moodUserRepository;
	@Autowired
	private MoodPlaylistRepository moodPlaylistRepository;
	@Autowired
	private MoodRepository moodRepository;

	public static int defaultMoodPerPage = 50;
	public static int maxMoodPerPage = 250;
	public static int maxMoodlist = 100;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// NOTE | Will add all moods in the playlist with incoming one.
	// NOTE | Will skip if the mood ID given is wrong.
	public List<MoodPlaylistModel> addMoodPlaylist(int playlistId, List<MoodModel> moodForm) {
		List<MoodPlaylistModel> result = new ArrayList<>();
		for (int i = 0; i < moodForm.size(); i++) {
			if (moodRepository.existsById(moodForm.get(i).getId())) {
				moodPlaylistRepository.insertMoodPlaylist(playlistId, moodForm.get(i).getId());
				result.add(new MoodPlaylistModel(new MoodPlaylistCompKey(playlistId, moodForm.get(i).getId()), 1,
						moodForm.get(i)));
			}
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// NOTE | Purge all moods from either track or playlist.
	public void purgeMoodTrack(int trackId) {
		moodTrackRepository.deleteMoodTrack(trackId);
	}

	public void purgeMoodPlaylist(int playlistId) {
		moodPlaylistRepository.deleteMoodPlaylist(playlistId);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// List all mood
	public Page<MoodModel> listMood(int page, int pageSize, String searchContent) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > maxMoodPerPage) {
			pageSize = defaultMoodPerPage;
		}

		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<MoodModel> result;
		result = moodRepository.listAll(searchContent, pageRequest);
		if (result.getNumberOfElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] No mood found.");
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// get mood by id
	public MoodModel getMoodById(int moodId) {
		return moodRepository.findById(moodId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] This mode with the ID does not exist."));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// delete mood
	public void deleteMood(int moodId) {
		if (!moodRepository.existsById(moodId)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.RECORD_ALREADY_GONE, HttpStatus.NOT_FOUND,
					"[ RECORD_ALREADY_GONE ] This record does not exist in the database.");
		} else {
			moodRepository.deleteById(moodId);
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	//
	// Insert new mood into user
	public List<MoodUserModel> addMoodToUser(List<MoodModel> moods, UserAccountModel user) {
		// int numberOfEntiry = moods.size();
		List<MoodUserModel> newMoodList = new ArrayList<>();
		for (int i = 0; i < moods.size(); i++) {
			MoodUserModel newMood = new MoodUserModel();
			newMood.setId(new MoodUserCompKey(moods.get(i).getId(), user.getAccountId()));
			newMood.setRatio(1);
			try {
				newMood = moodUserRepository.save(newMood);
				newMoodList.add(newMood);
			} catch (Exception e) {
				System.out.println("[ WARNING ] No mood or user found. Skip adding mood to user.");
			}
		}
		return newMoodList;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	//
	// Insert mood into track
	public List<MoodTrackModel> addMoodToTrack(int trackId, List<MoodModel> moods) {
		List<MoodModel> existingMoods = moodRepository.listMoodByTrack(trackId);
		List<MoodTrackModel> trackMood = new ArrayList<>();
		for (int i = 0; i < moods.size(); i++) {
			if (!existingMoods.contains(moods.get(i))) {
				MoodTrackModel newMood = new MoodTrackModel();
				newMood.setId(new MoodTrackCompKey(trackId, moods.get(i).getId()));
				newMood.setRatio(1);
				try {
					newMood = moodTrackRepository.save(newMood);
					trackMood.add(newMood);
				} catch (Exception e) {
					System.out.println("[ WARNING ] No mood or user found. Skip adding mood to user.");
				}
			}
		}
		return trackMood;
	}

}
