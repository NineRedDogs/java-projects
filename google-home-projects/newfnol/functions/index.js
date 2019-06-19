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
  conv.ask(`${prefix},  Do you want to make a new claim ?`);
}

// Handle the Dialogflow intent named 'Start Intent'.
app.intent('allSafe', (conv) => {
  // uncomment following line to clear stored user info
  conv.user.storage = {};
  const name = conv.user.storage.userName;

  if (!name) {
    // Asks the user's permission to know their name, for personalization.
    conv.ask(new Permission({
      context: 'Great, Welcome to Admiral Home Claim centre. 1357',
      //permissions: 'NAME',
      permissions: ['NAME', 'DEVICE_PRECISE_LOCATION', 'DEVICE_COARSE_LOCATION'],
    }));
  } else {
    getFnolStatus(`Hi again, ${name}`, conv);
  }
 });

app.intent('sandbox', (conv) => {
  // Choose one or more supported permissions to request:
  // NAME, DEVICE_PRECISE_LOCATION, DEVICE_COARSE_LOCATION
  const options = {
    context: 'To address you by name and know your location',
    // Ask for more than one permission. User can authorize all or none.
    permissions: ['NAME', 'DEVICE_PRECISE_LOCATION'],
  };
conv.ask(new Permission(options));
});



// Handle the DialogFlow intent named 'actions_intent_PERMISSION'. If user
// agreed to PERMISSION prompt, then boolean value 'permissionGranted' is true.
app.intent('actions_intent_PERMISSION', (conv, params, permissionGranted) => {
  var queryPrefix="No worries";
  if (permissionGranted) {
    conv.user.storage.user = conv.user.name;
    conv.user.storage.userName = conv.user.name.given;
    conv.user.storage.location = conv.device;
    conv.user.storage.locFormatted = conv.device.formattedAddress
    //queryPrefix=`OK, ${conv.user.storage.userName} of ${conv.user.storage.location.formattedAddress} full user : ${conv.user.storage.user}`;
    queryPrefix=`OK, ${conv.user.storage.userName}`;
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
  conv.ask('Using your location, Ive identified 3 local builders, Which firm would you like to use, AJW, Best or MandR?');
  // If the user is using a screened device, display the carousel
  if (conv.screen) return conv.ask(buildersCarousel());
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
 
 


// ================================================

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
