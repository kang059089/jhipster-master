package com.kang.jhipster.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A AdminVersionFile.
 */
@Entity
@Table(name = "admin_version_file")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AdminVersionFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version_no")
    private String versionNo;

    @Column(name = "version_info")
    private String versionInfo;

    @Column(name = "force_update_state")
    private Boolean forceUpdateState;

    @Column(name = "version_release_state")
    private Boolean versionReleaseState;

    @Column(name = "app_download_url")
    private String appDownloadUrl;

    @Column(name = "version_release_date")
    private Instant versionReleaseDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public AdminVersionFile versionNo(String versionNo) {
        this.versionNo = versionNo;
        return this;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public AdminVersionFile versionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
        return this;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public Boolean isForceUpdateState() {
        return forceUpdateState;
    }

    public AdminVersionFile forceUpdateState(Boolean forceUpdateState) {
        this.forceUpdateState = forceUpdateState;
        return this;
    }

    public void setForceUpdateState(Boolean forceUpdateState) {
        this.forceUpdateState = forceUpdateState;
    }

    public Boolean isVersionReleaseState() {
        return versionReleaseState;
    }

    public AdminVersionFile versionReleaseState(Boolean versionReleaseState) {
        this.versionReleaseState = versionReleaseState;
        return this;
    }

    public void setVersionReleaseState(Boolean versionReleaseState) {
        this.versionReleaseState = versionReleaseState;
    }

    public String getAppDownloadUrl() {
        return appDownloadUrl;
    }

    public AdminVersionFile appDownloadUrl(String appDownloadUrl) {
        this.appDownloadUrl = appDownloadUrl;
        return this;
    }

    public void setAppDownloadUrl(String appDownloadUrl) {
        this.appDownloadUrl = appDownloadUrl;
    }

    public Instant getVersionReleaseDate() {
        return versionReleaseDate;
    }

    public AdminVersionFile versionReleaseDate(Instant versionReleaseDate) {
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
        AdminVersionFile adminVersionFile = (AdminVersionFile) o;
        if (adminVersionFile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), adminVersionFile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AdminVersionFile{" +
            "id=" + getId() +
            ", versionNo='" + getVersionNo() + "'" +
            ", versionInfo='" + getVersionInfo() + "'" +
            ", forceUpdateState='" + isForceUpdateState() + "'" +
            ", versionReleaseState='" + isVersionReleaseState() + "'" +
            ", appDownloadUrl='" + getAppDownloadUrl() + "'" +
            ", versionReleaseDate='" + getVersionReleaseDate() + "'" +
            "}";
    }
}
