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
package org.hawkular.agent.javaagent.config;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ConfigExpressionTest {

    @Test
    public void testBooleanExpression() throws Exception {
        ObjectMapper mapper = createObjectMapper();
        Configuration config;
        String yaml;
        String serializedYaml;

        // TEST EXPRESSION WITH A DEFAULT

        yaml = "" +
                "subsystem:\n" +
                "  enabled: ${my-sysprop:true}";

        // make sure the BooleanExpression type is deserialized correctly
        config = mapper.readValue(yaml, Configuration.class);
        Assert.assertEquals(true, config.getSubsystem().getEnabled());
        System.setProperty("my-sysprop", "false");
        Assert.assertEquals(false, config.getSubsystem().getEnabled());
        System.clearProperty("my-sysprop");
        Assert.assertEquals(true, config.getSubsystem().getEnabled());

        // make sure the BooleanExpression type is serialized correctly
        serializedYaml = mapper.writeValueAsString(config);
        Assert.assertTrue(serializedYaml.contains("  enabled: \"${my-sysprop:true}\""));

        // TEST EXPRESSION WITH NO DEFAULT

        yaml = "" +
                "subsystem:\n" +
                "  enabled: ${my-sysprop}\n";

        // make sure the BooleanExpression type is deserialized correctly
        config = mapper.readValue(yaml, Configuration.class);
        try {
            config.getSubsystem().getEnabled();
            Assert.fail("Should have failed - the boolean expression did not evaluate to true or false");
        } catch (Exception expected) {
        }
        System.setProperty("my-sysprop", "true");
        Assert.assertEquals(true, config.getSubsystem().getEnabled());
        System.clearProperty("my-sysprop");
        try {
            config.getSubsystem().getEnabled();
            Assert.fail("Should have failed - the boolean expression did not evaluate to true or false");
        } catch (Exception expected) {
        }

        // make sure the BooleanExpression type is serialized correctly
        serializedYaml = mapper.writeValueAsString(config);
        Assert.assertTrue(serializedYaml.contains("  enabled: \"${my-sysprop}\""));

        // TEST WITH NO EXPRESSION - ACTUAL VALUE

        yaml = "" +
                "subsystem:\n" +
                "  enabled: true\n";

        // make sure the BooleanExpression type is deserialized correctly
        config = mapper.readValue(yaml, Configuration.class);
        Assert.assertEquals(true, config.getSubsystem().getEnabled());

        // make sure the BooleanExpression type is serialized correctly
        serializedYaml = mapper.writeValueAsString(config);
        Assert.assertTrue(serializedYaml.contains("  enabled: \"true\""));

        // TEST WITH MISSING PROPERTY - FALLBACK TO DEFAULT

        yaml = "" +
                "subsystem:\n" +
                "  auto-discovery-scan-period-secs: 12345";

        // make sure the BooleanExpression type is deserialized correctly
        config = mapper.readValue(yaml, Configuration.class);
        Assert.assertEquals(true, config.getSubsystem().getEnabled());

        // make sure the BooleanExpression type is serialized correctly
        serializedYaml = mapper.writeValueAsString(config);
        Assert.assertTrue(serializedYaml.contains("  enabled: \"true\"")); // default appears in serialized yaml
    }

    @Test
    public void testIntegerExpression() throws Exception {
        ObjectMapper mapper = createObjectMapper();
        Configuration config;
        String yaml;
        String serializedYaml;

        // TEST EXPRESSION WITH A DEFAULT

        yaml = "" +
                "subsystem:\n" +
                "  auto-discovery-scan-period-secs: ${my-sysprop:12345}";

        // make sure the IntegerExpression type is deserialized correctly
        config = mapper.readValue(yaml, Configuration.class);
        Assert.assertEquals(Integer.valueOf(12345), config.getSubsystem().getAutoDiscoveryScanPeriodSecs());
        System.setProperty("my-sysprop", "9876");
        Assert.assertEquals(Integer.valueOf(9876), config.getSubsystem().getAutoDiscoveryScanPeriodSecs());
        System.clearProperty("my-sysprop");
        Assert.assertEquals(Integer.valueOf(12345), config.getSubsystem().getAutoDiscoveryScanPeriodSecs());

        // make sure the IntegerExpression type is serialized correctly
        serializedYaml = mapper.writeValueAsString(config);
        Assert.assertTrue(serializedYaml.contains("  auto-discovery-scan-period-secs: \"${my-sysprop:12345}\""));

        // TEST EXPRESSION WITH NO DEFAULT

        yaml = "" +
                "subsystem:\n" +
                "  auto-discovery-scan-period-secs: ${my-sysprop}\n";

        // make sure the IntegerExpression type is deserialized correctly
        config = mapper.readValue(yaml, Configuration.class);
        try {
            config.getSubsystem().getAutoDiscoveryScanPeriodSecs();
            Assert.fail("Should have failed - the integer expression did not evaluate to true or false");
        } catch (Exception expected) {
        }
        System.setProperty("my-sysprop", "9876");
        Assert.assertEquals(Integer.valueOf(9876), config.getSubsystem().getAutoDiscoveryScanPeriodSecs());
        System.clearProperty("my-sysprop");
        try {
            config.getSubsystem().getAutoDiscoveryScanPeriodSecs();
            Assert.fail("Should have failed - the integer expression did not evaluate to true or false");
        } catch (Exception expected) {
        }

        // make sure the IntegerExpression type is serialized correctly
        serializedYaml = mapper.writeValueAsString(config);
        Assert.assertTrue(serializedYaml.contains("  auto-discovery-scan-period-secs: \"${my-sysprop}\""));

        // TEST WITH NO EXPRESSION - ACTUAL VALUE

        yaml = "" +
                "subsystem:\n" +
                "  auto-discovery-scan-period-secs: 12345\n";

        // make sure the InetgerExpression type is deserialized correctly
        config = mapper.readValue(yaml, Configuration.class);
        Assert.assertEquals(Integer.valueOf(12345), config.getSubsystem().getAutoDiscoveryScanPeriodSecs());

        // make sure the IntegerExpression type is serialized correctly
        serializedYaml = mapper.writeValueAsString(config);
        Assert.assertTrue(serializedYaml.contains("  auto-discovery-scan-period-secs: \"12345\""));

        // TEST WITH MISSING PROPERTY - FALLBACK TO DEFAULT

        yaml = "" +
                "subsystem:\n" +
                "  enabled: true\n";

        // make sure the IntegerExpression type is deserialized correctly
        config = mapper.readValue(yaml, Configuration.class);
        Assert.assertEquals(Integer.valueOf(600), config.getSubsystem().getAutoDiscoveryScanPeriodSecs()); // default is harcoded

        // make sure the IntegerExpression type is serialized correctly
        serializedYaml = mapper.writeValueAsString(config);
        Assert.assertTrue(serializedYaml.contains("  auto-discovery-scan-period-secs: \"600\"")); // default appears in serialized yaml

    }

    @Test
    public void testStringExpression() throws Exception {
        ObjectMapper mapper = createObjectMapper();
        StorageAdapter config;
        String yaml;
        String serializedYaml;

        // TEST EXPRESSION WITH A DEFAULT

        yaml = "url: ${my-sysprop:http://test}";

        // make sure the StringExpression type is deserialized correctly
        config = mapper.readValue(yaml, StorageAdapter.class);
        Assert.assertEquals("http://test", config.getUrl());
        System.setProperty("my-sysprop", "http://override");
        Assert.assertEquals("http://override", config.getUrl());
        System.clearProperty("my-sysprop");
        Assert.assertEquals("http://test", config.getUrl());

        // make sure the StringExpression type is serialized correctly
        serializedYaml = mapper.writeValueAsString(config);
        Assert.assertTrue(serializedYaml.contains("url: \"${my-sysprop:http://test}\""));

        // TEST EXPRESSION WITH NO DEFAULT

        yaml = "url: ${my-sysprop}";

        // make sure the StringExpression type is deserialized correctly
        config = mapper.readValue(yaml, StorageAdapter.class);
        Assert.assertEquals("${my-sysprop}", config.getUrl());
        System.setProperty("my-sysprop", "http://override");
        Assert.assertEquals("http://override", config.getUrl());
        System.clearProperty("my-sysprop");
        Assert.assertEquals("${my-sysprop}", config.getUrl());

        // make sure the StringExpression type is serialized correctly
        serializedYaml = mapper.writeValueAsString(config);
        Assert.assertTrue(serializedYaml.contains("url: \"${my-sysprop}\""));

        // TEST WITH NO EXPRESSION - ACTUAL VALUE

        yaml = "url: http://test";

        // make sure the StringExpression type is deserialized correctly
        config = mapper.readValue(yaml, StorageAdapter.class);
        Assert.assertEquals("http://test", config.getUrl());

        // make sure the StringExpression type is serialized correctly
        serializedYaml = mapper.writeValueAsString(config);
        Assert.assertTrue(serializedYaml.contains("url: \"http://test\""));

        // TEST WITH MISSING PROPERTY - FALLBACK TO DEFAULT

        yaml = "url: http://required";

        // make sure the StringExpression type is deserialized correctly
        config = mapper.readValue(yaml, StorageAdapter.class);
        Assert.assertEquals("", config.getUsername()); // username default is empty string

        // make sure the StringExpression type is serialized correctly
        serializedYaml = mapper.writeValueAsString(config);
        Assert.assertTrue(serializedYaml.contains("username: \"\""));
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper;
    }
}
