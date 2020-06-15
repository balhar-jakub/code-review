/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zaasclient;

public class ConfigProperties {

    private String apimlHost;
    private String apimlPort;
    private String apimlBaseUrl;
    private String keyStoreType;
    private String keyStorePath;
    private String keyStorePassword;
    private String trustStoreType;
    private String trustStorePath;
    private String trustStorePassword;

    public ConfigProperties() {
    }

    public String getApimlHost() {
        return apimlHost;
    }

    public void setApimlHost(String apimlHost) {
        this.apimlHost = apimlHost;
    }

    public String getApimlPort() {
        return apimlPort;
    }

    public void setApimlPort(String apimlPort) {
        this.apimlPort = apimlPort;
    }

    public String getApimlBaseUrl() {
        return apimlBaseUrl;
    }

    public void setApimlBaseUrl(String apimlBaseUrl) {
        this.apimlBaseUrl = apimlBaseUrl;
    }

    public String getKeyStoreType() {
        return keyStoreType;
    }

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getTrustStoreType() {
        return trustStoreType;
    }

    public void setTrustStoreType(String trustStoreType) {
        this.trustStoreType = trustStoreType;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    @Override
    public String toString() {
        return "ConfigProperties{" +
                "apimlHost='" + apimlHost + '\'' +
                ", apimlPort='" + apimlPort + '\'' +
                ", apimlBaseUrl='" + apimlBaseUrl + '\'' +
                ", keyStoreType='" + keyStoreType + '\'' +
                ", keyStorePath='" + keyStorePath + '\'' +
                ", keyStorePassword='" + "" + '\'' +
                ", trustStoreType='" + trustStoreType + '\'' +
                ", trustStorePath='" + trustStorePath + '\'' +
                ", trustStorePassword='" + "" + '\'' +
                '}';
    }
}
