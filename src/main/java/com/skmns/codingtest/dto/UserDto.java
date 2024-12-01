package com.skmns.codingtest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skmns.codingtest.model.User;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.stream.Collectors;

public class UserDto {

    @NotNull
    @Size(min = 3, max = 100) 
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 255) 
    private String password;

    private Set<AuthorityDto> authorityDtoSet;

    public UserDto() {
    }

    public UserDto(String username, String password, Set<AuthorityDto> authorityDtoSet) {
        this.username = username;
        this.password = password;
        this.authorityDtoSet = authorityDtoSet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<AuthorityDto> getAuthorityDtoSet() {
        return authorityDtoSet;
    }

    public void setAuthorityDtoSet(Set<AuthorityDto> authorityDtoSet) {
        this.authorityDtoSet = authorityDtoSet;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String username;
        private String password;
        private Set<AuthorityDto> authorityDtoSet;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder authorityDtoSet(Set<AuthorityDto> authorityDtoSet) {
            this.authorityDtoSet = authorityDtoSet;
            return this;
        }

        public UserDto build() {
            return new UserDto(username, password, authorityDtoSet);
        }
    }

    public static UserDto from(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .username(user.getUsername())
                .password(null)
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(authority -> new AuthorityDto(authority.getAuthorityName()))
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", authorityDtoSet=" + authorityDtoSet +
                '}';
    }
}
