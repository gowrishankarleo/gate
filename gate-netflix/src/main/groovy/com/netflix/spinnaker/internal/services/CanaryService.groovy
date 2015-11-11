/*
 * Copyright 2015 Netflix, Inc.
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

package com.netflix.spinnaker.internal.services

import com.netflix.spinnaker.gate.services.commands.HystrixFactory
import com.netflix.spinnaker.internal.services.internal.MineService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import retrofit.RetrofitError
import static com.netflix.spinnaker.gate.retrofit.UpstreamBadRequest.classifyError

/**
 *
 * @author sthadeshwar
 */
@Component
@CompileStatic
class CanaryService {

  private static final String HYSTRIX_GROUP = "canaries"

  @Autowired(required = false)
  MineService mineService

  void generateCanaryResult(String canaryId, int duration, String durationUnit) {
    mineService?.generateCanaryResult(canaryId, duration, durationUnit)
  }

  List<Map> getCanaryAnalysisHistory(String canaryDeploymentId) {
    mineService ? mineService.getCanaryAnalysisHistory(canaryDeploymentId) : []
  }

  Map endCanary(String canaryId, String result, String reason) {
    mineService ? mineService.endCanary(canaryId, result, reason) : [:]
  }

  Map showCanary(String canaryId) {
    HystrixFactory.newMapCommand(HYSTRIX_GROUP, "showCanary", {
      try {
        mineService ? mineService.showCanary(canaryId) : [:]
      } catch (RetrofitError error) {
        throw classifyError(error)
      }
    }).execute()
  }

  List<String> getCanaryConfigNames() {
    mineService ? mineService.getCanaryConfigNames() : []
  }

}
