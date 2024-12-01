package com.skmns.codingtest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "authority")
public class Authority {

    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;

    public Authority() {
    }

    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "authorityName='" + authorityName + '\'' +
                '}';
    }
    public static class Builder {
        private String authorityName;

        public Builder authorityName(String authorityName) {
            this.authorityName = authorityName;
            return this;
        }

        public Authority build() {
            return new Authority(authorityName);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
