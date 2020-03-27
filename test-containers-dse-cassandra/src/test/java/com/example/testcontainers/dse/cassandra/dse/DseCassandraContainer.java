package com.example.testcontainers.dse.cassandra.dse;

import com.datastax.driver.core.Cluster;
import com.github.dockerjava.api.command.InspectContainerResponse;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.delegate.DatabaseDelegate;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import org.testcontainers.utility.MountableFile;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class DseCassandraContainer<SELF extends DseCassandraContainer<SELF>> extends GenericContainer<SELF> {

    private static final String IMAGE = "datastax/cassandra";
    private static final Integer CQL_PORT = 9042;
    private static final String CONTAINER_CONFIG_LOCATION = "/etc/cassandra";
    private static final String USERNAME = "cassandra";
    private static final String PASSWORD = "cassandra";

    private String configLocation;
    private String initScriptPath;
    private boolean enableJmxReporting;

    public DseCassandraContainer(String dseCassandraVersion) {
        super(IMAGE + ":" + dseCassandraVersion);
        addExposedPort(CQL_PORT);
        setStartupAttempts(3);
        this.enableJmxReporting = false;
    }

    @Override
    protected void configure() {
        optionallyMapResourceParameterAsVolume(CONTAINER_CONFIG_LOCATION, configLocation);
    }

    @Override
    protected void containerIsStarted(InspectContainerResponse containerInfo) {
        runInitScriptIfRequired();
    }

    /**
     * Load init script content and apply it to the database if initScriptPath is set
     */
    private void runInitScriptIfRequired() {
        if (initScriptPath != null) {
            try {
                URL resource = Thread.currentThread().getContextClassLoader().getResource(initScriptPath);
                if (resource == null) {
                    logger().warn("Could not load classpath init script: {}", initScriptPath);
                    throw new ScriptUtils.ScriptLoadException("Could not load classpath init script: " + initScriptPath + ". Resource not found.");
                }
                String cql = IOUtils.toString(resource, StandardCharsets.UTF_8);
                DatabaseDelegate databaseDelegate = getDatabaseDelegate();
                ScriptUtils.executeDatabaseScript(databaseDelegate, initScriptPath, cql);
            } catch (IOException e) {
                logger().warn("Could not load classpath init script: {}", initScriptPath);
                throw new ScriptUtils.ScriptLoadException("Could not load classpath init script: " + initScriptPath, e);
            } catch (ScriptException e) {
                logger().error("Error while executing init script: {}", initScriptPath, e);
                throw new ScriptUtils.UncategorizedScriptException("Error while executing init script: " + initScriptPath, e);
            }
        }
    }

    /**
     * Map (effectively replace) directory in Docker with the content of resourceLocation if resource location is not null
     * <p>
     * Protected to allow for changing implementation by extending the class
     *
     * @param pathNameInContainer path in docker
     * @param resourceLocation    relative classpath to resource
     */
    private void optionallyMapResourceParameterAsVolume(String pathNameInContainer, String resourceLocation) {
        Optional.ofNullable(resourceLocation)
                .map(MountableFile::forClasspathResource)
                .ifPresent(mountableFile -> withCopyFileToContainer(mountableFile, pathNameInContainer));
    }

    /**
     * Initialize Cassandra with the custom overridden Cassandra configuration
     * <p>
     * Be aware, that Docker effectively replaces all /etc/cassandra content with the content of config location, so if
     * Cassandra.yaml in configLocation is absent or corrupted, then Cassandra just won't launch
     *
     * @param configLocation relative classpath with the directory that contains cassandra.yaml and other configuration files
     */
    public SELF withConfigurationOverride(String configLocation) {
        this.configLocation = configLocation;
        return self();
    }

    /**
     * Initialize Cassandra with init CQL script
     * <p>
     * CQL script will be applied after container is started (see using WaitStrategy)
     *
     * @param initScriptPath relative classpath resource
     */
    public SELF withInitScript(String initScriptPath) {
        this.initScriptPath = initScriptPath;
        return self();
    }

    /**
     * Initialize Cassandra client with JMX reporting enabled or disabled
     */
    public SELF withJmxReporting(boolean enableJmxReporting) {
        this.enableJmxReporting = enableJmxReporting;
        return self();
    }

    /**
     * Get username
     * <p>
     * By default Cassandra has authenticator: AllowAllAuthenticator in cassandra.yaml
     * If username and password need to be used, then authenticator should be set as PasswordAuthenticator
     * (through custom Cassandra configuration) and through CQL with default cassandra-cassandra credentials
     * user management should be modified
     */
    public String getUsername() {
        return USERNAME;
    }

    /**
     * Get password
     * <p>
     * By default Cassandra has authenticator: AllowAllAuthenticator in cassandra.yaml
     * If username and password need to be used, then authenticator should be set as PasswordAuthenticator
     * (through custom Cassandra configuration) and through CQL with default cassandra-cassandra credentials
     * user management should be modified
     */
    public String getPassword() {
        return PASSWORD;
    }

    /**
     * Get configured Cluster
     * <p>
     * Can be used to obtain connections to Cassandra in the container
     */
    public Cluster getCluster() {
        return getCluster(this, enableJmxReporting);
    }

    private static Cluster getCluster(ContainerState containerState, boolean enableJmxReporting) {
        final Cluster.Builder builder = Cluster.builder()
                .addContactPoint(containerState.getContainerIpAddress())
                .withPort(containerState.getMappedPort(CQL_PORT));
        if (!enableJmxReporting) {
            builder.withoutJMXReporting();
        }
        return builder.build();
    }

    static Cluster getCluster(ContainerState containerState) {
        return getCluster(containerState, false);
    }

    private DatabaseDelegate getDatabaseDelegate() {
        return new CassandraDatabaseDelegate(this);
    }
}
