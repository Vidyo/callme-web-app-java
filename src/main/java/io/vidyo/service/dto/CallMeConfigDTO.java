package io.vidyo.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the CallMeConfig entity.
 */
public class CallMeConfigDTO implements Serializable {

    private Long id;

    @Size(max = 64)
    private String appKey;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String appId;

    @Size(max = 255)
    private String devKey;

    @Size(max = 255)
    private String referrers;

    @Size(max = 255)
    private String emailTo;

    @Size(max = 255)
    private String emailFrom;

    @Size(max = 255)
    private String emailSubject;

    @Size(max = 15)
    private String smsTo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getDevKey() {
        return devKey;
    }

    public void setDevKey(String devKey) {
        this.devKey = devKey;
    }
    public String getReferrers() {
        return referrers;
    }

    public void setReferrers(String referrers) {
        this.referrers = referrers;
    }
    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }
    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }
    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }
    public String getSmsTo() {
        return smsTo;
    }

    public void setSmsTo(String smsTo) {
        this.smsTo = smsTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CallMeConfigDTO callMeConfigDTO = (CallMeConfigDTO) o;

        if ( ! Objects.equals(id, callMeConfigDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CallMeConfigDTO{" +
            "id=" + id +
            ", appKey='" + appKey + "'" +
            ", name='" + name + "'" +
            ", appId='" + appId + "'" +
            ", devKey='" + devKey + "'" +
            ", referrers='" + referrers + "'" +
            ", emailTo='" + emailTo + "'" +
            ", emailFrom='" + emailFrom + "'" +
            ", emailSubject='" + emailSubject + "'" +
            ", smsTo='" + smsTo + "'" +
            '}';
    }
}
