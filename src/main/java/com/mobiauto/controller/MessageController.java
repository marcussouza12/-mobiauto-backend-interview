package com.mobiauto.controller;

import com.mobiauto.data.ApiResponseDTO;
import com.mobiauto.data.Message;
import com.mobiauto.service.AuthorizationService;
import com.mobiauto.service.MessageService;
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


@Controller("/messages")
public class MessageController {

    private final MessageService messageService;
    private final AuthorizationService authorizationService;

    public MessageController(MessageService messageService, AuthorizationService authorizationService) {
        this.messageService = messageService;
        this.authorizationService = authorizationService;
    }

    @Get(produces = "application/json")
    @Operation(
            tags = {"Messages"},
            summary = "Get all messages",
            description = "Returns all messages from the database",
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
                            description = "Messages found in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "No messages found")
            })
    public HttpResponse<ApiResponseDTO> getAllMessages(@Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        List<Message> messages = messageService.getAllMessages();

        if (messages != null && !messages.isEmpty()) {
            return HttpResponse.status(HttpStatus.FOUND).body(new ApiResponseDTO(null, messages));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("No messages found", null));
    }

    @Get(value="/{id}", produces = "application/json")
    @Operation(
            tags = {"Messages"},
            summary = "Get message by ID",
            description = "Returns a message by ID from the database",
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
                            description = "Message found in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "Message not found")
            })
    public HttpResponse<ApiResponseDTO> getMessageById(@PathVariable Long id, @Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        Message message = messageService.getMessageById(id);
        if (message != null) {
            return HttpResponse.status(HttpStatus.FOUND).body(new ApiResponseDTO(null, message));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("Message not found", null));

    }

    @Post(produces = "application/json")
    @Operation(
            tags = {"Messages"},
            summary = "Create a message",
            description = "Creates a message in the database",
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
                            description = "Message created in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "302", description = "Message already exists")
            })
    public HttpResponse<ApiResponseDTO> createMessage(@Body Message messageRequest, @Header("x-api-key") String apiKey) {

        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        Message message = messageService.getMessageById(messageRequest.getId());
        if (message != null) {
            return HttpResponse.status(HttpStatus.FOUND).body(new ApiResponseDTO("Message already exists", message));
        }

        return HttpResponse.status(HttpStatus.CREATED).body(new ApiResponseDTO("Message created", messageService.saveMessage(message)));

    }

    @Put(value="/{id}",produces = "application/json")
    @Operation(
            tags = {"Messages"},
            summary = "Update a message",
            description = "Updates a message in the database",
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
                            description = "Message updated in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "Message not found")
            })
    public HttpResponse<ApiResponseDTO> updateMessage(@PathVariable Long id, @Body Message messageDetails, @Header("x-api-key") String apiKey) {

        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        Message message = messageService.getMessageById(id);
        if (message != null) {
            message.setContent(messageDetails.getContent());
            message.setNegotiation(messageDetails.getNegotiation());
            message.setSender(messageDetails.getSender());
            message.setTimestamp(messageDetails.getTimestamp());

            return HttpResponse.status(HttpStatus.ACCEPTED).body(new ApiResponseDTO(null, messageService.updateMessage(message)));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("Message not found", null));

    }

    @Delete(value = "/{id}",produces = "application/json")
    @Operation(
            tags = {"Messages"},
            summary = "Delete a message",
            description = "Delete a message in the database",
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
                            description = "Message deleted in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "Message not found")
            })
    public HttpResponse<ApiResponseDTO> deleteMessage(@PathVariable Long id, @Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        Message message = messageService.getMessageById(id);
        if (message != null) {
            messageService.deleteMessage(message.getId());
            return HttpResponse.status(HttpStatus.ACCEPTED).body(new ApiResponseDTO("Message deleted", null));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("Message not found", null));

    }

}
