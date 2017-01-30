package io.vidyo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;

/**
 * Properties specific to Vidyo.
 *
 * <p>
 *     Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "vidyo", ignoreUnknownFields = false)
public class VidyoProperties {

    private final Saml saml = new Saml();
    private final CallMe callMe = new CallMe();
    private final Twilio twilio = new Twilio();

    public Saml getSaml() {
        return saml;
    }
    public CallMe getCallMe() { return callMe; }
    public Twilio getTwilio() { return twilio; }

    public static class Saml {

        private String spEntityBaseURL = "";
        private String spEntityId = "";
        private String spAlias = "";
        private String idpMetadataXMLPath = "";
        private Keystore keystore = new Keystore();

        public String getSpEntityBaseURL() {
            return spEntityBaseURL;
        }

        public void setSpEntityBaseURL(String spEntityBaseURL) {
            this.spEntityBaseURL = spEntityBaseURL;
        }

        public String getSpEntityId() {
            return spEntityId;
        }

        public void setSpEntityId(String spEntityId) {
            this.spEntityId = spEntityId;
        }

        public String getSpAlias() {
            return spAlias;
        }

        public void setSpAlias(String spAlias) {
            this.spAlias = spAlias;
        }

        public String getIdpMetadataXMLPath() {
            return idpMetadataXMLPath;
        }

        public void setIdpMetadataXMLPath(String idpMetadataXMLPath) {
            this.idpMetadataXMLPath = idpMetadataXMLPath;
        }

        public Keystore getKeystore() {
            return keystore;
        }

        public void setKeystore(Keystore keystore) {
            this.keystore = keystore;
        }

        public static class Keystore {
            private String path = "";
            private String password = "";
            private String privateKeyAlias = "";
            private String privateKeyPassword= "";
            private String defaultPrivateKeyAlias= "";

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getPrivateKeyAlias() {
                return privateKeyAlias;
            }

            public void setPrivateKeyAlias(String privateKeyAlias) {
                this.privateKeyAlias = privateKeyAlias;
            }

            public String getPrivateKeyPassword() {
                return privateKeyPassword;
            }

            public void setPrivateKeyPassword(String privateKeyPassword) {
                this.privateKeyPassword = privateKeyPassword;
            }

            public String getDefaultPrivateKeyAlias() {
                return defaultPrivateKeyAlias;
            }

            public void setDefaultPrivateKeyAlias(String defaultPrivateKeyAlias) {
                this.defaultPrivateKeyAlias = defaultPrivateKeyAlias;
            }
        }
    }

    public static class CallMe {

        private String appBaseUrl = "http://127.0.0.1";
        private int conferenceTtlMs = 3600000;
        private int tokenTtlSec = 600;

        public String getAppBaseUrl() {
            return appBaseUrl;
        }

        public void setAppBaseUrl(String appBaseUrl) {
            this.appBaseUrl = appBaseUrl;
        }

        public int getConferenceTtlMs() {
            return conferenceTtlMs;
        }

        public void setConferenceTtlMs(int conferenceTtlMs) {
            this.conferenceTtlMs = conferenceTtlMs;
        }

        public int getTokenTtlSec() {
            return tokenTtlSec;
        }

        public void setTokenTtlSec(int tokenTtlSec) {
            this.tokenTtlSec = tokenTtlSec;
        }

    }

    public static class Twilio {

        private String accountSid;
        private String authToken;
        private String from;

        public String getAccountSid() {
            return accountSid;
        }

        public void setAccountSid(String accountSid) {
            this.accountSid = accountSid;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }


    }

}
