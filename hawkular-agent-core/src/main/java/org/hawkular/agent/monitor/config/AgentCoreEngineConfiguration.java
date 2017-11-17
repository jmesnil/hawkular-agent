/*
 * Copyright 2015-2017 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.agent.monitor.config;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.hawkular.agent.monitor.inventory.ConnectionData;
import org.hawkular.agent.monitor.inventory.Name;
import org.hawkular.agent.monitor.inventory.TypeSets;
import org.hawkular.agent.monitor.log.AgentLoggers;
import org.hawkular.agent.monitor.log.MsgLogger;
import org.hawkular.agent.monitor.protocol.dmr.DMRNodeLocation;
import org.hawkular.agent.monitor.protocol.jmx.JMXNodeLocation;

/**
 * This represents the monitor service extension's XML configuration in a more consumable form.
 * To build this from the actual service model, see {@link MonitorServiceConfigurationBuilder}.
 */
public class AgentCoreEngineConfiguration {
    private static final MsgLogger log = AgentLoggers.getLogger(AgentCoreEngineConfiguration.class);

    /**
     * If feed ID is expicitly set to this value, it means the feed ID should be autogenerated at runtime.
     */
    private static final String FEED_ID_AUTOGENERATE = "autogenerate";

    public static class StorageAdapterConfiguration {
        private final String username;
        private final String password;
        private final String feedId;
        private final String url;
        private final boolean useSSL;
        private final String inventoryContext;
        private final String feedcommContext;
        private final String hawkularContext;
        private final String keystorePath;
        private final String keystorePassword;
        private final String securityRealm;
        private final int connectTimeoutSeconds;
        private final int readTimeoutSeconds;

        public StorageAdapterConfiguration(
                String username,
                String password,
                String feedId,
                String url,
                boolean useSSL,
                String inventoryContext,
                String feedcommContext,
                String hawkularContext,
                String keystorePath,
                String keystorePassword,
                String securityRealm,
                int connectTimeoutSeconds,
                int readTimeoutSeconds) {
            this.username = username;
            this.password = password;
            this.feedId = (FEED_ID_AUTOGENERATE.equalsIgnoreCase(feedId)) ? null : feedId;
            this.url = url;
            this.useSSL = useSSL;
            this.inventoryContext = inventoryContext;
            this.feedcommContext = feedcommContext;
            this.hawkularContext = hawkularContext;
            this.keystorePath = keystorePath;
            this.keystorePassword = keystorePassword;
            this.securityRealm = securityRealm;
            this.connectTimeoutSeconds = connectTimeoutSeconds;
            this.readTimeoutSeconds = readTimeoutSeconds;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        /**
         * This is the preconfigured feed ID and may not be set. If this is null (which under normal circumstances
         * it probably is) the agent will determine its feed ID at runtime. If this is not null, this is
         * the feed ID that the agent will be forced to use. It is here to allow a user to override the
         * runtime algorithm the agent uses to determine its feed ID.
         *
         * @return the feed ID to be used; may be <code>null</code>
         */
        public String getFeedId() {
            return feedId;
        }

        public String getUrl() {
            return url;
        }

        public boolean isUseSSL() {
            return useSSL;
        }

        public String getInventoryContext() {
            return inventoryContext;
        }

        public String getHawkularContext() {
            return hawkularContext;
        }

        public String getFeedcommContext() {
            return feedcommContext;
        }

        public String getKeystorePath() {
            return keystorePath;
        }

        public String getKeystorePassword() {
            return keystorePassword;
        }

        public String getSecurityRealm() {
            return securityRealm;
        }

        public int getConnectTimeoutSeconds() {
            return connectTimeoutSeconds;
        }

        public int getReadTimeoutSeconds() {
            return readTimeoutSeconds;
        }

    }

    public static class DiagnosticsConfiguration {
        public static final DiagnosticsConfiguration EMPTY = new DiagnosticsConfiguration(false, 0, null);
        private final boolean enabled;
        private final int interval;
        private final TimeUnit timeUnits;

        public DiagnosticsConfiguration(boolean enabled, int interval, TimeUnit timeUnits) {
            super();
            this.enabled = enabled;
            this.interval = interval;
            this.timeUnits = timeUnits;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public int getInterval() {
            return interval;
        }

        public TimeUnit getTimeUnits() {
            return timeUnits;
        }
    }

    public static class GlobalConfiguration {

        private final boolean subsystemEnabled;
        private final boolean immutable;
        private final boolean inContainer;
        private final int autoDiscoveryScanPeriodSeconds;
        private final String typeVersion;
        private final int numDmrSchedulerThreads;

        public GlobalConfiguration(
                boolean subsystemEnabled,
                boolean immutable,
                boolean inContainer,
                int autoDiscoveryScanPeriodSeconds,
                String typeVersion,
                int numDmrSchedulerThreads) {
            super();
            this.subsystemEnabled = subsystemEnabled;
            this.immutable = immutable;
            this.inContainer = inContainer;
            this.autoDiscoveryScanPeriodSeconds = autoDiscoveryScanPeriodSeconds;
            this.typeVersion = typeVersion;
            this.numDmrSchedulerThreads = numDmrSchedulerThreads;
        }

        public boolean isSubsystemEnabled() {
            return subsystemEnabled;
        }

        public boolean isImmutable() {
            return immutable;
        }

        public boolean isInContainer() {
            return inContainer;
        }

        public int getAutoDiscoveryScanPeriodSeconds() {
            return autoDiscoveryScanPeriodSeconds;
        }

        public String getTypeVersion() {
            return typeVersion;
        }

        public int getNumDmrSchedulerThreads() {
            return numDmrSchedulerThreads;
        }
    }

    public static class MetricsExporterConfiguration {

        private final boolean enabled;
        private final String host;
        private final int port;
        private final String configDir;
        private final String configFile;

        public MetricsExporterConfiguration(
                boolean enabled,
                String host,
                int port,
                String configDir,
                String configFile) {
            this.enabled = enabled;
            this.host = host;
            this.port = port;
            this.configDir = configDir;
            this.configFile = configFile;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String getConfigDir() {
            return configDir;
        }

        public String getConfigFile() {
            return configFile;
        }
    }

    public static class PlatformConfiguration {

        private final boolean enabled;
        private final boolean memoryEnabled;
        private final boolean fileStoresEnabled;
        private final boolean processorsEnabled;
        private final boolean powerSourcesEnabled;
        private final String machineId;
        private final String containerId;

        public PlatformConfiguration(
                boolean enabled,
                boolean memoryEnabled,
                boolean fileStoresEnabled,
                boolean processorsEnabled,
                boolean powerSourcesEnabled,
                String machineId,
                String containerId) {
            this.enabled = enabled;
            this.memoryEnabled = memoryEnabled;
            this.fileStoresEnabled = fileStoresEnabled;
            this.processorsEnabled = processorsEnabled;
            this.powerSourcesEnabled = powerSourcesEnabled;
            this.machineId = machineId;
            this.containerId = containerId;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public boolean isMemoryEnabled() {
            return memoryEnabled;
        }

        public boolean isFileStoresEnabled() {
            return fileStoresEnabled;
        }

        public boolean isProcessorsEnabled() {
            return processorsEnabled;
        }

        public boolean isPowerSourcesEnabled() {
            return powerSourcesEnabled;
        }

        public String getMachineId() {
            return machineId;
        }

        public String getContainerId() {
            return containerId;
        }
    }

    public static class ProtocolConfiguration<L> {

        public static <L> Builder<L> builder() {
            return new Builder<L>();
        }

        public static class Builder<L> {
            private TypeSets<L> typeSets;
            private Map<String, EndpointConfiguration> endpoints = new LinkedHashMap<>();

            public Builder<L> endpoint(EndpointConfiguration endpoint) {
                endpoints.put(endpoint.getName(), endpoint);
                return this;
            }

            public Builder<L> typeSets(TypeSets<L> typeSets) {
                this.typeSets = typeSets;
                return this;
            }

            public ProtocolConfiguration<L> build() {
                for (EndpointConfiguration server : endpoints.values()) {
                    if (server.getResourceTypeSets() != null) {
                        for (Name resourceTypeSetName : server.getResourceTypeSets()) {
                            if (!typeSets.getResourceTypeSets().containsKey(resourceTypeSetName)) {
                                log.warnResourceTypeSetDoesNotExist(server.getName().toString(),
                                        resourceTypeSetName.toString());
                            }
                        }
                    }
                }

                return new ProtocolConfiguration<>(typeSets, endpoints);
            }
        }

        private final TypeSets<L> typeSets;
        private final Map<String, EndpointConfiguration> endpoints;

        public ProtocolConfiguration(TypeSets<L> typeSets,
                Map<String, EndpointConfiguration> managedServers) {
            super();
            this.typeSets = typeSets;
            this.endpoints = managedServers;
        }

        public TypeSets<L> getTypeSets() {
            return typeSets;
        }

        public Map<String, EndpointConfiguration> getEndpoints() {
            return endpoints;
        }
    }

    public static class AbstractEndpointConfiguration {

        public static class WaitFor {
            private final String resource;

            public WaitFor(String resource) {
                this.resource = resource;
            }

            /**
             * @return The resource to wait for (can be things like a DMR path or JMX object name).
             */
            public String getResource() {
                return this.resource;
            }
        }

        private final String name;
        private final boolean enabled;
        private final ConnectionData connectionData;
        private final String securityRealm;
        private final Map<String, String> metricLabels;
        private final Map<String, ? extends Object> customData;
        private final List<WaitFor> waitForResources;

        public AbstractEndpointConfiguration(String name, boolean enabled, ConnectionData connectionData,
                String securityRealm, Map<String, String> metricLabels, Map<String, ? extends Object> customData,
                List<WaitFor> waitForResources) {
            super();
            this.name = name;
            this.enabled = enabled;
            this.connectionData = connectionData;
            this.securityRealm = securityRealm;
            this.metricLabels = metricLabels;
            this.customData = (customData != null) ? Collections.unmodifiableMap(customData) : Collections.emptyMap();
            this.waitForResources = (waitForResources != null) ? Collections.unmodifiableList(waitForResources)
                    : Collections.emptyList();
        }

        public boolean isEnabled() {
            return enabled;
        }

        public String getName() {
            return name;
        }

        public ConnectionData getConnectionData() {
            return connectionData;
        }

        public String getSecurityRealm() {
            return securityRealm;
        }

        /**
         * @return if not null this is name/value pairs of labels to be associated with metrics that are
         *         associated with resources associated with this managed server.
         */
        public Map<String, String> getMetricLabels() {
            return metricLabels;
        }

        /**
         * @return custom information related to an endpoint. The endpoint service should know the value types.
         */
        public Map<String, ? extends Object> getCustomData() {
            return customData;
        }

        public boolean isLocal() {
            return connectionData == null;
        }

        /**
         * @return list of resources to wait for before starting to monitor the endpoint (will not be null)
         */
        public List<WaitFor> getWaitForResources() {
            return waitForResources;
        }
    }

    public static class EndpointConfiguration extends AbstractEndpointConfiguration {
        private final Collection<Name> resourceTypeSets;

        public EndpointConfiguration(String name, boolean enabled, Collection<Name> resourceTypeSets,
                ConnectionData connectionData, String securityRealm, Map<String, String> metricLabels,
                Map<String, ? extends Object> customData, List<WaitFor> waitForResources) {
            super(name, enabled, connectionData, securityRealm, metricLabels, customData, waitForResources);
            this.resourceTypeSets = resourceTypeSets;
        }

        public Collection<Name> getResourceTypeSets() {
            return resourceTypeSets;
        }
    }

    private final GlobalConfiguration globalConfiguration;
    private final MetricsExporterConfiguration metricsExporterConfiguration;
    private final DiagnosticsConfiguration diagnostics;
    private final StorageAdapterConfiguration storageAdapter;
    private final PlatformConfiguration platformConfiguration;
    private final ProtocolConfiguration<DMRNodeLocation> dmrConfiguration;
    private final ProtocolConfiguration<JMXNodeLocation> jmxConfiguration;

    public AgentCoreEngineConfiguration(
            GlobalConfiguration globalConfiguration,
            MetricsExporterConfiguration metricsExporterConfiguration,
            DiagnosticsConfiguration diagnostics,
            StorageAdapterConfiguration storageAdapter,
            PlatformConfiguration platformConfiguration,
            ProtocolConfiguration<DMRNodeLocation> dmrConfiguration,
            ProtocolConfiguration<JMXNodeLocation> jmxConfiguration) {
        super();
        this.globalConfiguration = globalConfiguration;
        this.metricsExporterConfiguration = metricsExporterConfiguration;
        this.diagnostics = diagnostics;
        this.storageAdapter = storageAdapter;
        this.platformConfiguration = platformConfiguration;
        this.dmrConfiguration = dmrConfiguration;
        this.jmxConfiguration = jmxConfiguration;
    }

    public AgentCoreEngineConfiguration cloneWith(StorageAdapterConfiguration newStorageAdapter) {
        return new AgentCoreEngineConfiguration(
                globalConfiguration,
                metricsExporterConfiguration,
                diagnostics,
                newStorageAdapter,
                platformConfiguration,
                dmrConfiguration,
                jmxConfiguration);
    }

    public GlobalConfiguration getGlobalConfiguration() {
        return globalConfiguration;
    }

    public MetricsExporterConfiguration getMetricsExporterConfiguration() {
        return metricsExporterConfiguration;
    }

    public StorageAdapterConfiguration getStorageAdapter() {
        return storageAdapter;
    }

    public DiagnosticsConfiguration getDiagnostics() {
        return diagnostics;
    }

    public PlatformConfiguration getPlatformConfiguration() {
        return platformConfiguration;
    }

    public ProtocolConfiguration<DMRNodeLocation> getDmrConfiguration() {
        return dmrConfiguration;
    }

    public ProtocolConfiguration<JMXNodeLocation> getJmxConfiguration() {
        return jmxConfiguration;
    }
}
