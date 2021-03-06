/**
 * Copyright 2016-2017 Symphony Integrations - Symphony LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.symphonyoss.integration.webhook.github.parser.v1;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.symphonyoss.integration.json.JsonUtils;
import org.symphonyoss.integration.webhook.github.CommonGithubTest;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.symphonyoss.integration.webhook.github.parser.GithubParserException;
import org.symphonyoss.integration.webhook.github.parser.GithubParserUtils;

import java.io.IOException;
import java.util.Collections;

/**
 * Unit tests for {@link DeploymentGithubParser}
 *
 * Created by rsanchez on 23/09/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeploymentGithubParserTest extends CommonGithubTest {

  private static final String USER_URL = "https://api.github.com/users/baxterthehacker";

  @Mock
  private GithubParserUtils utils;

  @InjectMocks
  private DeploymentGithubParser parser = new DeploymentGithubParser();

  @Test
  public void testDeploymentEvent() throws IOException, GithubParserException {
    JsonNode publicUserInfoBaxter = JsonUtils.readTree(
        classLoader.getResourceAsStream("parser/payload_github_public_info_baxterthehacker.json"));
    doReturn(publicUserInfoBaxter).when(utils).doGetJsonApi(USER_URL);

    JsonNode node = JsonUtils.readTree(
        classLoader.getResourceAsStream("parser/deployment/payload_xgithubevent_deployment.json"));

    String expected = readFile(
        "parser/deployment/v1/payload_xgithubevent_deployment_expected_message.xml");
    String result = parser.parse(Collections.<String, String>emptyMap(), node).getMessage();

    assertEquals(expected, result);
  }

  @Test
  public void testDeploymentEventWithoutUserInfoAndDescription() throws IOException, GithubParserException {
    doReturn(null).when(utils).doGetJsonApi(USER_URL);

    JsonNode node = JsonUtils.readTree(classLoader.getResourceAsStream(
        "parser/deployment/payload_xgithubevent_deployment_without_description.json"));

    String expected =
        readFile(
            "parser/deployment/v1/payload_xgithubevent_deployment_without_userinfo_expected_message.xml");
    String result = parser.parse(Collections.<String, String>emptyMap(), node).getMessage();

    assertEquals(expected, result);
  }

}
