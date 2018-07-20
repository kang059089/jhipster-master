package com.kang.jhipster.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.io.Serializable;
import java.util.Objects;

/**
 * A VersionFile.
 * 版本更新对象（此对象用于.xml模式的版本信息）
 */
@XmlAccessorType(XmlAccessType.FIELD)
// XML文件中的根标识
@XmlRootElement(name = "VersionFile")
// 控制JAXB 绑定类中属性和字段的排序
@XmlType(propOrder = {
    "id",
    "versionNo",
    "versionInfo",
    "updateDate",
    "latestVersionNo",
    "latestVersionInfo",
    "latestUpdateDate",
    "appDownloadUrl",
    "forceUpdateState"
})
@Entity
@Table(name = "version_file")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VersionFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version_no")
    private String versionNo;//版本号

    @Column(name = "version_info")
    private String versionInfo;//版本信息

    @Column(name = "update_date")
    private String updateDate;//版本更新日期

    @Column(name = "latest_version_no")
    private String latestVersionNo;//最新版本号

    @Column(name = "latest_version_info")
    private String latestVersionInfo;//最新版本信息

    @Column(name = "latest_update_date")
    private String latestUpdateDate;//最新版本更新日期

    @Column(name = "app_download_url")
    private String appDownloadUrl;//app下载地址

    @Column(name = "force_update_state")
    private String forceUpdateState;//强制更新状态值（0：不强制更新，1：强制更新）

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

    public VersionFile versionNo(String versionNo) {
        this.versionNo = versionNo;
        return this;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public VersionFile versionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
        return this;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public VersionFile updateDate(String updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getLatestVersionNo() {
        return latestVersionNo;
    }

    public VersionFile latestVersionNo(String latestVersionNo) {
        this.latestVersionNo = latestVersionNo;
        return this;
    }

    public void setLatestVersionNo(String latestVersionNo) {
        this.latestVersionNo = latestVersionNo;
    }

    public String getLatestVersionInfo() {
        return latestVersionInfo;
    }

    public VersionFile latestVersionInfo(String latestVersionInfo) {
        this.latestVersionInfo = latestVersionInfo;
        return this;
    }

    public void setLatestVersionInfo(String latestVersionInfo) {
        this.latestVersionInfo = latestVersionInfo;
    }

    public String getLatestUpdateDate() {
        return latestUpdateDate;
    }

    public VersionFile latestUpdateDate(String latestUpdateDate) {
        this.latestUpdateDate = latestUpdateDate;
        return this;
    }

    public void setLatestUpdateDate(String latestUpdateDate) {
        this.latestUpdateDate = latestUpdateDate;
    }

    public String getAppDownloadUrl() {
        return appDownloadUrl;
    }

    public VersionFile appDownloadUrl(String appDownloadUrl) {
        this.appDownloadUrl = appDownloadUrl;
        return this;
    }

    public String getForceUpdateState() {
        return forceUpdateState;
    }

    public void setForceUpdateState(String forceUpdateState) {
        this.forceUpdateState = forceUpdateState;
    }

    public void setAppDownloadUrl(String appDownloadUrl) {
        this.appDownloadUrl = appDownloadUrl;
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
        VersionFile versionFile = (VersionFile) o;
        if (versionFile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), versionFile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VersionFile{" +
            "id=" + getId() +
            ", versionNo='" + getVersionNo() + "'" +
            ", versionInfo='" + getVersionInfo() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", latestVersionNo='" + getLatestVersionNo() + "'" +
            ", latestVersionInfo='" + getLatestVersionInfo() + "'" +
            ", latestUpdateDate='" + getLatestUpdateDate() + "'" +
            ", appDownloadUrl='" + getAppDownloadUrl() + "'" +
            ", forceUpdateState='" + getForceUpdateState() + "'" +
            "}";
    }
}
