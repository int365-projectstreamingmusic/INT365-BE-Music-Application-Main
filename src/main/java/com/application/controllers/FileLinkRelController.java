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
	// InsertNewLinkRel
	public FileLinkRefModel insertNewLinkRel(MultipartFile multipartFile, int typeId, int recordRel) {
		FileLinkRefModel newFileRecord = new FileLinkRefModel();

		newFileRecord.setFileType(fileTypeRepository.findById(typeId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ FileLinkRelController ] A file type with this ID does not exists.")));
		newFileRecord.setTargetTrackId(recordRel);
		newFileRecord.setFileId(minioStorageService.uploadImageToStorage(multipartFile, typeId + "-",
				newFileRecord.getFileType().getPathRel()));

		fileLinkRefRepository.save(newFileRecord);
		return newFileRecord;
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
	public Resource retriveImageByTargetRef(int targetRef, int typeId) {
		FileLinkRefModel fileModel = fileLinkRefRepository.findByTargetRefAndTypeId(targetRef,
				fileTypeRepository.findById(typeId).orElseThrow(
						() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
								"[ FileLinkRelController ] The type with id " + typeId + " not found.")));
		String targetFileInMinIo = fileModel.getFileType().getPathRel() + fileModel.getFileId();
		return minioStorageService.getImageFromMinIoByNameLocation(targetFileInMinIo);
	}

}
