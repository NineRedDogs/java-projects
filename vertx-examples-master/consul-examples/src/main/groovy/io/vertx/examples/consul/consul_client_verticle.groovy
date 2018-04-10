import io.vertx.ext.consul.ConsulClient
def consulClient = ConsulClient.create(vertx)
consulClient.putValue("key11", "value11", { putResult ->
  if (putResult.succeeded()) {
    println("KV pair saved")
    consulClient.getValue("key11", { getResult ->
      if (getResult.succeeded()) {
        println("KV pair retrieved")
        println(getResult.result().value)
      } else {
        getResult.cause().printStackTrace()
      }
    })
  } else {
    putResult.cause().printStackTrace()
  }
})
