importScripts('https://www.gstatic.com/firebasejs/7.24.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/7.24.0/firebase-messaging.js');

firebase.initializeApp({
  apiKey: "AIzaSyB7AQYerl4_dsut7jmrc-6csbrIAaU2P9Q",
  authDomain: "notification-d53ec.firebaseapp.com",
  projectId: "notification-d53ec",
  storageBucket: "notification-d53ec.appspot.com",
  messagingSenderId: "63255710253",
  appId: "1:63255710253:web:c050842f8c067b3ff846a4",
  measurementId: "G-EPJZCBSS0N"
});

const messaging = firebase.messaging();

// Sử dụng VAPID Key
messaging.usePublicVapidKey('BEo-xXC-e2SUiExE3Jn5qSxGbgI7X5dDQQMV8mTt3Vo4vzRbzcdGWHXovp_rkRlT74N-SKBAc9U7ucP083kVcd4'); // Thay YOUR_PUBLIC_VAPID_KEY bằng VAPID key của bạn
