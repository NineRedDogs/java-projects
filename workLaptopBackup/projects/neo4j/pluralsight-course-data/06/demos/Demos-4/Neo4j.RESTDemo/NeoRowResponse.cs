using System.Collections.Generic;
using Newtonsoft.Json;

namespace Neo4j.RESTDemo
{
    public class Row
    {
        [JsonProperty("name")]
        public string Name { get; set; }
        [JsonProperty("wikipedia")]
        public string Wikipedia { get; set; }
        [JsonProperty("salary")]
        public int? Salary { get; set; }

        public override string ToString()
        {
            return string.Format("Name: {0}, Wikipedia: {1}, Salary: {2}", Name, Wikipedia, Salary);
        }
    }

    public class Record
    {
        [JsonProperty("row")]
        public IList<Row> Rows { get; set; }
    }

    public class Result
    {
        [JsonProperty("columns")]
        public IList<string> Columns { get; set; }
        [JsonProperty("data")]
        public IList<Record> Data { get; set; }
    }

    public class NeoRowResponse
    {
        [JsonProperty("results")]
        public IList<Result> Results { get; set; }
        [JsonProperty("errors")]
        public IList<object> Errors { get; set; }
    }
}
