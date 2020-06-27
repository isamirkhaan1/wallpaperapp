
/**
*  
*
*   File: index.js
*
*   This file includes Firebase Cloud Functions
*
*
*   Google official documentations
*   https://firebase.google.com/docs/functions/write-firebase-functions
*   https://firebase.google.com/docs/functions/firestore-events
*
*
*   Google example source code 
*   https://github.com/firebase/functions-samples/blob/master/fcm-notifications/functions/index.js
*
*   Use Firebase Cloud Functions with Android
*   https://codingwithmitch.com/blog/android-firebase-cloud-messages-cloud-function/#getting-started
*/



const functions = require('firebase-functions');

let admin = require('firebase-admin')
admin.initializeApp(functions.config().firebase);


//Themes relative path in firestore  
const DOC_THEME_PATH = "today/themes"


//FCM has only the following 3 topics
//Doc themes has also only these 3 keys, each one has a value (an image URL) for corresponding theme and
//each theme value is daily updating 
const TOPIC_WHITE = "white"
const TOPIC_BLACK = "black"
const TOPIC_COLOR = "color"


//  When value of document themes change, it notify all users who are subscribed to that theme
exports.sendNotification = functions.firestore.document(DOC_THEME_PATH).onWrite((change, context) => {

    var newData = change.after.data()
    var oldData = change.before.data()

    //default theme of user is theme-color
    var topic = TOPIC_COLOR

    //find which theme value is changed
    if(newData.white === oldData.white && newData.black === oldData.black)
        topic = TOPIC_COLOR
    else if(newData.black === oldData.black && newData.color === oldData.color)
        topic = TOPIC_WHITE
    else
        topic = TOPIC_BLACK

    //generate message for subscribers
    //important thing is value of changed theme i.e. new image URL
    var message = {
            data: {
            url: newData[topic]
        }
    }

    //log to firebase console
    console.log('info',`theme:${topic}, url:${newData[topic]}`)
    
    //send message to subscribers
    return admin.messaging().sendToTopic(topic, message).then((response) => {
        // Response is a message ID string.
        console.log('Successfully notification sent:', response);
        return response
  }).catch((error) => {
        console.log('Error sending notification:', error);
        throw new Error(error)
  });
 });
