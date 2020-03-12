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
package org.neo4j.doc.server.helpers;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import org.apache.commons.io.FileUtils;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.logging.LogProvider;
import org.neo4j.server.NeoServer;

public class ServerHelper
{
    public static void cleanTheDatabase( final NeoServer server )
    {
        if ( server == null )
        {
            return;
        }

        rollbackAllOpenTransactions( server );

        cleanTheDatabase( server.getDatabase().getGraph() );

        removeLogs( server );
    }

    public static void cleanTheDatabase( GraphDatabaseAPI db )
    {
        new Transactor( db, new DeleteAllData( db ), 10 ).execute();
        new Transactor( db, new DeleteAllSchema( db ), 10 ).execute();
    }

    private static void removeLogs( NeoServer server )
    {
        File logDir = new File( server.getDatabase().getLocation() + File.separator + ".." + File.separator + "log" );
        try
        {
            FileUtils.deleteDirectory( logDir );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static NeoServer createNonPersistentServer( LogProvider logProvider ) throws IOException
    {
        return createServer( CommunityServerBuilder.server( logProvider ), false, null );
    }

    public static NeoServer createNonPersistentServer( CommunityServerBuilder builder ) throws IOException
    {
        return createServer( builder, false, null );
    }

    private static NeoServer createServer( CommunityServerBuilder builder, boolean persistent, File path )
            throws IOException
    {
        if ( persistent )
        {
            builder = builder.persistent();
        }
        NeoServer server = builder
                .usingDataDir( path != null ? path.getAbsolutePath() : null )
                .build();

        checkServerCanStart( server.baseUri().getHost(), server.baseUri().getPort() );

        server.start();
        return server;
    }

    private static void checkServerCanStart( String host, int port ) throws IOException
    {
        ServerSocket serverSocket = null;
        try
        {
            serverSocket = new ServerSocket( port, 1, InetAddress.getByName( host ) );
        }
        catch ( IOException ex )
        {
            throw new RuntimeException( "Unable to start server on " + host + ":" + port, ex );
        }
        finally
        {
            if ( serverSocket != null )
            {
                serverSocket.close();
            }
        }
    }

    private static void rollbackAllOpenTransactions( NeoServer server )
    {
        server.getTransactionRegistry().rollbackAllSuspendedTransactions();
    }

    private static class DeleteAllData implements UnitOfWork
    {
        private final GraphDatabaseAPI db;

        public DeleteAllData( GraphDatabaseAPI db )
        {
            this.db = db;
        }

        @Override
        public void doWork()
        {
            deleteAllNodesAndRelationships();
            deleteAllIndexes();
        }

        private void deleteAllNodesAndRelationships()
        {
            Iterable<Node> allNodes = db.getAllNodes();
            for ( Node n : allNodes )
            {
                Iterable<Relationship> relationships = n.getRelationships();
                for ( Relationship rel : relationships )
                {
                    rel.delete();
                }
                n.delete();
            }
        }

        private void deleteAllIndexes()
        {
            IndexManager indexManager = db.index();

            for ( String indexName : indexManager.nodeIndexNames() )
            {
                try
                {
                    db.index()
                      .forNodes( indexName )
                      .delete();
                }
                catch ( UnsupportedOperationException e )
                {
                    // Encountered a read-only index.
                }
            }

            for ( String indexName : indexManager.relationshipIndexNames() )
            {
                try
                {
                    db.index()
                      .forRelationships( indexName )
                      .delete();
                }
                catch ( UnsupportedOperationException e )
                {
                    // Encountered a read-only index.
                }
            }

            for ( String k : indexManager.getNodeAutoIndexer().getAutoIndexedProperties() )
            {
                indexManager.getNodeAutoIndexer().stopAutoIndexingProperty( k );
            }
            indexManager.getNodeAutoIndexer().setEnabled( false );

            for ( String k : indexManager.getRelationshipAutoIndexer().getAutoIndexedProperties() )
            {
                indexManager.getRelationshipAutoIndexer().stopAutoIndexingProperty( k );
            }
            indexManager.getRelationshipAutoIndexer().setEnabled( false );
        }
    }

    private static class DeleteAllSchema implements UnitOfWork
    {
        private final GraphDatabaseAPI db;

        public DeleteAllSchema( GraphDatabaseAPI db )
        {
            this.db = db;
        }

        @Override
        public void doWork()
        {
            deleteAllIndexRules();
            deleteAllConstraints();
        }

        private void deleteAllIndexRules()
        {
            for ( IndexDefinition index : db.schema().getIndexes() )
            {
                if ( !index.isConstraintIndex() )
                {
                    index.drop();
                }
            }
        }

        private void deleteAllConstraints()
        {
            for ( ConstraintDefinition constraint : db.schema().getConstraints() )
            {
                constraint.drop();
            }
        }
    }
}
