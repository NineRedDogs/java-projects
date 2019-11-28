
// Dialogflow
const app = dialogflow()

const imageResponses = [
  `AJG: Here is an andy grahame image`,
  new Image({
    url: 'https://thebestbuilderscardiff.co.uk/wp-content/uploads/2016/03/LOGO.png',
    alt: 'Andy Logo',
  })
]

app.intent('sandbox', conv => {
  const capability = 'actions.capability.SCREEN_OUTPUT'
  if (conv.surface.capabilities.has(capability)) {
    conv.close(...imageResponses)
  } else {
    conv.ask(new NewSurface({
      capabilities: capability,
      context: 'AJG: 11 To show you an image',
      notification: 'AJG: 22 Check out this image',
    }))
  }
})

// Create a Dialogflow intent with the `actions_intent_NEW_SURFACE` event
app.intent('Get New Surface', (conv, input, newSurface) => {
  if (newSurface.status === 'OK') {
    conv.close(...imageResponses)
  } else {
    conv.close(`AJG: 33 . Ok, I understand. You don't want to see pictures. Bye`)
  }
})
