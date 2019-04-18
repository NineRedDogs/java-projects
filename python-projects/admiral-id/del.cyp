CALL apoc.periodic.iterate("match (n) return n", "detach delete n", {batchSize:5000});
