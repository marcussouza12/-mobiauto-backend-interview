package com.mobiauto.controller;


import com.mobiauto.data.ApiResponseDTO;
import com.mobiauto.data.Message;
import com.mobiauto.data.Negotiation;
import com.mobiauto.service.AuthorizationService;
import com.mobiauto.service.NegotiationService;
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

@Controller("/negotiations")
public class NegotiationController {

    private final NegotiationService negotiationService;
    private final AuthorizationService authorizationService;

    public NegotiationController(NegotiationService negotiationService, AuthorizationService authorizationService) {
        this.negotiationService = negotiationService;
        this.authorizationService = authorizationService;
    }

    @Get(produces = "application/json")
    @Operation(
            tags = {"Negotiations"},
            summary = "Get all negotiations",
            description = "Returns all negotiations from the database",
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
                            description = "Negotiations found in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "No negotiations found")
            })
    public HttpResponse<ApiResponseDTO> getAllNegotiations(@Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        List<Negotiation> negotiations = negotiationService.getAllNegotiations();

        if (negotiations != null && !negotiations.isEmpty()) {
            return HttpResponse.status(HttpStatus.FOUND).body(new ApiResponseDTO(null, negotiations));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("No negotiations found", null));
    }

    @Get(value="/{id}", produces = "application/json")
    @Operation(
            tags = {"Negotiations"},
            summary = "Get negotiations by ID",
            description = "Returns a negotiation by ID from the database",
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
                            description = "Negotiation found in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "negotiation not found")
            })
    public HttpResponse<ApiResponseDTO> getNegotiationById(@PathVariable Long id, @Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        Negotiation negotiation = negotiationService.getNegotiationById(id);
        if (negotiation != null) {
            return HttpResponse.status(HttpStatus.FOUND).body(new ApiResponseDTO(null, negotiation));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("Negotiation not found", null));

    }

    @Post(produces = "application/json")
    @Operation(
            tags = {"Negotiations"},
            summary = "Create a negotiation",
            description = "Creates a negotiation in the database",
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
                            description = "Negotiation created in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "302", description = "Negotiation already exists")
            })
    public HttpResponse<ApiResponseDTO> createMessage(@Body Negotiation negotiationRequest, @Header("x-api-key") String apiKey) {

        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        Negotiation negotiation = negotiationService.getNegotiationById(negotiationRequest.getId());
        if (negotiation != null) {
            return HttpResponse.status(HttpStatus.FOUND).body(new ApiResponseDTO("Negotiation already exists", negotiation));
        }

        return HttpResponse.status(HttpStatus.CREATED).body(new ApiResponseDTO("Negotiation created", negotiationService.saveNegotiation(negotiationRequest)));

    }

    @Put(value="/{id}",produces = "application/json")
    @Operation(
            tags = {"Negotiations"},
            summary = "Update a negotiation",
            description = "Update a negotiation in the database",
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
                            description = "Negotiation updated in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "Negotiation not found")
            })
    public HttpResponse<ApiResponseDTO> updateMessage(@PathVariable Long id, @Body Negotiation negotiationDetails, @Header("x-api-key") String apiKey) {

        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        Negotiation negotiation = negotiationService.getNegotiationById(id);
        if (negotiation != null) {
            negotiation.setMessages(negotiationDetails.getMessages());
            negotiation.setBuyer(negotiationDetails.getBuyer());
            negotiation.setInitialOffer(negotiationDetails.getInitialOffer());
            negotiation.setStartDate(negotiationDetails.getStartDate());
            negotiation.setVehicle(negotiationDetails.getVehicle());

            return HttpResponse.status(HttpStatus.ACCEPTED).body(new ApiResponseDTO(null, negotiationService.updateNegotiation(negotiation)));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("Negotiation not found", null));

    }

    @Delete(value = "/{id}",produces = "application/json")
    @Operation(
            tags = {"Negotiations"},
            summary = "Delete a negotiation",
            description = "Deletes a negotiation in the database",
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
                            description = "Negotiation deleted in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "Negotiation not found")
            })
    public HttpResponse<ApiResponseDTO> deleteMessage(@PathVariable Long id, @Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        Negotiation negotiation = negotiationService.getNegotiationById(id);
        if (negotiation != null) {
            negotiationService.deleteNegotiation(negotiation.getId());
            return HttpResponse.status(HttpStatus.ACCEPTED).body(new ApiResponseDTO("Negotiation deleted", null));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("Negotiation not found", null));

    }
}
