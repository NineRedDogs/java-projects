using System;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace Neo4j.RESTDemo
{
    class Program
    {
        static void Main(string[] args)
        {
            var response = ExecuteCypherStatement("match (a:Actor) return a limit 100")
                .Result;
            foreach (var result in response.Results)
            {
                foreach (var data in result.Data)
                {
                    foreach (var column in data.Rows)
                        Console.WriteLine(column.ToString());
                }
            }
            Console.ReadKey();
        }

        static async Task<NeoRowResponse> ExecuteCypherStatement(string statement)
        {
            var httpClient = new HttpClient();
            httpClient.DefaultRequestHeaders.Accept
                .Add(new MediaTypeWithQualityHeaderValue("application/json"));
            httpClient.DefaultRequestHeaders.Authorization = 
                new AuthenticationHeaderValue("Basic", "bmVvNGo6TmVvNGo=");
            var neoStatement = new NeoStatementRequest(statement);

            var response = await httpClient
                .PostAsync(new Uri("http://localhost:7474/db/data/transaction/commit"), 
                    new StringContent(JsonConvert.SerializeObject(neoStatement), 
                        Encoding.UTF8, "application/json"));
            response.EnsureSuccessStatusCode();
            return JsonConvert.DeserializeObject<NeoRowResponse>
                (await response.Content.ReadAsStringAsync());
        }
    }
}
