/*
 * Licensed to Neo4j under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Neo4j licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.neo4j.examples;

import org.junit.Rule;
import org.junit.Test;

import org.neo4j.doc.test.rule.TestDirectory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

import static org.junit.Assert.assertNotNull;

public class StartWithConfigurationDocTest
{
    @Rule
    public final TestDirectory testDirectory = TestDirectory.testDirectory();

    @Test
    public void loadFromFile()
    {
        String pathToConfig = "src/test/resources/";
        // tag::startDbWithConfig[]
        GraphDatabaseService graphDb = new GraphDatabaseFactory()
            .newEmbeddedDatabaseBuilder( testDirectory.databaseDir() )
            .loadPropertiesFromFile( pathToConfig + "neo4j.conf" )
            .newGraphDatabase();
        // end::startDbWithConfig[]
        assertNotNull( graphDb );
        graphDb.shutdown();
    }

    @Test
    public void loadFromHashmap()
    {
        // tag::startDbWithMapConfig[]
        GraphDatabaseService graphDb = new GraphDatabaseFactory()
            .newEmbeddedDatabaseBuilder( testDirectory.databaseDir() )
            .setConfig( GraphDatabaseSettings.pagecache_memory, "512M" )
            .setConfig( GraphDatabaseSettings.string_block_size, "60" )
            .setConfig( GraphDatabaseSettings.array_block_size, "300" )
            .newGraphDatabase();
        // end::startDbWithMapConfig[]
        assertNotNull( graphDb );
        graphDb.shutdown();
    }
}
