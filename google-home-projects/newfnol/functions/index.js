// Copyright 2018, Google, Inc.
// Licensed under the Apache License, Version 2.0 (the 'License');
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an 'AS IS' BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

'use strict';

// Import the Dialogflow module from the Actions on Google client library.
// const {dialogflow} = require('actions-on-google');

// Import the Diagflow module and response creation dependencies from the
// Actions on Google client lib
const {
	dialogflow,
	Permission,
	Suggestions,
	BasicCard,
	Carousel,
  Image,
  NewSurface,
} = require('actions-on-google');

//
// Import the firebase-functions package for deployment.
const functions = require('firebase-functions');


const url = require('url');
const { ssml } = require('./node_modules/ssml');

var express = require('express');


const config = functions.config();
const STATIC_MAPS_ADDRESS = 'https://maps.googleapis.com/maps/api/staticmap';
const STATIC_MAPS_SIZE = '640x640';

const locationResponse = (city, speech) => {
  const staticMapsURL = url.parse(STATIC_MAPS_ADDRESS, true);
  staticMapsURL.query = {
    key: config.maps.key,
    size: STATIC_MAPS_SIZE,
  };
  staticMapsURL.query.center = city;
  const mapViewURL = url.format(staticMapsURL);
  return [
    speech,
    new Image({
      url: mapViewURL,
      alt: 'City Map',
    }),
  ];
};

const responses = {
  sayName: (name) => ssml`
    <speak>
      I am reading your mind now.
      <break time="2s"/>
      This is easy, you are ${name}
      <break time="500ms"/>
      I hope I pronounced that right.
      <break time="500ms"/>
      Okay! I am off to read more minds.
    </speak>
  `,
  sayLocation: (city) => locationResponse(city, ssml`
    <speak>
      I am reading your mind now.
      <break time="2s"/>
      This is easy, you are in ${city}
      <break time="500ms"/>
      That is a beautiful town.
      <break time="500ms"/>
      Okay! I am off to read more minds.
    </speak>
  `),
  greetUser: (user) => ssml`
    <speak>
      Welcome to your Psychic!
      <break time="500ms"/>
      My mind is more powerful than you know.
      I wonder which of your secrets I shall unlock.
      Would you prefer I guess your name, or your location?
    </speak>
  `,
  unhandledDeepLinks: (input) => ssml`
    <speak>
      Welcome to your Psychic! I can guess many things about you,
      but I cannot make guesses about ${input}.
      Instead, I shall guess your name or location. Which do you prefer?
    </speak>
  `,
  readMindError: (errorx) => ssml`
    <speak>
      Wow!
      <break time="1s"/>
      This has never happened before. I cannot read your mind.
      I need more practice.
      Ask me again later.
    </speak>
  `,
  permissionReason: 'To read your mind',
  newSurfaceContext: 'To be able to show you the possible builders',
  notificationText: 'Choose a builder...',
};

//

 /**
   * Shows the location of the user with a preference for a screen device.
   * If on a speaker device, asks to transfer dialog to a screen device.
   * Reads location from userStorage.
   * @param {object} conv - The conversation instance.
   * @return {Void}
   */
  const showLocationOnScreen = (conv) => {
    const capability = 'actions.capability.SCREEN_OUTPUT';
    if (conv.surface.capabilities.has(capability) || !conv.available.surfaces.capabilities.has(capability)) {
        return conv.close(...responses.sayLocation('caerphilly'));
        //return conv.close(...responses.sayLocation(conv.user.storage.location));
        //return conv.ask(buildersCarousel());
      }
    conv.ask(new NewSurface({
      context: responses.newSurfaceContext,
      notification: responses.notificationText,
      capabilities: capability,
    }));
  };


// Instantiate the Dialogflow client.
const app = dialogflow({debug: true});

function getFnolStatus(prefix, conv) {
  conv.ask(`${prefix},  I understand you'd like to make a claim against your home policy ?`);
}

// Handle the Dialogflow intent named 'Start Intent'.
app.intent('allSafe', (conv) => {
  // uncomment following line to clear stored user info
  conv.user.storage = {};
  const name = conv.user.storage.userName;

  if (!name) {
    // Asks the user's permission to know their name, for personalization.
    conv.ask(new Permission({
      context: 'Great, Welcome to Admiral Home Claim centre. ',
      //permissions: 'NAME',
      //permissions: ['NAME', 'DEVICE_COARSE_LOCATION'],
      permissions: ['NAME', 'DEVICE_PRECISE_LOCATION'],
    }));
  } else {
    getFnolStatus(`Hi again, ${name}`, conv);
  }
 });



// Handle the DialogFlow intent named 'actions_intent_PERMISSION'. If user
// agreed to PERMISSION prompt, then boolean value 'permissionGranted' is true.
app.intent('actions_intent_PERMISSION', (conv, params, permissionGranted) => {
  var queryPrefix="No worries";
  if (permissionGranted) {
    // save the info grabbed from google
    conv.user.storage.userName = conv.user.name.given;
    conv.user.storage.formattedAddress = conv.device.location.formattedAddress;
    conv.user.storage.postalAddress = conv.device.location.postalAddress;
    conv.user.storage.city = conv.device.location.city;
    conv.user.storage.zip = conv.device.location.zipCode;
    conv.user.storage.longitude = conv.device.location.coordinates.longitude;
    conv.user.storage.latitude = conv.device.location.coordinates.latitude;
    conv.user.storage.name = conv.device.location.name;
    conv.user.storage.phone  = conv.device.location.phoneNumber;
    conv.user.storage.notes  = conv.device.location.notes;
    //queryPrefix=`OK, ${conv.user.storage.userName} of ${conv.user.storage.formattedAddress}`;
    queryPrefix=`OK, ${conv.user.storage.userName} `;

    //return showLocationOnScreen(conv);
  }
  getFnolStatus(queryPrefix, conv);
});

// Handle the Dialogflow intent named 'noArrangeBuilder'.
app.intent('noArrangeBuilder', (conv) => {
  const name = conv.user.storage.userName;
  conv.close(`OK, ${name}, thanks for calling Admiral Home claims centre today. We'll be in touch regarding the claim. Cheerio.`);
});


// Handle the Dialogflow follow-up intents
app.intent(['arrangeBuilder', 'arrangeRoofer'], (conv) => {
  const capability = 'actions.capability.SCREEN_OUTPUT'

  if (conv.surface.capabilities.has(capability)) {
    conv.close(...imageResponses)
  } else {
    conv.ask(new NewSurface({
      capabilities: capability,
      context: `I see you're calling from ${conv.user.storage.city}, I'd like to send details of your nearest certified builder.`,
      notification: 'Check out this image',
    }))
  }
 });
 


// Handle the Dialogflow intent named 'Start Intent - new'.
app.intent('start Fnol - new', (conv) => {
  conv.close(`AJG10: you want to start a new FNOL`);
});

// Handle the Dialogflow intent named 'Start Intent - new'.
app.intent('start Fnol - new', (conv) => {
  conv.close(`AJG10: you want to resume an FNOL`);
});

// Handle the Dialogflow intent named 'Start Intent - new'.
app.intent('cInjury', (conv) => {
  conv.close(`${conv.user.storage.userName}, I'm sorry to hear that. To save your call charges and to ensure we provide you with the best possible service, we're going to get someone from our medical team to give you a call to discuss further. For info your claim number is 15001. We'll be in touch shortly. Goodbye.`);
});

// Handle the Dialogflow intent named 'Start Intent - new'.
app.intent('breakIn - no', (conv) => {
  conv.close(`${conv.user.storage.userName}, I'm really sorry, but we need the crime number to continue. Please can you get in touch with the police and call us back with the crime number. For your reference, your claim number is : 15001. Bye.`);
});


// Handle the Dialogflow intent named 'Start Intent - new'.
app.intent('breakIn - yes', (conv) => {
  conv.close(`Thanks ${conv.user.storage.userName}, I've just created claim 15001 to cover this issue, Next, we'll need to find out what's been stolen or damaged. To save your call charges, I'm going to get a colleague from our crime team to give you a call  straight after this call. Bye.`);
});

// Handle the Dialogflow intent named 'Start Intent - new'.
app.intent('leakIssues', (conv) => {
  conv.close(`Thanks ${conv.user.storage.userName}, We've got enough details to proceed. I see you're calling from ${conv.user.storage.city}, I've just emailed you details of a few local plumbers that you can contact to get some quotes. Give us a call when you have some news regarding a quote. Goodbye ${conv.user.storage.userName}`);
});

// Handle the Dialogflow intent named 'fnol whats the problem'.
// The intent collects a parameter named 'hhProblem'.
app.intent('fnol whats the problem', (conv, {hhProblem}) => {
  conv.close(`AJG10: so the problem is ${hhProblem}`);
});

// Handle the Dialogflow intent named 'fnol whats the problem'.
// The intent collects a parameter named 'hhProblem'.
app.intent('cStart', (conv) => {
  console.log("About to assign random claim id ...");
  conv.data.claimId = '2447';
  //conv.close(`AJG10: so the problem is ${hhProblem}`);
});

// Handle the Dialogflow intent named 'fnol whats the problem'.
// The intent collects a parameter named 'hhProblem'.
app.intent('cNotHabitable', (conv) => {
  console.log("About to assign random claim id ...");
  conv.ask('does policy have hotelcover ? ');
  //conv.data.hotel = true; //return true or false here based on random number gen - to decide if hotel cover exists on policy
  //conv.data.claimId = '2447';
  //conv.close(`AJG10: so the problem is ${hhProblem}`);
});


app.intent('createClaim', (conv) => {
  console.log("createClaim Intent");

  conv.ask('Do you want to hear about some stuff?');
  conv.ask(new Suggestions('Yes', 'No'));

  conv.ask(new Suggestions(['andy-1', 'andy-2', 'andy-3']));
  //const msg = pickRandomMessage(samWelcomeMessages);
  respond(conv, 'fred flintoff', 'https://fred.com');
});

 // Handle the Dialogflow intent named 'Start Intent'.
app.intent('Start Intent', (conv) => {
  // uncomment following line to clear stored user info
  conv.user.storage = {};
  const name = conv.user.storage.userName;
  if (!name) {
    // Asks the user's permission to know their name, for personalization.
    conv.ask(new Permission({
      context: 'Hi there, to get to know you better',
      permissions: 'NAME',
    }));
  } else {
    conv.ask(`Hi again, ${name}. What's your favorite color?`);
  }
 });
 
 // ==========================================================


//  const ajgCard = new BasicCard({

//   text: `This is a basic card.  Text in a basic card can include "quotes" and
//   most other unicode characters including emoji 📱.  Basic cards also support
//   some markdown formatting like *emphasis* or _italics_, **strong** or
//   __bold__, and ***bold itallic*** or ___strong emphasis___ as well as other
//   things like line  \nbreaks`, // Note the two spaces before '\n' required for
//                                // a line break to be rendered in the card.
//   subtitle: 'This is a subtitle',
//   title: 'Title: this is a title',
//   buttons: new Button({
//     title: 'This is a button',
//     url: 'https://assistant.google.com/',
//   }),
//   image: new Image({
//     url: 'https://example.com/image.png',
//     alt: 'Image alternate text',
//   }),
//   display: 'CROPPED',
// });

 const imageResponses = [
  `The nearest certified builder to you is Cardiff Premier Builders, are these ok ?`,
  new Image({
    url: 'https://thebestbuilderscardiff.co.uk/wp-content/uploads/2016/03/LOGO.png',
    alt: 'XYZ Logo',
  })
]

app.intent('sandbox', conv => {
  const capability = 'actions.capability.SCREEN_OUTPUT'
  console.log('ajg12')

  if (conv.surface.capabilities.has(capability)) {
    conv.close(...imageResponses)
    //conv.close(...ajgCard)
  } else {
    conv.ask(new NewSurface({
      capabilities: capability,
      context: `I see you're calling from ${conv.user.storage.city}, I'd like to send details of your nearest certified builder.`,
      notification: 'Check out this image',
    }))
  }
})

app.intent('actions.intent.NEW_SURFACE', (conv, input, newSurface) => {
  if (newSurface.status === 'OK') {
    //conv.close(new Image({url: url, alt: 'Google Pixel' }));
    return conv.ask(...imageResponses)
    //return conv.ask(...ajgCard)
    //conv.ask(buildersCarousel());
    //return conv.ask(buildersCarousel())
  } else {
    conv.close(`Ok, I understand. You don't want to see pictures. Bye`);
  }
});


// ================================================


// =================================================

// In the case the user is interacting with the Action on a screened device
// The Builders Carousel will display a carousel of potential builder cards
const buildersCarousel = () => {
  const carousel = new Carousel({
   items: {
    'AJW': {
      title: 'AJW',
      synonyms: ['ajw', 'llanishen'],
      url: 'https://www.builderscardiff.com/',
      image: new Image({
        url: 'https://www.builderscardiff.com/wp-content/uploads/2017/07/ajwbuilderscardifflogo.png',
        alt: 'AJW builders',
      }),
    },
    'Best': {
      title: 'Best',
      synonyms: ['best', 'cardiff'],
      url: 'https://thebestbuilderscardiff.co.uk/',
      image: new Image({
        url: 'https://thebestbuilderscardiff.co.uk/wp-content/uploads/2016/03/LOGO.png',
        alt: 'Best builders',
      }),
    },
    'MandR': {
      title: 'M and R Builders',
      synonyms: ['mandr', 'mnr'],
      url: 'https://t.co/yOT5u5Fkuu',
      image: new Image({
        url: 'https://pbs.twimg.com/media/Dv61b5_XQAUsGnM.jpg',
        alt: 'M and R builders',
      }),
    },
 }});
 return carousel;
};


// Handle the Dialogflow intent named 'favorite color'.
// The intent collects a parameter named 'color'.
app.intent('favorite color', (conv, {color}) => {
    const luckyNumber = color.length;
    const audioSound = 'https://actions.google.com/sounds/v1/cartoon/clang_and_wobble.ogg';

    // Respond with the user's lucky number and end the conversation.
    if (conv.user.storage.userName) {
	// if we've got the user name, address them by name and use SSML
	    // to embed an audio snippet in the response
        conv.ask(`<speak>${conv.user.storage.userName}, Your real lucky number is ` + 
	           `${luckyNumber}. <audio src="${audioSound}"></audio>` +
		`Would you like to hear some fake colors ?</speak>`);
    } else {
        conv.ask(`<speak>Your lucky number is ` + 
	           `${luckyNumber}. <audio src="${audioSound}"></audio>` +
		`Would you like to hear some fake colors ?</speak>`);
    }
});


// Handle the Dialogflow follow-up intents
app.intent(['favorite color - yes', 'favorite fake color - yes'], (conv) => {
 conv.ask('Which color, indigo taco, pink unicorn or blue grey coffee?');
 // If the user is using a screened device, display the carousel
 if (conv.screen) return conv.ask(fakeColorCarousel());
});






// Handle the Dialogflow NO_INPUT intent.
// Triggered when the user doesn't provide input to the Action
app.intent('actions_intent_NO_INPUT', (conv) => {
  // Use the number of reprompts to vary response
  const repromptCount = parseInt(conv.arguments.get('REPROMPT_COUNT'));
  if (repromptCount === 0) {
    conv.ask('Which color would you like to hear about?');
  } else if (repromptCount === 1) {
    conv.ask(`Please say the name of a color.`);
  } else if (conv.arguments.get('IS_FINAL_REPROMPT')) {
    conv.close(`Sorry we're having trouble. Let's ` +
      `try this again later. Goodbye.`);
  }
});


// Define a mapping of fake color strings to basic card objects.
const colorMap = {
  'indigo taco': {
    title: 'Indigo Taco',
    text: 'Indigo Taco is a subtle bluish tone.',
    image: {
      url: 'https://storage.googleapis.com/material-design/publish/material_v_12/assets/0BxFyKV4eeNjDN1JRbF9ZMHZsa1k/style-color-uiapplication-palette1.png',
      accessibilityText: 'Indigo Taco Color',
    },
    display: 'WHITE',
  },
  'pink unicorn': {
    title: 'Pink Unicorn',
    text: 'Pink Unicorn is an imaginative reddish hue.',
    image: {
      url: 'https://storage.googleapis.com/material-design/publish/material_v_12/assets/0BxFyKV4eeNjDbFVfTXpoaEE5Vzg/style-color-uiapplication-palette2.png',
      accessibilityText: 'Pink Unicorn Color',
    },
    display: 'WHITE',
  },
  'blue grey coffee': {
    title: 'Blue Grey Coffee',
    text: 'Calling out to rainy days, Blue Grey Coffee brings to mind your favorite coffee shop.',
    image: {
      url: 'https://storage.googleapis.com/material-design/publish/material_v_12/assets/0BxFyKV4eeNjDZUdpeURtaTUwLUk/style-color-colorsystem-gray-secondary-161116.png',
      accessibilityText: 'Blue Grey Coffee Color',
    },
    display: 'WHITE',
  },
};



// Handle the Dialogflow intent named 'favorite fake color'.
// The intent collects a parameter named 'fakeColor'.
app.intent('favorite fake color', (conv, {fakeColor}) => {
  fakeColor = conv.arguments.get('OPTION') || fakeColor;
  // Present user with the corresponding basic card and end the conversation.
  if (!conv.screen) {
    conv.ask(colorMap[fakeColor].text);
  } else {
    conv.ask(`Here you go.`, new BasicCard(colorMap[fakeColor]));
  }
  conv.ask('Do you want to hear about another fake color?');
  conv.ask(new Suggestions('Yes', 'No'));
});


// Set the DialogflowApp object to handle the HTTPS POST request.
exports.dialogflowFirebaseFulfillment = functions.https.onRequest(app);
