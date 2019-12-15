/*
 * Copyright 2019 Arcus Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arcussmarthome.platform.subsystem.pairing.state;

import com.arcussmarthome.common.subsystem.SubsystemContext;
import com.arcussmarthome.messages.capability.PairingSubsystemCapability;
import com.arcussmarthome.messages.model.subs.PairingSubsystemModel;
import com.arcussmarthome.platform.subsystem.pairing.PairingUtils;
import com.arcussmarthome.platform.subsystem.pairing.ProductLoaderForPairing.ProductCacheInfo;

public class IdleState extends PairingState {

	IdleState() {
		super(PairingStateName.Idle);
	}

	@Override
	public String onEnter(SubsystemContext<PairingSubsystemModel> context) {
		context.model().setPairingMode(PairingSubsystemCapability.PAIRINGMODE_IDLE);
		context.model().setSearchProductAddress("");
		ProductCacheInfo.clear(context);
		context.model().setSearchDeviceFound(false);
		context.model().setSearchIdle(false);
		context.model().setSearchIdleTimeout(PairingUtils.DEFAULT_TIMEOUT);
		context.model().setSearchTimeout(PairingUtils.DEFAULT_TIMEOUT);
		PairingUtils.clearBridgePairingInfo(context);
		PairingUtils.clearMockPairing(context);
		PairingUtils.clearPendingPairingState(context);
		// we don't clear the z-wave rebuild flag here because we re-enter this state from a StopPairingRequest
		// which happens before DismissAll
		stopHubPairingIfNeeded(context);
		return super.onEnter(context);
	}

}

