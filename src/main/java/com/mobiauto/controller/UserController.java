package com.mobiauto.controller;


import com.mobiauto.data.ApiResponseDTO;
import com.mobiauto.data.User;
import com.mobiauto.service.AuthorizationService;
import com.mobiauto.service.UserService;
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


@Controller("/users")
public class UserController {

    private final UserService userService;
    private final AuthorizationService authorizationService;

    public UserController(UserService userService, AuthorizationService authorizationService) {
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    @Get(produces = "application/json")
    @Operation(
            tags = {"Users"},
            summary = "Get all users",
            description = "Returns all users from the database",
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
                            description = "Users found in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "No users found")
            })
    public HttpResponse<ApiResponseDTO> getAllUsers(@Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        List<User> users = userService.getAllUsers();

        if (users != null && !users.isEmpty()) {
            return HttpResponse.status(HttpStatus.FOUND).body(new ApiResponseDTO(null, users));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("No users found", null));
    }

    @Get(value="/{id}", produces = "application/json")
    @Operation(
            tags = {"Users"},
            summary = "Get users by ID",
            description = "Returns a user by ID from the database",
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
                            description = "User found in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    public HttpResponse<ApiResponseDTO> getUserById(@PathVariable Long id, @Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        User user = userService.getUserById(id);
        if (user != null) {
            return HttpResponse.status(HttpStatus.FOUND).body(new ApiResponseDTO(null, user));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("User not found", null));

    }

    @Post(produces = "application/json")
    @Operation(
            tags = {"Users"},
            summary = "Create a user",
            description = "Creates a user in the database",
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
                            description = "User created in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "302", description = "User already exists")
            })
    public HttpResponse<ApiResponseDTO> createMessage(@Body User UserRequest, @Header("x-api-key") String apiKey) {

        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        User user = userService.getUserById(UserRequest.getId());
        if (user != null) {
            return HttpResponse.status(HttpStatus.FOUND).body(new ApiResponseDTO("User already exists", user));
        }

        return HttpResponse.status(HttpStatus.CREATED).body(new ApiResponseDTO("User created", userService.createUser(UserRequest)));

    }

    @Delete(value = "/{id}",produces = "application/json")
    @Operation(
            tags = {"Users"},
            summary = "Delete a user",
            description = "Deletes a user in the database",
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
                            description = "User deleted in the database",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "403", description = "Invalid API key"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    public HttpResponse<ApiResponseDTO> deleteMessage(@PathVariable Long id, @Header("x-api-key") String apiKey) {
        HttpResponse<ApiResponseDTO> isAuthorized = authorizationService.getHttpResponse(apiKey);

        if (isAuthorized.status() != HttpStatus.OK) {
            return isAuthorized;
        }

        User user = userService.getUserById(id);
        if (user != null) {
            userService.deleteUser(user.getId());
            return HttpResponse.status(HttpStatus.ACCEPTED).body(new ApiResponseDTO("User deleted", null));
        }

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("User not found", null));

    }
}

