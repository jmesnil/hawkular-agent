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
package org.hawkular.agent.ws.test;

import org.hawkular.cmdgw.ws.test.TestWebSocketClient;
import org.hawkular.inventory.api.model.Resource;
import org.hawkular.javaagent.itest.util.AbstractITest;

/**
 * Place for methods specific to command itests.
 */
public abstract class AbstractCommandITest extends AbstractITest {
    protected void forceInventoryDiscoveryScan() throws Throwable {
        waitForHawkularServerToBeReady();

        waitForAgentViaJMX();

        Resource agentResource = testHelper.waitForResourceContaining(
                hawkularFeedId, "Hawkular Java Agent", null, 5000, 10);

        String req = "ExecuteOperationRequest={\"authentication\":" + authentication + ", "
                + "\"feedId\":\"" + agentResource.getFeedId() + "\","
                + "\"resourceId\":\"" + agentResource.getId() + "\","
                + "\"operationName\":\"Inventory Discovery Scan\""
                + "}";
        String response = "ExecuteOperationResponse={"
                + "\"operationName\":\"Inventory Discovery Scan\","
                + "\"feedId\":\"" + agentResource.getFeedId() + "\","
                + "\"resourceId\":\"" + agentResource.getId() + "\","
                + "\"destinationSessionId\":\"{{sessionId}}\","
                + "\"status\":\"OK\","
                + "\"message\":\"Performed [Inventory Discovery Scan] on a [JMX MBean] given by Feed Id ["
                + agentResource.getFeedId() + "] Resource Id [" + agentResource.getId()
                + "]: Full inventory discovery scan completed in"; // will match anything after
        try (TestWebSocketClient testClient = TestWebSocketClient.builder()
                .url(baseGwUri + "/ui/ws")
                .expectWelcome(req)
                .expectGenericSuccess(agentResource.getFeedId())
                .expectText(response, TestWebSocketClient.Answer.CLOSE)
                .expectClose()
                .build()) {
            testClient.validate(10000);
        }
    }
}
