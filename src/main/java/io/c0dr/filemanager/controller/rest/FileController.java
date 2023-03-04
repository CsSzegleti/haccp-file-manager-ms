package io.c0dr.filemanager.controller.rest;

import io.c0dr.filemanager.controller.rest.config.HeaderNames;
import io.c0dr.filemanager.model.SearchResult;
import io.c0dr.filemanager.service.FileReaderService;
import io.c0dr.filemanager.service.FileSearchService;
import io.c0dr.filemanager.service.FileUrlService;
import io.c0dr.filemanager.service.exception.ErrorCode;
import io.c0dr.filemanager.service.exception.MissingEntityException;
import io.c0dr.filemanager.service.model.UrlFileModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.convert.DurationFormat;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.Duration;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class FileController {

    private final FileSearchService fileSearchService;
    private final FileUrlService fileUrlService;
    private final FileReaderService fileReaderService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful query",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SearchResult.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request (validation error)"),
            @ApiResponse(responseCode = "404", description = "No such file")
    })
    @Operation(summary = "Search files for private use.")
    @GetMapping("/private/{id}")
    public ResponseEntity<SearchResult> getFileInformationByIdPrivate(
            @Parameter(description = "File id")
            @PathVariable Integer id,

            @Parameter(description = "Correlation ID")
            @NotNull(message = "error.field.file.null")
            @NotBlank(message = "error.field.file.blank")
            @NotEmpty(message = "error.field.file.empty")
            @RequestHeader(value = HeaderNames.CORRELATION_ID) String pCID) throws MissingEntityException {
        var result = fileSearchService.getFileByIdToPrivate(id);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful query",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SearchResult.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request (validation error)"),
            @ApiResponse(responseCode = "404", description = "No such file")
    })
    @Operation(summary = "Search files for public use.")
    @GetMapping("/public/{id}")
    public ResponseEntity<SearchResult> getFileInformationByIdPublic(
            @Parameter(description = "File id")
            @PathVariable Integer id,

            @Parameter(description = "Correlation ID")
            @NotNull(message = "error.field.correlationId.null")
            @NotBlank(message = "error.field.correlationId.blank")
            @NotEmpty(message = "error.field.correlationId.empty")
            @RequestHeader(value = HeaderNames.CORRELATION_ID) String pCID,

            @RequestParam(required = false, defaultValue = "PT30M")
            @DurationFormat(DurationStyle.SIMPLE)
            Duration lifeTime,

            @Parameter(description = "User")
            @NotNull(message = "error.field.user.null")
            @NotBlank(message = "error.field.user.blank")
            @NotEmpty(message = "error.field.user.empty")
            @RequestHeader(value = HeaderNames.USER_NAME) String user,

            HttpServletRequest request) throws MissingEntityException {

        var result = fileSearchService.getFileByIdToPublic(id, lifeTime.toMinutes());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful query",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SearchResult.class))}),
            @ApiResponse(responseCode = "404", description = "No such file with that id")
    })
    @GetMapping("/private_url/{id}")
    public ResponseEntity<String> getPrivateUrl(
            @PathVariable Integer id,

            @Parameter(description = "Correlation ID")
            @NotNull(message = "error.field.file.null")
            @NotBlank(message = "error.field.file.blank")
            @NotEmpty(message = "error.field.file.empty")
            @RequestHeader(value = HeaderNames.CORRELATION_ID) String pCID
    ) throws MissingEntityException {
        var result = fileSearchService.getFileByIdToPrivate(id);

        if (result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result.getUrl());
        } else
            throw new MissingEntityException(ErrorCode.ErrorType.MISSING_ENTITY, "Cannot find file with id: " + id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful query"),
            @ApiResponse(responseCode = "400", description = "Bad request (validation error)"),
            @ApiResponse(responseCode = "404", description = "No such file with that id")
    })
    @GetMapping("/public_url/{id}")
    public ResponseEntity<String> getPublicUrl(
            @PathVariable Integer id,

            @Parameter(description = "Correlation ID")
            @NotNull(message = "error.field.correlationId.null")
            @NotBlank(message = "error.field.correlationId.blank")
            @NotEmpty(message = "error.field.correlationId.empty")
            @RequestHeader(value = HeaderNames.CORRELATION_ID) String pCID,

            @RequestParam(required = false, defaultValue = "PT30M")
            Duration lifeTime
    ) throws MissingEntityException {
        var result = fileSearchService.getFileByIdToPublic(id, lifeTime.toMinutes());

        if (result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result.getUrl());
        } else
            throw new MissingEntityException(ErrorCode.ErrorType.MISSING_ENTITY, "Cannot find file with id: " + id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Download success"),
            @ApiResponse(responseCode = "404", description = "No such file to that url")
    })
    @GetMapping(
            value = "/download/{suffix}",
            produces = "application/stream+json"
    )
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("suffix") String urlSuffix,

            @Parameter(description = "Correlation ID")
            @NotNull(message = "error.field.correlationId.null")
            @NotBlank(message = "error.field.correlationId.blank")
            @NotEmpty(message = "error.field.correlationId.empty")
            @RequestHeader(value = HeaderNames.CORRELATION_ID) String pCID,

            @Parameter(description = "User")
            @NotNull(message = "error.field.user.null")
            @NotBlank(message = "error.field.user.blank")
            @NotEmpty(message = "error.field.user.empty")
            @RequestHeader(value = HeaderNames.USER_NAME) String user,

            HttpServletRequest request) throws MissingEntityException {
        UrlFileModel urlFileModel = fileUrlService.getLocation(urlSuffix, user);

        if (null == urlFileModel) {
            throw new MissingEntityException(ErrorCode.ErrorType.MISSING_ENTITY, "Cannot find file with url suffix: " + urlSuffix);
        }

        return createResponseEntity(getResource(urlFileModel.getRelativePath()), urlFileModel.getOriginalFileName(), request);
    }

    private Resource getResource(String fileName) {
        return fileReaderService.loadFileAsResource(fileName);
    }

    private ResponseEntity<Resource> createResponseEntity(Resource resource, String originalFileName, HttpServletRequest request) {
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"")
                .body(resource);
    }

    @Deprecated
    private String extractUrlSuffix(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
