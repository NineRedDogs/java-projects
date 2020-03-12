using System;
using System.Linq;
using Neo4j.Driver.V1;

namespace Neo4j.BoltDemo
{
    class Program
    {
        static void Main(string[] args)
        {
            using (var _driver = GraphDatabase.Driver("bolt://localhost:7687", 
                AuthTokens.Basic("neo4j", "Neo4j")))
            {
                using (var session = _driver.Session())
                {
                    var response = session.ReadTransaction(tx => 
                        tx.Run("match (a:Actor) return a limit 100"));
                    foreach (var result in response)
                    {
                        var firstValue = result.Values.First().Value; 
                        Console.WriteLine(((INode)firstValue)
                            .Properties["name"].As<string>());
                    }
                }
            }

            Console.ReadKey();
        }
    }
}
