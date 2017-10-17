const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.getSkiResorts = functions.https.onRequest((request, response) => {
    response.send("Hello from Firebase!");
});

exports.updateDB = functions.https.onRequest((request, response) => {
    var test = request.query.key;
    response.send("Updating DB, key: " + test);
});