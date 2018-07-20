package com.kang.jhipster.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A UserVersionFile.
 */
@Entity
@Table(name = "user_version_file")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserVersionFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "version_no")
    private String versionNo;

    @Column(name = "version_info")
    private String versionInfo;

    @Column(name = "version_release_date")
    private Instant versionReleaseDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public UserVersionFile userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public UserVersionFile versionNo(String versionNo) {
        this.versionNo = versionNo;
        return this;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public UserVersionFile versionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
        return this;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public Instant getVersionReleaseDate() {
        return versionReleaseDate;
    }

    public UserVersionFile versionReleaseDate(Instant versionReleaseDate) {
        this.versionReleaseDate = versionReleaseDate;
        return this;
    }

    public void setVersionReleaseDate(Instant versionReleaseDate) {
        this.versionReleaseDate = versionReleaseDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserVersionFile userVersionFile = (UserVersionFile) o;
        if (userVersionFile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userVersionFile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserVersionFile{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", versionNo='" + getVersionNo() + "'" +
            ", versionInfo='" + getVersionInfo() + "'" +
            ", versionReleaseDate='" + getVersionReleaseDate() + "'" +
            "}";
    }
}
