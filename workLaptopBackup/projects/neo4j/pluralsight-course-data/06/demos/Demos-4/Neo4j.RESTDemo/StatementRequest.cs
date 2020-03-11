using System;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;

namespace Neo4j.RESTDemo
{
    public class Statement
    {
        [JsonProperty("statement")]
        public string TheStatement { get; set; }
    }

    public class NeoStatementRequest
    {
        public NeoStatementRequest(params string[] statements)
        {
            this.statements = new List<Statement>();
            foreach (var statement in statements)
                this.statements.Add(new Statement{TheStatement = statement});
        }
        public List<Statement> statements { get; set; }
    }
}
