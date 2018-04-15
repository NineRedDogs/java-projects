/**
 * 
 */


  var eb = new EventBus("/eventbus/");
  eb.onopen = function () {
    eb.registerHandler("chat.to.client", function (err, msg) {
      $('#chat').append(msg.body + "\n");
    });
  };

  function send(event) {
    if (event.keyCode == 13 || event.which == 13) {
      var message = $('#input').val();
      if (message.length > 0) {
        eb.publish("chat.to.server", message);
        $('#input').val("");
      }
    }
  }
