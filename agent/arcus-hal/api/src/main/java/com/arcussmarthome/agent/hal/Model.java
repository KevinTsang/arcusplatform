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
package com.arcussmarthome.agent.hal;

public enum Model {
    IH200,
    IH300,
    IH304;

    public static boolean isIH200(String model) {
        if (model == null) return false;
        return model.toUpperCase().equals(IH200.toString());
    }

    public static boolean isIH300(String model) {
        if (model == null) return false;
        return model.toUpperCase().equals(IH300.toString());
    }
    public static boolean isIH304(String model) {        
        if (model == null) return false;
        return model.toUpperCase().equals(IH304.toString());
    }
    
    public static boolean isV2(String model) {
        return isIH200(model);
    }
    
    public static boolean isV3(String model) {
        return isIH300(model) || isIH304(model);
    }
    
}

