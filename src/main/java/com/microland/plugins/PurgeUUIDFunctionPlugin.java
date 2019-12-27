package com.microland.plugins;

import org.graylog2.plugin.Plugin;
import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.PluginModule;

import java.util.Collection;
import java.util.Collections;

/**
 * Implement the Plugin interface here.
 */
public class PurgeUUIDFunctionPlugin implements Plugin {
    @Override
    public PluginMetaData metadata() {
        return new PurgeUUIDFunctionMetaData();
    }

    @Override
    public Collection<PluginModule> modules () {
        return Collections.<PluginModule>singletonList(new PurgeUUIDFunctionModule());
    }
}
