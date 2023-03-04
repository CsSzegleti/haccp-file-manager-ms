package io.c0dr.filemanager.controller.rest;


import io.c0dr.filemanager.controller.rest.converter.RestMessageConverter;
import io.c0dr.filemanager.model.ExtraData;
import io.c0dr.filemanager.service.FileManagerFacade;
import io.c0dr.filemanager.service.exception.ApiError;
import io.c0dr.filemanager.service.exception.SecurityConstraintException;
import io.c0dr.filemanager.service.exception.WriteException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class FileUploaderController {

    private final HttpServletRequest request;
    private final FileManagerFacade fileService;
    private final RestMessageConverter converter;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres file feltöltés"),
            @ApiResponse(responseCode = "400", description = "Nem megfelelő kérés paraméterek (validációs hiba)",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)) }),
            @ApiResponse(responseCode = "500", description = "Sikertelen file feltöltés (belső rendszer hiba)",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)) })
    })
    @Operation(summary = "Upload file")
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
    public HttpStatus uploadFile(
            @Parameter(description = "File to upload", style = ParameterStyle.FORM)
            @NotNull(message = "error.field.file.null")  @RequestPart(value = "file")
            MultipartFile file,

            @Parameter(description = "Metadata",
                    style = ParameterStyle.FORM,
                    content = @Content(
                            encoding = @Encoding(contentType = MediaType.APPLICATION_JSON_VALUE)))
            @RequestPart(value = "extraData", required = false)
            ExtraData extraData
    ) throws WriteException, SecurityConstraintException, IOException {
        fileService.saveFile(converter.convert(file.getBytes(), file.getOriginalFilename(), extraData, request));

        return HttpStatus.OK;
    }
}
