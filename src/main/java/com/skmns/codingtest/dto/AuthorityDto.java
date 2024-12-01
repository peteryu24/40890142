package com.skmns.codingtest.dto;

public class AuthorityDto {

    private String authorityName;

    public AuthorityDto() {
    }

    public AuthorityDto(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String authorityName;

        public Builder authorityName(String authorityName) {
            this.authorityName = authorityName;
            return this;
        }

        public AuthorityDto build() {
            return new AuthorityDto(authorityName);
        }
    }

    @Override
    public String toString() {
        return "AuthorityDto{" +
                "authorityName='" + authorityName + '\'' +
                '}';
    }
}
