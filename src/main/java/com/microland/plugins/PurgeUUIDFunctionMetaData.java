package com.microland.plugins;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

/**
 * Implement the PluginMetaData interface here.
 */
public class PurgeUUIDFunctionMetaData implements PluginMetaData {
    private static final String PLUGIN_PROPERTIES = "com.microland.plugins.graylog-plugin-uuid-pipeline-purge/graylog-plugin.properties";

    @Override
    public String getUniqueId() {
        return "com.microland.plugins.PurgeUUIDFunctionPlugin";
    }

    @Override
    public String getName() {
        return "PurgeUUIDFunction";
    }

    @Override
    public String getAuthor() {
        return "Lavish Jain <lavishrjain1997@gmail.com>";
    }

    @Override
    public URI getURL() {
        return URI.create("https://github.com/Microland/graylog-plugin-uuid-pipeline-purge");
    }

    @Override
    public Version getVersion() {
        return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "version", Version.from(0, 0, 0, "unknown"));
    }

    @Override
    public String getDescription() {
        // TODO Insert correct plugin description
        return "Description of PurgeUUIDFunction plugin";
    }

    @Override
    public Version getRequiredVersion() {
        return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "graylog.version", Version.from(0, 0, 0, "unknown"));
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
