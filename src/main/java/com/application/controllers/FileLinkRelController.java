package com.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.entities.models.FileLinkRefModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.FileLinkRefRepository;
import com.application.repositories.FileTypeRepository;
import com.application.utilities.MinioStorageService;

@Service
public class FileLinkRelController {

	@Autowired
	private FileLinkRefRepository fileLinkRefRepository;
	@Autowired
	private FileTypeRepository fileTypeRepository;
	@Autowired
	private MinioStorageService minioStorageService;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// Add image to track.
	public String insertNewTrackObjectLinkRel(MultipartFile multipartFile, int typeId, int recordRel) {
		FileLinkRefModel newFileRecord = new FileLinkRefModel();

		newFileRecord.setFileType(fileTypeRepository.findById(typeId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.FILE_INVALID_TYPE, HttpStatus.I_AM_A_TEAPOT,
						"[ FILE_INVALID_TYPE ] A file type with this ID does not exists.")));
		newFileRecord.setFileId(minioStorageService.uploadImageToStorage(multipartFile, typeId + "-",
				newFileRecord.getFileType().getPathRel()));
		newFileRecord.setTargetRef(recordRel);
		fileLinkRefRepository.save(newFileRecord);
		return newFileRecord.getFileId();
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Upload track to storage
	public String uploadNewTrack(MultipartFile trackFile, int trackId) {
		String fileName = minioStorageService.uploadTrackToStorage(trackFile, "tracks/musics/");
		FileLinkRefModel newFile = new FileLinkRefModel();
		newFile.setFileType(fileTypeRepository.findById(1001)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.FILE_INVALID_TYPE, HttpStatus.I_AM_A_TEAPOT,
						"[ FILE_INVALID_TYPE ] A file type with this ID does not exists.")));
		newFile.setFileId(fileName);
		newFile.setTargetRef(trackId);
		fileLinkRefRepository.save(newFile);
		return fileName;
	}
	

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// Delete file by its name.
	public void deleteTargetFileByName(String fileName) {
		FileLinkRefModel target = fileLinkRefRepository.findById(fileName).orElseThrow(() -> new ExceptionFoundation(
				EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
				"[ FileLinkRelController] Can't delete this record because the file " + fileName + " is unreachable."));
		String destination = target.getFileType().getPathRel() + fileName;
		minioStorageService.DeleteObjectFromMinIoByPathAndName(destination);
		fileLinkRefRepository.delete(target);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Upload track to storage
	public String deleteTrack(String fileName) {
		return "";
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// Delete track image using type ID and ref ID.
	public void deleteTargetFileByTypeIdAndLinkRef(int typeId, int refId) {
		FileLinkRefModel target = fileLinkRefRepository.findByTargetRefAndTypeId(typeId, refId);
		String destination = target.getFileType().getPathRel() + target.getFileId();
		minioStorageService.DeleteObjectFromMinIoByPathAndName(destination);
		fileLinkRefRepository.delete(target);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// RetriveImageByName
	public Resource retriveImageByName(String fileName) {
		FileLinkRefModel targetFile = fileLinkRefRepository.findById(fileName)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ FileLinkRelController ] File name " + fileName + "does not exist in the record."));
		String fileNameLocation = targetFile.getFileType().getPathRel() + fileName;

		return minioStorageService.getImageFromMinIoByNameLocation(fileNameLocation);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// RetriveImageByTargetRef
	public Resource retriveImageByTargetRef(int typeId, int targetRef) {
		FileLinkRefModel fileModel = fileLinkRefRepository.findByTargetRefAndTypeId(typeId, targetRef);
		String targetFileInMinIo = fileModel.getFileType().getPathRel() + fileModel.getFileId();
		return minioStorageService.getImageFromMinIoByNameLocation(targetFileInMinIo);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// isExistsInRecord
	public boolean isExistsInRecord(String fileName) {
		if (fileLinkRefRepository.existsById(fileName)) {
			return true;
		} else {
			return false;
		}
	}

}
