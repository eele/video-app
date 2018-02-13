import Vue from 'vue'
import App from './App.vue'
import DeviceCtrl from './deviceCtrl'
// Import F7
import Framework7 from 'framework7/dist/framework7.esm.bundle.js';
// Import F7 Vue Plugin
import Framework7Vue from 'framework7-vue/dist/framework7-vue.esm.bundle.js';
// Import F7 Styles
import Framework7Styles from 'framework7/dist/css/framework7.css';
// Import Icons
import Icons from './lib/ionicons-2.0.1/css/ionicons.min.css'
// Import Routes
import Routes from './routes.js'

// Init F7-Vue Plugin
Vue.use(Framework7Vue, Framework7)

new Vue({
  el: '#app',
  render: h => h(App),
  framework7: {
    // App root element
    root: '#app',
    // App Name
    name: 'videoapp',
    // Enable swipe panel
    panel: {
      swipe: 'left',
    },
    theme: "ios",
    routes: Routes
  }
})
