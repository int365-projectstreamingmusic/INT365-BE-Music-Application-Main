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

	// OK!
	// insertNewTrackObjectLinkRel
	public String insertNewTrackObjectLinkRel(MultipartFile multipartFile, int typeId, int recordRel) {
		FileLinkRefModel newFileRecord = new FileLinkRefModel();

		newFileRecord.setFileType(fileTypeRepository.findById(typeId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ FileLinkRelController ] A file type with this ID does not exists.")));
		newFileRecord.setFileId(minioStorageService.uploadImageToStorage(multipartFile, typeId + "-",
				newFileRecord.getFileType().getPathRel()));
		newFileRecord.setTargetRef(recordRel);
		fileLinkRefRepository.save(newFileRecord);
		return newFileRecord.getFileId();
	}

	// OK!
	// DeleteTargetFileByName
	public void deleteTargetFileByName(String fileName) {
		FileLinkRefModel target = fileLinkRefRepository.findById(fileName).orElseThrow(() -> new ExceptionFoundation(
				EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
				"[ FileLinkRelController] Can't delete this record because the file " + fileName + " is unreachable."));
		String destination = target.getFileType().getPathRel() + fileName;
		minioStorageService.deleteObjectFromMinioByName(destination);
		fileLinkRefRepository.deleteById(fileName);
	}

	// OK!
	// RetriveImageByName
	public Resource retriveImageByName(String fileName) {
		FileLinkRefModel targetFile = fileLinkRefRepository.findById(fileName)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ FileLinkRelController ] File name " + fileName + "does not exist in the record."));
		String fileNameLocation = targetFile.getFileType().getPathRel() + fileName;

		return minioStorageService.getImageFromMinIoByNameLocation(fileNameLocation);
	}

	// OK!
	// RetriveImageByTargetRef
	public Resource retriveImageByTargetRef(int typeId, int targetRef) {
		FileLinkRefModel fileModel = fileLinkRefRepository.findByTargetRefAndTypeId(typeId, targetRef);
		String targetFileInMinIo = fileModel.getFileType().getPathRel() + fileModel.getFileId();
		return minioStorageService.getImageFromMinIoByNameLocation(targetFileInMinIo);
	}

}
