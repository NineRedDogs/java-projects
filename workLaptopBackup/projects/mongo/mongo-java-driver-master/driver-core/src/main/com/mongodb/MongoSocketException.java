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

package com.mongodb;

/**
 * Subclass of {@link MongoException} representing a network-related exception
 *
 * @since 2.12
 */
public class MongoSocketException extends MongoException {

    private static final long serialVersionUID = -4415279469780082174L;

    private final ServerAddress serverAddress;

    /**
     * @param serverAddress the address
     * @param msg the message
     * @param e the cause
     */
    MongoSocketException(final String msg, final ServerAddress serverAddress, final Throwable e) {
        super(-2, msg, e);
        this.serverAddress = serverAddress;
    }

    /**
     * Construct a new instance.
     *
     * @param message the message
     * @param serverAddress the address
     */
    public MongoSocketException(final String message, final ServerAddress serverAddress) {
        super(-2, message);
        this.serverAddress = serverAddress;
    }

    /**
     * Gets the server address for this exception.
     *
     * @return the address
     */
    public ServerAddress getServerAddress() {
        return serverAddress;
    }
}
