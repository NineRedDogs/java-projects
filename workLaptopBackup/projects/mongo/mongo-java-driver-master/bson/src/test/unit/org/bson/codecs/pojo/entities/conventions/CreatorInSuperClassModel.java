/*
 * Copyright 2008-present MongoDB, Inc.
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

package org.bson.codecs.pojo.entities.conventions;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public abstract class CreatorInSuperClassModel {
    @BsonCreator
    public static CreatorInSuperClassModel newInstance(@BsonProperty("propertyA") final String propertyA,
                                                       @BsonProperty("propertyB") final String propertyB) {
        return new CreatorInSuperClassModelImpl(propertyA, propertyB);
    }
    public abstract String getPropertyA();
    public abstract String getPropertyB();
}
