package com.example.testtask.controller;

import com.example.testtask.dto.UserDTO;
import com.example.testtask.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Return list of users",
            description = "Return aggregated list of users from all databases")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User list was returned successfully"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping
    public List<UserDTO> getUsers(@Parameter(description = "Username filter") @RequestParam(required = false) String username,
                                  @Parameter(description = "First name filter") @RequestParam(required = false) String name,
                                  @Parameter(description = "Last name filter") @RequestParam(required = false) String surname) {
        List<UserDTO> userDTOS = userService.aggregateUsers(username, name, surname);
        System.out.println("from db: " + Arrays.toString(userDTOS.toArray()));
        return userDTOS;
    }
}
