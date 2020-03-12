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

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.api.client.Client;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import org.neo4j.doc.server.rest.JaxRsResponse;
import org.neo4j.doc.server.rest.RestRequest;
import org.neo4j.doc.server.rest.domain.GraphDbHelper;
import org.neo4j.server.NeoServer;
import org.neo4j.server.rest.web.RestfulGraphDatabase;

import static org.neo4j.server.rest.web.RestfulGraphDatabase.PATH_AUTO_INDEX;

public final class FunctionalTestHelper
{
    private final NeoServer server;
    private final GraphDbHelper helper;

    public static final Client CLIENT = Client.create();
    private RestRequest request;

    public FunctionalTestHelper( NeoServer server )
    {
        if ( server.getDatabase() == null )
        {
            throw new RuntimeException( "Server must be started before using " + getClass().getName() );
        }
        this.helper = new GraphDbHelper( server.getDatabase() );
        this.server = server;
        this.request = new RestRequest(server.baseUri().resolve("db/data/"));
    }

    public static Matcher<String[]> arrayContains( final String element )
    {
        return new TypeSafeMatcher<String[]>()
        {
            private String[] array;

            @Override
            public void describeTo( Description descr )
            {
                descr.appendText( "The array " )
                        .appendText( Arrays.toString( array ) )
                        .appendText( " does not contain <" )
                        .appendText( element )
                        .appendText( ">" );
            }

            @Override
            public boolean matchesSafely( String[] array )
            {
                this.array = array;
                for ( String string : array )
                {
                    if ( element == null )
                    {
                        if ( string == null ) return true;
                    }
                    else if ( element.equals( string ) )
                    {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public GraphDbHelper getGraphDbHelper()
    {
        return helper;
    }

    public String dataUri()
    {
        return server.baseUri().toString() + "db/data/";
    }

    public String nodeUri()
    {
        return dataUri() + "node";
    }

    public String nodeUri( long id )
    {
        return nodeUri() + "/" + id;
    }

    public String nodePropertiesUri( long id )
    {
        return nodeUri( id ) + "/properties";
    }

    public String nodePropertyUri( long id, String key )
    {
        return nodePropertiesUri( id ) + "/" + key;
    }

    String relationshipUri()
    {
        return dataUri() + "relationship";
    }

    public String relationshipUri( long id )
    {
        return relationshipUri() + "/" + id;
    }

    public String relationshipPropertiesUri( long id )
    {
        return relationshipUri( id ) + "/properties";
    }

    public String relationshipsUri( long nodeId, String dir, String... types )
    {
        StringBuilder typesString = new StringBuilder();
        for ( String type : types )
        {
            typesString.append( typesString.length() > 0 ? "&" : "" );
            typesString.append( type );
        }
        return nodeUri( nodeId ) + "/relationships/" + dir + "/" + typesString;
    }

    public String indexUri()
    {
        return dataUri() + "index/";
    }

    public String nodeIndexUri()
    {
        return indexUri() + "node/";
    }

    public String relationshipIndexUri()
    {
        return indexUri() + "relationship/";
    }

    public String managementUri()
    {
        return server.baseUri()
                .toString() + "db/manage";
    }

    public String indexNodeUri( String indexName )
    {
        return nodeIndexUri() + indexName;
    }

    public String indexNodeUri( String indexName, String key, Object value )
    {
        return indexNodeUri( indexName ) + "/" + key + "/" + value;
    }

    public String indexRelationshipUri( String indexName )
    {
        return relationshipIndexUri() + indexName;
    }

    public String indexRelationshipUri( String indexName, String key, Object value )
    {
        return indexRelationshipUri( indexName ) + "/" + key + "/" + value;
    }

    public JaxRsResponse get(String path) {
        return request.get(path);
    }

    public long getNodeIdFromUri( String nodeUri )
    {
        return Long.valueOf( nodeUri.substring( nodeUri.lastIndexOf( "/" ) +1 , nodeUri.length() ) );
    }

    public long getRelationshipIdFromUri( String relationshipUri )
    {
        return getNodeIdFromUri( relationshipUri );
    }

    public Map<String, Object> removeAnyAutoIndex( Map<String, Object> map )
    {
        Map<String, Object> result = new HashMap<>();
        for ( Map.Entry<String, Object> entry : map.entrySet() )
        {
            Map<?, ?> innerMap = (Map<?,?>) entry.getValue();
            String template = innerMap.get( "template" ).toString();
            if ( !template.contains( PATH_AUTO_INDEX.replace("{type}", RestfulGraphDatabase.NODE_AUTO_INDEX_TYPE) ) &&
                 !template.contains( PATH_AUTO_INDEX.replace("{type}", RestfulGraphDatabase.RELATIONSHIP_AUTO_INDEX_TYPE) ) &&
                 !template.contains( "_auto_" ) )
                result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    public URI baseUri()
    {
        return server.baseUri();
    }

}
