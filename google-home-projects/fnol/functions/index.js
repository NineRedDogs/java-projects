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
} = require('actions-on-google');

//
//
// Import the firebase-functions package for deployment.
const functions = require('firebase-functions');

// Instantiate the Dialogflow client.
const app = dialogflow({debug: true});


function getFnolStatus(prefix, conv) {
  conv.ask(`${prefix},  Do you want to start a new FNOL or resume an existing one ?`);
  conv.ask(new Suggestions('new', 'resume', 'cancel'));
}

// Handle the Dialogflow intent named 'Start Intent'.
app.intent('start Fnol', (conv) => {
  // uncomment following line to clear stored user info
  //conv.user.storage = {};
  const name = conv.user.storage.userName;
  if (!name) {
    // Asks the user's permission to know their name, for personalization.
    conv.ask(new Permission({
      context: 'Hi there, if its ok, it would be so nice to get to know you better',
      permissions: 'NAME',
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
    conv.user.storage.userName = conv.user.name.given;
    queryPrefix=`Great, ${conv.user.storage.userName}`;
  }
  getFnolStatus(queryPrefix, conv);
});

// Handle the Dialogflow intent named 'Start Intent - new'.
app.intent('start Fnol - new', (conv) => {
  conv.close(`AJG10: you want to start a new FNOL`);
});

// Handle the Dialogflow intent named 'Start Intent - new'.
app.intent('start Fnol - new', (conv) => {
  conv.close(`AJG10: you want to resume an FNOL`);
});


// Handle the Dialogflow intent named 'fnol whats the problem'.
// The intent collects a parameter named 'hhProblem'.
app.intent('fnol whats the problem', (conv, {hhProblem}) => {
  conv.close(`AJG10: so the problem is ${hhProblem}`);
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
 
 


// ================================================

// In the case the user is interacting with the Action on a screened device
// The Fake Color Carousel will display a carousel of color cards
const fakeColorCarousel = () => {
  const carousel = new Carousel({
   items: {
     'indigo taco': {
       title: 'Indigo Taco',
       synonyms: ['indigo', 'taco'],
       image: new Image({
         url: 'https://storage.googleapis.com/material-design/publish/material_v_12/assets/0BxFyKV4eeNjDN1JRbF9ZMHZsa1k/style-color-uiapplication-palette1.png',
         alt: 'Indigo Taco Color',
       }),
     },
     'pink unicorn': {
       title: 'Pink Unicorn',
       synonyms: ['pink', 'unicorn'],
       image: new Image({
         url: 'https://storage.googleapis.com/material-design/publish/material_v_12/assets/0BxFyKV4eeNjDbFVfTXpoaEE5Vzg/style-color-uiapplication-palette2.png',
         alt: 'Pink Unicorn Color',
       }),
     },
     'blue grey coffee': {
       title: 'Blue Grey Coffee',
       synonyms: ['blue', 'grey', 'coffee'],
       image: new Image({
         url: 'https://storage.googleapis.com/material-design/publish/material_v_12/assets/0BxFyKV4eeNjDZUdpeURtaTUwLUk/style-color-colorsystem-gray-secondary-161116.png',
         alt: 'Blue Grey Coffee Color',
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
