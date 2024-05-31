package com.mobiauto.controller;


import com.mobiauto.data.ApiResponseDTO;
import com.mobiauto.data.Vehicle;
import com.mobiauto.service.AuthorizationService;
import com.mobiauto.service.VehicleService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;

import java.util.List;

@Controller("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final AuthorizationService authorizationService;

    public VehicleController(VehicleService vehicleService, AuthorizationService authorizationService) {
        this.vehicleService = vehicleService;
        this.authorizationService = authorizationService;
    }

    @Get(produces = "application/json")
    @Transactional
    @Operation(
            tags = {"Vehicles"},
            summary = "Get all vehicles",
            description = "Returns all vehicles from the database",
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
                            description = "Vehicles found in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "No vehicles found")
            })
    public HttpResponse<ApiResponseDTO> getAllVehicles(@Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        if (vehicles != null && !vehicles.isEmpty()) {
            ApiResponseDTO apiResponseDTO = new ApiResponseDTO(null, vehicles);
            return HttpResponse.status(HttpStatus.FOUND).body(apiResponseDTO);
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("No vehicles found", null));
    }

    @Get(value="/{id}", produces = "application/json")
    @Operation(
            tags = {"Vehicles"},
            summary = "Get vehicle by ID",
            description = "Returns a vehicle by ID from the database",
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
                            description = "Vehicle found in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found")
            })
    public HttpResponse<ApiResponseDTO> getVehicleById(@PathVariable Long id, @Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        Vehicle vehicle = vehicleService.getVehicleById(id);
        if (vehicle != null) {
            return HttpResponse.status(HttpStatus.FOUND).body(new ApiResponseDTO(null, vehicle));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("Vehicle not found", null));

    }

    @Post(produces = "application/json")
    @Transactional
    @Operation(
            tags = {"Vehicles"},
            summary = "Create a vehicle",
            description = "Creates a vehicle in the database",
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
                            description = "Vehicle created in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "302", description = "Vehicle already exists")
            })
    public HttpResponse<ApiResponseDTO> createMessage(@Body Vehicle vehicleRequest, @Header("x-api-key") String apiKey) {

        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        Vehicle vehicle = vehicleService.getVehicleById(vehicleRequest.getId());
        if (vehicle != null) {
            return HttpResponse.status(HttpStatus.FOUND).body(new ApiResponseDTO("Vehicle already exists", vehicle));
        }

        return HttpResponse.status(HttpStatus.CREATED).body(new ApiResponseDTO("Vehicle created", vehicleService.saveVehicle(vehicleRequest)));

    }

    @Put(value="/{id}",produces = "application/json")
    @Transactional
    @Operation(
            tags = {"Vehicles"},
            summary = "Update a Vehicle",
            description = "Update a Vehicle in the database",
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
                            description = "Vehicle updated in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found")
            })
    public HttpResponse<ApiResponseDTO> updateMessage(@PathVariable Long id, @Body Vehicle vehicleDetails, @Header("x-api-key") String apiKey) {

        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        Vehicle vehicle = vehicleService.getVehicleById(id);
        if (vehicle != null) {
            vehicle.setDescription(vehicleDetails.getDescription());
            vehicle.setMake(vehicleDetails.getMake());
            vehicle.setModel(vehicleDetails.getModel());
            vehicle.setYear(vehicleDetails.getYear());
            vehicle.setPrice(vehicleDetails.getPrice());
            vehicle.setImages(vehicleDetails.getImages());

            return HttpResponse.status(HttpStatus.ACCEPTED).body(new ApiResponseDTO(null, vehicleService.updateVehicle(vehicle)));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("Vehicle not found", null));

    }

    @Delete(value = "/{id}",produces = "application/json")
    @Transactional
    @Operation(
            tags = {"Vehicles"},
            summary = "Delete a vehicle",
            description = "Deletes a vehicle in the database",
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
                            description = "Vehicle deleted in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "Vehicle not found")
            })
    public HttpResponse<ApiResponseDTO> deleteMessage(@PathVariable Long id, @Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        Vehicle vehicle = vehicleService.getVehicleById(id);
        if (vehicle != null) {
            vehicleService.deleteVehicle(vehicle.getId());
            return HttpResponse.status(HttpStatus.ACCEPTED).body(new ApiResponseDTO("Vehicle deleted", null));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("Vehicle not found", null));

    }
}

