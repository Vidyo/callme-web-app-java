package io.vidyo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CallMeConfig.
 */
@Entity
@Table(name = "call_me_config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CallMeConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 64)
    @Column(name = "app_key", length = 64)
    private String appKey;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Size(max = 255)
    @Column(name = "app_id", length = 255, nullable = false)
    private String appId;

    @NotNull
    @Size(max = 255)
    @Column(name = "dev_key", length = 255, nullable = false)
    private String devKey;

    @Size(max = 255)
    @Column(name = "referrers", length = 255)
    private String referrers;

    @Size(max = 255)
    @Column(name = "email_to", length = 255)
    private String emailTo;

    @Size(max = 255)
    @Column(name = "email_from", length = 255)
    private String emailFrom;

    @Size(max = 255)
    @Column(name = "email_subject", length = 255)
    private String emailSubject;

    @Size(max = 15)
    @Column(name = "sms_to", length = 15)
    private String smsTo;

    @ManyToOne
    private User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppKey() {
        return appKey;
    }

    public CallMeConfig appKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getName() {
        return name;
    }

    public CallMeConfig name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public CallMeConfig appId(String appId) {
        this.appId = appId;
        return this;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDevKey() {
        return devKey;
    }

    public CallMeConfig devKey(String devKey) {
        this.devKey = devKey;
        return this;
    }

    public void setDevKey(String devKey) {
        this.devKey = devKey;
    }

    public String getReferrers() {
        return referrers;
    }

    public CallMeConfig referrers(String referrers) {
        this.referrers = referrers;
        return this;
    }

    public void setReferrers(String referrers) {
        this.referrers = referrers;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public CallMeConfig emailTo(String emailTo) {
        this.emailTo = emailTo;
        return this;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public CallMeConfig emailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
        return this;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public CallMeConfig emailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
        return this;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getSmsTo() {
        return smsTo;
    }

    public CallMeConfig smsTo(String smsTo) {
        this.smsTo = smsTo;
        return this;
    }

    public void setSmsTo(String smsTo) {
        this.smsTo = smsTo;
    }

    public User getOwner() {
        return owner;
    }

    public CallMeConfig owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CallMeConfig callMeConfig = (CallMeConfig) o;
        if (callMeConfig.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, callMeConfig.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CallMeConfig{" +
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
