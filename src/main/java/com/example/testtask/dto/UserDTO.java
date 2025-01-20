package com.example.testtask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

        @JsonProperty("id")
        private String id;
        @JsonProperty("username")
        private String username;
        @JsonProperty("name")
        private String name;
        @JsonProperty("surname")
        private String surname;

        public UserDTO(String id, String username, String name, String surname) {
            this.id = id;
            this.username = username;
            this.name = name;
            this.surname = surname;
        }


}
