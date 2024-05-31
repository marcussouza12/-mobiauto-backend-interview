package com.mobiauto.controller;


import com.mobiauto.data.ApiResponseDTO;
import com.mobiauto.data.VehicleHistory;
import com.mobiauto.service.AuthorizationService;
import com.mobiauto.service.VehicleHistoryService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;


@Controller("/vehicleHistories")
public class VehicleHistoryController {

    private final VehicleHistoryService vehicleHistoryService;
    private final AuthorizationService authorizationService;

    public VehicleHistoryController(VehicleHistoryService vehicleHistoryService, AuthorizationService authorizationService) {
        this.vehicleHistoryService = vehicleHistoryService;
        this.authorizationService = authorizationService;
    }

    @Get(produces = "application/json")
    @Operation(
            tags = {"Vehicle History"},
            summary = "Get all vehicleHistorys",
            description = "Returns all vehicleHistorys from the database",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "x-api-key",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "202",
                            description = "Vehicle History found in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "No Vehicle History found")
            })
    public HttpResponse<ApiResponseDTO> getAllVehicleHistory(@Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        List<VehicleHistory> vehicleHistorys = vehicleHistoryService.getAllVehicleHistorys();

        if (vehicleHistorys != null && !vehicleHistorys.isEmpty()) {
            ApiResponseDTO apiResponseDTO = new ApiResponseDTO(null, vehicleHistorys);
            return HttpResponse.status(HttpStatus.FOUND).body(apiResponseDTO);
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("No Vehicle History found", null));
    }

    @Get(value="/{id}", produces = "application/json")
    @Operation(
            tags = {"Vehicle History"},
            summary = "Get Vehicle History by ID",
            description = "Returns a Vehicle History by ID from the database",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "x-api-key",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "202",
                            description = "Vehicle History found in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "Vehicle History not found")
            })
    public HttpResponse<ApiResponseDTO> getVehicleHistoryById(@PathVariable Long id, @Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        VehicleHistory vehicleHistory = vehicleHistoryService.getVehicleHistoryById(id);
        if (vehicleHistory != null) {
            return HttpResponse.status(HttpStatus.FOUND).body(new ApiResponseDTO(null, vehicleHistory));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("Vehicle History not found", null));

    }

    @Post(produces = "application/json")
    @Operation(
            tags = {"Vehicle History"},
            summary = "Create a Vehicle History",
            description = "Creates a Vehicle History in the database",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "x-api-key",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Vehicle History created in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "302", description = "Vehicle History already exists")
            })
    public HttpResponse<ApiResponseDTO> createMessage(@Body VehicleHistory vehicleHistoryRequest, @Header("x-api-key") String apiKey) {

        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        VehicleHistory vehicleHistory = vehicleHistoryService.getVehicleHistoryById(vehicleHistoryRequest.getId());
        if (vehicleHistory != null) {
            return HttpResponse.status(HttpStatus.FOUND).body(new ApiResponseDTO("VehicleHistory already exists", vehicleHistory));
        }

        return HttpResponse.status(HttpStatus.CREATED).body(new ApiResponseDTO("VehicleHistory created", vehicleHistoryService.saveVehicleHistory(vehicleHistoryRequest)));

    }

    @Put(value="/{id}",produces = "application/json")
    @Operation(
            tags = {"Vehicle History"},
            summary = "Update a Vehicle History",
            description = "Update a Vehicle History in the database",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "x-api-key",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "202",
                            description = "Vehicle History updated in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "Vehicle History not found")
            })
    public HttpResponse<ApiResponseDTO> updateMessage(@PathVariable Long id, @Body VehicleHistory vehicleHistoryDetails, @Header("x-api-key") String apiKey) {

        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        VehicleHistory vehicleHistory = vehicleHistoryService.getVehicleHistoryById(id);
        if (vehicleHistory != null) {
            vehicleHistory.setVehicle(vehicleHistoryDetails.getVehicle());
            vehicleHistory.setDescription(vehicleHistoryDetails.getDescription());
            vehicleHistory.setDescription(vehicleHistoryDetails.getDescription());

            return HttpResponse.status(HttpStatus.ACCEPTED).body(new ApiResponseDTO(null, vehicleHistoryService.updateVehicleHistory(vehicleHistory)));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("Vehicle History not found", null));

    }

    @Delete(value = "/{id}",produces = "application/json")
    @Operation(
            tags = {"Vehicle History"},
            summary = "Delete a Vehicle History",
            description = "Deletes a Vehicle History in the database",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "x-api-key",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "202",
                            description = "Vehicle History deleted in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "Vehicle History not found")
            })
    public HttpResponse<ApiResponseDTO> deleteMessage(@PathVariable Long id, @Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        VehicleHistory vehicleHistory = vehicleHistoryService.getVehicleHistoryById(id);
        if (vehicleHistory != null) {
            vehicleHistoryService.deleteVehicleHistory(vehicleHistory.getId());
            return HttpResponse.status(HttpStatus.ACCEPTED).body(new ApiResponseDTO("VehicleHistory deleted", null));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("VehicleHistory not found", null));

    }
}

