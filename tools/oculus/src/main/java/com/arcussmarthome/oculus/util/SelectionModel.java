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
/**
 * 
 */
package com.arcussmarthome.oculus.util;

import com.google.common.base.Optional;
import com.arcussmarthome.client.event.Listener;
import com.arcussmarthome.client.event.ListenerRegistration;

/**
 * 
 */
public interface SelectionModel<M> {

   boolean hasSelection();
   
   void setSelection(M item);
   
   void clearSelection();
   
   Optional<M> getSelectedItem();
   
   ListenerRegistration addSelectionListener(Listener<Optional<M>> listener);
}

